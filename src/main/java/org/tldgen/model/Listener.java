package org.tldgen.model;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;

import static org.tldgen.util.JavadocUtils.getAnnotation;

public class Listener {

    private String listenerClass;

    public String getListenerClass() {
        return listenerClass;
    }

    public void setListenerClass(String listenerClass) {
        this.listenerClass = listenerClass;
    }

    public static Listener createInstance(ClassDoc doc) {
        Listener listener = new Listener();
        AnnotationDesc ann = getAnnotation(doc, org.tldgen.annotations.Listener.class);
        ClassDoc classDoc = (ClassDoc) doc;
        listener.setListenerClass(classDoc.qualifiedTypeName());
        return listener;
    }
}
