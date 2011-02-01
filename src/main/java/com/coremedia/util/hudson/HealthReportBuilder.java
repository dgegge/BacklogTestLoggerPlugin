package com.coremedia.util.hudson;


import hudson.model.HealthReport;

import java.io.Serializable;

/**
 * Creates a health report for integer values based on healthy and unhealthy
 * thresholds.
 *
 * @author Ulli Hafner
 * @see HealthReport
 */
public class HealthReportBuilder implements Serializable {
  /**
   * Unique identifier of this class.
   */
  private static final long serialVersionUID = 5191317904662711835L;
  /**
   * Health descriptor.
   */
  private final HealthDescriptor healthDescriptor;

  /**
   * Creates a new instance of {@link HealthReportBuilder}.
   *
   * @param healthDescriptor health descriptor
   */
  public HealthReportBuilder(final HealthDescriptor healthDescriptor) {
    this.healthDescriptor = healthDescriptor;
  }


  /**
   * Returns whether this health report build is enabled, i.e. at least one of
   * the health or failed thresholds are provided.
   *
   * @return <code>true</code> if health or failed thresholds are provided
   */
  public boolean isEnabled() {
    return healthDescriptor.isHealthAnalyse();
  }


}

