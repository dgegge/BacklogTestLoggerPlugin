package com.coremedia.util.hudson.projectsAction;

import com.coremedia.util.hudson.*;
import com.coremedia.util.hudson.AbstractBacklogTestLoggerAction;
import com.coremedia.util.hudson.BacklogTestLoggerBuildAction;
import com.coremedia.util.hudson.Report.StoryContainer;
import com.coremedia.util.hudson.matrixBuild.BacklogTestLoggerMatrixBuild;
import com.coremedia.util.hudson.matrixBuild.BacklogTestLoggerMatrixSubBuild;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.model.AbstractBuild;
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
import java.util.*;
import java.util.List;

/**
 * Action used for BacklogTestLogger report on matrix project level.
 *
 * @author Daniel Gegenheimer
 * @see com.coremedia.util.hudson.AbstractBacklogTestLoggerAction
 */
public class BacklogTestLoggerMatrixProjectAction extends
        AbstractBacklogTestLoggerAction {

  /**
   * The associated matrix project
   */
  private final MatrixProject project;

  /**
   * The maximum number of build to display
   */
  private final int max_nb_build = 10;

  /**
   * Constructor
   *
   * @param project the current matrix project
   */
  public BacklogTestLoggerMatrixProjectAction(MatrixProject project) {
    this.project = project;
  }

  /**
   * Getter for the display name which it used by hudson for the link menu
   * display
   */
  public String getDisplayName() {
    return BacklogTestLoggerPlugin.GENERAL_DISPLAY_NAME;
  }

  /**
   * Getter of the current matrix project
   *
   * @return the current matrix project
   */
  public MatrixProject getProject() {
    return project;
  }

  /**
   * @return
   */
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
