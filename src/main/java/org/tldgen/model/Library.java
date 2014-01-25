package org.tldgen.model;

import java.util.Set;
import java.util.TreeSet;

/**
 * Holds the information of a JSP tag library, to be written as a TLD  file
 * @author icoloma
 *
 */
public class Library {
	
	/** the data identifying this library */
	private LibrarySignature librarySignature;
	
	/** list of tags in this library */
	private Set<Tag> tags = new TreeSet<Tag>();

	/** list of functions in this library */
	private Set<Function> functions = new TreeSet<Function>();

	/** list of listeners in this library */
	private Set<Listener> listeners = new TreeSet<Listener>();

	public Library(LibrarySignature librarySignature) {
		this.librarySignature = librarySignature;
	}

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

	/**
	 * Convenience method to get a listener by class name
	 *
	 * @return the listener with the matching class name, null if none was found
	 */
	public Listener getListener(String name) {
		for (Listener listener : listeners) {
			if (name.equals(listener.getListenerClass())) {
				return listener;
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

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public Set<Function> getFunctions() {
		return functions;
	}

	public Set<Listener> getListeners() {
		return listeners;
	}

	public LibrarySignature getLibrarySignature() {
		return librarySignature;
	}

	public void validate() {
		if (librarySignature.getShortName() == null) {
			throw new IllegalArgumentException("Attribute shortName for @Library annotation is mandatory.");
		}
		if (librarySignature.getUri() == null) {
			throw new IllegalArgumentException("Attribute uri for library is mandatory.");
		}
	}
	
}
