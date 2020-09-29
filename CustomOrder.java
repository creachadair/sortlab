/*
  Name:     CustomOrder.java
  Purpose:  Defines an interface for selecting an initial ordering.
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id: CustomOrder.java,v 1.2 2006/02/28 06:11:54 sting Exp $

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

public interface CustomOrder
{
    public void orderElements(int[] array);
    public void setRandomSeed(long seed);
}

/* Here there be dragons */
