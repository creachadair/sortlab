/*
  Name:     OrderPanel.java
  Purpose:  Main interface panel for the sorting laboratory.
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id: OrderPanel.java,v 1.5 2006/02/28 05:58:49 sting Exp $
  
  Copyright (C) 2003-2006 M. J. Fromberger, All Rights Reserved.
 */

import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class OrderPanel extends JPanel 
    implements CustomOrder, SortModel
{
    /* Order selectors    */
    public static final int ORDER_RANDOM = 0;
    public static final int ORDER_ASCENDING = 1;
    public static final int ORDER_DESCENDING = 2;

    /* Constructor:  Use default ordering rules, with given selector */
    public OrderPanel(int num_points, int otype)
    {
	m_points = new int[num_points];
	m_order = this;
	m_order_type = otype;
	m_rand = new Random();

	setOpaque(true);
	m_order.orderElements(m_points);
	show_feedback = true;
	resetSort();
    }
    
    /* Constructor:  Use default ordering rules for random order */
    public OrderPanel(int num_points)
    {
	this(num_points, ORDER_RANDOM);
    }

    /* Constructor:  Use custom ordering rule; order type is ignored */
    public OrderPanel(int num_points, CustomOrder order)
    {
	m_points = new int[num_points];
	m_order = order;
	m_order_type = -1;
	m_rand = new Random();

	setOpaque(true);
	m_order.orderElements(m_points);
	show_feedback = true;
	resetSort();
    }

    /* Determine whether visual feedback is shown */
    public void setFeedback(boolean on)
    {
	show_feedback = on;
	repaint();
    }

    public void clearFeedback()
    {
	m_lastc_1 = m_lastc_2 = -1;
	m_lasts_1 = m_lasts_2 = -1;	
    }

    /* Set a custom ordering routine, overriding the built-in defaults */
    public void setOrdering(CustomOrder order)
    {
	m_order = order;
	m_order_type = -1;
	m_order.orderElements(m_points);
    }

    /* Set a different ordering type for the built-in ordering routine */
    public void setOrdering(int otype)
    {
	boolean changed = false;

	if(m_order != this) {
	    m_order = this;
	    changed = true;
	}
	if(m_order_type != otype) {
	    m_order_type = otype;
	    changed = true;
	}

	if(changed) {
	    m_order.orderElements(m_points);
	    repaint();
	}
    }

    /* Reset the current sort so that you can run it again */
    public void resetSort() { resetSort(false); }

    public void resetSort(boolean reshuffle)
    {
	m_num_compares = 0;
	m_num_moves = 0;
	clearFeedback();

	if(reshuffle) {
	    m_order.orderElements(m_points);
	    repaint();
	}
    }

    /* Call this when you're done sorting to disable the feedback. */
    public void doneSorting()
    {
	clearFeedback();

	repaint();
    }

    /* Accessors to count number of comparisons/swaps. */
    public int getNumCompares() { return m_num_compares; }
    public int getNumMoves() { return m_num_moves; }

    /* Compare the indexed elements; increment comparison counter;
       Provided in satisfaction of the SortModel interface. */
    public int compareElements(int i, int j)
    {
	++m_num_compares;
	m_lastc_1 = i;
	m_lastc_2 = j;

	repaint();

	if(m_points[i] < m_points[j])
	    return O_LT;
	else if(m_points[i] == m_points[j])
	    return O_EQ;
	else
	    return O_GT;
    }

    /* Provided in satisfaction of the SortModel interface. */
    public int  compareElementToValue(int i, int value)
    {
	++m_num_compares;
	m_lastc_1 = i;
	m_lastc_2 = -1;

	repaint();
	
	if(m_points[i] < value)
	    return O_LT;
	else if(m_points[i] == value)
	    return O_EQ;
	else
	    return O_GT;
    }

    /* Read the indexed elements; increment swap counter. 
       Note, we count swaps in place, too, as currently implemented 
       Provided in satisfaction of the SortModel interface. */
    public void swapElements(int i, int j)
    {
	m_num_moves += 2;
	m_lasts_1 = i;
	m_lasts_2 = j;

	int tmp = m_points[i];

	m_points[i] = m_points[j];
	m_points[j] = tmp;

	repaint();
    }

    /* Exchange a given external value for the value at the given
       position in the array.  Returns the old value, and increments
       the swap counter.

       Provided in satisfaction of the SortModel interface. */
    public int  exchangeElement(int i, int value)
    {
	++m_num_moves;
	m_lasts_1 = i;
	m_lasts_2 = -1;

	int out = m_points[i];
	m_points[i] = value;

	repaint();

	return out;
    }

    /* Provided in satisfaction of the SortModel interface. */
    public int getElement(int i) { return m_points[i]; }

    /* Return the number of points being sorted.
       Provided in satisfaction of the SortModel interface. */
    public int getNumElements()
    {
	return m_points.length;
    }

    public void setNumElements(int num_points)
    {
	if(num_points != m_points.length) {
	    m_points = new int[num_points];
	    resetSort(true);
	}
    }

    /* Check whether the list is properly sorted; does not increment
       the comparison counter.  

       Provided in satisfaction of the SortModel interface.  */
    public boolean isSorted()
    {
	for(int ix = 0; ix < m_points.length - 1; ++ix)
	    if(m_points[ix] > m_points[ix + 1])
		return false;

	return true;
    }

    /* Provided in satisfaction of the CustomOrder interface */
    public void orderElements(int[] array)
    {
	switch(m_order_type) {
	case ORDER_RANDOM:
	default:
	    initRandomOrder(array);
	    break;
	case ORDER_ASCENDING:
	    for(int ix = 0; ix < array.length; ++ix)
		array[ix] = ix;
	    break;
	case ORDER_DESCENDING:
	    for(int ix = 0, jx = array.length - 1; 
		ix < array.length; 
		++ix, --jx)
		array[ix] = jx;
	    break;
	}
    }

    /* Provided in satisfaction of the CustomOrder interface. */
    public void setRandomSeed(long seed)
    {
	m_rand.setSeed(seed);
    }

    private void initRandomOrder(int[] array)
    {
	for(int ix = 0; ix < array.length; ++ix) 
	    array[ix] = ix;

	/* Simple uniformly-distributed shuffle */
	for(int ix = array.length - 1; ix >= 0; --ix) {
	    int jx = (int)(m_rand.nextDouble() * ix);
	    int tmp = array[ix];

	    array[ix] = array[jx];
	    array[jx] = tmp;
	}
    }

    public int countMaxElements(int pixSize)
    {
	int width = getWidth();

	return (int)((double) width / pixSize);
    }

    public void paint(Graphics g)
    {
	int width = getWidth(), height = getHeight();
	
	double pointWidth  = (double) width  / m_points.length;
	double pointHeight = (double) height / m_points.length;
	
	g.clearRect(0, 0, width, height);

	for(int ix = 0; ix < m_points.length; ++ix) {
	    int hpos = (int) (ix * pointWidth);
	    int vpos = (int) (height - pointHeight - 
			      m_points[ix] * pointHeight);
	    int halves = 0;  // Detect coincident compare and swap locations
	    
	    if(show_feedback) {
		if(ix == m_lastc_1 || ix == m_lastc_2) {
		    ++halves;
		}
		if(ix == m_lasts_1 || ix == m_lasts_2) {
		    g.setColor(Color.gray);
		    g.drawLine((int) (hpos + (pointWidth / 2)), 0,
			       (int) (hpos + (pointWidth / 2)), height);
		    ++halves;
		}
	    }
	    
	    g.setColor(Color.black);
	    g.fillRect(hpos, vpos, (int) (pointWidth + 1.0), height - vpos);
	    
	    if(show_feedback) {
		if(ix == m_lastc_1 || ix == m_lastc_2) {
		    g.setColor(Color.green);
		    g.fillRect((int) (hpos + (pointWidth / 2) - 1),
			       vpos,
			       (pointWidth < 3 ? (int) pointWidth : 3),
			       height - vpos);

		    g.setColor(Color.blue);
		    g.fillRect(hpos, vpos, 
			       ((halves == 2) ? 
				(int) (pointWidth / 2) :
				(int) pointWidth), (int) pointHeight);
		}
		if(ix == m_lasts_1 || ix == m_lasts_2) {
		    g.setColor(Color.red);
		    
		    g.fillRect(hpos + 
			       ((halves == 2) ? 
				(int) (pointWidth / 2) : 0),
			       vpos, 
			       ((halves == 2) ?
				(int) (pointWidth / 2) : (int) pointWidth),
			       (int) pointHeight);
		}
	    }
	}
    }

    private int m_points[];
    private CustomOrder m_order;
    private int m_order_type;
    private int m_num_compares;  /* How many comparisons so far */
    private int m_num_moves;     /* How many data moves so far  */
    private int m_lastc_1, m_lastc_2;
    private int m_lasts_1, m_lasts_2;
    private boolean show_feedback;
    private Random m_rand;

    /* Test driver.  Tests using a couple of simple sorting algorithms */
    public static void main(String[] args)
	throws InterruptedException
    {
	final int SLOW_SPEED = 250;
	final int MEDIUM_SPEED = 100;
	final int FAST_SPEED = 30;
	final int HYPER_SPEED = 3;

	JFrame f = new JFrame();

	f.setTitle("Ordered Panel Test");
	f.setSize(600, 600);

	OrderPanel op = new OrderPanel(0);
	
	f.setContentPane(op);
	f.setVisible(true);

	op.setNumElements(op.countMaxElements(20));

	// Selection sort 
	System.out.println("--- SELECTION SORT");
	SelectionSort ssort = new SelectionSort(op);

	ssort.beginSorting();
	while(!ssort.isDone()) {
	    ssort.nextStep();
	    Thread.sleep(FAST_SPEED);
	}
	System.out.println("Sorting took " + op.getNumCompares() +
			   " comparisons and " + op.getNumMoves() + 
			   " swaps.");
	if(op.isSorted())
	    System.out.println(op.getNumElements() + " elements were sorted.");
	else
	    System.out.println("The sort failed.");
	Thread.sleep(1000);

	// Insertion sort 
	System.out.println("--- INSERTION SORT");
	op.resetSort(true);
	InsertionSort isort = new InsertionSort(op);

	isort.beginSorting();
	while(!isort.isDone()) {
	    isort.nextStep();
	    Thread.sleep(FAST_SPEED);
	}
	System.out.println("Sorting took " + op.getNumCompares() +
			   " comparisons and " + op.getNumMoves() + 
			   " swaps.");
	if(op.isSorted())
	    System.out.println(op.getNumElements() + " elements were sorted.");
	else
	    System.out.println("The sort failed.");
	Thread.sleep(1000);

	// Bubble sort (ugh) 
	System.out.println("--- BUBBLE SORT");
	op.resetSort(true);
	BubbleSort bsort = new BubbleSort(op);

	bsort.beginSorting();
	while(!bsort.isDone()) {
	    bsort.nextStep();
	    Thread.sleep(FAST_SPEED);
	}
	System.out.println("Sorting took " + op.getNumCompares() +
			   " comparisons and " + op.getNumMoves() + 
			   " swaps.");
	if(op.isSorted())
	    System.out.println(op.getNumElements() + " elements were sorted.");
	else
	    System.out.println("The sort failed.");	
	Thread.sleep(1000);

	// Shell sort
	System.out.println("--- SHELL SORT");
	op.resetSort(true);
	ShellSort shsort = new ShellSort(op);

	shsort.beginSorting();
	while(!shsort.isDone()) {
	    shsort.nextStep();
	    Thread.sleep(FAST_SPEED);
	}
	System.out.println("Sorting took " + op.getNumCompares() +
			   " comparisons and " + op.getNumMoves() + 
			   " swaps.");
	if(op.isSorted())
	    System.out.println(op.getNumElements() + " elements were sorted.");
	else
	    System.out.println("The sort failed.");	
	Thread.sleep(1000);

	// Quicksort 
	System.out.println("--- QUICKSORT");
	op.resetSort(true);
	QuickSortM3 qsort = new QuickSortM3(op);

	qsort.beginSorting();
	while(!qsort.isDone()) {
	    qsort.nextStep();
	    Thread.sleep(FAST_SPEED);
	}
	System.out.println("Sorting took " + op.getNumCompares() +
			   " comparisons and " + op.getNumMoves() + 
			   " swaps.");
	if(op.isSorted())
	    System.out.println(op.getNumElements() + " elements were sorted.");
	else
	    System.out.println("The sort failed.");
	Thread.sleep(1000);

	// Heapsort 
	System.out.println("--- HEAPSORT");
	op.resetSort(true);
	HeapSort hsort = new HeapSort(op);

	hsort.beginSorting();
	while(!hsort.isDone()) {
	    hsort.nextStep();
	    Thread.sleep(FAST_SPEED);
	}
	System.out.println("Sorting took " + op.getNumCompares() +
			   " comparisons and " + op.getNumMoves() + 
			   " swaps.");
	if(op.isSorted())
	    System.out.println(op.getNumElements() + " elements were sorted.");
	else
	    System.out.println("The sort failed.");
	Thread.sleep(1000);

	// Merge sort 
	System.out.println("--- MERGE SORT");
	op.resetSort(true);
	MergeSort msort = new MergeSort(op);

	msort.beginSorting();
	while(!msort.isDone()) {
	    msort.nextStep();
	    Thread.sleep(FAST_SPEED);
	}
	System.out.println("Sorting took " + op.getNumCompares() +
			   " comparisons and " + op.getNumMoves() + 
			   " swaps.");
	if(op.isSorted())
	    System.out.println(op.getNumElements() + " elements were sorted.");
	else
	    System.out.println("The sort failed.");
    }
}

/* Here there be dragons */
