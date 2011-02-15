package com.coremedia.util.hudson;

import com.coremedia.util.model.pojo.Story;
import com.coremedia.util.model.pojo.StoryState;
import hudson.model.AbstractBuild;
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
 * This class represents story details of a story
 */
public class SingleStoryDetails extends AbstractDetails {
  private final Story story;

  public SingleStoryDetails(final AbstractBuild<?, ?> owner, Story story) {
    super(owner);
    this.story = story;
  }

  public Story getStory() {
    return story;
  }

  public String getDisplayName() {
    return "Details of story No: " + story.getId();
  }

  /**
   * This method will create a chart. This could be implemented in the jelly-file via <img src="stateGraph">
   *
   * @param request
   * @param response
   * @throws IOException
   */
  public void doStateGraph(StaplerRequest request,
                           StaplerResponse response) throws IOException {
    ChartUtil.generateGraph(request, response, createSuccessGraph(),
            800, 250);
  }

  /**
   * creates the chart
   *
   * @return the chart
   */
  private JFreeChart createSuccessGraph() {
    DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> builder = new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>();

    for (Object build : _owner.getProject().getBuilds()) {
      AbstractBuild abstractBuild = (AbstractBuild) build;
      if (!abstractBuild.isBuilding()) {
        BacklogTestLoggerBuildAction action = abstractBuild.getAction(BacklogTestLoggerBuildAction.class);

        if (action != null && action.getStories() != null) {

          Story story = action.getStoryContainer().getStoryWithId(this.story.getId());

          if (story != null) {
            int i = 0;

            if (story.getState().equals(StoryState.SUCCESS))
              i = 3;

            if (story.getState().equals(StoryState.INCOMPLETE))
              i = 2;

            if (story.getState().equals(StoryState.FAILED))
              i = 1;

            if (story.getState().equals(StoryState.UNTESTED))
              i = 0;

            builder.add(i, "Success", new ChartUtil.NumberOnlyBuildLabel(abstractBuild));
          }

        }
      }
    }
    JFreeChart chart = ChartFactory.createLineChart3D(
            "Evolution of State", "Build", "Success", builder
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
}