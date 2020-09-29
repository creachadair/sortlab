/*
  Name:     BidirectionalBubbleSort.java
  Purpose:  Implements the bidirectional bubble sort algorithm.
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id$

  Copyright (C) 2006 Michael J. Fromberger, All Rights Reserved.
 */

public class BidirectionalBubbleSort extends Sort
{
    public BidirectionalBubbleSort(SortModel model)
    {
	super(model);
    }

    public void beginSorting()
    {
	loMark = 0;
	pos = 0;
	ascending = true;
	hiMark = getModel().getNumElements() - 1;
	finished = true;  // Test for early termination
    }

    public boolean isDone()
    {
	return loMark >= hiMark;
    }

    public void nextStep()
    {
	if(loMark < hiMark) {
	    if(ascending) {
		if(pos < hiMark) {
		    if(getModel().compareElements(pos, pos + 1)
		       == SortModel.O_GT) {
			getModel().swapElements(pos, pos + 1);
			finished = false;
		    }

		    ++pos;
		}
		else if(finished) {
		    loMark = hiMark;
		}
		else {
		    --hiMark;
		    pos = hiMark;
		    ascending = false;
		    finished = true;
		}
	    }
	    else {
		if(pos > loMark) {
		    if(getModel().compareElements(pos, pos - 1)
		       == SortModel.O_LT) {
			getModel().swapElements(pos, pos - 1);
			finished = false;
		    }

		    --pos;
		}
		else if(finished) {
		    hiMark = loMark;
		}
		else {
		    ++loMark;
		    pos = loMark;
		    ascending = true;
		    finished = true;
		}
	    }
	}
    }

    private int loMark, hiMark, pos;
    private boolean ascending, finished;

    public String getAlgorithmName() { return ALGORITHM_NAME; }
    private static String ALGORITHM_NAME = "Bidirectional bubble sort";
}

/* Here there be dragons */
