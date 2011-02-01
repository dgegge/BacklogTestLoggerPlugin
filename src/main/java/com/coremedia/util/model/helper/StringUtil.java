package com.coremedia.util.model.helper;

import java.text.BreakIterator;

/**
 * Created by IntelliJ IDEA.
 * User: dgegenhe
 * Date: 28.01.2011
 * Time: 11:43:36
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {

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


    /*while (tmp.length()>90) {
            result.append(tmp.substring(0,90));
            result.append("<br />");
            tmp = tmp.substring(90);
    }*/
    return result.toString();

  }
}
