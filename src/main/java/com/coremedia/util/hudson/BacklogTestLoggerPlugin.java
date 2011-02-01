package com.coremedia.util.hudson;

import hudson.Plugin;
import hudson.tasks.BuildStep;

/**
 * Entry point for the BacklogTestLogger plugin.
 * 
 * @author Daniel Gegenheimer
 */
public class BacklogTestLoggerPlugin extends Plugin {

	public static final String ICON_FILE_NAME = "graph.gif";
	public static final String DISPLAY_NAME = "Global test report";
	public static final String GENERAL_DISPLAY_NAME = "Global test report";
	public static final String BUILD_DISPLAY_NAME = "Backlog test report";
	public static final String CONFIG_DISPLAY_NAME = "Activate Backlogger for this project";
	public static final String MATRIX_BUILD_DISPLAY_NAME = "Matrix build test report";
	public static final String MATRIX_CONFIGURATION_DISPLAY_NAME = "Matrix configuration test report";
	
	public static final String URL = "backlogtestlogger";
	

	@Override
	public void start() throws Exception {
		BuildStep.PUBLISHERS.addRecorder(BacklogTestLoggerPublisher.DESCRIPTOR);
	}
}
