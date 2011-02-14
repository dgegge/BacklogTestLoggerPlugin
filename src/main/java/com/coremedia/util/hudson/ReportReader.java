package com.coremedia.util.hudson;


import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Stories;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.reader.XMLReader;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.PrintStream;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Reads all the generated reports
 *
 * @author Daniel Gegenheimer
 */

public class ReportReader {

  private List<Story> stories;

  private transient final PrintStream hudsonConsoleWriter;

  /**
   * Construct a result reader for BacklogTestLogger out log files.
   *
   * @param is     The input stream giving the out log file.
   * @param logger Logger to print messages to.
   * @throws BacklogTestLoggerParseException
   *          Thrown if the parsing fails.
   */
  public ReportReader(URI is, PrintStream logger) {
    hudsonConsoleWriter = logger;
    parse(is);
  }


  private void parse(URI is) {
    final String errMsg = "[BacklogTestLogger] Problem parsing Performance report file";
    if (is == null) {
      throw new BacklogTestLoggerParseException("Empty input stream");
    }
    try {
      XMLReader reader = new XMLReader(new Class[]{Stories.class, Story.class, SingleTest.class}, new File(getClass().getResource("/xsd/backlogtestlogger.xsd").getPath()));
      JAXBElement element = reader.unmarshal(is.getPath());
      this.stories = reader.getStories(element);
    } catch (JAXBException e) {
      hudsonConsoleWriter.println(errMsg + ": " + e.getMessage());
      e.printStackTrace(hudsonConsoleWriter);
      throw new BacklogTestLoggerParseException(errMsg, e);
    } catch (SAXException e) {
      hudsonConsoleWriter.println(errMsg + ": " + e.getMessage());
      e.printStackTrace(hudsonConsoleWriter);
      throw new BacklogTestLoggerParseException(errMsg, e);
    }
  }

  public List<Story> getStories() {
    return this.stories;
  }
}
