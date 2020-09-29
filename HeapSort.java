/*
  Name:     HeapSort.java
  Purpose:  Implements in-place heapsort (using a max-heap).
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id: HeapSort.java,v 1.2 2006/02/28 06:11:54 sting Exp $

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

public class HeapSort extends Sort
{
    public HeapSort(SortModel model)
    {
	super(model);
    }

    public void beginSorting()
    {
	m_root = (getModel().getNumElements() - 1) / 2;
	m_cursor = m_root;
	m_state = S_HEAPIFY;
    }

    public boolean isDone()
    {
	return m_state == S_IDLE;
    }

    public void nextStep()
    {
	if((m_state & S_HEAPIFY) != 0)
	    advanceHeapify();

	else if((m_state & S_EXTRACT) != 0)
	    advanceExtract();
    }

    private void advanceExtract()
    {
	/* If we're not percolating, it's time to extract the next element
	   from the head of the heap (this is always index 0).

	   In this state, m_root points to the last "heap" element, and
	   everything after that is already correctly sorted.  */
	if((m_state & S_PERKING) == 0) {
	    if(m_root <= 0) {
		m_state = S_IDLE; /* Indicates done sorting */
		return;
	    }
	    
	    else {
		/* Put the max element at the root's location, and perk the
		   new root into the correct location. */
		getModel().swapElements(0, m_root - 1);
		m_cursor = 0;
		--m_root;
		m_state |= S_PERKING;
	    }

	} 

	int lc = 2 * m_cursor, rc = lc + 1, max = m_cursor;

	if(lc < m_root && getModel().compareElements(lc, max) == 
	   SortModel.O_GT) {
	    max = lc;
	} 

	if(rc < m_root && getModel().compareElements(rc, max) ==
	   SortModel.O_GT) {
	    max = rc;
	}

	if(max != m_cursor) {
	    getModel().swapElements(m_cursor, max);
	    m_cursor = max;
	} else {
	    m_state &= ~S_PERKING;  /* Done percolating this element */
	}
    }

    private void advanceHeapify()
    {
	int n_elt = getModel().getNumElements();

	if((m_state & S_PERKING) == 0) {
	    if(m_root < 0) {
		m_state = S_EXTRACT; /* Done heapifying */
		m_root = n_elt;
		return;
	    }
	    
	    else {
		m_cursor = m_root;
		--m_root;
		m_state |= S_PERKING;
	    }
	}

	int lc = 2 * m_cursor, rc = lc + 1, max = m_cursor;

	if(lc < n_elt && getModel().compareElements(lc, max) == 
	   SortModel.O_GT) {
	    max = lc;
	} 

	if(rc < n_elt && getModel().compareElements(rc, max) ==
	   SortModel.O_GT) {
	    max = rc;
	}

	if(max != m_cursor) {
	    getModel().swapElements(m_cursor, max);
	    m_cursor = max;
	} else {
	    m_state &= ~S_PERKING; /* Done percolating this element */
	}
    }

    private static final int S_IDLE    = 0;
    private static final int S_PERKING = 1;
    private static final int S_EXTRACT = 2;
    private static final int S_HEAPIFY = 4;

    private int m_state;
    private int m_root, m_cursor;

    public String getAlgorithmName() { return ALGORITHM_NAME; }
    private static String ALGORITHM_NAME = "Heapsort";
}

/* Here there be dragons */
