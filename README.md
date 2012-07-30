About TLDGen
===
TLDGen is an extremely useful tool if you're working with JSP and custom tag and functions. It automatically generates the TLD (Tag Library Descriptor) files from annotations in your Java classes.And the nice part is that you don't need TLDGen at runtime. 

You can use TLDGen in three ways: from the command line, ant or maven. It can also generate HTML documentation for your TLD [See example](http://loom.sourceforge.net/docs/loom-core/tlddoc/form.html).

Why
===

Java web applications based on JSP are traditionally bound to a great amount of redundancy in the development process:
<ul>
<li><b>Tag classes must be kept up-to-date</b> with the TLD by hand. This means a big deal of duplicate effort.</li>
<li>For any non-trivial tag library, <b>inheritance is a fact</b>. In order to avoid replication in the TLD your libraries are bound to use XML entities, which is cumbersome and prone to error.
</li>
<li><b>IDEs are extracting JSP coding hints from the TLD file</b>, which implies that the developer should be introducing the tag documentation into the TLD by hand.</li>
<li>Since this documentation is already available in the javadoc, it implies maintaining <b>duplicate documentation</b>: java(doc) and TLD.</li>
<li>Make it triple, if you want <b>online HTML documentation</b>.</li>
<li>There is no way to document <b>deprecated behavior</b>.</li>
</ul>

It can be used to create TLD files from scratch or to upgrade your existing web application to remove the hassle of maintaining these files by hand. In both cases the procedure is the same.

Using tldgen annotations
===

First, add tldgen.jar to your project dependencies. You can get it from the project downloads or use maven (group org.extrema-sistemas, artifact tldgen). 

Once the jar file is included, you can add the annotations to any existing JSP tag class:
```java
package com.acme.tags;

import org.tldgen.annotations.Tag;
import org.tldgen.annotations.BodyContent;
import org.tldgen.annotations.Attribute;
import javax.servlet.jsp.tagext.SimpleTagSupport;

@Tag(
  bodyContent=BodyContent.SCRIPTLESS,
  example="&lt;p:customer name=\"John Doe\"/>"
)
public class CustomerTag extends SimpleTagSupport {

  /** The name of this customer */
  @Attribute
  private String name;

  /** 
   * The id of the customer. This is not used anymore.
   * @deprecated use name instead
   */
  @Attribute
  private int id;

}
```

You can also annotate JSP function methods:

```java
package com.acme.tags;

import org.tldgen.annotations.Function;

public class Functions {

  private static Random random = new Random();

  /**
   * Return a random number between 0 (inclusive) and the provided value (exclusive)
   */
  @Function(example="${l:random(10)}")
  public static int random(int maxValue) {
    return random.nextInt(maxValue);
  }

}
```

Launch the tldgen tool
===

Now you should download the tldgen.zip file, install it somewhere and execute the command-line tool indicating the library data and the HTML and TLD file locations. For example, with Linux (all on the same line):

```
tldgen 
  -classpath 'lib/*' 
  -sourcepath src/main/java 
  -subpackages com.acme.tags 
  -tldFile src/main/resources/META-INF/acme.tld 
  -name acme 
  -uri http://acme.com/acme.tld 
  -htmlFolder build/docs/tlddoc
```

That's it. To get a list of available command-line arguments, execute "tldgen --help".

Ant usage
===

```xml
  <javadoc access="private" useexternalfile="true"
      packagenames="com.*" sourcepath="/src/main/java"
      classpath="/target/classes" classpathref="libraries"
      docletpath="/target/classes" docletpathref="libraries">
    <doclet name="org.tldgen.TldDoclet">
      <param name="-htmlFolder" value="/target/docs/tld"/>
      <param name="-tldFolder" value="/target/classes/META-INF"/>
      <param name="-license" value="NONE"/>
    </doclet>
  </javadoc>
```

This example is following Maven conventions, but you can easily adapt them for your own needs. The `libraries` set must contain the TLDGen jar.

Maven usage
===

```xml
<dependency>
  <groupId>org.extrema-sistemas</groupId>
  <artifactId>tldgen</artifactId>
  <version>1.3.1</version>
  <scope>compile</scope>
</dependency>
...
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>2.7</version>
    <configuration>
        <includes>
            <include>**</include>
        </includes>
        <doclet>org.tldgen.TldDoclet</doclet>
        <docletArtifacts>
            <docletArtifact>
                <groupId>org.extrema-sistemas</groupId>
                <artifactId>tldgen</artifactId>
                <version>1.3</version>
            </docletArtifact>
        </docletArtifacts>
        <show>private</show>
        <additionalparam>
            -htmlFolder ${basedir}/target/docs
            -tldFolder ${basedir}/target/classes/META-INF
            -license NONE
        </additionalparam>
        <useStandardDocletOptions>true</useStandardDocletOptions>
        <author>false</author>
        <encoding>utf-8</encoding>
    </configuration>
    <executions>
        <execution>
            <phase>generate-resources</phase>                                       
            <goals>
                <goal>javadoc</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
