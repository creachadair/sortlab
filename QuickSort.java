/*
  Name:     QuickSort.java
  Purpose:  Implements Quicksort except pivot selection.
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id: QuickSort.java,v 1.4 2006/02/28 06:11:54 sting Exp $

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

import java.util.Stack;

abstract public class QuickSort extends Sort
{
    public QuickSort(SortModel model)
    {
	super(model);
	m_to_do = null;
    }

    public void beginSorting()
    {
	m_to_do = new Stack();
	m_lo = m_hi = m_pivot = -1;
	Range r = new Range(0, getModel().getNumElements() - 1);
	m_to_do.push(r);
    }

    public boolean isDone()
    {
	return m_to_do.empty();
    }

    public void nextStep()
    {
	if(m_to_do.empty())
	    return;
	
	Range r = (Range) m_to_do.peek();
	
	/* Base cases */
	if(handleBaseCase(r)) {
	    m_to_do.pop();
	    return;
	}
	
	/* m_pivot is the position of the pivot element.  
	   When m_pivot < 0, it means we need to choose a new pivot
	   element.  This element is moved to the bottom of the range.
	   
	   The pivot choice is overridden by subclasses.
	 */
	if(m_pivot < 0) {
	    m_lo = r.getLo();
	    m_hi = r.getHi();
	    m_pivot = choosePartition(r);

	    if(m_pivot != m_lo) {
		getModel().swapElements(m_lo, m_pivot);
		m_pivot = m_lo;
	    }
	    return;
	}

	/* Advance the low-side pointer, if necessary. */
	if(m_lo <= m_hi &&
	   getModel().compareElements(m_lo, m_pivot) <= SortModel.O_EQ) {
	    ++m_lo;
	    return;
	}
	
	/* Advance the high-side pointer, if necessary. */
	if(getModel().compareElements(m_hi, m_pivot) > SortModel.O_EQ) {
	    --m_hi;
	    return;
	}
	
	/* If we get here, either m_lo >= m_hi, or we know that
	   A[m_lo] > A[m_pivot] and A[m_hi] <= A[m_pivot]. 
	 */
	if(m_lo < m_hi) {
	    getModel().swapElements(m_lo, m_hi);
	    ++m_lo;
	    --m_hi;
	}
	else {
	    int split = m_lo - 1;
	    
	    int pivot = getModel().getElement(m_pivot);
	    for(int ix = r.getLo(); ix <= split; ++ix)
		assert getModel().getElement(ix) <= pivot : 
		    ("Order violation at " + ix);
	    for(int ix = split + 1; ix <= r.getHi(); ++ix)
		assert getModel().getElement(ix) > pivot : 
		    ("Order violation at " + ix);
	    
	    getModel().swapElements(m_pivot, split);
	    
	    m_to_do.pop();
	    if(split < r.getHi())
		m_to_do.push(new Range(split + 1, r.getHi()));
	    
	    m_to_do.push(new Range(r.getLo(), split));
	    m_pivot = -1;
	} 
    }
    
    abstract protected int choosePartition(Range r);

    /* Handle base cases for sorting separately */
    protected boolean handleBaseCase(Range r) 
    {
	switch (r.getWidth()) {
	case 0: case 1:
	    return true;
	case 2:
	    if(getModel().compareElements(r.getLo(), r.getHi()) == 
	       SortModel.O_GT) {
		getModel().swapElements(r.getLo(), r.getHi());
	    }
	    return true;
	default:
	    return false;
	}
    }

    protected class Range
    {
	Range(int lo, int hi) { m_lo = lo; m_hi = hi; }

	int getLo() { return m_lo; }
	int getHi() { return m_hi; }
	int getWidth() {
	    if(m_hi >= m_lo)
		return (m_hi + 1 - m_lo);
	    else
		return 0;
	}

	private int m_lo, m_hi;
	
	public String toString() {
	    return "#<range " + m_lo + ",.." + m_hi + ">";
	}
    }

    protected Stack    m_to_do;
    protected int      m_lo, m_hi, m_pivot;
    
    abstract public String getAlgorithmName();
}

/* Here there be dragons */
