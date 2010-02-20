package org.tldgen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * A tag attribute according to the <a href="http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd">JSP schema</a>.
 * @author jralonso
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Attribute {
	
	/** The name of the attribute. If empty, the java attribute name will be used. */
	String name() default "";

	/** Whether the attribute is required or optional, default false*/
	boolean required() default false;         

	/** 
	 * Whether the attribute is a runtime attribute, default true.  
	 * Notice that this default value is different from the one specified 
	 * in the schema, since this is a far more common use case. 
	 */
	boolean rtexprvalue() default true;

}
