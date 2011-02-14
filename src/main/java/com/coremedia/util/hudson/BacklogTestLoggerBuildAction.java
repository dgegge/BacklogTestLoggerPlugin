package com.coremedia.util.hudson;

import com.coremedia.util.hudson.Report.StoryContainer;
import com.coremedia.util.hudson.Report.TrendReport;
import com.coremedia.util.model.helper.MathHelper;
import com.coremedia.util.model.helper.StoryHelper;
import com.coremedia.util.model.pojo.SingleTest;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.pojo.StoryState;
import hudson.model.AbstractBuild;
import hudson.model.Result;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action used for BacklogTestLogger report on build level.
 * This is the "main"-class which is used by hudson/jenkins
 * Use the getter-methods in jelly-files. e.g. it.build.numFailedStories
 * returns the content of the field numFailedStories
 *
 * @author Daniel Gegenheimer
 */
public class BacklogTestLoggerBuildAction extends AbstractBacklogTestLoggerAction {

  private final AbstractBuild<?, ?> build;
  private TrendReport trendReport;
  private StoryContainer stories = new StoryContainer();
  private List<Story> failedStories = new ArrayList<Story>();
  private List<Story> successedStories = new ArrayList<Story>();
  private List<Story> incompletedStories = new ArrayList<Story>();
  private List<Story> untestedStories = new ArrayList<Story>();

  private Map<String, SingleTest> tests = new HashMap<String, SingleTest>();

  private int numFailedStories;
  private int numSuccessedStories;
  private int numIncompletedStories;
  private int numUntestedStories;
  private int numStories;

  private double perFailedStories;
  private double perSuccessedStories;
  private double perIncompletedStories;
  private double perUntestedStories;

  /**
   * Returns the build as owner of this action.
   *
   * @return the owner
   */
  public final AbstractBuild<?, ?> getOwner() {
    return build;
  }

  public String getDisplayName() {
    return BacklogTestLoggerPlugin.BUILD_DISPLAY_NAME;
  }

  public AbstractBuild<?, ?> getBuild() {
    return build;
  }

  public List<Story> getStories() {
    return stories.getStories();
  }

  public List<Story> getFailedStories() {
    return this.failedStories;
  }

  public List<Story> getSuccessedStories() {
    return successedStories;
  }

  public List<Story> getIncompletedStories() {
    return incompletedStories;
  }

  public List<Story> getUntestedStories() {
    return untestedStories;
  }

  public int getNumFailedStories() {
    this.numFailedStories = failedStories.size();
    return numFailedStories;
  }

  public int getNumSuccessedStories() {
    this.numSuccessedStories = successedStories.size();
    return numSuccessedStories;
  }

  public int getNumIncompletedStories() {
    this.numIncompletedStories = incompletedStories.size();
    return numIncompletedStories;
  }

  public int getNumUntestedStories() {
    this.numUntestedStories = untestedStories.size();
    return numUntestedStories;
  }

  public int getNumStories() {
    this.numStories = failedStories.size() + incompletedStories.size() + successedStories.size() + untestedStories.size();
    return numStories;
  }

  public double getPerFailedStories() {
    this.perFailedStories = ((double) getNumFailedStories() / getNumStories()) * 100;
    return MathHelper.floor(perFailedStories, 2);
  }

  public double getPerSuccessedStories() {
    this.perSuccessedStories = ((double) getNumSuccessedStories() / getNumStories()) * 100;
    return MathHelper.floor(perSuccessedStories, 2);
  }

  public double getPerIncompletedStories() {
    this.perIncompletedStories = ((double) getNumIncompletedStories() / getNumStories()) * 100;
    return MathHelper.floor(perIncompletedStories, 2);
  }

  public double getPerUntestedStories() {
    this.perUntestedStories = ((double) getNumUntestedStories() / getNumStories()) * 100;
    return MathHelper.floor(perUntestedStories, 2);
  }

  public StoryContainer getStoryContainer() {
    return this.stories;
  }

