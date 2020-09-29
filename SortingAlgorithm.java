/*
 Name:     SortingAlgorithm.java
 Purpose:  Defines the interface for a sorting algorithm simulator.
 Author:   M. J. Fromberger

 Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.

 Defines an interface whereby a sorting algorithm can be run
 "stepwise" to display its progress.
*/

public interface SortingAlgorithm {
  /* This is called once at the beginning of each data set to be
  sorted, to give the algorithm the opportunity to initialize its
  data structures. */
  public void beginSorting();

  /* This should return true when sorting is complete, and false at
  all other times. */
  public boolean isDone();

  /* This is called to "step" the sorting algorithm.  It is up to
  the algorithm to determine what this means. */
  public void nextStep();
}

/* Here there be dragons */
