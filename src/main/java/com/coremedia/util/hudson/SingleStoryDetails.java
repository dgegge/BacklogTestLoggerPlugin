package com.coremedia.util.hudson;

import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Story;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.ModelObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * This class represents story details of a story
 */
public class SingleStoryDetails implements ModelObject {
  private final Story story;
  private final AbstractBuild<?, ?> _owner;

  public SingleStoryDetails(final AbstractBuild<?, ?> owner, Story story) {
    this.story = story;
    this._owner = owner;
  }

  public Story getStory() {
    return story;
  }

  public AbstractBuild<?, ?> get_owner() {
    return _owner;
  }

  public String getDisplayName() {
    return "Details of story No: " + story.getId();
  }

  /**
   * Returns the dynamic result
   *
   * @param link     the link to identify the sub page to show
   * @param request  Stapler request
   * @param response Stapler response
   * @return the dynamic result of the analysis.
   */
  public Object getDynamic(final String link, final StaplerRequest request,
                           final StaplerResponse response) {
    Object result = null;
    for (Action action : _owner.getActions()) {
      if (action instanceof BacklogTestLoggerBuildAction) {
        result = ((BacklogTestLoggerBuildAction) action).getDynamic(link,request,response);
      }
    }
    return result;
  }
}