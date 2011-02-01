package com.coremedia.util.hudson.projectsAction;

import com.coremedia.util.hudson.AbstractBacklogTestLoggerAction;
import com.coremedia.util.hudson.BacklogTestLoggerBuildAction;
import com.coremedia.util.hudson.BacklogTestLoggerPlugin;
import com.coremedia.util.hudson.Report.StoryContainer;
import hudson.matrix.MatrixConfiguration;
import hudson.model.AbstractBuild;
import hudson.model.Project;
import hudson.model.Result;
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
 * This class is in charge of retrieving informations
 * on a single configuration of a Multi-Config project.
 *
 * @author gbossert
 */
public class BacklogTestLoggerMatrixConfigurationAction extends AbstractBacklogTestLoggerAction {

  private MatrixConfiguration project;

  public BacklogTestLoggerMatrixConfigurationAction(MatrixConfiguration project) {
    this.project = project;
  }

  public String getDisplayName() {
    return BacklogTestLoggerPlugin.MATRIX_CONFIGURATION_DISPLAY_NAME;
  }

  public StoryContainer getReports() {
    Object ob = project.getLastSuccessfulBuild();
    AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) ob;
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

  public Project getProject() {
    return project;
  }

  private JFreeChart createStdDevGraph() {
    return createNumberBuildGraph("Standart time deviation", "Time (ms)");
  }

  private JFreeChart createMeanRespLengthGraph() {
    return createNumberBuildGraph("Mean respond time", "Length (bytes)");
  }

  private JFreeChart createNumberBuildGraph(String valueName, String unitName) {
    DataSetBuilder<String, NumberOnlyBuildLabel> builder = new DataSetBuilder<String, NumberOnlyBuildLabel>();

    for (Object build : project.getBuilds()) {
      AbstractBuild abstractBuild = (AbstractBuild) build;
      if (!abstractBuild.isBuilding()
              && abstractBuild.getResult().isBetterOrEqualTo(
              Result.UNSTABLE)) {
        BacklogTestLoggerBuildAction action = abstractBuild
                .getAction(BacklogTestLoggerBuildAction.class);
        builder.add(1, valueName, new NumberOnlyBuildLabel(
                abstractBuild));
      }
    }

    JFreeChart chart = ChartFactory.createStackedAreaChart(valueName
            + " Trend", "Build", unitName, builder.build(),
            PlotOrientation.VERTICAL, false, false, false);

    chart.setBackgroundPaint(Color.WHITE);

    CategoryPlot plot = chart.getCategoryPlot();
    plot.setBackgroundPaint(Color.WHITE);
    plot.setOutlinePaint(null);
    plot.setForegroundAlpha(0.8f);
    plot.setRangeGridlinesVisible(true);
    plot.setRangeGridlinePaint(Color.black);

    CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
    plot.setDomainAxis(domainAxis);
    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
    domainAxis.setLowerMargin(0.0);
    domainAxis.setUpperMargin(0.0);
    domainAxis.setCategoryMargin(0.0);

    CategoryItemRenderer renderer = plot.getRenderer();
    renderer.setSeriesPaint(2, ColorPalette.RED);
    renderer.setSeriesPaint(1, ColorPalette.YELLOW);
    renderer.setSeriesPaint(0, ColorPalette.BLUE);

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

    // crop extra space around the graph
    plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

    return chart;
  }

  private boolean shouldReloadGraph(StaplerRequest request,
                                    StaplerResponse response) throws IOException {
    return shouldReloadGraph(request, response, project
            .getLastSuccessfulBuild());
  }

}
