/*
  Name:     Sort.java
  Purpose:  Default wrapper for a sorting simulator class.
  Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
  Info:     $Id: Sort.java,v 1.2 2006/02/28 06:11:54 sting Exp $

  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

abstract public class Sort implements SortingAlgorithm
{
    public Sort(SortModel model)
    {
	setModel(model);
    }

    abstract public String getAlgorithmName();

    public SortModel getModel() { return m_model; }
    public void      setModel(SortModel model) { m_model = model; }

    private SortModel  m_model;
}

/* Here there be dragons */
