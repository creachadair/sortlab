/*
 Name:     MergeSort.java
 Purpose:  Implements bottom-up mergesort.
 Author:   M. J. Fromberger

 Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
*/

import java.util.LinkedList;

public class MergeSort extends Sort {
  public MergeSort(SortModel model) {
    super(model);
  }

  public void beginSorting() {
    int n_elt = getModel().getNumElements();

    m_temp = new int[n_elt];
    m_piles = new LinkedList();
    m_next = new LinkedList();

    for (int ix = 0; ix < n_elt; ++ix) m_piles.addLast(new Range(ix, ix));

    m_state = S_IDLE;
  }

  public boolean isDone() {
    return m_state == S_DONE;
  }

  public void nextStep() {
    switch (m_state) {
      case S_IDLE: // Select more work, or mark sorting as complete
        if (m_piles.size() == 1) m_next.addLast(m_piles.removeFirst());

        if (m_piles.size() == 0) {
          LinkedList tmp = m_piles;

          m_piles = m_next;
          m_next = tmp;
        }

        if (m_piles.size() < 2) m_state = S_DONE;
        else {
          m_left = (Range) m_piles.removeFirst();
          m_right = (Range) m_piles.removeFirst();
          m_lcur = m_left.m_lo;
          m_rcur = m_right.m_lo;
          m_tpos = 0;
          m_tmax = m_left.size() + m_right.size();
          m_state = S_MERGING;
        }
        break;

      case S_MERGING: // Comparison/load phase of merge
        int grab;

        if (m_lcur > m_left.m_hi && m_rcur > m_right.m_hi) {
          m_state = S_COPYING;
          m_ccur = 0;
          break;
        } else if (m_lcur > m_left.m_hi) grab = m_rcur++;
        else if (m_rcur > m_right.m_hi) grab = m_lcur++;
        else if (getModel().compareElements(m_lcur, m_rcur) <= SortModel.O_EQ) grab = m_lcur++;
        else grab = m_rcur++;

        m_temp[m_tpos++] = getModel().getElement(grab);
        break;

      case S_COPYING: // Copying phase of merge
        if (m_ccur >= m_tmax) {
          m_next.addLast(new Range(m_left.m_lo, m_right.m_hi));
          m_left = m_right = null;
          m_state = S_IDLE;
          break;
        }

        getModel().exchangeElement(m_left.m_lo + m_ccur, m_temp[m_ccur]);
        ++m_ccur;
        break;

      default: // Sorting is complete
        m_piles = m_next = null;
        break;
    }
  }

  private class Range {
    Range(int lo, int hi) {
      m_lo = lo;
      m_hi = hi;
    }

    public int size() {
      return m_hi + 1 - m_lo;
    }

    public int m_lo, m_hi;
  }

  public static final int S_IDLE = 0;
  public static final int S_MERGING = 1;
  public static final int S_COPYING = 2;
  public static final int S_DONE = 3;

  private LinkedList m_piles, m_next;
  private int[] m_temp;
  private int m_lcur, m_rcur, m_ccur, m_tpos, m_tmax;
  private int m_state;
  private Range m_left, m_right;

  public String getAlgorithmName() {
    return ALGORITHM_NAME;
  }

  private static String ALGORITHM_NAME = "Mergesort (bottom up)";
}

/* Here there be dragons */
