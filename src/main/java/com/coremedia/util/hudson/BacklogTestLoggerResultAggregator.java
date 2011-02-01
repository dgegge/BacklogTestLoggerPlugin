package com.coremedia.util.hudson;

import hudson.Launcher;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.model.BuildListener;

import java.io.IOException;
import java.util.Map;

public class BacklogTestLoggerResultAggregator extends MatrixAggregator {

  MatrixTestReportAction result;

  public BacklogTestLoggerResultAggregator(MatrixBuild build, Launcher launcher,
                                       BuildListener listener) {
    super(build, launcher, listener);
  }

  public boolean startBuild() throws InterruptedException, IOException {
    result = new MatrixTestReportAction(build);
    build.addAction(result);
    return true;
  }


  public boolean endRun(MatrixRun run) throws InterruptedException,
          IOException {
    Map<String, String> buildVariables = run.getBuildVariables();

    BacklogTestLoggerBuildAction buildAction = run.getAction(BacklogTestLoggerBuildAction.class);
    if (buildAction != null) {
      result.addSubBuildResult(buildAction.getStoryContainer(), buildVariables);
    }
    return true;
  }

  public boolean endBuild() throws InterruptedException, IOException {
    result.computeStats();
    return true;
  }

}
