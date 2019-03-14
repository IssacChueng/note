## HashMap和ConcurrentHashMap的分析

###主要参考了[Java7/8 中的 HashMap 和 ConcurrentHashMap 全解析](http://www.importnew.com/28263.html)

### HashMap中一些字段  
-   capacity: 容量,始终保持2^n, 可以扩容,扩容后数组大小为当前2倍  
-   loadFactor: 负载因子, 默认0.75
-   threshold: 扩容阈值, 元素数量达到这么大后进行扩容

### JDK7 HashMap结构示意图
![jdk7HashMap](/jdk/struct/jdk7HashMap.png)
> 基本上, HashMap是一个数组,每个数组元素都是一个单向列表, 列表元素类型是内部类Entry<K,V>, 包含四个属性, key, value, hash值和用于单向列表的next  

### JDK7 HashMap的方法

- put(K key, V value)  

        这个方法用于往数组中插入数据    
        第一步,判断内部数组是不是未初始化过的空数组, 如果是,需要先初始化数组,初始化数组时, 将重新设置扩容阈值为初始化容量与负载因子的乘积.  
        接下来判断key是否为空,如果为空进入putForNullKey方法,设置空元素,这个元素的key是null, 且放在内部数组的第一个位置的链表上, 并返回key为null时的旧值, put方法结束  
        当key不为空,需要计算key的hash值, 并且取hash值二进制最后几位(具体几位与当前数组的长度有关, 计算方式是hash & (table.length -1)), 将这个值作为元素将要放到数组的链表位置.  
        接下来有两种情况: 没有插入过这个key,和已经插入过.  
        没有插入过,将key和value插入到key对应的数组对应链表头部.  
        插入过又分为两种情况: key相同,key不同但hash相同(hash冲突).  
        key相同的情况: 设置新的值,并返回原来的值.  
        key的hash相同: 将新的值插入到原先的数组对应位置的链表头部  

    > 在往map中插入新值的时候会出现数组扩容的情况,触发条件是当前元素没有插入时,size >= 扩容阈值, 并且key对应的数组位置上已经有值, 扩容后的数组大小是原先数组大小的两倍,并且,原先的链表上的每个元素key都需要重新计算hash值, 重新分配到数组上  

- get(K key)    

        这个方法用于从map中获取数据, 这里分为key为null或不为空. 
        如果为空的话,执行getForNullKey()方法,这个方法会去数组第一个位置的链表中遍历出key为null的Entry. 
        如果不为空, 则执行getEntry方法, 计算hash,再计算hash在数组中的偏移量, 再对这个偏移量位置的链表进行遍历, 再将获取到的Entry的value返回   

### JDK7 ConcurrentHashMap 结构示意图  
![jdk7ConcurrentHashMap](/jdk/struct/jdk7ConcurrentHashMap.png)       

> 注意这里的segment, 分段锁.    
> ConcurrentHashMap中包含的数组元素变为Segment类, 每个Segment中包含一个HashEntry<K,V>数组, 数组元素是一个单向列表, 也就是说,每个Segment看起来像一个小MAP    
> 这个MAP实现的接口也不一样,是ConCurrentMap<K,V>

### JDK7 ConcurrentHashMap的方法  

1.   构造方法  
> 这个方法主要是通过concurrentLevel计算并设置ConcurrentHashMap的segmentShift和segmentMask字段    
> 初始化Segment数组

```java
    /**
     * initialCapacity 初始化容量,这个容量是Map的总容量
     * loadFactor 负载因子,每个segment都有一个负载因子(为什么要放在Segment里)
     * concurrencyLevel 并发级别, 跟Segment数组的长度有关
     * */
    public ConcurrentHashMap(int initialCapacity,
                             float loadFactor, int concurrencyLevel) {
        //检查参数                        
        if (!(loadFactor > 0) || initialCapacity < 0 || concurrencyLevel <= 0)
            throw new IllegalArgumentException();
        //并发级别最大 2的16次方
        if (concurrencyLevel > MAX_SEGMENTS)
            concurrencyLevel = MAX_SEGMENTS;
        // Find power-of-two sizes best matching arguments
        //这两个数纯粹用于计算下面的segmentShift和segmentMask
        //sshift是当ssize大于等于concurrentLevel时的指数
        int sshift = 0;
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ++sshift;
            ssize <<= 1;
        }
        //Segment数组偏移量,跟插入时key的hash值位移有关
        //Segment掩码,key的hash值通过掩码最终获取是设置到哪个Segment中
        //segmentShift = 32 - n
        //segmentMask = $2^n$
        this.segmentShift = 32 - sshift;
        this.segmentMask = ssize - 1;
        //最大容量约束
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        //表示每个Segment中的HashEntry数组的大小
        int c = initialCapacity / ssize;
        //这是确保 Segment数量和每个Segment的容量乘积不小于初始容量
        if (c * ssize < initialCapacity)
            ++c;
        //最小Segment中的长度为2, cap最终是2的幂
        int cap = MIN_SEGMENT_TABLE_CAPACITY;
        while (cap < c)
            cap <<= 1;
        // 初始化Segment数组,并创建一个Segment作为数组的第一个元素
        Segment<K,V> s0 =
            new Segment<K,V>(loadFactor, (int)(cap * loadFactor),
                             (HashEntry<K,V>[])new HashEntry[cap]);
        Segment<K,V>[] ss = (Segment<K,V>[])new Segment[ssize];
        //原子操作, 将Segment写入Segment数组
        UNSAFE.putOrderedObject(ss, SBASE, s0); // ordered write of segments[0]
        //赋值到ConcurrentHashMap的segments字段
        this.segments = ss;
    }
```

