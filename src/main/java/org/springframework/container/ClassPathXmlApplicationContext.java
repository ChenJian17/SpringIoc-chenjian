package org.springframework.container;

import org.springframework.annotation.*;
import org.springframework.proxy.JdkProxy;
import org.springframework.xml.SpringConfigPaser;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by chenjian on 2021/6/11 21:59
 */
public class ClassPathXmlApplicationContext {

    //application-context.xml
    private String springConfig="applicationContext.xml";//默认的配置文件名字

    //存储扫描路径下所有的类的全地址
    private List<String> classPaths = new ArrayList<>();

    //存储对象名和对象实例,Spring的IOC以名字为key的容器
    private Map<String, Object> iocNameContainer = new ConcurrentHashMap<>();

    //Spring的IOC容器，对象以class为key
    private Map<Class<?>, Object> iocContainer = new ConcurrentHashMap<>();

    //Spring的IOC容器，对象实现的接口为key，接口的实现类对象为value
    private Map<Class<?>, List<Object>> iocInterfaces = new ConcurrentHashMap<>();

    //存储切面类的集合
    private Set<Class<?>> aopClassSet = new CopyOnWriteArraySet<Class<?>>();

    //存储的是要被动态代理的类
    private Set<Class<?>> proxyedClassSet = new CopyOnWriteArraySet<Class<?>>();


    public ClassPathXmlApplicationContext(String springConfig){
        this.springConfig = springConfig;
        init();
    }

    //最开始的初始化方法
    private void init(){
        //调用XML的解析方法
        String basePackage=SpringConfigPaser.getBasePackage(springConfig);
        System.out.println("Spring的要扫描的基础包："+basePackage);
        //加载所有的类
        this.loadClasses(basePackage);
        //执行类的实例化，把扫描包下的class反射创建了对象
        this.doInitInstances();

        //在依赖注入前执行AOP
        this.doAop();

        System.out.println("IOCName容器："+iocNameContainer);
        System.out.println("IOC容器："+iocContainer);
        System.out.println("IOC接口容器："+iocInterfaces);

        //实现对象的依赖注入
        this.doDI();

    }

