package test;


import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Stories;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.reader.XMLReader;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: dgegenhe
 * Date: 21.01.2011
 * Time: 15:33:36
 * To change this template use File | Settings | File Templates.
 */
public class XMLReaderTest {

  @Test
  public void testParseEntireDocument() throws JAXBException, SAXException {
    XMLReader reader = new XMLReader(new Class[]{Stories.class, Story.class, SingleTest.class}, new File(getClass().getResource("/backlogtestlogger.xsd").getPath()));
    URL resource = getClass().getResource("/example.xml");

    File file = new File(resource.getPath());

    JAXBElement elementFile = reader.unmarshal(file);
    JAXBElement elementPath = reader.unmarshal(resource.getPath());

    assertEquals(Stories.class, elementFile.getValue().getClass());
    assertEquals(Stories.class, elementPath.getValue().getClass());

    testElement(elementFile, reader);
    testElement(elementPath, reader);
  }

  private void testElement(JAXBElement element, XMLReader reader) {

    List<Story> list = reader.getStories(element);
    assertEquals(3, list.size());
    assertEquals(2, list.get(0).getTests().size());
  }
}