2.   put(K key, V value)方法

```java
/**
 * 首先判空,如果value为空,抛出空指针, 所以concurrentHashMap不支持空value,
 * 再获取对应的segment, 如果segment没有初始化则初始化.
 * 最终调用的是put(key,hash,value,false)方法
 * */
public V put(K key, V value) {
        Segment<K,V> s;
        if (value == null)
            throw new NullPointerException();
        //计算key的hash值
        int hash = hash(key);
        //hash在右移segmentShift位之后,只剩高位,高位数量就是segmentMask的2进制位数,而且segmentMask每一位都是1
        int j = (hash >>> segmentShift) & segmentMask;
        if ((s = (Segment<K,V>)UNSAFE.getObject          // nonvolatile; recheck
             (segments, (j << SSHIFT) + SBASE)) == null) //  in ensureSegment
            s = ensureSegment(j);
        return s.put(key, hash, value, false);
    }
```
   1.  ensureSegment(int j) 方法
        > 确保Map中第j个Segment不为空  
        > 总体流程比较明确, 以Segment[0]为原型,创建length和loadfactor一样的HashEntry数组(或者用代码中的table)  
        > 有一点要注意就是在创建Segment之前重新检查了一下Segment是否为空,并且创建创建完了之后在将Segment设置到Segment数组之前还检查了一遍,而且这里用的是while循环,应该是为了多线程同时在这个位置创建Segment考虑  
        > 参考的网页上提到,不清楚为什么在这里需要用while来判断   

        ```java
        private Segment<K,V> ensureSegment(int k) {
        final Segment<K,V>[] ss = this.segments;
        //通过UNSAFE获取Segment需要用偏移量
        long u = (k << SSHIFT) + SBASE; // raw offset
        Segment<K,V> seg;
        //第一次判空
        if ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u)) == null) {
            //以Segment[0]为原型
            Segment<K,V> proto = ss[0]; // use segment 0 as prototype
            int cap = proto.table.length;
            float lf = proto.loadFactor;
            int threshold = (int)(cap * lf);
            HashEntry<K,V>[] tab = (HashEntry<K,V>[])new HashEntry[cap];
            //第二次判空
            if ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u))
                == null) { // recheck
                //设置的参数都跟Segment[0]一样,如果Segment扩容过,这边应该也会扩容,但机会应该不大,不太可能出现Segment[0]扩容了但还有Segment没有初始化的情况
                Segment<K,V> s = new Segment<K,V>(lf, threshold, tab);
                //循环判空
                while ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u))
                       == null) {
                //CAS操作
                    if (UNSAFE.compareAndSwapObject(ss, u, null, seg = s))
                        break;
                }
            }
        }
        return seg;
        }
        ```

   2.   Segment.put(K key, int hash, V value, boolean onlyIfAbsent) 
        > 第一步获取独占锁,因为Segment继承了ReentrantLock,可以直接调用tryLock
        > 第二步获取要插入的表偏移量, 根据偏移量获取表中对应链表的头部  
        > 第三步, 循环遍历获取的链表, 如果链表上有数据,一个个判断key是否相等(判断条件就是按== || (hash && equal)), 如果没有数据, 将现在的元素放在链表头部 
        > 第四步, Segment的count加1, 并计算是否需要扩容(Segment中所有元素数量是否到了阈值,并且是否小于最大值$2^{30}$), 保存元素,并将oldValue置空, 跳出循环
        ```java
        final V put(K key, int hash, V value, boolean onlyIfAbsent) {
            //获取锁
            //
            HashEntry<K,V> node = tryLock() ? null :
                scanAndLockForPut(key, hash, value);
            V oldValue;
            try {
                HashEntry<K,V>[] tab = table;
                //获取key的hash值在HashEntry表的偏移量
                int index = (tab.length - 1) & hash;
                //获取对应链表的表头
                HashEntry<K,V> first = entryAt(tab, index);
                for (HashEntry<K,V> e = first;;) {
                    //这一步跟HashMap中的put一样,判断两个key是否相同
                    //注意一下这里的判断逻辑
                    if (e != null) {
                        K k;
                        if ((k = e.key) == key ||
                            (e.hash == hash && key.equals(k))) {
                            oldValue = e.value;
                            if (!onlyIfAbsent) {
                                e.value = value;
                                ++modCount;
                            }
                            break;
                        }
                        e = e.next;
                    }
                    else {
                        //如果获取锁失败
                        if (node != null)
                            node.setNext(first);
                        else
                        //实例化HashEntry
                            node = new HashEntry<K,V>(hash, key, value, first);
                        //根据需要扩容或直接设置元素
                        int c = count + 1;
                        if (c > threshold && tab.length < MAXIMUM_CAPACITY)
                            rehash(node);
                        else
                            setEntryAt(tab, index, node);
                        ++modCount;
                        count = c;
                        //这个值是要返回的,但看不出为什么不在上面就设置为空而是要在这边设置
                        oldValue = null;
                        break;
                    }
                }
            } finally {
                //别忘了释放锁
                unlock();
            }
            return oldValue;
        }
        ```
    3. Segment.scanAndLockForPut(K key, int hash, V value)方法
        > 当在Segment.put方法开始没有获取到锁时,需要调用这个方法获取到锁,并且有可能会返回一个新的HashEntry. 方法内通过自旋获取锁, 上限是根据CPU核数来的,如果是多核就是64次,单核就1次.  这个方法有一个地方要注意就是自旋锁内的最后一个else if, 这个逻辑表示出现并发往同一个Segment的table的同一个链表上插入元素, 处理方法是重新获取表头,重置获取锁次数, 重新开始一边获取锁流程  
        第一步获取对应链表上的表头, 设置获取锁次数为-1  
        开始自旋锁, 当获取锁的次数小于0, 有三种情况  
          第一种, 链表的表头为空, 这时候需要实例化一个HashEntry,并将获取锁次数置为0.  第二种, 不为空,但表头的key和方法的参数的key不一样, 需要沿着链表往下.  第三种, 两个key一样, 则设置获取锁次数为0. 除了第二种情况以外, 获取锁次数都会变为0.  
        当获取锁次数大于等于0, 将次数+1, 如果已经超出最大获取锁次数限制, 调用lock方法阻塞获取锁,获取锁后跳出循环, 否则再次tryLock  
        当获取锁次数为偶数时, 如果获取到的表头元素改变,则重新开始获取锁  

        ```java
        private HashEntry<K,V> scanAndLockForPut(K key, int hash, V value) {
            //获取表头
            HashEntry<K,V> first = entryForHash(this, hash);
            HashEntry<K,V> e = first;
            //初始化node为空
            HashEntry<K,V> node = null;
            //初始化重复次数为-1
            int retries = -1; // negative while locating node
            //尝试获取锁
            while (!tryLock()) {
                //里面的逻辑都是获取锁失败后的逻辑
                HashEntry<K,V> f; // to recheck first below
                //刚开始获取次数还小于0
                if (retries < 0) {
                    //判断表头是否为空
                    if (e == null) {
                        //node是否为空,如果不为空,那这段逻辑已经执行过
                        if (node == null) // speculatively create node
                            node = new HashEntry<K,V>(hash, key, value, null);
                        //获取次数置为0
                        retries = 0;
                    }
                    //如果两边的key一样, 置获取次数为0
                    else if (key.equals(e.key))
                        retries = 0;
                    //否则沿着链表往下
                    else
                        e = e.next;
                }
                //当获取次数超过上限,通过阻塞获取锁,并跳出
                else if (++retries > MAX_SCAN_RETRIES) {
                    lock();
                    break;
                }
                //这个逻辑表示出现并发问题,需要重新获取锁
                else if ((retries & 1) == 0 &&
                         (f = entryForHash(this, hash)) != first) {
                    e = first = f; // re-traverse if entry changed
                    retries = -1;
                }
            }
            //返回可能实例化过的node
            return node;
        }
        ```
    4. rehash(HashEntry<K,V> node) 扩容方法  
        > 将Segment中的table扩容一倍, 重新分配元素,并将新元素插入,这个方法出发在put方法中,并且此时已经拿到互斥锁,所以不用考虑并发问题  
        ```java
        private void rehash(HashEntry<K,V> node) {
            
            HashEntry<K,V>[] oldTable = table;
            int oldCapacity = oldTable.length;
            //新容量为原来的两倍
            int newCapacity = oldCapacity << 1;
            //新阈值计算
            threshold = (int)(newCapacity * loadFactor);
            //实例化新的HashEntry
            HashEntry<K,V>[] newTable =
                (HashEntry<K,V>[]) new HashEntry[newCapacity];
            //掩码, 11111很多个1
            int sizeMask = newCapacity - 1;
            for (int i = 0; i < oldCapacity ; i++) {
                //表头
                HashEntry<K,V> e = oldTable[i];
                if (e != null) {
                    //如果只有表头一个元素,那只要将这个元素重新新分配一下就行
                    HashEntry<K,V> next = e.next;
                    int idx = e.hash & sizeMask;
                    if (next == null)   //  Single node on list
                        newTable[idx] = e;
                    else { // Reuse consecutive sequence at same slot
                        HashEntry<K,V> lastRun = e;
                        int lastIdx = idx;
                        //遍历剩下的元素,将最后一个计算出的偏移量与idx不同的元素放到新数组对应表头,这时这个元素后面的元素也相当于到了新的数组
                        for (HashEntry<K,V> last = next;
                             last != null;
                             last = last.next) {
                            int k = last.hash & sizeMask;
                            if (k != lastIdx) {
                                lastIdx = k;
                                lastRun = last;
                            }
                        }
                        newTable[lastIdx] = lastRun;
                        // Clone remaining nodes
                        //从表头到上一个循环找到的新数组的表头元素,计算k,放到对应的元素中
                        for (HashEntry<K,V> p = e; p != lastRun; p = p.next) {
                            V v = p.value;
                            int h = p.hash;
                            int k = h & sizeMask;
                            HashEntry<K,V> n = newTable[k];
                            newTable[k] = new HashEntry<K,V>(h, p.key, v, n);
                        }
                    }
                }
            }
            //添加元素
            int nodeIndex = node.hash & sizeMask; // add the new node
            node.setNext(newTable[nodeIndex]);
            newTable[nodeIndex] = node;
            table = newTable;
        }
        ```  

