/*
  Name:     InsertionSort.java
  Purpose:  Implements the insertion sort algorithm.
  Author:   M. J. Fromberger

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

public class InsertionSort extends Sort
{
    public InsertionSort(SortModel model)
    {
	super(model);
    }

    public void beginSorting()
    {
	ix = jx = 1;
    }

    public boolean isDone()
    {
	return (ix >= getModel().getNumElements());
    }

    public void nextStep()
    {
	if(ix < getModel().getNumElements()) {
	    if(jx > 0 && 
	       getModel().compareElements(jx, jx - 1) == SortModel.O_LT) {
		getModel().swapElements(jx, jx - 1);
		--jx;
	    } else {
		++ix;
		jx = ix;
	    }
	}
    }

    private int ix, jx;

    public String getAlgorithmName() { return ALGORITHM_NAME; }
    private static String ALGORITHM_NAME = "Insertion sort";
}

/* Here there be dragons */
