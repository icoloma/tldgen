package org.tldgen.model;

import static org.tldgen.util.JavadocUtils.getAnnotation;
import static org.tldgen.util.JavadocUtils.getAnnotationArrayAttribute;
import static org.tldgen.util.JavadocUtils.getBooleanAttribute;
import static org.tldgen.util.JavadocUtils.getClassAttribute;
import static org.tldgen.util.JavadocUtils.getEnumAttribute;
import static org.tldgen.util.JavadocUtils.getStringArrayAttribute;
import static org.tldgen.util.JavadocUtils.getStringAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.jsp.tagext.TagExtraInfo;

import com.sun.javadoc.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tldgen.annotations.BodyContent;
import org.tldgen.annotations.ExcludeProperties;
import org.tldgen.annotations.VariableScope;
import org.tldgen.util.JavadocUtils;

/**
 * Information of a tag class
 * @author icoloma
 *
 */
public class Tag extends AbstractTldContainerElement {
	
	/** the body content type (required) */
	private BodyContent bodyContent;
	
	/** true if the tag allows dynamic attributes (optional) */
	private Boolean dynamicAttributes = false;
	
	/** the list of attributes */
	private SortedSet<Attribute> attributes = new TreeSet<Attribute>();
	
	/** the list of attributes sorted by name */
	private Map<String, Attribute> attributesByName = new HashMap<String, Attribute>();
	
	/** list of Variables used by this tag */
	private List<Variable> variables = new ArrayList<Variable>();
	
	/** An optional {@link TagExtraInfo} */
	private String teiClass;
	
	private static Logger log = LoggerFactory.getLogger(Tag.class);
	
	public static Tag createInstance(ClassDoc doc) {
    	Tag tag = new Tag();
    	AnnotationDesc ann = getAnnotation(doc, ExcludeProperties.class);
    	Set<String> excludeProperties = ann == null? new HashSet<String>() : new TreeSet<String>(Arrays.asList(getStringArrayAttribute(ann, "value")));
		recollectTagData(doc, tag, excludeProperties);
    	return tag;
	}
	
	@Override
	public void postProcessElement(ProgramElementDoc doc, AnnotationDesc annotation) {
		super.postProcessElement(doc, annotation);
		
		// body content
		String bodyContent = getEnumAttribute(annotation, "bodyContent");
		this.setBodyContent(bodyContent == null? BodyContent.SCRIPTLESS : BodyContent.valueOf(bodyContent));
		
		// dynamic attributes
		Boolean dynamicAttributes = getBooleanAttribute(annotation, "dynamicAttributes");
		this.setDynamicAttributes(dynamicAttributes != null? dynamicAttributes : false);
		
		// tei
		ClassDoc teiClassName = getClassAttribute(annotation, "teiClass");
		if (teiClassName != null && !teiClassName.toString().equals(TagExtraInfo.class.getName())) {
			this.setTeiClass(teiClassName.toString());
		}
		
		// variables
		AnnotationDesc[] variables = getAnnotationArrayAttribute(annotation, "variables");
		if (variables != null && variables.length > 0) {
			for (AnnotationDesc variableAnnotation : variables) {
				addVariable(null, variableAnnotation);
			}
		}
		
	}
	
	private static void recollectTagData(ClassDoc doc, Tag tag, Set<String> excludeProperties) {
		if (!Object.class.getName().equals(doc.qualifiedName())) {
			recollectTagData(doc.superclass(), tag, excludeProperties);
		}
		
		// process any @Tag annotation
		AnnotationDesc annotation = getAnnotation(doc, org.tldgen.annotations.Tag.class);
		if (annotation != null) {
			tag.postProcessElement(doc, annotation);
		}
    	
    	// add annotated attributes
    	for (FieldDoc fieldDoc : doc.fields()) {
			Attribute attribute = addMember(fieldDoc, tag, excludeProperties);
			if (attribute != null) {
				AnnotationDesc variableAnnotation = getAnnotation(fieldDoc, org.tldgen.annotations.Variable.class);
				if (variableAnnotation != null) {
					tag.addVariable(attribute.getName(), variableAnnotation);
				}
			}
			
		}
    	
    	// add annotated setter methods
    	for (MethodDoc methodDoc : doc.methods()) {
			addMember(methodDoc, tag, excludeProperties);
    	}
    	
	}
	
	private static Attribute addMember(MemberDoc doc, Tag tag, Set<String> excludeProperties) {
		Attribute attribute = parseAttribute(doc);
		if (attribute != null) {
			if (excludeProperties.contains(attribute.getName())) {
				log.debug("Skipped " + tag.getName() + "." + attribute.getName() + " because it was contained in @ExcludeProperties");
			} else {
				tag.addAttribute(attribute);
			}
		}		
		return attribute;
	}
	
