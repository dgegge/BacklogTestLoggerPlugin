package com.coremedia.util.hudson;

import com.coremedia.util.model.pojo.SingleTest;
import hudson.model.AbstractBuild;
import hudson.model.ModelObject;

/**
 * This class represents test details of a singleTest
 */
public class SingleTestDetails implements ModelObject {
  private final SingleTest test;
  private final AbstractBuild<?, ?> _owner;

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
}
