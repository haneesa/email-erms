package com.little.star.email.erms.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.little.star.email.erms.configurations.ERMSConfiguration;

/***
 * Annotation - to be used in the consumer Application
 * 
 * @author haneesa (Haneesa Abdulhakkim)
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ERMSConfiguration.class)
public @interface EmailERMS {

}
