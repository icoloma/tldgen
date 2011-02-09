package org.tldgen.model;

import static org.tldgen.util.JavadocUtils.getAnnotation;
import static org.tldgen.util.JavadocUtils.getBooleanAttribute;
import static org.tldgen.util.JavadocUtils.getClassAttribute;
import static org.tldgen.util.JavadocUtils.getEnumAttribute;
import static org.tldgen.util.JavadocUtils.getStringArrayAttribute;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.jsp.tagext.TagExtraInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tldgen.annotations.BodyContent;
import org.tldgen.annotations.ExcludeProperties;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;

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
	
	/** An optional {@link TagExtraInfo} */
	private String teiClass;
	
	private static Log log = LogFactory.getLog(Tag.class);
	
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
		String bodyContent = getEnumAttribute(annotation, "bodyContent");
		this.setBodyContent(bodyContent == null? BodyContent.SCRIPTLESS : BodyContent.valueOf(bodyContent));
		Boolean dynamicAttributes = getBooleanAttribute(annotation, "dynamicAttributes");
		this.setDynamicAttributes(dynamicAttributes != null? dynamicAttributes : false);
		ClassDoc teiClassName = getClassAttribute(annotation, "teiClass");
		if (teiClassName != null && !teiClassName.toString().equals(TagExtraInfo.class.getName())) {
			this.setTeiClass(teiClassName.toString());
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
			Attribute attribute = parseAttribute(fieldDoc);
			if (attribute != null) {
				if (excludeProperties.contains(attribute.getName())) {
					log.debug("Skipped " + tag.getName() + "." + attribute.getName() + " because it was contained in @ExcludeProperties");
				} else {
					tag.addAttribute(attribute);
				}
			}
		}
    	
    	// add annotated setter methods
    	for (MethodDoc methodDoc : doc.methods()) {
    		Attribute attribute = parseAttribute(methodDoc);
    		if (attribute != null) {
    			if (excludeProperties.contains(attribute.getName())) {
    				log.debug("Skipped " + tag.getName() + "." + attribute.getName() + " because it was contained in @ExcludeProperties");
    			} else {
    				tag.addAttribute(attribute);
    			}
    		}
    	}
    	
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
		return attribute;
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
	
	/**
	 * Remove an attribuite if it exists, do nothing if not
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

}
