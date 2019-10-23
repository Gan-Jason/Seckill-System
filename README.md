## 商品秒杀系统  


* 一个基于Spring,Spring MVC,Mybatis框架的实战项目



* 流程图
![](https://github.com/Gan-Jason/Seckill-System/blob/master/system-architecture.png?raw=true)  

* RESTful接口  
![](https://github.com/Gan-Jason/Seckill-System/blob/master/RESTful.png?raw=true)

* 分布式架构方案
![](https://github.com/Gan-Jason/Seckill-System/blob/master/distributed.png?raw=true)


* 项目源码结构  
├─main
│  ├─java
│  │  └─com
│  │      └─gan
│  │          ├─dao
│  │          ├─dto
│  │          ├─entity
│  │          ├─enums
│  │          ├─exception
│  │          ├─service
│  │          │  └─impl
│  │          └─web
│  ├─resources
│  │  ├─mapper
│  │  └─spring
│  ├─sql
│  └─webapp
│      ├─resource
│      │  └─script
│      └─WEB-INF
│          └─jsp
│              └─common
└─test
    └─java
        └─com
            └─gan
                ├─dao
                └─service
                    └─impl
