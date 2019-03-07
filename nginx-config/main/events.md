## events模块包含nginx中所有连接的设置  
-   举例  
    ```
    events {
        use epoll;
        worker_connections 20000;
        client_header_buffer_size 4k;
        open_file_cache max=2000 inactive=60s;
        open_file_cache_valid 60s;
        open_file_cache_min_uses 1;
    }
    ```

-   use epoll

        使用epoll I/O模型(如果不设置,Nginx自己选一个最适合系统的模型)   
        有以下几种模型:     
        标准事件模型:   select,poll 
        高效事件模型:   
            Kqueue:  用于reeBSD 4.1+, OpenBSD 2.9+, NetBSD 2.0 和 MacOS X.使用双处理器的MacOS X系统使用kqueue可能会造成内核崩溃 
            Epoll:  使用于Linux内核2.6版本及以后的系统  
            /dev/poll: 使用于Solaris 7 11/99+, HP/UX 11.22+ (eventport), IRIX 6.5.15+ 和 Tru64 UNIX 5.1A+   
            Eventport：使用于Solaris 10. 为了防止出现内核崩溃的问题， 有必要安装安全补丁    

-   worker-connections 20000;    

        工作进程的最大连接数, 理论上Nginx服务器的最大连接数为worker-connections * worker-processes  

-   keepalive_time 60;

        keepalive超时时间, 这里是http层面的keepalive, 而非tcp的keepalive, 这点可以查看 [KeepAlive详解](http://www.bubuko.com/infodetail-260176.html)  


-   client_header_buffer_size 4k;

        
