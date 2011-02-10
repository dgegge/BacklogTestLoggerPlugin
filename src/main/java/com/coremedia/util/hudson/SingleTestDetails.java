package com.coremedia.util.hudson;

import com.coremedia.util.model.pojo.SingleTest;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.ModelObject;
import hudson.model.Result;
import hudson.util.ChartUtil;
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
 * This class represents test details of a singleTest
 */
public class SingleTestDetails implements ModelObject {
  private final SingleTest test;
  private final AbstractBuild<?, ?> _owner;

  /**
   * Constructor
   * @param owner
   * @param test
   */
  public SingleTestDetails(final AbstractBuild<?, ?> owner, SingleTest test) {
    this.test = test;
    this._owner = owner;
  }

  public SingleTest getTest() {
    return test;
  }

  public AbstractBuild<?, ?> get_owner() {
    return _owner;
  }

  public String getDisplayName() {
    return "Details of test " + test.getClazz() + "." + test.getMethod();
  }

  /**
   * This method will create a chart. This could be implemented in the jelly-file via <img src="executionTimeGraph">
   *
   * @param request
   * @param response
   * @throws IOException
   */
  public void doExecutionTimeGraph(StaplerRequest request,
                                   StaplerResponse response) throws IOException {
    ChartUtil.generateGraph(request, response, createExecutionTimeGraph(),
            800, 250);
  }

  /**
   * creates the chart
   *
   * @return the chart
   */
  private JFreeChart createExecutionTimeGraph() {
    DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> builder = new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>();

    for (Object build : _owner.getProject().getBuilds()) {
      AbstractBuild abstractBuild = (AbstractBuild) build;
      if (!abstractBuild.isBuilding()) {
        BacklogTestLoggerBuildAction action = abstractBuild.getAction(BacklogTestLoggerBuildAction.class);

        if (action != null && action.getStories() != null) {

          SingleTest testWithName = action.getStoryContainer().getTestWithName(test.getTestname());

          if (testWithName != null && testWithName.getExecutiontime() != null & testWithName.getExecutiontime().length() > 0) {
            builder.add(new Double(testWithName.getExecutiontime()), "ExecutionTime", new ChartUtil.NumberOnlyBuildLabel(abstractBuild));
          }
          
        }
      }
    }
    JFreeChart chart = ChartFactory.createLineChart3D(
            "Evolution of Execution Time", "Build", "Execution time", builder
                    .build(), PlotOrientation.VERTICAL, true, true, false);

    chart.setBackgroundPaint(Color.WHITE);

    CategoryPlot plot = chart.getCategoryPlot();
    plot.setBackgroundPaint(Color.WHITE);
    plot.setOutlinePaint(null);
    plot.setForegroundAlpha(0.4f);
    plot.setRangeGridlinesVisible(true);
    plot.setRangeGridlinePaint(Color.black);

    CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
    plot.setDomainAxis(domainAxis);
    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
    domainAxis.setLowerMargin(0.0);
    domainAxis.setUpperMargin(0.0);
    domainAxis.setCategoryMargin(0.0);

    CategoryItemRenderer renderer = plot.getRenderer();
    renderer.setSeriesPaint(0, ColorPalette.BLUE);
    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

    // crop extra space around the graph
    plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

    return chart;
  }

  /**
   * Returns the dynamic result
   * This method is called, when a redirect is made.
   * it calls the  BacklogTestLoggerAction.getDynamic method
   *
   * @param link     the link to identify the sub page to show
   * @param request  Stapler request
   * @param response Stapler response
   * @return the dynamic result of the analysis.
   */
  public Object getDynamic(final String link, final StaplerRequest request,
                           final StaplerResponse response) {
    Object result = null;
    for (Action action : _owner.getActions()) {
      if (action instanceof BacklogTestLoggerBuildAction) {
        result = ((BacklogTestLoggerBuildAction) action).getDynamic(link,request,response);
      }
    }
    return result;
  }
}
