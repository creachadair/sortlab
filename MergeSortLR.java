/*
  Name:     MergeSortLR.java
  Purpose:  Implements left-to-right mergesort.
  Author:   M. J. Fromberger

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

import java.util.Stack;

public class MergeSortLR extends Sort
{
    public MergeSortLR(SortModel model)
    {
	super(model);
    }

    public void beginSorting()
    {
	int n_elt = getModel().getNumElements();

	m_to_do = new Stack();
	m_merge = new int[n_elt];

	m_to_do.push(new Range(0, n_elt - 1));
	m_lo_cursor = m_hi_cursor = m_out_cursor = -1;
	m_midmark = -1;
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

	switch(r.getAction()) {
	case A_SORT:
	    if(r.isUnit())
		m_to_do.pop();  /* Singletons are already sorted */
	    
	    else {
		int mid = (r.getLo() + r.getHi() + 1) / 2;

		r.setAction(A_MERGE);
		Range  left = new Range(r.getLo(), mid - 1);
		Range right = new Range(mid, r.getHi());

		if(!right.isUnit())
		    m_to_do.push(right);

		if(!left.isUnit())
		    m_to_do.push(left);
	    }
	    break;

	case A_MERGE:
	    if(m_out_cursor < 0) {
		m_lo_cursor = r.getLo();
		m_midmark = (r.getHi() + r.getLo() + 1) / 2;
		m_hi_cursor = m_midmark;
		m_out_cursor = m_lo_cursor;
	    }

	    if(m_lo_cursor >= m_midmark && m_hi_cursor > r.getHi()) {
		m_out_cursor = r.getLo();
		r.setAction(A_COPY);
		return;
	    }

	    if(m_lo_cursor < m_midmark &&
	       (m_hi_cursor > r.getHi() ||
		getModel().compareElements(m_lo_cursor, m_hi_cursor) <=
		SortModel.O_EQ)) {

		m_merge[m_out_cursor++] = 
		    getModel().getElement(m_lo_cursor++);

		return;
	    }

	    m_merge[m_out_cursor++] = 
		getModel().getElement(m_hi_cursor++);

	    break;

	case A_COPY:
	    if(m_out_cursor > r.getHi()) {
		m_to_do.pop();
		m_out_cursor = -1;
		return;
	    }

	    getModel().exchangeElement(m_out_cursor, 
				       m_merge[m_out_cursor]);

	    ++m_out_cursor;
	    break;
	}
    }

    private static final int A_SORT   = 1;
    private static final int A_MERGE  = 2;
    private static final int A_COPY   = 3;

    private class Range
    {
	Range(int lo, int hi) { m_lo = lo; m_hi = hi; m_act = A_SORT; }

	public int getLo() { return m_lo; }
	public int getHi() { return m_hi; }
	public int getAction() { return m_act; }
	public void setAction(int act) { m_act = act; }

	public boolean isUnit() { return (m_hi - m_lo) < 1; }

	public String toString()
	{
	    return "[" + m_lo + ".." + m_hi + "]<" + m_act + ">";
	}

	private int m_lo, m_hi, m_act;
    }

    private int[] m_merge;
    private Stack m_to_do;
    private int   m_lo_cursor, m_hi_cursor, m_out_cursor;
    private int   m_midmark;

    public String getAlgorithmName() { return ALGORITHM_NAME; }
    private static String ALGORITHM_NAME = "Mergesort (left-to-right)";
}

/* Here there be dragons */
