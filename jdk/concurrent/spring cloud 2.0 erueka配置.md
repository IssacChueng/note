####server pom.xml中eureka依赖为
```xml
       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
       </dependency>

```
####client pom.xml中的eureka依赖为
```xml
       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
       </dependency>
```

    这两个依赖需要一一对应，否则@EnableDicoveryClient无效