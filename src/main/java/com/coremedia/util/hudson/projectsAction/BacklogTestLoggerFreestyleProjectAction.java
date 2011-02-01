package com.coremedia.util.hudson.projectsAction;

import com.coremedia.util.hudson.AbstractBacklogTestLoggerAction;
import com.coremedia.util.hudson.BacklogTestLoggerBuildAction;
import com.coremedia.util.hudson.BacklogTestLoggerPlugin;
import com.coremedia.util.hudson.Report.StoryContainer;
import hudson.model.AbstractBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Project;

/**
 * Action used for BacklogTestLogger report on project level.
 *
 * @author Daniel Gegenheimer
 */
public class BacklogTestLoggerFreestyleProjectAction extends AbstractBacklogTestLoggerAction {

  private final Project project;

  public BacklogTestLoggerFreestyleProjectAction(FreeStyleProject project) {
    this.project = project;
  }

  public String getDisplayName() {
    return BacklogTestLoggerPlugin.GENERAL_DISPLAY_NAME;
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
