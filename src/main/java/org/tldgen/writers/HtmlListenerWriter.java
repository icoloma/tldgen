package org.tldgen.writers;

import org.tldgen.model.Library;
import org.tldgen.model.Listener;

import java.io.IOException;

public class HtmlListenerWriter extends AbstractHtmlWriter {

  public HtmlListenerWriter(String htmlFile) throws IOException {
    super(htmlFile);
  }


  /**
   * Fill the HTML main content with Tag information
   * @param listener {@link org.tldgen.model.Listener}
   */
  /*private void writeListener(Listener listener) throws IOException{

    String description = listener.getHtmlDescription();

    startTag("p").print(description).endTag("p");

    startTag("table", "class", "tag-info");
    startTag("thead");
    startTag("tr");
    startTag("th", "colspan", "2").print("Listener Information").endTag("th");
    endTag("tr");
    endTag("thead");
    startTag("tbody");
    printTableEntry("Listener Class", listener.getListenerClass());
    endTag("tbody");
    endTag("table");
  }*/

  /**
   * Fill the HTML main content with Listener information
   * @param listener {@link Listener}
   * @throws IOException
   */
  private void writeListener(Listener listener) throws IOException {

    printHeader(2, listener.getName());
    print(listener.getHtmlDescription());
    startTag("table");
    startTag("tbody");
    writeInfo(listener);
    endTag("tbody");
    endTag("table");
  }

  /**
   * Write content of the Function information
   * @param listener {@link org.tldgen.model.Listener}
   * @throws IOException
   */
  private void writeInfo(Listener listener) throws IOException {
    printTableEntry("Listener Class", listener.getListenerClass());
  }

  /**
   * @param library the library data model to process
   */
  public void write(Library library) throws IOException{
    startDocument("Listeners");
    startBody();

    printHeader(1, "Listeners");

    for (Listener listener: library.getListeners()) {
      startTag("div", "id", listener.getName(), "class","yui-g bottom-delimiter");
      writeListener(listener);
      endTag("div");
    }

    printMenu(library, library.getListeners().iterator().next());

    endBody("Listeners");
    endDocument();
  }

}
