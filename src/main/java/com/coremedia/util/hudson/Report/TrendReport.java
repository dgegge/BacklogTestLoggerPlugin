package com.coremedia.util.hudson.Report;

/**
 * Class representation of trend between two reports
 *
 * @author Daniel Gegenheimer
 */
public class TrendReport {

  private StoryContainer actualResult;
  private StoryContainer oldResult;

  public TrendReport(StoryContainer actualReport, StoryContainer oldReport) {
    this.actualResult = actualReport;
    this.oldResult = oldReport;
  }

  /**
   * Round a double with n decimals
   *
   * @param a value to convert
   * @param n Number of decimals
   * @return the rounded number
   */
  public static double floor(double a, int n) {
    double p = Math.pow(10.0, n);
    return Math.floor((a * p) + 0.5) / p;
  }

  /**
   * @return the actual report
   */
  public StoryContainer getActualReportContainer() {
    return actualResult;
  }

  /**
   * @param actualReportContainer the actual report
   */
  public void setActualReportContainer(StoryContainer actualReportContainer) {
    this.actualResult = actualReportContainer;
  }

  /**
   * @return the old report
   */
  public StoryContainer getOldReport() {
    return oldResult;
  }

  /**
   * @param oldReportContainer the old report
   */
  public void setOldReport(StoryContainer oldReportContainer) {
    this.oldResult = oldReportContainer;
  }

  /**
   * @return true if the number of test has increased
   */
  public boolean isNumberOfStoriesHasIncrease() {
    return (actualResult.getStories().size() > oldResult.getStories().size());
  }

  /**
   * @return true if the number of test has increased
   */
  public boolean isNumberOfStoriesHasDecrease() {
    return (actualResult.getStories().size() < oldResult.getStories().size());
  }

  public boolean isNumberOfStoriesStable() {
    return (actualResult.getStories().size() == oldResult.getStories().size());
  }

  /**
   * @return true if the number of test has increased
   */
  public boolean isNumberOfSuccessedStoriesHasIncrease() {
    return (actualResult.getSuccessfulStories().size() > oldResult.getSuccessfulStories().size());
  }

  /**
   * @return true if the number of test has increased
   */
  public boolean isNumberOfSuccessedStoriesHasDecrease() {
    return (actualResult.getSuccessfulStories().size() < oldResult.getSuccessfulStories().size());
  }

  public boolean isNumberOfSuccessedStoriesStable() {
    return (actualResult.getSuccessfulStories().size() == oldResult.getSuccessfulStories().size());
  }

  /**
   * @return true if the number of test has increased
   */
  public boolean isNumberOfFailedStoriesHasIncrease() {
    return (actualResult.getFailedStories().size() > oldResult.getFailedStories().size());
  }

  /**
   * @return true if the number of test has decreased
   */
  public boolean isNumberOfFailedStoriesHasDecrease() {
    return (actualResult.getFailedStories().size() < oldResult.getFailedStories().size());
  }

  public boolean isNumberOfFailedStoriesStable() {
    return (actualResult.getFailedStories().size() == oldResult.getFailedStories().size());
  }

  /**
   * @return true if the number of test has increased
   */
  public boolean isNumberOfUntestedStoriesHasIncrease() {
    return (actualResult.getUntestedStories().size() > oldResult.getUntestedStories().size());
  }

  /**
   * @return true if the number of test has decreased
   */
  public boolean isNumberOfUntestedStoriesHasDecrease() {
    return (actualResult.getUntestedStories().size() < oldResult.getUntestedStories().size());
  }

  public boolean isNumberOfUntestedStoriesStable() {
    return (actualResult.getUntestedStories().size() == oldResult.getUntestedStories().size());
  }

  /**
   * @return true if the number of test has increased
   */
  public boolean isNumberOfIncompletedStoriesHasIncrease() {
    return (actualResult.getIncompleteStories().size() > oldResult.getIncompleteStories().size());
  }

  /**
   * @return true if the number of test has decreased
   */
  public boolean isNumberOfIncompletedStoriesHasDecrease() {
    return (actualResult.getIncompleteStories().size() < oldResult.getIncompleteStories().size());
  }

  public boolean isNumberOfIncompletedStoriesStable() {
    return (actualResult.getIncompleteStories().size() == oldResult.getIncompleteStories().size());
  }

  public boolean isAvgExecutionTimeHasIncrease() {
    return (actualResult.getAvgExecutionTime() > oldResult.getAvgExecutionTime());
  }

  public boolean isAvgExecutionTimeHasDecrease() {
    return (actualResult.getAvgExecutionTime() < oldResult.getAvgExecutionTime());
  }

  public boolean isAvgExecutionTimeStable() {
    return (actualResult.getAvgExecutionTime() == oldResult.getAvgExecutionTime());
  }


  /**
   * @return true if the percent of passed test has increase
   */
  public boolean isPercentOfSuccessedStoriesHasIncrease() {
    return (actualResult.getSuccessfulStories().size() / actualResult.getStories().size() / 100) >
            (oldResult.getSuccessfulStories().size() / oldResult.getStories().size() / 100);
  }
  
  /**
   * @return true if the percent of passed test has increase
   */
  public boolean isPercentOfFailedStoriesHasIncrease() {
    return (actualResult.getFailedStories().size() / actualResult.getStories().size() / 100) >
            (oldResult.getFailedStories().size() / oldResult.getStories().size() / 100);
  }




  /**
   * @return true if the percent of passed test has increase
   */
  public boolean isPercentOfIncompletedStoriesHasIncrease() {
    return (actualResult.getIncompleteStories().size() / actualResult.getStories().size() / 100) >
            (oldResult.getIncompleteStories().size() / oldResult.getStories().size() / 100);
  }


  /**
   * @return true if the percent of passed test has increase
   */
  public boolean isPercentOfUntestedStoriesHasIncrease() {
    return (actualResult.getUntestedStories().size() / actualResult.getStories().size() / 100) >
            (oldResult.getUntestedStories().size() / oldResult.getStories().size() / 100);
  }



}
