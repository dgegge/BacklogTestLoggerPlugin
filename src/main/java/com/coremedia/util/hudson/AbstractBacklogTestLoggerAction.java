package com.coremedia.util.hudson;

import hudson.model.Action;
import hudson.model.Run;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;

/**
 * Abstract class with functionality common to all BacklogTestLogger actions.
 *
 * @author Daniel Gegenheimer <d.gegge@googlemail.com>
 */
public class AbstractBacklogTestLoggerAction implements Action {
   public String getIconFileName() {
      return BacklogTestLoggerPlugin.ICON_FILE_NAME;
   }

   public String getDisplayName() {
      return BacklogTestLoggerPlugin.DISPLAY_NAME;
   }

   public String getUrlName() {
      return BacklogTestLoggerPlugin.URL;
   }

   protected boolean shouldReloadGraph(StaplerRequest request, StaplerResponse response, Run build) throws IOException {
      return !request.checkIfModified(build.getTimestamp(), response);
   }
}
