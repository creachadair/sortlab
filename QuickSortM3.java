/*
 Name:     QuickSortM3.java
 Purpose:  Implements Quicksort with median-of-three pivot selection.
 Author:   M. J. Fromberger

 Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
*/

public class QuickSortM3 extends QuickSort {
  public QuickSortM3(SortModel model) {
    super(model); /* do your thing on the catwalk, baby */
  }

  protected int choosePartition(Range r) {
    int mid = (r.getLo() + r.getHi()) / 2;
    int a = getModel().getElement(r.getLo());
    int b = getModel().getElement(mid);
    int c = getModel().getElement(r.getHi());

    if (a < c) {
      if (b < a) return r.getLo();
      else if (b < c) return mid;
      else return r.getHi();
    } else {
      if (b < c) return r.getHi();
      else if (b < a) return mid;
      else return r.getLo();
    }
  }

  public String getAlgorithmName() {
    return ALGORITHM_NAME;
  }

  private static String ALGORITHM_NAME = "QuickSort (M3)";
}

/* Here there be dragons */