    /**
     * 在执行Aop操作
     */
    private void doAop() {
        //遍历切面类集合
        if (aopClassSet.size() > 0){
            try {
                for (Class<?> c: aopClassSet){
                    //获取切面类中的方法
                    Method[] declaredMethods = c.getDeclaredMethods();
                    if (declaredMethods != null){
                        for (Method method: declaredMethods){
                            boolean annotationPresent = method.isAnnotationPresent(Around.class);
                            if (annotationPresent){
                                Around around = method.getAnnotation(Around.class);
                                //切入点表达式:com.chenjian.service.impl.OrderServiceImpl.addOrder
                                String execution = around.execution();
                                //要代理的目标类：com.chenjian.service.impl.OrderServiceImpl
                                String fullClass = execution.substring(0, execution.lastIndexOf("."));
                                //要被代理的方法的名字
                                String methodName = execution.substring(execution.lastIndexOf(".")+1);
                                //要代理的目标类
                                Class<?> targetClass = Class.forName(fullClass);
                                //将要被动态代理的类存起来
                                proxyedClassSet.add(targetClass);
                                //要被代理的类的对象--目标对象
                                Object targetObject = iocContainer.get(targetClass);

                                //在生成代理类对象之前，先注入一把
                                doDIByClasses(targetClass);

                                //准备把targetObject中的methodName方法进行动态代理
                                JdkProxy<Object> jdkProxy = new JdkProxy<Object>(c,targetObject,methodName,method);
                                //代理类的对象(OrderServiceImpl的代理对象)
                                Object proxyInstance = jdkProxy.getProxyInstance();
                                //com.sun.proxy.$Proxy6
                                //System.out.println(proxyInstance.getClass().getName());
                                //将原始集合中的对象替换成新的被代理之后的对象  替换----》iocContainer
                                iocContainer.put(targetClass,proxyInstance);

                                //2.替换iocNameContainer
                                String simpleName = targetClass.getSimpleName();
                                String className = String.valueOf(simpleName.charAt(0)).toLowerCase()+simpleName.substring(1);
                                Service service = targetClass.getAnnotation(Service.class);
                                Controller controller = targetClass.getAnnotation(Controller.class);
                                Repository repository = targetClass.getAnnotation(Repository.class);
                                if (service != null){
                                    String value = service.value();
                                    if (!"".equals(value)){
                                        className = value;
                                    }
                                }else if (controller != null){
                                    String value = controller.value();
                                    if (!"".equals(value)){
                                        className = value;
                                    }
                                }else if (repository !=null){
                                    String value = repository.value();
                                    if (!"".equals(value)){
                                        className = value;
                                    }
                                }
                                iocNameContainer.put(className, proxyInstance);

                                //3.替换接口集合容器
                                Class<?>[] interfaces = targetClass.getInterfaces();
                                if (interfaces != null){
                                    for (Class<?> anInterface: interfaces){
                                        List<Object> objects = iocInterfaces.get(anInterface);
                                        if (objects != null){
                                            for (int i=0; i<objects.size(); i++){
                                                Object o = objects.get(i);
                                                if (o.getClass() == targetClass){
                                                    objects.set(i, proxyInstance);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }

        }
    }

    /**
     * 执行依赖注入
     */
    private void doDI() {
        Set<Class<?>> classes = iocContainer.keySet();
        if (classes != null){
            for (Class<?> aclass: classes){
                if (!proxyedClassSet.contains(aclass)){
                    //把不是要代理的类注入一下，即排序要代理的类的注入
                    doDIByClasses(aclass);
                }


            }

        }

    }

    private void doDIByClasses(Class<?> aclass) {
        //一个类一个类去判断要不要注入
        //getDeclaredFields() 方法则可以获取包括私有属性在内的所有属性
        Field[] declaredFields = aclass.getDeclaredFields();
        if (declaredFields !=null){
            for (Field declaredField: declaredFields){
                //判断属性中是否有Autowired注解
                boolean annotationPresent = declaredField.isAnnotationPresent(Autowired.class);
                if (annotationPresent){
                    //类中属性需要依赖注入，给属性赋值
                    Autowired autowired = declaredField.getAnnotation(Autowired.class);
                    String value = autowired.value();
                    Object bean = null;
                    if (!"".equals(value)){
                        //(1)根据value上的名字去匹配对象
                        bean = this.getBean(value);
                        if (bean == null){
                            throw new RuntimeException("The bean named "+value+" has not been found!!!");
                        }
                    }else {
                        //(2)注解上没有名字，根据类型去匹配对象
                        Class<?> type = declaredField.getType();
                        if (bean == null){
                            //根据本类的类型去匹配
                            bean = this.getBean(type);
                            if (bean == null){
                                //(3)再去根据接口类型去匹配对象
                                bean = this.getBeanByInterface(type);
                                if (bean == null){
                                    throw new RuntimeException("No qualifying bean of type '"+aclass+"' available!!");
                                }
                            }
                        }
                    }

                    try {
                        //对象匹配到，使用反射的方式，注入给属性(类中的成员变量为private,故必须进行此操作)
                        declaredField.setAccessible(true);
                        //反射属性赋值
                        declaredField.set(iocContainer.get(aclass), bean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {

                    }
                }


            }
        }
    }

    /**
     * 把IOC容器中的类进行实例化
     */
    private void doInitInstances() {
        try{
            for (String classPath: classPaths){
                Class<?> c = Class.forName(classPath);

                //定位哪些类有@Aspect注解
                if (c.isAnnotationPresent(Aspect.class)){
                    //切面类，存储到一个容器
                    aopClassSet.add(c);
                    continue;
                }

                if (c.isAnnotationPresent(Controller.class) || c.isAnnotationPresent(Service.class) || c.isAnnotationPresent(Repository.class)){
                    //对象的实例化
                    Object o = c.newInstance();
                    Class<?>[] interfaces = c.getInterfaces();
                    if (interfaces != null){
                        for (Class<?> anInterface: interfaces){
                            //接口在IOC容器中的实现类
                            List<Object> objects = iocInterfaces.get(anInterface);
                            if (objects == null){
                                List<Object> objs = new ArrayList<>();
                                objs.add(o);
                                iocInterfaces.put(anInterface, objs);
                            }else {
                                objects.add(o);
                            }

                        }
                    }


                    //IOC容器存值
                    iocContainer.put(c, o);
                    //从该类中获取Controller注解
                    Controller controllerAnnotation = c.getAnnotation(Controller.class);
                    Service serviceAnnotation = c.getAnnotation(Service.class);
                    Repository repositoryAnnotation = c.getAnnotation(Repository.class);
                    //如果有Controller注解
                    if (controllerAnnotation != null || serviceAnnotation != null || repositoryAnnotation != null) {
                        //获取注解的value属性值
                        String value = null;
                        if (controllerAnnotation != null){
                            value = controllerAnnotation.value();
                        }else if(serviceAnnotation != null){
                            value = serviceAnnotation.value();
                        }else{
                            value = repositoryAnnotation.value();
                        }
                        String objectName = "";
                        if ("".equals(value)){
                            //默认的对象的名字就是类名首字母小写
                            String className = c.getSimpleName();
                            //对象的名字
                            objectName = String.valueOf(className.charAt(0)).toLowerCase()+className.substring(1);
                        }else {
                            //注解指定了value
                            objectName = value;
                        }

                        if (iocNameContainer.containsKey(objectName)){
                            throw new Exception("Spring ioc container is already exists the bean name:"+objectName);
                        }

                        iocNameContainer.put(objectName,o);

                    }

                    //如果有Service注解
//                    if (serviceAnnotation != null) {
//                        String value = serviceAnnotation.value();
//                        String objectName = "";
//                        if ("".equals(value)){
//                            String className = c.getSimpleName();
//                            //对象的名字
//                            objectName = String.valueOf(className.charAt(0)).toLowerCase()+className.substring(1);
//                        }else {
//                            objectName = value;
//                        }
//                        //对象的实例化
//                        Object o = c.newInstance();
//                        iocContainer.put(objectName,o);
//
//                    }

                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 根据对象名字获取对象
     * @param name
     * @return
     */
    public Object getBean(String name){
        return iocNameContainer.get(name);
    }

    /**
     * 根据类型获取对象
     * @param c
     * @return
     */
    public Object getBean(Class<?> c){
        return iocContainer.get(c);
    }

    /**
     * 根据接口获取对象
     * @param c
     * @return
     */
    public Object getBeanByInterface(Class<?> c) {
        List<Object> objects = iocInterfaces.get(c);
        if (objects == null)
            return null;
        if (objects.size() > 1)
            throw new RuntimeException("No qualifying bean of type '"+c+"' available: expected single matching bean but found "+objects.size());
        return objects.get(0);

    }

    /**
     * 加载基础包下所有的classes
     * @param basePackage
     */
    private void loadClasses(String basePackage){
//        URL url = ClassPathXmlApplicationContext.class.getClassLoader().getResource("");
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        basePackage = basePackage.replace(".", File.separator);
//        System.out.println(basePackage);

        File file = new File(url.toString().replace("file:/",""),basePackage);
        String path = file.toString();
        if (path.contains("test-classes")){
            path = path.replace("test-classes", "classes");
        }
        //D:\IdeaProjects\SpringIoc-chenjian\target\classes\com\chenjian
        System.out.println(path);
        this.findAllClasses(file);


//        System.out.println(url);
    }


    private void findAllClasses(File f){
        File[] files = f.listFiles();
        for (File file: files){
            if (!file.isDirectory()){
                String fileName = file.getName();
                if (fileName.endsWith(".class")){
                    String path = this.handlerPath(file.getPath());
                    System.out.println(path);
                    classPaths.add(path);
                }
            }else {
                this.findAllClasses(file);
            }
        }

    }

    private String handlerPath(String path){
        int index = path.indexOf("classes\\");
        path = path.substring(index+8, path.length()-6);
        path = path.replace(File.separator, ".");
        return path;
    }
}
