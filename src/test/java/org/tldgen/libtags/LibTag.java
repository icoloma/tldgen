package org.tldgen.libtags;

import org.tldgen.annotations.Attribute;
import org.tldgen.annotations.Tag;

@Tag
public class LibTag {
	
	@SuppressWarnings("unused")
	@Attribute
	private String foo;


    @SuppressWarnings("unused")
    @Attribute
    private int fooInt;

    @Attribute
    private void setAnInt(int anInt) {

    }

    @Attribute
    private void setABoolean(Boolean aBoolean) {

    }

}
