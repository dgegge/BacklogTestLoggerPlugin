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
    StringBuffer line = new StringBuffer();
    String tmp = string;

    //tmp is greater then 70 --> divide the string into string with <br/> elements after 70 chars
    if (tmp.length() > 70) {
      BreakIterator iterator = BreakIterator.getWordInstance();
      iterator.setText(tmp);
      int index = 0;
      while (iterator.next() != BreakIterator.DONE) {
        String currentWord = tmp.substring(index, iterator.current());

        if ((line.length() + currentWord.length()) < 70) {
          line.append(currentWord);
        } else {
          result.append(line);
          result.append(currentWord);
          result.append("<br />");
          line = new StringBuffer();
        }
        index = iterator.current();
      }
    }
    else {
      result.append(tmp);
    }
    result.append(line);
    return result.toString();
  }
}
