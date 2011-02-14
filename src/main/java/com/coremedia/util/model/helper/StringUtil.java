package com.coremedia.util.model.helper;

import java.text.BreakIterator;

/**
 * Helper class for strings
 */
public class StringUtil {

  /**
   * Prints a pretty html string seperated by words
   * @param string which should be made pretty
   * @return pretty html string
   */
  public static String makePrettyHtmlString(String string) {
    if (string == null) {
      return "";
    }
    StringBuffer result = new StringBuffer();
    String tmp = string;

    if (tmp.length() > 70) {
      BreakIterator iterator = BreakIterator.getWordInstance();
      iterator.setText(tmp);

      StringBuffer line = new StringBuffer();

      int index = 0;
      while (iterator.next() != BreakIterator.DONE) {
        if (line.length() < 70) {
          line.append(tmp.substring(index, iterator.current()));
        } else {
          result.append(line);
          result.append("<br />");
          line = new StringBuffer();
        }
        index = iterator.current();
      }
    }
    else {
      result.append(tmp);
    }

    return result.toString();
  }
}
