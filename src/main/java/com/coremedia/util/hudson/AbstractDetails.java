package com.coremedia.util.hudson;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.ModelObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Abstract class which holds information for further detail pages
 */
public abstract class AbstractDetails implements ModelObject {
  protected final AbstractBuild<?, ?> _owner;

  public AbstractDetails(final AbstractBuild<?, ?> owner) {
    this._owner = owner;
  }

  public final AbstractBuild<?, ?> get_owner() {
    return _owner;
  }

  public abstract String getDisplayName();

  /**
   * Returns the dynamic result
   * This method is called, when a redirect is made.
   * it calls the  BacklogTestLoggerAction.getDynamic method
   *
   * @param link     the link to identify the sub page to show
   * @param request  Stapler request
   * @param response Stapler response
   * @return the dynamic result of the analysis.
   */
  public final Object getDynamic(final String link, final StaplerRequest request,
                           final StaplerResponse response) {
    Object result = null;
    for (Action action : _owner.getActions()) {
      if (action instanceof BacklogTestLoggerBuildAction) {
        result = ((BacklogTestLoggerBuildAction) action).getDynamic(link, request, response);
      }
    }
    return result;
  }
}
