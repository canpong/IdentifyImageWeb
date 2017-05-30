/**
 * 
 */
package com.pong.reflect.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Description
 * @Author canpong
 * @CreateTime 2017年5月29日 下午2:04:43
 * @version
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface DateFormat{
	public String name() default "";
}
