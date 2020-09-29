/*
  Name:     CustomOrder.java
  Purpose:  Defines an interface for selecting an initial ordering.
  Author:   M. J. Fromberger

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

public interface CustomOrder
{
    public void orderElements(int[] array);
    public void setRandomSeed(long seed);
}

/* Here there be dragons */