  public double getAvgExecutionTime() {
    return stories.getAvgExecutionTime();
  }

  /**
   * You can see this method as the "main"-method. It is called whenever is build is made
   *
   * @param build
   * @param files
   * @param logger
   */
  public BacklogTestLoggerBuildAction(AbstractBuild<?, ?> build,
                                      ArrayList<String> files, PrintStream logger) {
    this.build = build;

    /**
     * Parse the reports and create story-information
     */
    for (String file : files) {
      String current_report = file;
      URI is;
      try {
        is = build.getWorkspace().child(current_report).toURI();

        logger.println("[BacklogTestLogger] Parsing Report : "
                + current_report);
        ReportReader rs = new ReportReader(is, logger);

        this.stories.getStories().addAll(StoryHelper.calculateStates(rs.getStories()));

        makeAllocationTestsToStories(this.stories.getStories());

        allocateStories(this.stories.getStories());

      } catch (IOException e) {
        logger.println("[BacklogTestLogger] Impossible to analyse report "
                + current_report + ", file can't be read.");
        build.setResult(Result.UNSTABLE);
      } catch (InterruptedException e) {
        logger.println("[BacklogTestLogger] Impossible to analyse report "
                + current_report + ", file can't be read.");
        build.setResult(Result.UNSTABLE);
      }
    }
  }

  /**
   * Allocates stories to tests
   * This is needed, because we also want to show the stories for each test
   *
   * @param stories
   */
  private void makeAllocationTestsToStories(List<Story> stories) {
    for (Story story : stories) {
      for (SingleTest test : story.getTests()) {
        SingleTest singleTest = tests.get(test.getTestname());

        if (singleTest == null) {
          singleTest = test;
        }

        singleTest.addStoryToTest(story);

        tests.put(test.getTestname(), singleTest);

      }
    }
  }

  /**
   * Calculates the state of a story due to its test-results
   *
   * @param stories
   */
  private void allocateStories(List<Story> stories) {
    //successed
    this.successedStories = StoryHelper.allocateStoriesWithState(stories, StoryState.SUCCESS);
    //failed
    this.failedStories = StoryHelper.allocateStoriesWithState(stories, StoryState.FAILED);
    //incompleted
    this.incompletedStories = StoryHelper.allocateStoriesWithState(stories, StoryState.INCOMPLETE);
    //untested
    this.untestedStories = StoryHelper.allocateStoriesWithState(stories, StoryState.UNTESTED);
  }


  /**
   * @return the associated trend report
   */
  public TrendReport getTrendReport() {
    if (this.trendReport == null) {
      this.trendReport = computeTrendReport();
    }
    return this.trendReport;
  }

  private TrendReport computeTrendReport() {
    Object ob = build.getPreviousBuild();
    AbstractBuild build = (AbstractBuild) ob;
    if (build != null) {
      BacklogTestLoggerBuildAction ac = build
              .getAction(BacklogTestLoggerBuildAction.class);
      if (ac != null) {
        return new TrendReport(stories, ac.getStoryContainer());
      }
    }
    return null;
  }

