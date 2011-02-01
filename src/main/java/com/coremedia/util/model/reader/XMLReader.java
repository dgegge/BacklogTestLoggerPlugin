package com.coremedia.util.model.reader;

import com.coremedia.util.model.pojo.Errorlog;
import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Stories;
import com.coremedia.util.model.pojo.Story;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class, which ummarshals a given resource
 */
public class XMLReader {

  private final JAXBContext ctx;
  private final Unmarshaller unmarshaller;

  // Attribute
  private static URI xml_path;
  private static Collection<String> metrics;

  /**
   * Constructor, which creates a JAXBContext and an unmarshaller to parse a give resource
   *
   * @param classes which should be analyzed by the JAXBContext
   * @throws JAXBException if anything fails
   */
  public XMLReader(Class[] classes) throws JAXBException {
    ctx = JAXBContext.newInstance(classes);
    unmarshaller = ctx.createUnmarshaller();
  }

  /**
   * Unmarshal a file and return JAXBElements
   *
   * @param file to be unmarshalled
   * @return unmarshalled JAXBElement
   * @throws JAXBException if anything fails
   */
  public JAXBElement unmarshal(File file) throws JAXBException {
    JAXBElement element = (JAXBElement) unmarshaller.unmarshal(file);
    return element;
  }

  /**
   * Unmarshal a file and return JAXBElements
   *
   * @param path of a file to be unmarshalled
   * @return unmarshalled JAXBElement
   * @throws JAXBException if anything fails
   */
  public JAXBElement unmarshal(String path) throws JAXBException {
    return unmarshal(new File(path));
  }

  public List<Story> getStories(JAXBElement element, PrintStream hudsonConsoleWriter) {

    List<Story> retList = new ArrayList<Story>();

    Stories stories = (Stories) element.getValue();

    for (Story story : stories.getStories()) {

      for (Serializable serTests : story.getContent()) {
        try {
          JAXBElement elementTest = (JAXBElement) serTests;
          SingleTest test = (SingleTest) elementTest.getValue();
          hudsonConsoleWriter.println("Found test " + test.getClass() + "." + test.getMethod());

          try {
            if ((!test.getContent().isEmpty()) && test.getContent().size() > 1) {
              JAXBElement elementError = (JAXBElement) test.getContent().get(0);
              Errorlog error = (Errorlog) elementError.getValue();
              test.setError(error);
            }
          }
          catch (ClassCastException e) {
            hudsonConsoleWriter.println(e);
            throw e;
          }

          hudsonConsoleWriter.println("Add Test to Story");
          story.addTest(test);
        }
        catch (ClassCastException e) {
          //nothing to do
        }


      }
      hudsonConsoleWriter.println("Add Test to List");
      retList.add(story);
    }
    return retList;
  }

}
