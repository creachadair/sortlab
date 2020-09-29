/*
  Name:     SortModel.java
  Purpose:  Defines the interface between controller and view.
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id: SortModel.java,v 1.2 2006/02/28 06:11:55 sting Exp $

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

public interface SortModel
{
    /* Comparison results */
    public static final int O_LT = -1;
    public static final int O_EQ = 0;
    public static final int O_GT = 1;

    public int  compareElements(int i, int j);
    public int  compareElementToValue(int i, int value);
    public void swapElements(int i, int j);
    public int  getElement(int i);
    public int  getNumElements();
    public int  exchangeElement(int i, int value);
    public boolean isSorted();
}

/* Here there be dragons */
