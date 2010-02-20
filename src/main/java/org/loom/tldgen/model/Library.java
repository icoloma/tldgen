package org.loom.tldgen.model;

import java.util.Set;
import java.util.TreeSet;

/**
 * Holds the information of a JSP tag library, to be written as a TLD  file
 * @author icoloma
 *
 */
public class Library {
	
	private String shortName;
	
	private String displayName;
	
	private String uri;

	/** list of tags in this library */
	private Set<Tag> tags = new TreeSet<Tag>();
	
	/** list of functions in this library */
	private Set<Function> functions = new TreeSet<Function>();

	/**
	 * Convenience method to get tag by name
	 * @return the tag with a matching name,  null if none was found
	 */
	public Tag getTag(String name) {
		for (Tag tag : tags) {
			if (name.equals(tag.getName())) {
				return tag;
			}
		}
		return null;
	}
	
	/**
	 * Convenience method to get function by name
	 * @return the function with a matching name,  null if none was found
	 */
	public Function getFunction(String name) {
		for (Function function : functions) {
			if (name.equals(function.getName())) {
				return function;
			}
		}
		return null;
	}
	
	public void add(Tag tag) {
		tags.add(tag);
	}
	
	public void add(Function function) {
		functions.add(function);
	}
	
	public Set<Tag> getTags() {
		return tags;
	}

	public Set<Function> getFunctions() {
		return functions;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String name) {
		this.shortName = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	
}
