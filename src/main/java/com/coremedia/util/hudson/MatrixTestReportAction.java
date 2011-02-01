package com.coremedia.util.hudson;

import com.coremedia.util.hudson.Report.StoryContainer;
import com.coremedia.util.hudson.matrixBuild.BacklogTestLoggerMatrixBuild;
import com.coremedia.util.hudson.matrixBuild.BacklogTestLoggerMatrixSubBuild;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.model.Action;
import java.util.*;
import java.util.Map.Entry;

public class MatrixTestReportAction extends AbstractBacklogTestLoggerAction
        implements Action {

  BacklogTestLoggerMatrixBuild matrixbuild;
  private int numberOfExecutedTest;
  private int numberOfTest;
  private int numberOfPassedTest;
  private int numberOfNotExecutedTest;
  private int numberOfTrueFalseTest;
  private int numberOfFailedTest;
  private MatrixBuild build;
  private MatrixProject project;

  public MatrixTestReportAction(MatrixBuild build) {
    this.matrixbuild = new BacklogTestLoggerMatrixBuild(build.getNumber());
    this.build = build;
    this.project = build.getProject();
  }

  public MatrixBuild getBuild() {
    return build;
  }

  public MatrixProject getProject() {
    return project;
  }

  public String getDisplayName() {
    return BacklogTestLoggerPlugin.MATRIX_BUILD_DISPLAY_NAME;
  }

  public String getSearchUrl() {
    return BacklogTestLoggerPlugin.URL;
  }

  public String getIconFileName() {
    return BacklogTestLoggerPlugin.ICON_FILE_NAME;
  }

  public String getUrlName() {
    return BacklogTestLoggerPlugin.URL;
  }

  /**
   * Add a subBuild result
   *
   * @param report
   * @param buildVariables
   */
  public void addSubBuildResult(StoryContainer report,
                                Map<String, String> buildVariables) {
    Map<String, String> buildVars = new TreeMap<String, String>();
    buildVars.putAll(buildVariables);
    this.matrixbuild.addSubBuild(new BacklogTestLoggerMatrixSubBuild(buildVars, report));
    computeStats();
  }

  public BacklogTestLoggerMatrixBuild getMatrixBuild() {
    return this.matrixbuild;
  }

  public void computeStats() {
    numberOfExecutedTest = 0;
    numberOfFailedTest = 0;
    numberOfTest = 0;
    numberOfPassedTest = 0;
    numberOfTrueFalseTest = 0;

    List<BacklogTestLoggerMatrixSubBuild> subBuilds = this.matrixbuild.getSubBuilds();
//    for (int i = 0; i < subBuilds.size(); i++) {
//      this.numberOfExecutedTest += subBuilds.get(i).getReport().getNumberOfExecutedTest();
//      this.numberOfFailedTest += subBuilds.get(i).getReport().getNumberOfFailedTest();
//      this.numberOfTest += subBuilds.get(i).getReport().getNumberOfTest();
//      this.numberOfPassedTest += subBuilds.get(i).getReport().getNumberOfPassedTest();
//      this.numberOfTrueFalseTest += subBuilds.get(i).getReport().getNumberOfTrueFalseTest();
//    }


  }

  /**
   * @return Summary HTML
   */
  public String getSummary() {
    StringBuilder strbuilder = new StringBuilder();
    strbuilder.append("<div class=\"progress-container\">");
    int tmp1 = this.getNumberOfFailedTest();
    int tmp2 = this.getNumberOfPassedTest();
    double tmp3 = this.getPercentOfFailedTest();
    double tmp4 = this.getPercentOfPassedTest();

    if (tmp3 < 15) {
      strbuilder.append("<div id=\"red\" style=\"width:15%;\">" + tmp1 + "</div>");
      strbuilder.append("<div id=\"blue\" style=\"width:85%;\">" + tmp2 + "</div>");
    } else {
      strbuilder.append("<div id=\"red\" style=\"width:" + tmp3 + "%;\">" + tmp3 + "% (" + tmp1 + ")</div>");
      strbuilder.append("<div id=\"blue\" style=\"width:" + tmp4 + "%;\">" + tmp4 + "% (" + tmp2 + ")</div>");
    }
    strbuilder.append("</div>");
    return strbuilder.toString();
  }

  private int getNumberOfFailedTest() {
    return this.numberOfFailedTest;
  }

  private int getNumberOfPassedTest() {
    return this.numberOfPassedTest;
  }

  private int getNumberOfTrueFalseTest() {
    return this.numberOfTrueFalseTest;
  }

  private double getPercentOfFailedTest() {
    double resultat = 0;
    resultat = ((double) getNumberOfFailedTest() / getNumberOfTrueFalseTest()) * 100;
    return floor(resultat, 2);
  }

  private double getPercentOfPassedTest() {
    double resultat = 0;
    resultat = ((double) getNumberOfPassedTest() / getNumberOfTrueFalseTest()) * 100;
    return floor(resultat, 2);
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

  public String getDetailSummary() {
    StringBuilder strbuilder = new StringBuilder();

    strbuilder.append("Number of executed tests : <b>" + this.getNumberOfExecutedTest() + ".</b>");
    strbuilder.append("<br />");

    return strbuilder.toString();
  }

//	public String getRegression() {
//		
//		StringBuilder strb = new StringBuilder();
//		List<Test> regressions = new ArrayList<Test>();
//		if (this.getTrendReport()!=null) {
//		List<Test> tmpTests = this.getTrendReport().getSuccessStatusChangedTests();
//		for (int i=0; i<tmpTests.size(); i++) {
//			if (!tmpTests.get(i).isSuccessfull()) {
//				regressions.add(tmpTests.get(i));
//			}
//		}
//		if (!regressions.isEmpty()) {
//			strb.append("<div class=\"warning_regression\">");
//			strb.append("This build has discovered "+regressions.size()+" regression(s).");
//			strb.append("</div>");
//		}
//		}
//		
//		return strb.toString();
//	}

  public String getHtmlArrayDisplay() {
    StringBuilder strb = new StringBuilder();
    /**
     * Generate HTML Header Table
     */
    strb.append("<table class=\"matrix_table\">\n");
    strb.append(generateHtmlArrayHeader());

    /**
     * Generate HTML Table Content
     */
    //strb.append(generateHtmlArrayContent());
    strb.append("</table>");

    return strb.toString();
  }


  private String generateHtmlContentRow(Map<String, List<Integer>> values) {
    StringBuilder strb = new StringBuilder();
    Set<Entry<String, List<Integer>>> entry = values.entrySet();
    Iterator<Entry<String, List<Integer>>> iterator = entry.iterator();
    int zebra = 2;

    while (iterator.hasNext()) {
      Entry<String, List<Integer>> axe = iterator.next();
      strb.append("<tr class=\"zebra" + zebra + "\">\n");
      strb.append("<td class=\"header\"><small>" + axe.getKey() + "</small></td>\n");

      List<Integer> i_min = new ArrayList<Integer>();
      int tmp_min = 0;
      List<Integer> i_max = new ArrayList<Integer>();
      int tmp_max = 0;


      for (int i = 0; i < axe.getValue().size(); i++) {
        /**
         * Compute min and max
         * !! I Know this should be improve
         */
        for (int j = 0; j < axe.getValue().size(); j++) {
          if (j == 0) {
            tmp_min = axe.getValue().get(j);
            tmp_max = axe.getValue().get(j);
            i_min.add(j);
            i_max.add(j);
          } else {
            if (tmp_min > axe.getValue().get(j)) {
              tmp_min = axe.getValue().get(j);
              i_min = new ArrayList<Integer>();
              i_min.add(j);
            }
            if (tmp_max < axe.getValue().get(j)) {
              tmp_max = axe.getValue().get(j);
              i_max = new ArrayList<Integer>();
              i_max.add(j);
            }
            if (tmp_min == axe.getValue().get(j)) {
              i_min.add(j);
            }
            if (tmp_max == axe.getValue().get(j)) {
              i_max.add(j);
            }
          }
        }


        if (i_min.contains(i)) {
          strb.append("<td class=\"min\">" + axe.getValue().get(i) + "</td>");
        } else if (i_max.contains(i)) {
          strb.append("<td class=\"max\">" + axe.getValue().get(i) + "</td>");
        } else {
          strb.append("<td>" + axe.getValue().get(i) + "</td>");
        }


      }
      strb.append("</tr>\n");
      if (zebra == 1) {
        zebra = 2;
      } else {
        zebra = 1;
      }
    }
    return strb.toString();
  }

  private String generateHtmlArrayHeader() {
    StringBuilder strb = new StringBuilder();

//		/**
//		 * First a row which contains as cols as combinations
//		 */
//		strb.append("<thead>\n");
//		strb.append("<tr>\n");
//		
//		strb.append("<th>Sub-Builds</hd>\n");
//		for (int i=0; i<this.matrixbuild.getNbCombinations(); i++) {
//			strb.append("<th>"+i+"</th>\n");
//		}
//		strb.append("</tr>\n");
//		strb.append("</thead>\n");

    /**
     * Rows for each axis
     */
    int factor = 1;
    int colspan = this.matrixbuild.getNbCombinations();
    for (int i_row = 0; i_row < this.matrixbuild.getNbAxis(); i_row++) {
      strb.append("<tr>");
      String axis = this.matrixbuild.getAxis().get(i_row);
      /**
       * Add the row description
       */
      strb.append("<td class=\"axe\">" + axis + "</td>\n");

      /**
       * Separate the current row with axis values cols
       */
      List<String> values = matrixbuild.getAxisValues(axis);
      Collections.sort(values);

      colspan = colspan / values.size();
      /**
       * repeat axis cols on the same row
       */
      for (int i_factor = 0; i_factor < factor; i_factor++) {
        for (int i_value = 0; i_value < values.size(); i_value++) {
          if (colspan > 1) {
            strb.append("<td COLSPAN=" + colspan + " class=\"axe_value\">" + values.get(i_value) + "</td>");
          } else {
            strb.append("<td class=\"axe_value\">" + values.get(i_value) + "</td>\n");
          }
        }
      }
      factor *= values.size();
      strb.append("</tr>\n");
    }
    return strb.toString();
  }

  public int getNumberOfExecutedTest() {
		return numberOfExecutedTest;
	}

}
