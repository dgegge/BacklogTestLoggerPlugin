package com.coremedia.util.model.helper;

import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.pojo.StoryState;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper-class for Stories
 */
public class StoryHelper {

  /**
   * Calculates the status of a story according to the tests, the story contains
   * @param stories which should be calculated
   * @return calculated stories
   */
  public static List<Story> calculateStates(final List<Story> stories) {
    List<Story> retStories = new ArrayList<Story>();
    for (Story story : stories) {
      story.setState(StoryState.SUCCESS);

      if (story.getTests() == null || story.getTests().isEmpty()) {
        story.setState(StoryState.UNTESTED);
      }

      boolean done = true;
      boolean success = true;

      for (SingleTest test : story.getTests()) {

        if (!test.getDone().booleanValue() && !story.getState().equals(StoryState.FAILED)) {
          done = false;
        }

        if (!test.getSuccess().booleanValue()) {
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
}
