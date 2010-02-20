package org.tldgen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * A JSP <a href="http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd">function</a>.
 * @author jralonso
 *
 */
@Target(ElementType.METHOD)
@Documented
public @interface Function {

	/**
	 * Optional icon element to be displayed by tools
	 */
	String icon() default "";
	
	/**
	 * Optional informal description of an example of use of this function
	 */
	String example() default "";

	/**
	 * Optional short name to be displayed by tools
	 */
	String displayName() default "";

}
