package com.qpf.system.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 字节码文件工具类
 */
public class ClassUtils {
    /**
     * 获取指定包中所有类的字节码对象:
     *
     * @param pack 指定包名
     * @return 字节码对象集合
     */
    public static Set<Class<?>> getClasses(String pack) {
        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字,并进行替换.
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            //获取当前项目中所有包含指定包结构的路径
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代下去
            while (dirs.hasMoreElements()) {
                //获取每个路径资源
                URL url = dirs.nextElement();
                //得到每个路径资源的协议名称（也可以理解为资源类型）
                String protocol = url.getProtocol();
                //如果是文件形式（file也代表文件夹），就保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 获取文件夹下所有的class文件，并保存到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }


    /**
     * 2.获取文件夹下所有的class文件，并保存到集合中
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName 包名（真实报名）
     * @param packagePath 包路径（真实物理路径）
     * @param recursive   是否递归获取
     * @param classes     字节码对象集合
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 1.把包路径转换为File对象
        File dir = new File(packagePath);
        //2.判断，如果不存在或者不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        //3.判断，如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //4.循环所有文件和目录
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                //5. 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // classes.add(Class.forName(packageName + '.' + className));
                    // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
}