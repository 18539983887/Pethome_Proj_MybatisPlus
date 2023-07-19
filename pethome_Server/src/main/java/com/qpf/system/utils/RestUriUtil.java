package com.qpf.system.utils;

import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Rest风格Controll的工具类
 */
public class RestUriUtil {

    /**
     * 获取Rest风格Controller中方法的请求路径
     *
     * @param clazz  Controller字节码文件
     * @param method Controller中的方法
     * @return
     */
    public static String getUri(Class clazz, Method method) {
        //获取类上的请求路径：/department
        String classPath = "";
        //从类上去找RequestMapping注解
        Annotation annotation = clazz.getAnnotation(RequestMapping.class);
        if (annotation != null) {
            RequestMapping requestMapping = (RequestMapping) annotation;
            //获取value
            String[] values = requestMapping.value();
            if (values != null && values.length > 0) {
                classPath = values[0];
                if (!"".equals(classPath) && !classPath.startsWith("/"))
                    classPath = "/" + classPath;
            }
        }
        //以下是获取方法上的请求路径：/{id}
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        String methodPath = "";
        if (getMapping != null) {
            //获取方法上的GetMapping注解的value值
            String[] values = getMapping.value();
            if (values != null && values.length > 0) {
                methodPath = values[0];
                if (!"".equals(methodPath) && !methodPath.startsWith("/"))
                    methodPath = "/" + methodPath;
            }
        }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            String[] values = postMapping.value();
            if (values != null && values.length > 0) {
                methodPath = values[0];
                if (!"".equals(methodPath) && !methodPath.startsWith("/"))
                    methodPath = "/" + methodPath;
            }
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            String[] values = deleteMapping.value();
            if (values != null && values.length > 0) {
                methodPath = values[0];
                if (!"".equals(methodPath) && !methodPath.startsWith("/"))
                    methodPath = "/" + methodPath;
            }
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (putMapping != null) {
            String[] values = putMapping.value();
            if (values != null && values.length > 0) {
                methodPath = values[0];
                if (!"".equals(methodPath) && !methodPath.startsWith("/"))
                    methodPath = "/" + methodPath;
            }

        }
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        if (patchMapping != null) {
            String[] values = patchMapping.value();
            if (values != null && values.length > 0) {
                methodPath = values[0];
                if (!"".equals(methodPath) && !methodPath.startsWith("/"))
                    methodPath = "/" + methodPath;
            }
        }
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            String[] values = requestMapping.value();
            if (values != null && values.length > 0) {
                methodPath = values[0];
                if (!"".equals(methodPath) && !methodPath.startsWith("/"))
                    methodPath = "/" + methodPath;
            }
        }
        return classPath + methodPath;  // /department/{id}
    }
}
