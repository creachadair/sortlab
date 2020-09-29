/*
 Name:     CountingSort.java
 Purpose:  Implements a variation of the counting sort algorithm.
 Author:   M. J. Fromberger

 Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.

 Notes:  This version of counting sort uses a clever rearrangement
 algorithm that works by chasing the cycles of the permutation once
 it is known.  It requires O(n) auxiliary storage and O(n) time.
*/

public class CountingSort extends Sort {
  public CountingSort(SortModel model) {
    super(model);
    store = null;
  }

  public void beginSorting() {
    int n_elts = getModel().getNumElements();

    if (store == null || n_elts != store.length) store = new int[n_elts];

    reading = true;
    pos = 0;
  }

  public boolean isDone() {
    return (!reading && pos >= getModel().getNumElements());
  }

  public void nextStep() {
    if (reading) {
      if (pos < getModel().getNumElements()) {
        getModel().compareElementToValue(pos, pos); /* pro forma */

        ++pos;
      } else {
        reading = false;
        pos = start = save = -1;
      }
    } else {
      if (pos == start) {
        do {
          ++start;
        } while (start < getModel().getNumElements()
            && getModel().compareElementToValue(start, start) == 0);

        if (start >= getModel().getNumElements()) {
          pos = start;
          return;
        }

        pos = start;
        save = getModel().getElement(pos);
      }

      pos = save;
      save = getModel().exchangeElement(pos, save);
    }
  }

  private boolean reading;
  private int pos;
  private int start;
  private int save;
  private int[] store;

  public String getAlgorithmName() {
    return ALGORITHM_NAME;
  }

  private static String ALGORITHM_NAME = "Counting sort";
}

/* Here there be dragons */
