package com.coremedia.util.hudson;

/**
 * Exception used to show BacklogTestLogger parsing has failed.
 *
 * @author Daniel Gegenheimer
 */
public class BacklogTestLoggerParseException extends RuntimeException {
   public BacklogTestLoggerParseException(String msg, Exception e) {
      super(msg, e);
   }

   public BacklogTestLoggerParseException(String msg) {
      super(msg);
   }
}
