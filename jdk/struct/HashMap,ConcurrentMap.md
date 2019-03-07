## HashMap和ConcurrentHashMap的分析

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
