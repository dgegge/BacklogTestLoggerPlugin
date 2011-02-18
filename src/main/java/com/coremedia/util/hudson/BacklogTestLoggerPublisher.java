package com.coremedia.util.hudson;


import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The publisher creates the results we want from the BacklogTestLogger execution.
 *
 * @author Daniel Gegenheimer
 */
public class BacklogTestLoggerPublisher extends Publisher {

  private String name;
  public static final Descriptor<Publisher> DESCRIPTOR = new BacklogTestLoggerDescriptor();

  public String getName() {
    return name;
  }

  public Descriptor<Publisher> getDescriptor() {
    return DESCRIPTOR;
  }

  public BuildStepMonitor getRequiredMonitorService() {
    return BuildStepMonitor.BUILD;
  }

  /**
   * This method configures the plugin. it is called when a change under "configuration"-page in jenkins is made
   *
   * @param name is just an acronym for "path of report-files"
   */
  @DataBoundConstructor
  public BacklogTestLoggerPublisher(String name) {
    this.name = name;
  }

  /**
   * Performes the action. "main"-method equivalent
   *
   * @param build    AbstractBuild
   * @param launcher Launcher
   * @param listener BuildListener
   * @return boolean
   * @throws InterruptedException failed
   * @throws IOException          failed
   */
  public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
                         BuildListener listener) throws InterruptedException, IOException {

    PrintStream logger = listener.getLogger();

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
    FilePath path = build.getWorkspace();

    for (String filePath : files) {
      List<File> tmp_files = new ArrayList<File>();
      File directory;
      File file = new File(filePath);
      final String XML = "*.xml";
      if (filePath.endsWith(XML)) {
        String dir_path = path.getRemote() + File.separator + filePath.substring(0, filePath.length() - (XML.length() + 1));
        logger.println("Searching for files in: " + dir_path);
        directory = new File(dir_path);

        if (directory != null) {
          for (File f : directory.listFiles()) {
            logger.println("Found file to analyze: " + f.getAbsolutePath());
            if (f.getAbsolutePath().toLowerCase().endsWith(".xml")) {
              tmp_files.add(f);
            } else {
              logger.println("File has not a .xml-ending");
            }
          }
        } else {
          logger.println("Directory doesn't exist");
        }


      } else if (file.exists() && file.isFile()) {
        logger.println("Found file to analyze: " + file.getAbsolutePath());
        tmp_files.add(file);
      }

      for (File tmp_file : tmp_files) {
        if (build.getProject().getWorkspace().child(tmp_file.getAbsolutePath()).exists()) {
          filesToParse.add(tmp_file.getAbsolutePath());
        } else {
          logger.println("[CapsAnalysis] Impossible to analyse report " + tmp_file + " file not found!");
          build.setResult(Result.UNSTABLE);
        }
      }
    }

    try {
      build.addAction(new BacklogTestLoggerBuildAction(build, filesToParse, logger));

    } catch (BacklogTestLoggerParseException gpe) {
      logger.println("[CapsAnalysis] generating reports analysis failed!");
      build.setResult(Result.UNSTABLE);
    }
    return true;
  }

  /**
   * Descriptor for the BacklogTestLogger plugin
   * Must extends BuildStepDescriptor since issue HUDSON-5612
   *
   * @author Daniel Gegenheimer
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

  /**
   * Class representing a file filter, which only looks for files ending with .xml
   */
  private class xmlFilenameFilter implements FilenameFilter {

    /**
     * {@inheritDoc}
     */
    public boolean accept(File dir, String name) {
      return new File(dir, name).isFile() && name.toLowerCase().endsWith(".xml");
    }
  }


}
