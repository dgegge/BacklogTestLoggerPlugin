package com.coremedia.util.hudson.Report;

import com.coremedia.util.model.helper.MathHelper;
import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.pojo.StoryState;

import java.util.ArrayList;
import java.util.List;

public class StoryContainer {

  ArrayList<Story> stories;
  ArrayList<Story> successfulStories = new ArrayList<Story>();
  ArrayList<Story> failedStories = new ArrayList<Story>();
  ArrayList<Story> incompleteStories = new ArrayList<Story>();
  ArrayList<Story> untestedStories = new ArrayList<Story>();

  public StoryContainer() {
    stories = new ArrayList<Story>();
  }

  /**
   * Add a report into Container
   */
  public void addStory(Story story) {
    stories.add(story);
  }

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
    int count = 0;

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

    return MathHelper.floor(sumExecutionTime / count, 2);
  }

  public SingleTest getTestWithName(String aVoid) {
    for (Story story : this.getStories()) {
      for (SingleTest test : story.getTests()) {
        if (aVoid.equals(test.getClazz() + "." + test.getMethod())) {
          return test;
        }
      }
    }
    return null;
  }

  public Story getStoryWithId(String id) {
    for (Story story : this.getStories()) {
      if (id.equalsIgnoreCase(story.getId())) {
        return story;
      }
    }
    return null;
  }

  public List<Story> getStoriesWithIteration(String string) {
    List<Story> retList = new ArrayList<Story>();

    for (Story story: this.getStories()) {
      if (story.getIteration().equals(string)) {
        retList.add(story);
      }
    }


    return retList;

  }
}