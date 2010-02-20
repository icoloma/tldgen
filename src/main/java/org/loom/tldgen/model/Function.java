package org.loom.tldgen.model;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;

/**
 * Information about a JSP function
 * @author icoloma
 *
 */
public class Function extends AbstractTldContainerElement {

	/** the method signature */
	private String signature;
	
	@Override
	public void postProcessElement(ProgramElementDoc doc, AnnotationDesc annotation) {
		super.postProcessElement(doc, annotation);
		MethodDoc methodDoc = (MethodDoc) doc;
		signature = methodDoc.returnType() + " " + methodDoc.name() + methodDoc.signature();
	}
	
	public static Function createinstance(MethodDoc doc,
			AnnotationDesc annotation) {
		Function function = new Function();
		function.postProcessElement(doc, annotation);
		return function;
	}

	@Override
	protected String calculateDefaultElementName(Doc doc) {
		return doc.name();
	}

	public String getSignature() {
		return signature;
	}

}
