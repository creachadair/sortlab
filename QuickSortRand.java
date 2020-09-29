/*
  Name:     QuickSortRand.java
  Purpose:  Implements Quicksort with randomized pivot selection.
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id: QuickSortRand.java,v 1.3 2006/02/28 06:11:54 sting Exp $

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

import java.util.Stack;
import java.util.Random;

public class QuickSortRand extends QuickSort
{
    public QuickSortRand(SortModel model)
    {
	this(model, new Random());
    }

    public QuickSortRand(SortModel model, Random rand)
    {
	super(model);  /* tall and skinny! */

	m_rand = rand;
    }

    public void   setRandom(Random rand) { m_rand = rand; }
    public Random getRandom() { return m_rand; }

    protected int choosePartition(Range r)
    {
	int pivot = m_rand.nextInt(r.getHi() + 1 - r.getLo()) + r.getLo();
	
	return pivot;
    }

    protected Random   m_rand;

    public String getAlgorithmName() { return ALGORITHM_NAME; }
    private static String ALGORITHM_NAME = "QuickSort (R)";
}

/* Here there be dragons */
