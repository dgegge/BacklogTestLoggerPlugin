package com.coremedia.util.model;

import com.coremedia.util.model.helper.MathHelper;
import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.pojo.StoryState;

import java.util.ArrayList;
import java.util.List;

/**
 * Containerclass which holds some information about the different stories
 */
public class StoryContainer {

  ArrayList<Story> stories;
  ArrayList<Story> successfulStories = new ArrayList<Story>();
  ArrayList<Story> failedStories = new ArrayList<Story>();
  ArrayList<Story> incompleteStories = new ArrayList<Story>();
  ArrayList<Story> untestedStories = new ArrayList<Story>();

  /**
   * Constructor
   */
  public StoryContainer() {
    stories = new ArrayList<Story>();
  }

  /**
   * get all Stories
   * @return all stories
   */
  public ArrayList<Story> getStories() {
    return stories;
  }

  

  public ArrayList<Story> getSuccessfulStories() {
    successfulStories = new ArrayList<Story>();
    for (Story story : this.getStories()) {
      if (story.getState().equals(StoryState.SUCCESS)) {
        successfulStories.add(story);
      }
    }
    return successfulStories;
  }

  public ArrayList<Story> getFailedStories() {
    failedStories = new ArrayList<Story>();
    for (Story story : this.getStories()) {
      if (story.getState().equals(StoryState.FAILED)) {
        failedStories.add(story);
      }
    }
    return failedStories;
  }

  public ArrayList<Story> getIncompleteStories() {
    incompleteStories = new ArrayList<Story>();
    for (Story story : this.getStories()) {
      if (story.getState().equals(StoryState.INCOMPLETE)) {
        incompleteStories.add(story);
      }
    }
    return incompleteStories;
  }

  public ArrayList<Story> getUntestedStories() {
    untestedStories = new ArrayList<Story>();
    for (Story story : this.getStories()) {
      if (story.getState().equals(StoryState.UNTESTED)) {
        untestedStories.add(story);
      }
    }
    return untestedStories;
  }

  public double getAvgExecutionTime() {
    double sumExecutionTime = 0;
    int count=0;

    for (Story story : this.getSuccessfulStories()) {
      for (SingleTest test : story.getTests()) {
         sumExecutionTime += Double.valueOf(test.getExecutiontime());
         count++;
      }
    }

    for (Story story : this.getFailedStories()) {
      for (SingleTest test : story.getTests()) {
         sumExecutionTime += Double.valueOf(test.getExecutiontime());
         count++;
      }
    }

    for (Story story : this.getIncompleteStories()) {
      for (SingleTest test : story.getTests()) {
         sumExecutionTime += Double.valueOf(test.getExecutiontime());
         count++;
      }
    }

    return MathHelper.floor(sumExecutionTime/count,2);
  }
}