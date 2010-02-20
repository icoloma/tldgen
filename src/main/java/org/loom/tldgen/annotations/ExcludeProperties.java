package org.loom.tldgen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Specify the attributes of the parent classes that should be ignored.
 * @author icoloma
 *
 */
@Target(ElementType.TYPE)
@Documented
public @interface ExcludeProperties {

	/**
	 * The list of attributes of the parent classes that should be ignored
	 */
	String[] value() default {};
	
}
