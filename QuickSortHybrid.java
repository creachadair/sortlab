/*
  Name:     QuickSortHybrid.java
  Purpose:  Hybrid QuickSort simulation with insertion sort.
  Author:   M. J. Fromberger

  Copyright (C) 2003-2004 Michael J. Fromberger, All Rights Reserved.
 */

import java.util.Stack;

public class QuickSortHybrid extends QuickSortM3
{
    /* How big a range has to be before we'll actually recurse. */
    private static final int MIN_RECUR_SIZE = 20;

    /* Indices for insertion sorting */
    private int ix, jx;

    public QuickSortHybrid(SortModel model)
    {
	super(model); /* do your thing on the catwalk, baby */
    }

    public void beginSorting()
    {
	super.beginSorting();

	ix = jx = -1;
    }

    public void nextStep()
    {
	if(m_to_do.empty())
	    return;

	Range r = (Range) m_to_do.peek();
	
	/* If we're in a sufficiently small range, use insertion sort instead
	   of recurring on subparts. 
 	 */
	if(r.getWidth() <= MIN_RECUR_SIZE) {
	    if(ix < 0) {
		ix = jx = r.getLo() + 1;
	    }
	    
	    if(ix <= r.getHi()) {
		if(jx > r.getLo() &&
		   getModel().compareElements(jx, jx - 1) == SortModel.O_LT) {
		    getModel().swapElements(jx, jx - 1);
		    --jx;
		}
		else {
		    ++ix;
		    jx = ix;
		}
	    }
	    else {
		ix = jx = -1;
		m_to_do.pop();
	    }
	    
	    return;
	}

	super.nextStep();
    }
    
    public String getAlgorithmName() { return ALGORITHM_NAME; }
    private static String ALGORITHM_NAME = "QuickSort (M3) Hybrid";
}

/* Here there be dragons */
