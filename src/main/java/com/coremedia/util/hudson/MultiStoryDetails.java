package com.coremedia.util.hudson;

import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.pojo.StoryState;
import com.coremedia.util.model.pojo.Testable;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.ModelObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.util.List;


/**
 * This class represents story details of a story
 */
public class MultiStoryDetails extends AbstractDetails {
  private final List<Story> stories;
  private StoryState state;
  private Testable testable;

  public MultiStoryDetails(final AbstractBuild<?, ?> owner, List<Story> stories, StoryState state) {
    super(owner);
    this.state = state;
    this.stories = stories;
  }

  public MultiStoryDetails(AbstractBuild<?, ?> owner, List<Story> stories, Testable testable) {
    super(owner);
    this.stories = stories;    
    this.testable = testable;
  }

  public StoryState getState() {
    return state;
  }

  public Testable getTestable() {
    return testable;
  }

  public List<Story> getStories() {
    return stories;
  }

  public String getDisplayName() {
    if (state != null) {
      return "Details of Stories with State: " + this.state;
    } else {
      return "Details of Testable: " + this.testable;
    }
  }
}