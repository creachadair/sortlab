/*
  Name:     SelectionSort.java
  Purpose:  Implements the selection sort algorithm.
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id: SelectionSort.java,v 1.2 2006/02/28 06:11:54 sting Exp $

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

public class SelectionSort extends Sort
{
    public SelectionSort(SortModel model)
    {
	this(model, false);
    }

    public SelectionSort(SortModel model, boolean rev)
    {
	super(model);
	if(rev)
	    compSense = SortModel.O_GT;
	else
	    compSense = SortModel.O_LT;
    }

    public void beginSorting()
    {
	ix = 0;
	jx = ix + 1;
	minIndex = 0;
    }

    public boolean isDone()
    {
	return (ix >= getModel().getNumElements());
    }

    public void nextStep()
    {
	if(ix < getModel().getNumElements()) {
	    if(jx < getModel().getNumElements()) {
		if(getModel().compareElements(jx, minIndex) == compSense)
		    minIndex = jx;

		++jx;
	    } else {
		getModel().swapElements(ix, minIndex);

		++ix;
		minIndex = ix;
		jx = ix + 1;
	    }
	}
    }

    private int ix, jx, minIndex;
    private int compSense;

    public String getAlgorithmName() { return ALGORITHM_NAME; }
    private static String ALGORITHM_NAME = "Selection sort";
}

/* Here there be dragons */
