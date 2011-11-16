package org.tldgen.model;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;

/**
 * Information about a JSP function
 * @author icoloma
 *
 */
public class Function extends AbstractTldContainerElement {

	/** the method signature */
	private String signature;
	
	/** the parameters javadoc */
	private FunctionParameter[] parameters;
	
	/** the documentation of the return value */
	private String returnDescription;
	
	@Override
	public void postProcessElement(ProgramElementDoc doc, AnnotationDesc annotation) {
		MethodDoc mdoc = (MethodDoc) doc;
		super.postProcessElement(doc, annotation);
		//setDescription(doc.getRawCommentText());
		processParameters(mdoc);
		signature = mdoc.returnType() + " " + mdoc.name() + mdoc.signature();
		processReturnDescription(mdoc);
	}
	
	private void processReturnDescription(MethodDoc mdoc) {
		Tag[] r = mdoc.tags("return");
		if (r.length > 0) {
			returnDescription = r[0].text();
		}
	}

	/**
	 * Extract information for function parameters 
	 */
	private void processParameters(MethodDoc mdoc) {
		Parameter[] params = mdoc.parameters();
		ParamTag[] paramTags = mdoc.paramTags();
		parameters = new FunctionParameter[params.length];
		int i = 0;
		for (Parameter param : params) {
			String paramName = param.name();
			String description = null;
			for (ParamTag paramTag : paramTags) {
				if (paramTag.parameterName().equals(paramName)) {
					description = paramTag.parameterComment();
					break;
				}
			}
			parameters[i++] = new FunctionParameter(param.type().toString(), paramName, description);
		}
	}
	
	public static Function createinstance(MethodDoc doc,
			AnnotationDesc annotation) {
		Function function = new Function();
		function.postProcessElement(doc, annotation);
		return function;
	}


	public String getSignature() {
		return signature;
	}
	
	@Override
	protected String calculateDefaultElementName(Doc doc) {
		return doc.name();
	}

	public FunctionParameter[] getParameters() {
		return parameters;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setParameters(FunctionParameter[] parameters) {
		this.parameters = parameters;
	}

	public String getReturnDescription() {
		return returnDescription;
	}

	public void setReturnDescription(String returnDescription) {
		this.returnDescription = returnDescription;
	}

	public String getTldDescription() {
		StringBuilder sb = new StringBuilder();
		if (getDescription() != null) {
			sb.append(getDescription());
		}
		if (parameters != null && parameters.length > 0) {
			for (FunctionParameter param : parameters) {
				sb.append("\n * " + param.getType() + " " + param.getName() + ": " + param.getDescription());
			}
		}
		if (returnDescription != null) {
			sb.append("\nReturn: ").append(returnDescription);
		}
		return sb.length() == 0? null : sb.toString();
	}

}
