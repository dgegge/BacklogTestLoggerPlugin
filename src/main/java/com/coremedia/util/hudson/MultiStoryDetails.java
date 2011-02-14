package com.coremedia.util.hudson;

import com.coremedia.util.model.helper.StoryHelper;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.pojo.StoryState;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.ModelObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.util.List;


/**
 * This class represents story details of a story
 */
public class MultiStoryDetails implements ModelObject {
  private final List<Story> stories;
  private final AbstractBuild<?, ?> _owner;
  private final StoryState state;

  public MultiStoryDetails(final AbstractBuild<?, ?> owner, List<Story> stories, StoryState state) {
    this.state = state;
    this.stories = stories;
    this._owner = owner;
  }

  public StoryState getState() {
    return state;
  }

  public  List<Story> getStories() {
    return stories;
  }

  public String getDisplayName() {
    return "Details of Stories with State: " + this.state;
  }

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
  public Object getDynamic(final String link, final StaplerRequest request,
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