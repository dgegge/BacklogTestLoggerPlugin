package com.coremedia.util.hudson.Report;

import com.coremedia.util.hudson.Helper;
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

  public static List<Story> calculateStates(List<Story> stories) {
    List<Story> retStories = new ArrayList<Story>();
    for (Story story : stories) {
      story.setState(StoryState.SUCCESS);

      if (story.getTests() == null || story.getTests().isEmpty()) {
        story.setState(StoryState.UNTESTED);
      }

      boolean done = true;
      boolean success = true;

      for (SingleTest test : story.getTests()) {

        //System.out.println("Done: " + test.getDone().booleanValue() +  " Success " + test.getSuccess().booleanValue());

        if (!test.getDone().booleanValue() && !story.getState().equals(StoryState.FAILED)) {
          //System.out.println("Done: " + test.getDone().booleanValue());
          done = false;
        }

        if (!test.getSuccess().booleanValue()) {
          //System.out.println("Success " + test.getSuccess().booleanValue());
          success = false;
        }
      }

      if (!done) {
        story.setState(StoryState.INCOMPLETE);
      }
      if (!success) {
        story.setState(StoryState.FAILED);
      }
      retStories.add(story);
    }

    return retStories;
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

    return Helper.floor(sumExecutionTime/count,2);
  }
}