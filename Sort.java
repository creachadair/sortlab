/*
 Name:     Sort.java
 Purpose:  Default wrapper for a sorting simulator class.
 Author:   M. J. Fromberger

 Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
*/

public abstract class Sort implements SortingAlgorithm {
  public Sort(SortModel model) {
    setModel(model);
  }

  public abstract String getAlgorithmName();

  public SortModel getModel() {
    return m_model;
  }

  public void setModel(SortModel model) {
    m_model = model;
  }

  private SortModel m_model;
}

/* Here there be dragons */
