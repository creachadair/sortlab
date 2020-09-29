/*
  Name:     ShellSort.java
  Purpose:  Implements D. H. Shell's extension of Insertion Sort.
  Author:   M. J. Fromberger

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

public class ShellSort extends Sort
{
    public ShellSort(SortModel model)
    {
	super(model);
    }

    public void beginSorting()
    {
	int n_elts = getModel().getNumElements();

	inc = 3 + (n_elts / 100);

	ix = jx = 0;
	temp = getModel().getElement(ix);
    }

    public boolean isDone()
    {
	return (inc <= 0);
    }

    public void nextStep()
    {
	if(inc <= 0)
	    return;

	if(ix < getModel().getNumElements()) {
	    /* Insertion loop, skipping */
	    if((jx >= inc) && 
	       getModel().compareElementToValue(jx - inc, temp) ==
	       SortModel.O_GT) {

		int val = getModel().getElement(jx - inc);

		getModel().exchangeElement(jx, val);
		jx -= inc;

		return;
	    }
	    getModel().exchangeElement(jx, temp);

	    /* Continuation of the position loop */
	    if(++ix < getModel().getNumElements()) {
		jx = ix;
		temp = getModel().getElement(ix);
	    }

	    return;
	}

	if(inc > 1)
	    inc /= 2;
	else if(inc == 1)
	    inc = 0;
	else
	    inc = 1;

	/* Reset position loop */
	ix = 0;
	jx = ix;
	temp = getModel().getElement(ix);
    }

    private int     ix, jx;
    private int     temp;
    private int     inc;

    public String getAlgorithmName() { return ALGORITHM_NAME; }
    private static String ALGORITHM_NAME = "Shell sort";
}

/* Here there be dragons */
