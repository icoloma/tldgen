package org.tldgen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A JSP <a href="http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_1.xsd">variable</a>.
 * Variables are explained <a href="http://download.oracle.com/docs/cd/B25221_05/web.1013/b14430/taglibs.htm#sthref456">here</a>. 
 * @author icoloma
 *
 */
@Target({ ElementType.FIELD })
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Variable {

	/** 
	 * The variable name as a constant. Required if this annotation 
	 * is used inside the variables attribute of the {@link Tag} annotation 
	 */
	String nameGiven() default "";

	/** description of the variable */
	String description() default "";
	
	/** Whether the variable is declared or not. True is the default. */
	boolean declare() default true;
	
	/** The scope of the scripting variable. NESTED is default. */
	VariableScope scope() default VariableScope.NESTED;
	
	/** Class of the variable, default String */
	Class<?> variableClass() default String.class;
	
}