	/**
	 * Parse a Variable annotation. If bound to an attribute, use the nameFromAttribute
	 * @param attributeName if bound to an attribute, the attribute name. Otherwise, null
	 * @param the Variable annotation
	 */
	private Variable addVariable(String attributeName, AnnotationDesc annotation) {
		Variable variable = new Variable();
		String nameGiven = JavadocUtils.getStringAttribute(annotation, "nameGiven");
		if (attributeName != null) {
			if (nameGiven != null && nameGiven.length() > 0) {
				throw new IllegalArgumentException("Cannot specify @Variable.nameGiven bound to attribute '" + attributeName + "'. Use @Tag.variables instead");
			}
			variable.setNameFromAttribute(attributeName);
		} else {
			if (nameGiven == null || nameGiven.length() == 0) {
				throw new IllegalArgumentException("Missing @Variable.nameGiven value");
			}
			variable.setNameGiven(nameGiven);
		}
		
		Boolean declare = getBooleanAttribute(annotation, "declare");
		if (declare != null) {
			variable.setDeclare(declare);
		}
		ClassDoc variableClass = getClassAttribute(annotation, "variableClass");
		if (variableClass != null) {
			variable.setVariableClass(variableClass);
		}
		String scopeValue = getEnumAttribute(annotation, "scope");
		if (scopeValue != null) {
			variable.setScope(VariableScope.valueOf(scopeValue));
		}
		String description = getStringAttribute(annotation, "description");
		if (description != null && description.length() > 0) {
			variable.setDescription(description);
		}
		return this.addVariable(variable);
	}
	
	/**
	 * Parse a field or method javadoc and return the instantiated {@link Attribute} instance 
	 * @param doc the javadoc to parse
	 * @return the created Attribute instance, null if none
	 */
	private static Attribute parseAttribute(MemberDoc doc) {
		AnnotationDesc annotation = getAnnotation(doc, org.tldgen.annotations.Attribute.class);
		if (annotation == null) {
			return null;
		}
		Attribute attribute = new Attribute();
		attribute.postProcessElement(doc, annotation);
		Boolean required = getBooleanAttribute(annotation, "required");
		attribute.setRequired(required != null? required : false);
		Boolean rtexprvalue = getBooleanAttribute(annotation, "rtexprvalue");
		attribute.setRtexprvalue((rtexprvalue != null)? rtexprvalue : true);
        attribute.setType(parseAttributeType(doc));
		return attribute;
	}

    private static String parseAttributeType(MemberDoc doc) {

        Type type = null;

        if(doc instanceof FieldDoc) {
            FieldDoc fieldDoc = (FieldDoc) doc;
            type = fieldDoc.type();
        }
        if(doc instanceof MethodDoc) {
            MethodDoc methodDoc = (MethodDoc) doc;
            Parameter[] parameter = methodDoc.parameters();
            if(parameter.length == 1)
                type = parameter[0].type();
        }

        if(type == null || type.isPrimitive())
            return "java.lang.String";
        else
            return type.qualifiedTypeName();
    }
	
	@Override
	protected String calculateDefaultElementName(Doc doc) {
		// calculate default tag name
		String name = StringUtils.substringBeforeLast(doc.name(), "Tag");
		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
		return name;
	}
	
	public Attribute addAttribute(Attribute attribute) {
		Attribute oldValue = removeAttribute(attribute.getName());
		log.debug("Adding " + getName() + "." + attribute.getName());
		attributes.add(attribute);
		attributesByName.put(attribute.getName(), attribute);
		return oldValue;
	}
	
	public Variable addVariable(Variable variable) {
		this.variables.add(variable);
		return variable;
	}
	
	/**
	 * Remove an attribute if it exists, do nothing if not
	 * @param name the name of the attribute
	 * @return the removed attribute, if any. Null if not found 
	 */
	public Attribute removeAttribute(String name) {
		Attribute attribute = getAttribute(name);
		if (attribute != null) {
			attributes.remove(attribute);
			attributesByName.remove(attribute.getName());
		}
		return attribute;
	}
	
	/**
	 * Return an attribute. 
	 * @param name the attribute name
	 * @return the attribute, if it exists. Null if not found
	 */
	public Attribute getAttribute(String name) {
		return attributesByName.get(name);
	}
	
	public BodyContent getBodyContent() {
		return bodyContent;
	}
	
	public void setBodyContent(BodyContent bd) {
		this.bodyContent = bd;
	}

	public Boolean isDynamicAttributes() {
		return dynamicAttributes;
	}
	
	public void setDynamicAttributes(Boolean dynamicAttributes) {
		this.dynamicAttributes = dynamicAttributes;
	}
	
	public Collection<Attribute> getAttributes() {
		return attributes;
	}

	public String getTeiClass() {
		return teiClass;
	}

	public void setTeiClass(String teiClass) {
		this.teiClass = teiClass;
	}

	public List<Variable> getVariables() {
		return variables;
	}

}
