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
        > 第一步, 
