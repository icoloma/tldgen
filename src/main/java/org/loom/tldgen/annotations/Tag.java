package org.loom.tldgen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


/**
 * A JSP tag to be included in the generated TLD.
 * @author jralonso
 *
 */
@Target(ElementType.TYPE)
@Documented
public @interface Tag {

	/** the body-content type, default {@link BodyContent SCRIPTLESS} */
	BodyContent bodyContent() default BodyContent.SCRIPTLESS;

	/** the unique tag name. If empty, the unqualified class name will be used without the "Tag" suffix */
	String name() default "";
	
	/**
	 * A short name that is intended to be displayed by tools, default none
	 */
	String displayName() default "";
	
	/**
	 * Optional icon element that can be used by tools, default none
	 */
	String icon() default "";
	
	/**
	 * Whether this tag supports additional attributes with dynamic names.  If true, the tag-class must implement the
	 * javax.servlet.jsp.tagext.DynamicAttributes interface.  Defaults to false.
	 */
	boolean dynamicAttributes() default false;

	/**
	 * Optional informal description of an example of a use of this tag
	 */
	String example() default "";
	
}
