package org.tldgen.model;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;

import static org.tldgen.util.JavadocUtils.getAnnotation;

public class Listener extends AbstractTldElement {//implements Comparable<Listener>{

    private String listenerClass;

    public String getListenerClass() {
        return listenerClass;
    }

    public void setListenerClass(String listenerClass) {
        this.listenerClass = listenerClass;
        this.setName(parseSimpleName());
    }

    public static Listener createInstance(ClassDoc doc) {
        Listener listener = new Listener();
        AnnotationDesc ann = getAnnotation(doc, org.tldgen.annotations.Listener.class);
        ClassDoc classDoc = (ClassDoc) doc;
        listener.setListenerClass(classDoc.qualifiedTypeName());
        listener.setName(listener.listenerClass);
        listener.postProcessElement(doc, ann);
        return listener;
    }

    @Override
    protected String calculateDefaultElementName(Doc doc) {
      return parseSimpleName();
    }

    private String parseSimpleName() {
      int startIndex = listenerClass.lastIndexOf(".");
      return startIndex > 0 ? listenerClass.substring(++startIndex) : listenerClass;
    }
}
