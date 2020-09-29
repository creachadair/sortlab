/*
 Name:     BubbleSort.java
 Purpose:  Implements the ascending bubble sort algorithm.
 Author:   M. J. Fromberger

 Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
*/

public class BubbleSort extends Sort {
  public BubbleSort(SortModel model) {
    super(model);
  }

  public void beginSorting() {
    ix = getModel().getNumElements() - 1;
    jx = 0;
    finished = true;
  }

  public boolean isDone() {
    return (ix <= 0);
  }

  public void nextStep() {
    if (ix > 0) {
      if (jx < ix) {
        if (getModel().compareElements(jx, jx + 1) == SortModel.O_GT) {
          getModel().swapElements(jx, jx + 1);
          finished = false;
        }

        ++jx;
      } else if (finished) {
        ix = 0;
      } else {
        --ix;
        jx = 0;
        finished = true;
      }
    }
  }

  private int ix, jx;
  private boolean finished;

  public String getAlgorithmName() {
    return ALGORITHM_NAME;
  }

  private static String ALGORITHM_NAME = "Bubble sort";
}

/* Here there be dragons */
