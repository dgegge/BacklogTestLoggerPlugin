package com.coremedia.util.hudson;

import com.coremedia.util.hudson.projectsAction.BacklogTestLoggerFreestyleProjectAction;
import com.coremedia.util.hudson.projectsAction.BacklogTestLoggerMatrixConfigurationAction;
import com.coremedia.util.hudson.projectsAction.BacklogTestLoggerMatrixProjectAction;
import hudson.Launcher;
import hudson.matrix.*;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * The publisher creates the results we want from the BacklogTestLogger execution.
 *
 * @author Daniel Gegenheimer
 */
public class BacklogTestLoggerPublisher extends HealthPublisher implements MatrixAggregatable {

  public String getName() {
    return name;
  }

  public String getThreshold() {
    return threshold;
  }

  public String getHealthy() {
    return healthy;
  }

  public String getUnhealthy() {
    return unhealthy;
  }

  public String getMetrics() {
    return metrics;
  }

  private String name;
  private String threshold;
  private String healthy;
  private String unhealthy;
  private String metrics;

  @DataBoundConstructor
  public BacklogTestLoggerPublisher(String name, String threshold,
                                    String healthy, String unhealthy, String metrics) {
    this.name = name;
    if (threshold != "") {
      this.threshold = threshold;
    } else {
      this.threshold = "0";
    }
    if (healthy != "") {
      this.healthy = healthy;
    } else {
      this.healthy = "0";
    }
    if (unhealthy != "") {
      this.unhealthy = unhealthy;
    } else {
      this.unhealthy = "0";
    }
    this.metrics = metrics;
  }


  public Descriptor<Publisher> getDescriptor() {
    return DESCRIPTOR;
  }

  public MatrixAggregator createAggregator(final MatrixBuild matrixBuild, Launcher launcher, BuildListener listener) {
    return new BacklogTestLoggerResultAggregator(matrixBuild, launcher, listener);
  }

  public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
                         BuildListener listener) throws InterruptedException, IOException {

    PrintStream logger = listener.getLogger();
    /**
     * Compute metrics parametring
     */
    Map<String, String> list_metrics = new HashMap<String, String>();
    //Parse the field to understand the metrics
    //Format : name=xmlfield;
    if (metrics != null && metrics.length() > 0) {
      List<String> tmps = Arrays.asList(this.metrics.split(";"));
      for (String tmp : tmps) {
        List<String> f = Arrays.asList(tmp.split("="));
        if (f.size() == 2 && f.get(0).trim().length() > 0 && f.get(1).length() > 0) {
          list_metrics.put(f.get(0).trim(), f.get(1).trim());
        }
      }
    }

    /**
     * Compute the HealthDescription
     */
    HealthDescriptor hl = new HealthDescriptor();
    try {
      hl.setMaxHealth(Integer.parseInt(unhealthy));
    } catch (java.lang.NumberFormatException e) {
      hl.setMaxHealth(0);
    }
    try {
      hl.setMinHealth(Integer.parseInt(healthy));
    } catch (java.lang.NumberFormatException e) {
      hl.setMinHealth(0);
    }
    try {
      hl.setUnstableHealth(Integer.parseInt(threshold));
    } catch (java.lang.NumberFormatException e) {
      hl.setUnstableHealth(0);
    }

    /**
     * Define if we must parse multiple file by searching for , in the name
     * var
     */

    if (name == null || name.length() < 1) {
      logger.println("[BacklogTestLogger] no reports configured to be analyzed ");
      return false;
    }

    String[] files = name.split(",");
    if (files.length > 1) {
      logger.println("[CapsAnalysis] Multiple reports detected.");
    }
    ArrayList<String> filesToParse = new ArrayList<String>();
    for (int i = 0; i < files.length; i++) {
      FileSet fileSet = new FileSet();
      File workspace = new File(build.getWorkspace().toURI());

      fileSet.setDir(workspace);
      fileSet.setIncludes(files[i].trim());
      Project antProject = new Project();
      fileSet.setProject(antProject);
      String[] tmp_files = fileSet.getDirectoryScanner(antProject).getIncludedFiles();
      for (int j = 0; j < tmp_files.length; j++) {
        if (build.getProject().getWorkspace().child(tmp_files[j]).exists()) {
          filesToParse.add(tmp_files[j]);
        } else {
          logger.println("[CapsAnalysis] Impossible to analyse report " + tmp_files[j] + " file not found!");
          build.setResult(Result.UNSTABLE);
        }
      }
    }

    try {
      build.addAction(new BacklogTestLoggerBuildAction(build, filesToParse,
              logger, hl, list_metrics));

    } catch (BacklogTestLoggerParseException gpe) {
      logger
              .println("[CapsAnalysis] generating reports analysis failed!");
      build.setResult(Result.UNSTABLE);
    }
    return true;
  }

  public Action getProjectAction(AbstractProject project) {
    if (project instanceof MatrixProject) {
      return new BacklogTestLoggerMatrixProjectAction((MatrixProject) project);
    } else if (project instanceof MatrixConfiguration) {
      return new BacklogTestLoggerMatrixConfigurationAction((MatrixConfiguration) project);
    } else if (project instanceof FreeStyleProject) {
      return new BacklogTestLoggerFreestyleProjectAction((FreeStyleProject) project);
    }
    return null;
  }

  public static final Descriptor<Publisher> DESCRIPTOR = new BacklogTestLoggerDescriptor();

  /**
   * Descriptor for the BacklogTestLogger plugin
   * Must extends BuildStepDescriptor since issue HUDSON-5612
   *
   * @author gbossert
   */
  public static final class BacklogTestLoggerDescriptor extends BuildStepDescriptor<Publisher> {
    protected BacklogTestLoggerDescriptor() {
      super(BacklogTestLoggerPublisher.class);
    }

    public String getDisplayName() {
      return BacklogTestLoggerPlugin.CONFIG_DISPLAY_NAME;
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
      return true;
    }
  }

  public BuildStepMonitor getRequiredMonitorService() {
    return BuildStepMonitor.BUILD;
  }
}
