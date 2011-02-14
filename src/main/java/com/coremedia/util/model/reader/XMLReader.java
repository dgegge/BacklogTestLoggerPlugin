package com.coremedia.util.model.reader;

import com.coremedia.util.model.pojo.Errorlog;
import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Stories;
import com.coremedia.util.model.pojo.Story;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class, which ummarshals a given resource
 */
public class XMLReader {

  private final JAXBContext ctx;
  private final Unmarshaller unmarshaller;

  /**
   * Constructor, which creates a JAXBContext and an unmarshaller to parse a give resource
   *
   * @param classes which should be analyzed by the JAXBContext
   * @throws JAXBException if anything fails
   */
  public XMLReader(Class[] classes, File schemaFile) throws JAXBException, SAXException {
    ctx = JAXBContext.newInstance(classes);
    unmarshaller = ctx.createUnmarshaller();
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = schemaFactory.newSchema(schemaFile);
      unmarshaller.setSchema(schema);
    } catch (Exception e) {
      //TODO: Logging
    } catch (NoSuchMethodError e) {
      //TODO: Logging
    }
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
   * @param path of a file to be unmarshalled
   * @return unmarshalled JAXBElement
   * @throws JAXBException if anything fails
   */
  public JAXBElement unmarshal(String path) throws JAXBException {
    return unmarshal(new File(path));
  }

  /**
   * get Stories from a JAXBElement.
   * @param element This element should be the "Stories"-Element
   * @return a list with parsed stories
   */
  public List<Story> getStories(JAXBElement element) {
    List<Story> retList = new ArrayList<Story>();
    Stories stories = (Stories) element.getValue();
    for (Story story : stories.getStories()) {
      for (Serializable serTests : story.getContent()) {
        try {
          JAXBElement elementTest = (JAXBElement) serTests;
          SingleTest test = (SingleTest) elementTest.getValue();
          try {
            if ((!test.getContent().isEmpty()) && test.getContent().size() > 1) {
              JAXBElement elementError = (JAXBElement) test.getContent().get(0);
              Errorlog error = (Errorlog) elementError.getValue();
              test.setError(error);
            }
          } catch (ClassCastException e) {
            throw e;
          }
          story.addTest(test);
        } catch (ClassCastException e) {
          //nothing to do
        }
      }
      retList.add(story);
    }
    return retList;
  }

}
