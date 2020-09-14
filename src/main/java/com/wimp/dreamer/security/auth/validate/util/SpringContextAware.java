package com.wimp.dreamer.security.auth.validate.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zy
 * @date 2020/9/11
 * <p>
 *  application上下文对象
 */
@Component("springContextAware")
public class SpringContextAware implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextAware.applicationContext = applicationContext;
        System.out.println(applicationContext);
    }

    /**
     * 获取application上下文对象
     * @return applicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取具体bean
     * @param name 名称
     * @param <T> 具体类型
     * @return 返回该类的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name){
        return (T) applicationContext.getBean(name);
    }
}
