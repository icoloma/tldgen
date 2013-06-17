package org.tldgen.factory;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tldgen.model.*;

import static org.tldgen.util.JavadocUtils.getAnnotation;

/**
 * Creates a {@link Library} instance from the javadoc information
 * @author icoloma
 *
 */
public class LibraryFactory {

	private static Logger log = LoggerFactory.getLogger(LibraryFactory.class);
	
	/**
	 * Create the intermediate memory model for the javadoc information
	 */
	public Library parse(ClassDoc[] classes, LibrarySignature librarySignature) {
		
		log.info("Parsing " + librarySignature.getShortName() + "...");
		Library library = new Library(librarySignature);
		for (ClassDoc clazz : classes) {
        	Tag tag = parseTag(clazz);
        	Listener listener = parseListener(clazz);
        	if (tag != null) {
        		library.add(tag);
        	} else if(listener != null) {
        		library.addListener(listener);
        	} else {
        		recollectFunctionData(clazz, library);
        	}
		}
		library.validate();
		return library;
		
	}
	
	/**
	 * Parse a Tag class from this class annotations
	 * @return the Tag class parsed for the provided class, null if  none
	 */
	private Tag parseTag(ClassDoc doc) {
		return !doc.isAbstract() && getAnnotation(doc, org.tldgen.annotations.Tag.class) != null? Tag.createInstance(doc) : null;
	}

	/**
	 * Parse the Listener from this class annotation
	 * @param doc
	 * @return the Listener class parsed for the provided class, null if none
	 */
	private Listener parseListener(ClassDoc doc) {
		return !doc.isAbstract() && getAnnotation(doc, org.tldgen.annotations.Listener.class) != null? Listener.createInstance(doc) : null;
	}

	
	/**
	 * Parse the methods of a class and its superclasses
	 * @param clazz the class to parse
	 * @param library the library objecto to insert the {@link Function} instances
	 */
	private void recollectFunctionData(ClassDoc clazz, Library library) {		
		for (MethodDoc methodDoc : clazz.methods()) {
			if (methodDoc.isStatic() && !methodDoc.isPrivate()) {
				AnnotationDesc annotation = getAnnotation(methodDoc, org.tldgen.annotations.Function.class);
				if (annotation != null){
					library.add(Function.createinstance(methodDoc, annotation));
				}
			}
		}
	}
	
}
