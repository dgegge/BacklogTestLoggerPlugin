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
public class SingleIterationDetails extends AbstractDetails {
  private final List<Story> stories;
  private final String iteration;

  public SingleIterationDetails(final AbstractBuild<?, ?> owner, List<Story> stories, String iteration) {
    super(owner);
    this.iteration = iteration;
    this.stories = stories;
  }

  public  List<Story> getStories() {
    return stories;
  }

  public List<Story> getSuccessedStories() {
    return StoryHelper.allocateStoriesWithState(this.getStories(), StoryState.SUCCESS);
  }

  public List<Story> getIncompletedStories() {
    return StoryHelper.allocateStoriesWithState(this.getStories(), StoryState.INCOMPLETE);
  }

  public List<Story> getFailedStories() {
    return StoryHelper.allocateStoriesWithState(this.getStories(), StoryState.FAILED);
  }

  public List<Story> getUntestedStories() {
    return StoryHelper.allocateStoriesWithState(this.getStories(), StoryState.UNTESTED);
  }

  @Override
  public String getDisplayName() {
    return "Details of Iteration: " + iteration;
  }
}