# dubbo对外提供接口
    
###配置

    配置分两部分,一部分配置注册中心信息,另一部分配置本服务信息
    注册中心,以zk为例
```xml
<xml>
   <dubbo:application name="${dubbo.application}"/>
       <!-- 定义 zookeeper 注册中心地址及协议 -->
   <dubbo:registry address="${dubbo.registry.address}" file="${dubbo.cache.file}"/>
</xml>

```
    application表示以什么应用名注册到注册中心