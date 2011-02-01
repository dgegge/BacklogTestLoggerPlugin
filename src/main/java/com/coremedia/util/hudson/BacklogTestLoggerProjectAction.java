package com.coremedia.util.hudson;

import com.coremedia.util.hudson.Report.StoryContainer;
import hudson.model.AbstractBuild;
import hudson.model.Project;
import hudson.model.Result;
import hudson.util.ChartUtil;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;
import hudson.util.ColorPalette;
import hudson.util.DataSetBuilder;
import hudson.util.ShiftedCategoryAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.ui.RectangleInsets;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.awt.*;
import java.io.IOException;

/**
 * Action used for BacklogTestLogger report on project level.
 *
 * @author Daniel Gegenheimer
 */
public class BacklogTestLoggerProjectAction extends AbstractBacklogTestLoggerAction {

  private final Project project;

  public BacklogTestLoggerProjectAction(Project project) {
    this.project = project;
  }

  public String getDisplayName() {
    return BacklogTestLoggerPlugin.DISPLAY_NAME;
  }

  public Project getProject() {
    return project;
  }

  public StoryContainer getReports() {
    Object ob = getProject().getLastSuccessfulBuild();
    AbstractBuild build = (AbstractBuild) ob;
    if (build != null) {
      BacklogTestLoggerBuildAction ac = build
              .getAction(BacklogTestLoggerBuildAction.class);
      if (ac != null) {
        return ac.getStoryContainer();
      }
    }
    return null;
  }

  public BacklogTestLoggerBuildAction getActionByBuildNumber(int number) {
    return project.getBuildByNumber(number).getAction(
            BacklogTestLoggerBuildAction.class);
  }
}
