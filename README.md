## 前言
* 手写简化版的Spring容器，借此项目熟悉spring源码和掌握spring的核心原理

## Spring-IOC
* 已完成IOC容器iocContainer，对象以class为key
* 已完成IOCName容器iocNameContainer，存储对象名和对象实例,Spring的IOC以名字为key的容器
* 已完成IOC接口容器iocInterfaces，对象实现的接口为key，接口的实现类对象为value
* 已完成实现依赖注入DI
* 待完成：Spirng中Bean的作用域
* 待完成：BeanDefinition对象
* 待完成：BeanNameAware接口
* 待完成：InitializingBean和BeanPostProcessor接口

## Spring-AOP
* Aop要在依赖注入前还是依赖注入后
* 如何代理，Procedd类