### JDK8的HashMap结构示意图  
![JDK8的HashMap结构示意图](/jdk/struct/jdk8HashMap.png)  
> jdk8中,HashMap中添加了红黑树,具体是在链表中元素超过8以后,链表就变成红黑树结构,在链表元素缩回到8以后,红黑树又变回为链表结构.  
> 这是因为在jdk7中,结构为数组加链表的结构, 当访问一个元素时, 定位出数组的位置很快, 接下去访问元素就需要遍历链表,时间复杂度为O(n), 通过将链表结构转换为红黑树,可以将时间复杂度降低到O($\log$N).  
> HashMap支持保存null  
> HashMap中的元素又两种模式,一种为Node,此时元素是链表形态,另一种为TreeNode,此时元素是红黑树形态

  1. put(int hash, K key, V value, boolean onlyIfAbsent, boolean evict)方法  
        > 上一步是put(K key, V value), 此时evict为true, onlyIfAbsent为false  

        ```java
        final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
            Node<K,V>[] tab; Node<K,V> p; int n, i;
            //判空并初始化table
            if ((tab = table) == null || (n = tab.length) == 0)
                n = (tab = resize()).length;
            //放到表头
            if ((p = tab[i = (n - 1) & hash]) == null)
                tab[i] = newNode(hash, key, value, null);
            else {
                Node<K,V> e; K k;
                //如果表头的key跟新元素的key相同,则设置新元素,返回老元素
                //此时p为key的hash对应的表头元素
                if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                    e = p;
                //红黑树结构的存放
                else if (p instanceof TreeNode)
                    e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
                //既不是表头,也不是红黑树结构
                else {
                    for (int binCount = 0; ; ++binCount) {
                        if ((e = p.next) == null) {
                            p.next = newNode(hash, key, value, null);
                            //插入新数据, 并且需要将链表扩容成红黑树
                            if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                                treeifyBin(tab, hash);
                            break;
                        }
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                            break;
                        p = e;
                    }
                }
                if (e != null) { // existing mapping for key
                    V oldValue = e.value;
                    if (!onlyIfAbsent || oldValue == null)
                        e.value = value;
                    afterNodeAccess(e);
                    return oldValue;
                }
            }
            ++modCount;
            if (++size > threshold)
                resize();
            afterNodeInsertion(evict);
            return null;
        }
        ```
