package org.tldgen.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.tools.javadoc.AnnotationDescImpl.ElementValuePairImpl;

/**
 * Methods that should be part of the javadoc standard
 * @author icoloma
 *
 */
public class JavadocUtils {

	/** hack to get access to each annotation attribute name */
	private static Field methAccessor;
	
	private static Logger log = LoggerFactory.getLogger(JavadocUtils.class);
	
	static {
		// hack: ElementValuePairImpl.meth is not accessible, and we need it to know about attribute names
		try {
			methAccessor = ElementValuePairImpl.class.getDeclaredField("meth");
			methAccessor.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param doc the TLD element to inspect
	 * @param annotationClass the annotation class to search for
	 * @return the annotation instance, if found. Null if  none
	 */
	public static AnnotationDesc getAnnotation(ProgramElementDoc doc, Class<? extends Annotation> annotationClass) {
		return getAnnotation(doc.annotations(), annotationClass);
	}
	
	/**
	 * 
	 * @param doc the package to inspect
	 * @param annotationClass the annotation class to search for
	 * @return the annotation instance, if found. Null if  none
	 */
	public static AnnotationDesc getAnnotation(PackageDoc doc, Class<? extends Annotation> annotationClass) {
		return getAnnotation(doc.annotations(), annotationClass);
	}
	
	private static AnnotationDesc getAnnotation(AnnotationDesc[] annotations, Class<? extends Annotation> annotationClass) {
		for (AnnotationDesc annotation : annotations) {
			if (isInstanceOf(annotation, annotationClass)) {
				return annotation;
			}
		}
		return null;
	}
	
	/**
	 * @return the String value of the specified attribute
	 */
	public static String getStringAttribute(AnnotationDesc annotation, String name) {
		return (String) getAttribute(annotation, name);
	}
	
	/**
	 * @return the Class value of the specified attribute
	 */
	public static ClassDoc getClassAttribute(AnnotationDesc annotation, String name) {
		return (ClassDoc) getAttribute(annotation, name);
	}
	
	/**
	 * @return the String[] value of the specified attribute, the empty array if it's empty
	 */
	public static String[] getStringArrayAttribute(AnnotationDesc annotation, String name) {
		AnnotationValue[] annValues = (AnnotationValue[]) getAttribute(annotation, name);
		String[] values = new String[annValues.length];
		for (int i = 0; i < annValues.length; i++) {
			values[i] = (String) annValues[i].value();
		}
		return values;
	}
	
	/**
	 * @return the AnnotationValue[] value of the specified attribute, the empty array if it's empty
	 */
	public static AnnotationDesc[] getAnnotationArrayAttribute(AnnotationDesc annotation, String name) {
		AnnotationValue[] values = (AnnotationValue[]) getAttribute(annotation, name);
		if (values == null) {
			return null;
		}
		AnnotationDesc[] ad = new AnnotationDesc[values.length];
		for (int i = 0; i < values.length; i++) {
			ad[i] = (AnnotationDesc) values[i].value();
		}
		return ad;
	}
	
	/**
	 * @return the String value of the specified Enum attribute
	 */
	public static String getEnumAttribute(AnnotationDesc annotation, String name) {
		FieldDoc fieldDoc = (FieldDoc) getAttribute(annotation, name);
		if (fieldDoc == null) {
			return null;
		}
		String value = fieldDoc.toString();
		return value.substring(value.lastIndexOf('.') + 1);
	}
	
	/**
	 * @return the Boolean value of the specified attribute
	 */
	public static Boolean getBooleanAttribute(AnnotationDesc annotation, String name) {
		Object value = getAttribute(annotation, name);
		return value == null? null : Boolean.parseBoolean(value.toString());
	}

	/**
	 * 
	 * @param annotation the annotation to inspect
	 * @param name the name of the attribute to retrieve
	 * @return the value of the specified attribute in the specified annotation
	 */
	private static Object getAttribute(AnnotationDesc annotation, String name) {
		try {
			name += "()";
			for (ElementValuePair pair : annotation.elementValues()) {
				String attributeName = methAccessor.get(pair).toString();
				if (name.equals(attributeName)) {
					return pair.value().value();
				}
			}
			return null;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * @return true if the annotation is an instance of the provided annotation class 
	 */
	private static boolean isInstanceOf(AnnotationDesc annotation, Class<? extends Annotation> annotationClass) {
		
		try {
			AnnotationTypeDoc typeDoc = annotation.annotationType();
			return annotationClass.getName().equals(typeDoc.qualifiedName());
		} catch (ClassCastException e) {
			// HEY! If you are looking here because  
			// "ClassDocImpl cannot be cast to AnnotationTypeDoc", the workaround is
			// adding your runtime jars to the classpath:
			// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6442982
			
			// but we are safe to assume that it was no TLDGen annotation,  so we are safe in returning false.
			log.debug("Detected CCE (javadoc bug 6442982), ignoring");
			return false;
		}
	}
}
