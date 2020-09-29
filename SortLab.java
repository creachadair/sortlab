/*
  Name:     SortLab.java
  Purpose:  Main driver and UI for sorting laboratory.
  Author:   M. J. Fromberger
  
  Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

public class SortLab
    extends JApplet
    implements ChangeListener, ActionListener, ItemListener
{
    public void init()
    {
	p_running = false;
	p_thread = null;
	p_algo = -1;
	p_begun = false;
	p_done = false;

	setupFonts();
	setupSortingAlgorithms();
	setupUserInterface();

	setVisible(true);

	updateAlgorithm();
	updatePointSize();
	updateSpeed();
	updateSortingStats();
    }

    public void paint(Graphics g)
    {
	if(p_running && p_done) 
	    ui_run.doClick();

	super.paint(g);
    }

    public void startRunning()
    {
	p_running = true;

	Enumeration e = ui_dis.elements();
	while(e.hasMoreElements()) {
	    JComponent c = (JComponent) e.nextElement();

	    c.setEnabled(false);
	}

	p_thread = new Thread(new SortRunner());
	p_thread.start();
    }

    public void stopRunning()
    {
	p_running = false;

	try {
	    p_thread.join();
	} 
	catch(InterruptedException e) {
	    /* ignore */
	}

	Enumeration e = ui_dis.elements();
	while(e.hasMoreElements()) {
	    JComponent c = (JComponent) e.nextElement();

	    c.setEnabled(true);
	}

	ui_model.clearFeedback();
	p_thread = null;
    }

    public void doSingleStep()
    {
	Sort s = s_algs[p_algo];

	if(!p_begun) {
	    s.beginSorting();
	    p_begun = true;
	}

	if(!s.isDone()) {
	    s.nextStep();
	    updateSortingStats();
	}
    }

    /* Handle state changes for the speed and size sliders */
    public void stateChanged(ChangeEvent e)
    {
	Object src = e.getSource();

	if(src == ui_speed) 
	    updateSpeed();
	else if(src == ui_size) 
	    updatePointSize();
    }

    /* Handle selections on the algorithm and sort type combo boxes */
    public void actionPerformed(ActionEvent e)
    {
	Object src = e.getSource();

	if(src == ui_algo)
	    updateAlgorithm();
	else if(src == ui_stype)
	    updateSortType();
	else if(src == ui_step) 
	    doSingleStep();
	else if(src == ui_run) {
	    if(p_running)
		stopRunning();
	    else
		startRunning();
	}
	else if(src == ui_reset) {
	    ui_model.setRandomSeed(p_seed);
	    ui_model.resetSort(true);
	    p_begun = false;
	    updateSortingStats();
	}
	else if(src == ui_reseed) {
	    Clock c = new Clock();

	    p_seed = c.getMilliseconds();
	}
    }

    /* Handle change events on checkboxes */
    public void itemStateChanged(ItemEvent e)
    {
	Object src = e.getItemSelectable();

	if(src == ui_feedp) {
	    boolean hide = e.getStateChange() == ItemEvent.DESELECTED;
	    ui_model.setFeedback(!hide);
	}
    }

    /* Update the currently selected algorithm */
    private void updateAlgorithm()
    {
	int chosen = ui_algo.getSelectedIndex();

	if(chosen != p_algo) {
	    ui_model.resetSort();
	    p_begun = false;
	    updateSortingStats();
	}

	p_algo = chosen;
    }

    /* Update the currently selected sort ordering */
    private void updateSortType()
    {
	switch(ui_stype.getSelectedIndex()) {
	case 0:
	    ui_model.setOrdering(OrderPanel.ORDER_ASCENDING);
	    break;
	case 1:
	    ui_model.setOrdering(OrderPanel.ORDER_DESCENDING);
	    break;
	case 2:
	    ui_model.setOrdering(OrderPanel.ORDER_RANDOM);
	    break;
	default:
	    break;
	}
    }

    /* Update the current point size; will cause re-ordering */
    private void updatePointSize()
    {
	int size = ui_size.getValue();
	
	if(size == 0)
	    size = 1; /* special case */

	ui_csize.setText("" + size);
	ui_model.setRandomSeed(p_seed);
	ui_model.setNumElements(ui_model.countMaxElements(size));
	ui_model.repaint();
	updateSortingStats();
    }

    /* Update the current running speed */
    private void updateSpeed()
    {
	int speed = ui_speed.getValue();

	p_speed = speed_values[speed];
    }

    /* Update user feedback */
    private void updateSortingStats()
    {
	ui_comps.setText("" + ui_model.getNumCompares());
	ui_moves.setText("" + ui_model.getNumMoves());
	ui_nelts.setText("" + ui_model.getNumElements());
    }

    /* Set up state information about the sorting algorithms.
       To add a new algorithm:
       - Add it to the s_algs[] array declared in this routine.

       - The rest of the UI should pick up the change when you
         recompile.
     */
    private void setupSortingAlgorithms()
    {
	s_algs = new Sort[12];

	ui_model = new OrderPanel(0); /* This will get resolved later */

	s_algs[0]  = new SelectionSort(ui_model);
	s_algs[1]  = new InsertionSort(ui_model);
	s_algs[2]  = new BubbleSort(ui_model);
	s_algs[3]  = new BidirectionalBubbleSort(ui_model);
	s_algs[4]  = new ShellSort(ui_model);
	s_algs[5]  = new QuickSortM3(ui_model);
	s_algs[6]  = new QuickSortRand(ui_model);
	s_algs[7]  = new QuickSortHybrid(ui_model);
	s_algs[8]  = new HeapSort(ui_model);
	s_algs[9]  = new MergeSort(ui_model);
	s_algs[10] = new MergeSortLR(ui_model);
	s_algs[11] = new CountingSort(ui_model);
	
	Clock c = new Clock();
	p_seed = c.getMilliseconds();
    }

    /* Create and assemble the user interface */
    private void setupUserInterface()
    {
	ui_dis = new Vector();

	ui_step = new JButton("Step");   ui_step.setFont(f_plain);
	ui_step.addActionListener(this); 
	ui_step.setPreferredSize(new Dimension(60, 30));
	ui_dis.add(ui_step);

	ui_reset = new JButton("Reset"); ui_reset.setFont(f_plain);
	ui_reset.addActionListener(this); 
	ui_reset.setPreferredSize(new Dimension(60, 30));
	ui_dis.add(ui_reset);

	ui_run = new JToggleButton("Run", false);
	ui_run.addActionListener(this);
	ui_run.setFont(f_bold);
	ui_run.setPreferredSize(new Dimension(50, 30));

	ui_reseed = new JButton("Reseed"); ui_reseed.setFont(f_plain);
	ui_reseed.addActionListener(this); 
	ui_reseed.setPreferredSize(new Dimension(70, 30));
	ui_dis.add(ui_reseed);

	ui_speed = new JSlider(JSlider.HORIZONTAL, 0, 4, 2);
	ui_speed.setSnapToTicks(true);
	ui_speed.setMajorTickSpacing(1);
	ui_speed.setPaintTicks(true);
	ui_speed.setPreferredSize(new Dimension(150, 40));
	ui_speed.addChangeListener(this);

	{ /* Create labels for the speed slider */
	    Hashtable h = new Hashtable();
	    Font f = f_plain.deriveFont((float) 8.0);

	    for(int ix = 0; ix < speed_labels.length; ++ix) {
		JLabel l = new JLabel(speed_labels[ix]);
		
		l.setFont(f);
		h.put(new Integer(ix), l);
	    }
	    ui_speed.setLabelTable(h);
	}
	ui_speed.setPaintLabels(true);
	
	ui_size = new JSlider(JSlider.HORIZONTAL, 0, 40, 10);
	ui_size.setMajorTickSpacing(10);
	ui_size.setMinorTickSpacing(5);
	ui_size.setPaintTicks(true);
	ui_size.setPreferredSize(new Dimension(150, 40));
	
	{ /* Create labels for the size slider */
	    Hashtable h = new Hashtable();
	    Font f = f_plain.deriveFont((float) 8.0);

	    for(int ix = 0; ix <= 40; ix += 5) {
		JLabel l = new JLabel("" + ix);

		l.setFont(f);
		h.put(new Integer(ix), l);
	    }
	    ui_size.setLabelTable(h);
	}
	ui_size.setPaintLabels(true);
	ui_size.addChangeListener(this);
	ui_dis.add(ui_size);

	// Checkbox to control whether comparison/move feedback is shown
	ui_feedp = new JCheckBox("Feedback", true);
	ui_feedp.addItemListener(this);
	ui_feedp.setFont(f_bold);

	// Menu of data ordering choices
	ui_stype = new JComboBox();
	ui_stype.setFont(f_plain);
	ui_stype.addItem("Ascending");
	ui_stype.addItem("Descending");
	ui_stype.addItem("Random");
	ui_stype.setSelectedIndex(ui_stype.getItemCount() - 1);
	ui_stype.setEditable(false);
	ui_stype.addActionListener(this);
	ui_dis.add(ui_stype);

	ui_algo = new JComboBox(); ui_algo.setFont(f_plain);
	for(int ix = 0; ix < s_algs.length; ++ix)
	    ui_algo.addItem(s_algs[ix].getAlgorithmName());
	ui_algo.setSelectedIndex(0);
	ui_algo.setEditable(false);
	ui_algo.addActionListener(this);
	ui_dis.add(ui_algo);

	ui_comps = new JLabel("?"); ui_comps.setFont(f_plain);
	ui_moves = new JLabel("?"); ui_moves.setFont(f_plain);
	ui_csize = new JLabel("?"); ui_csize.setFont(f_plain);
	ui_nelts = new JLabel("?"); ui_nelts.setFont(f_plain);

	/* Assemble the pieces into the visual hierarchy */
	ui_main = new JPanel();
	ui_main.setLayout(new BorderLayout());

	JPanel ctrl = new JPanel();
	((FlowLayout) ctrl.getLayout()).setAlignment(FlowLayout.LEFT);
	JLabel lb;
	ctrl.add(ui_step);
	ctrl.add(ui_run);
	ctrl.add(ui_reset);
	ctrl.add(ui_reseed);
	ctrl.add(Box.createHorizontalStrut(15));
	lb = new JLabel("Speed:"); lb.setFont(f_bold);
	ctrl.add(lb);
	ctrl.add(ui_speed);
	lb = new JLabel("Point size:"); lb.setFont(f_bold);
	ctrl.add(lb);
	ctrl.add(ui_size);
	ctrl.add(ui_csize);
	ctrl.add(ui_feedp);

	JPanel feed = new JPanel();
	((FlowLayout) feed.getLayout()).setAlignment(FlowLayout.LEFT);

	lb = new JLabel("Algorithm:"); lb.setFont(f_bold);
	feed.add(lb);
	feed.add(ui_algo);
	feed.add(Box.createHorizontalStrut(15));
	lb = new JLabel("Ordering:"); lb.setFont(f_bold);
	feed.add(lb);
	feed.add(ui_stype);
	feed.add(Box.createHorizontalStrut(15));
	lb = new JLabel("Comparisons:"); lb.setFont(f_bold);
	feed.add(lb);
	feed.add(ui_comps);
	feed.add(Box.createHorizontalStrut(15));
	lb = new JLabel("Moves:"); lb.setFont(f_bold);
	feed.add(lb);
	feed.add(ui_moves);
	feed.add(Box.createHorizontalStrut(15));
	lb = new JLabel("Elements:"); lb.setFont(f_bold);
	feed.add(lb);
	feed.add(ui_nelts);

	JPanel mfrm = new JPanel();
	CompoundBorder c = 
	    new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
			       LineBorder.createBlackLineBorder());
	mfrm.setLayout(new BorderLayout());
	mfrm.setBorder(c);
	mfrm.add(ui_model, BorderLayout.CENTER);

	ui_main.add(ctrl, BorderLayout.NORTH);
	ui_main.add(mfrm, BorderLayout.CENTER);
	ui_main.add(feed, BorderLayout.SOUTH);

	/* Set the contents of this frame to what was just built */
	getContentPane().add(ui_main);
    }

    private void setupFonts()
    {
	/* Start from the base font assigned to this component, and make
	   up some variants for more general use */

	Font base = getFont(); 

	f_plain = base.deriveFont(Font.PLAIN, (float) 10.0);
	f_bold = f_plain.deriveFont(Font.BOLD, (float) 10.0);

	setFont(f_plain);
    }
    
    /* State information... */
    private int            p_speed;   /* Current step delay       */
    private boolean        p_running; /* Are we running?          */
    private boolean        p_begun;   /* Has sorting begun?       */
    private boolean        p_done;    /* Has sorting finished?    */
    private Thread         p_thread;  /* The thread doing the run */
    private int            p_algo;    /* Current algorithm        */
    private long           p_seed;    /* Random seed              */

    /* Private UI components... */
    private JButton        ui_step;   /* Single-step current sort */
    private JToggleButton  ui_run;    /* Run current sort         */
    private JButton        ui_reset;  /* Reset current sort       */
    private JButton        ui_reseed; /* Re-seed random generator */
    private JSlider        ui_speed;  /* Adjust running speed     */
    private JSlider        ui_size;   /* Adjust display size      */
    private JComboBox      ui_stype;  /* Sorting order            */
    private JComboBox      ui_algo;   /* Sorting algorithm        */
    private JLabel         ui_comps;  /* Number of comparisons    */
    private JLabel         ui_moves;  /* Number of data moves     */
    private JLabel         ui_csize;  /* Current size of points   */
    private JLabel         ui_nelts;  /* Number of elements       */
    private JCheckBox      ui_feedp;  /* Show feedback?           */

    private JPanel         ui_main;   /* Controls and output      */

    private OrderPanel     ui_model;  /* Visual model of data     */

    /* What elements get disabled while running */
    private Vector         ui_dis;

    /* Fonts */
    private Font           f_plain;
    private Font           f_bold;

    /* Private algorithm instances... */
    private Sort[]         s_algs;

    /* Various labels and other constants ... */
    private static final String[] speed_labels =
    { "Crawl", "Slow", "Medium", "Fast", "Hyper" };

    private static final int[] speed_values = 
    { 500, 250, 100, 20, 5 }; /* delays, in milliseconds */

    /* This internal class runs the current algorithm */
    class SortRunner implements Runnable
    {
	public void run() 
	{
	    Sort who = s_algs[p_algo];

	    if(!p_begun) {
		who.beginSorting();
		p_begun = true;
	    }
	    while(p_running && !who.isDone()) {
		who.nextStep();
		updateSortingStats();
		try {
		    Thread.sleep(p_speed);
		} 
		catch(InterruptedException e) {
		    /* ignore */
		}
	    }

	    if(who.isDone()) {
		p_done = true;
		updateSortingStats();
		repaint();
	    }
	}
    }
}

/* Here there be dragons */
