package org.loom.tldgen.factory;

import static org.loom.tldgen.util.JavadocUtils.getAnnotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loom.tldgen.model.Function;
import org.loom.tldgen.model.Library;
import org.loom.tldgen.model.Tag;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

/**
 * Creates a {@link Library} instance from the javadoc information
 * @author icoloma
 *
 */
public class LibraryFactory {

	private static Log log = LogFactory.getLog(LibraryFactory.class);
	
	/**
	 * Create the intermediate memory model for the javadoc information
	 */
	public Library parse(RootDoc root) {
		
		log.info("Parsing TLD...");
		Library library = new Library();
		ClassDoc[] classes = root.classes();
		for (ClassDoc clazz : classes) {
        	Tag tag = parseTag(clazz);
        	if (tag != null) {
        		library.add(tag);
        	} else {
        		recollectFunctionData(clazz, library);
        	}
        }
		return library;
		
	}
	
	/**
	 * Parse a Tag class from this class annotations
	 * @return the Tag class parsed for the provided class, null if  none
	 */
	private Tag parseTag(ClassDoc doc) {
		return !doc.isAbstract() && getAnnotation(doc, org.loom.tldgen.annotations.Tag.class) != null? Tag.createInstance(doc) : null;
	}

	
	/**
	 * Parse the methods of a class and its superclasses
	 * @param clazz the class to parse
	 * @param library the library objecto to insert the {@link Function} instances
	 */
	private void recollectFunctionData(ClassDoc clazz, Library library) {		
		for (MethodDoc methodDoc : clazz.methods()) {
			if (methodDoc.isStatic() && !methodDoc.isPrivate()) {
				AnnotationDesc annotation = getAnnotation(methodDoc, org.loom.tldgen.annotations.Function.class);
				if (annotation != null){
					library.add(Function.createinstance(methodDoc, annotation));
				}
			}
		}
	}
	
}