  /**
   * creates a summary which could be implemented using it.summary in jelly files
   *
   * @return Summary HTML
   */
  public String getSummary() {
    StringBuilder strbuilder = new StringBuilder();
    strbuilder.append("<div class=\"progress-container\">");

    int failedStories = this.getNumFailedStories();
    int successedStories = this.getNumSuccessedStories();
    int incompletedStories = this.getNumIncompletedStories();
    int untestedStories = this.getNumUntestedStories();

    double perFailedStories = this.getPerFailedStories();           //red
    double perSuccessedStories = this.getPerSuccessedStories();     //green
    double perIncompletedStories = this.getPerIncompletedStories(); //yellow
    double perUntestedStories = this.getPerUntestedStories();       //blue

    //green
    strbuilder.append("<div id=\"green\" style=\"width:" + perSuccessedStories + "%;\">"
            + perSuccessedStories + "% (" + successedStories + ")");
    strbuilder.append("<span> " + "Successed Stories: " + perSuccessedStories + "% (" + successedStories + ")</span>" + "</div>");

    //yellow
    strbuilder.append("<div id=\"yellow\" style=\"width:" + perIncompletedStories + "%;\">"
            + perIncompletedStories + "% (" + incompletedStories + ")");
    strbuilder.append("<span> " + "Incompleted Stories: " + perIncompletedStories + "% (" + incompletedStories + ")</span>" + "</div>");

    //red
    strbuilder.append("<div id=\"red\" style=\"width:" + perFailedStories + "%;\">"
            + perFailedStories + "% (" + failedStories + ")");
    strbuilder.append("<span> " + "Failed Stories: " + perFailedStories + "% (" + failedStories + ")</span>" + "</div>");

    //blue
    strbuilder.append("<div id=\"blue\" style=\"width:" + perUntestedStories + "%;\">"
            + perUntestedStories + "% (" + untestedStories + ")");
    strbuilder.append("<span> " + "Untested Stories: " + perUntestedStories + "% (" + untestedStories + ")</span>" + "</div>");

    strbuilder.append("</div>");
    return strbuilder.toString();
  }

  /**
   * This is needed for the entry page. This method could be used by jelly files using it.smallSummary
   *
   * @return html-code with a tiny summary
   */
  public String getSmallSummary() {
    StringBuilder strbuilder = new StringBuilder();
    strbuilder.append("<div class=\"progress-container\">");

    int failedStories = this.getNumFailedStories();
    int successedStories = this.getNumSuccessedStories();
    int incompletedStories = this.getNumIncompletedStories();
    int untestedStories = this.getNumUntestedStories();

    double perFailedStories = this.getPerFailedStories();           //red
    double perSuccessedStories = this.getPerSuccessedStories();     //green
    double perIncompletedStories = this.getPerIncompletedStories(); //yellow
    double perUntestedStories = this.getPerUntestedStories();       //blue

    //green
    strbuilder.append("<div id=\"green\" style=\"width:" + perSuccessedStories + "%;\">");
    strbuilder.append("<span> " + "Successed Stories: " + perSuccessedStories + "% (" + successedStories + ")</span>" + "</div>");

    //yellow
    strbuilder.append("<div id=\"yellow\" style=\"width:" + perIncompletedStories + "%;\">");
    strbuilder.append("<span> " + "Incompleted Stories: " + perIncompletedStories + "% (" + incompletedStories + ")</span>" + "</div>");

    //red
    strbuilder.append("<div id=\"red\" style=\"width:" + perFailedStories + "%;\">");
    strbuilder.append("<span> " + "Failed Stories: " + perFailedStories + "% (" + failedStories + ")</span>" + "</div>");

    //blue
    strbuilder.append("<div id=\"blue\" style=\"width:" + perUntestedStories + "%;\">");
    strbuilder.append("<span> " + "Untested Stories: " + perUntestedStories + "% (" + untestedStories + ")</span>" + "</div>");

    strbuilder.append("</div>");
    return strbuilder.toString();

  }

  /**
   * Returns the dynamic result
   * Calculates an object which the referrer is pointing to
   *
   * @param link     the link to identify the sub page to show
   * @param request  Stapler request
   * @param response Stapler response
   * @return the dynamic result of the analysis.
   */
  public Object getDynamic(final String link, final StaplerRequest request,
                           final StaplerResponse response) {

    Object resultat = null;
    if (link.startsWith("testDetails.")) {
      String testName = StringUtils.substringAfter(link, "testDetails.");
      resultat = new SingleTestDetails(getOwner(), stories.getTestWithName(SingleTest.resolveTestNameInUrl(testName)));
    } else if (link.startsWith("storyDetails.")) {
      String storyId = StringUtils.substringAfter(link, "storyDetails.");
      resultat = new SingleStoryDetails(getOwner(), stories.getStoryWithId(storyId));
    }

    return resultat;
  }

}
