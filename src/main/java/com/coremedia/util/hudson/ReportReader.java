package com.coremedia.util.hudson;


import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Stories;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.reader.XMLReader;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
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
  public ReportReader(URI is, PrintStream logger, Map<String, String> metrics) {
    hudsonConsoleWriter = logger;
    parse(is, metrics.values());
  }

  private void parse(URI is, Collection<String> metrics) {
    final String errMsg = "[BacklogTestLogger] Problem parsing Performance report file";
    if (is == null) {
      throw new BacklogTestLoggerParseException("Empty input stream");
    }
    try {
      XMLReader reader = new XMLReader(new Class[]{Stories.class, Story.class, SingleTest.class});
      JAXBElement element = reader.unmarshal(is.getPath());
      this.stories = reader.getStories(element, hudsonConsoleWriter);      
    } catch (JAXBException e) {
      hudsonConsoleWriter.println(errMsg + ": " + e.getMessage());
      e.printStackTrace(hudsonConsoleWriter);
      throw new BacklogTestLoggerParseException(errMsg, e);
    }
  }

  public List<Story> getStories() {
    return this.stories;
  }
}