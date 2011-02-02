package com.coremedia.util.hudson;

import com.coremedia.util.hudson.Report.StoryContainer;
import com.coremedia.util.hudson.Report.TrendReport;
import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.pojo.StoryState;
import hudson.model.AbstractBuild;
import hudson.model.Result;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action used for BacklogTestLogger report on build level.
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

  /**
   * Parameters for the health report.
   */
  private final HealthDescriptor healthDescriptor;

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

  public BacklogTestLoggerBuildAction(AbstractBuild<?, ?> build,
                                      ArrayList<String> files, PrintStream logger,
                                      HealthDescriptor healthDescriptor, Map<String, String> metrics) {
    this.build = build;
    /**
     * Compute the healthDescription
     */
    this.healthDescriptor = healthDescriptor;
    /**
     * Log the metrics
     */
    if (metrics.keySet().size() > 0) {
      logger.println("[BacklogTestLogger] The following metrics will be computed");
    } else {
      logger.println("[BacklogTestLogger] No metrics configured.");
    }
    for (String metric_name : metrics.keySet()) {
      logger.println("[BacklogTestLogger] Metric : " + metric_name);
    }

    


    for (String file : files) {
      String current_report = file;
      URI is;
      try {
        is = build.getWorkspace().child(current_report).toURI();

        logger.println("[BacklogTestLogger] Parsing Report : "
                + current_report);
        ReportReader rs = new ReportReader(is, logger, metrics);

        this.stories.getStories().addAll(StoryContainer.calculateStates(rs.getStories()));

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
    return Helper.floor(perFailedStories, 2);
  }

  public double getPerSuccessedStories() {
    this.perSuccessedStories = ((double) getNumSuccessedStories() / getNumStories()) * 100;
    return Helper.floor(perSuccessedStories, 2);
  }

  public double getPerIncompletedStories() {
    this.perIncompletedStories = ((double) getNumIncompletedStories() / getNumStories()) * 100;
    return Helper.floor(perIncompletedStories, 2);
  }

  public double getPerUntestedStories() {
    this.perUntestedStories = ((double) getNumUntestedStories() / getNumStories()) * 100;
    return Helper.floor(perUntestedStories, 2);
  }

  public StoryContainer getStoryContainer() {
    return this.stories;
  }

  public double getAvgExecutionTime() {
    return stories.getAvgExecutionTime();
  }


  /**
   * Returns the healthDescriptor.
   *
   * @return the healthDescriptor
   */
  public HealthDescriptor getHealthDescriptor() {
    return healthDescriptor;
  }

  private void allocateStories(List<Story> stories) {

    //successed
    this.successedStories = allocateStoriesWithState(stories, StoryState.SUCCESS);

    //failed
    this.failedStories = allocateStoriesWithState(stories, StoryState.FAILED);

    //incompleted
    this.incompletedStories = allocateStoriesWithState(stories, StoryState.INCOMPLETE);

    //untested
    this.untestedStories = allocateStoriesWithState(stories, StoryState.UNTESTED);
  }

  private List<Story> allocateStoriesWithState(List<Story> stories, StoryState state) {
    List<Story> retList = new ArrayList<Story>();

    for (Story story : stories) {
      if (story.getState().toString().equals(state.toString())) {
        retList.add(story);
      }
    }

    return retList;
  }

  /**
   * Returns the associated health report builder.
   *
   * @return the associated health report builder
   */
  public final HealthReportBuilder getHealthReportBuilder() {
    return new HealthReportBuilder(getHealthDescriptor());
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
   * This is needed for the entry page
   *
   * @return
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

}
