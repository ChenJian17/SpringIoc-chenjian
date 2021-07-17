## 前言
* 手写简化版的Spring容器，借此项目熟悉spring源码和掌握spring的核心原理

## Spring-IOC
> **IOC的思路：**  
1.项目启动，解析applicationContex.xml文件，解析XML最常见的方式：Do4J工具 : com.chenjian  
2.递归扫描com.chenjian包(com/chenjian/)下:所有的class文件  com.chenjian.service.imppl.xxx.class 存入集合中
3.根据class路径获取类的全路径 com.chenjian.service.impl.xxx、com.chenjian.service.xxx、com.chenjian.controller.xxx
4.获取所有的class全路径，循环判断(反射)这个类是否有 @Controller注解、@Service注解，如果类上有类似上述注解，以为这个类需要反射对象，
  然后把对象存入IOC的集合中(ConcurrentHashMap 线程安全)
5.遍历全路径的类，反射这个类中的属性，使用反射检测属性上是否有@Autowired注解或者@Resource,如果有那么就需要进行属性注入，按照类型匹配或者名字匹配
  如果匹配到多个抛异常，如果匹配到0个也抛异常
6.对外提供一个获取IOC容器对象的API，context.getBean(OrderController.class)
> SpringIOC实现流程图概览
![image](https://user-images.githubusercontent.com/31843897/126051773-305cb77e-43f0-4dac-8843-3fb9043a2706.png)

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

## Spring事务
* 本质上Spring的事务管理也是使用AOP操作，这里简述下我的理解：
> * 数据库的增删查改也是个事务，这是数据库层面的事务，只是更细。但在代码里的事务需要更粗一些，比如将两个数据库操作合为一个事务，这时候就要
> * 使用代理。将方法所在的类进行代理。
>

## SpringMvc
* 使用单例模式使得ClassPathXmlApplicationContext只被创建一次
* SpringMVC其实就是接收参数响应数据的框架
