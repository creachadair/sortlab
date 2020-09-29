##
## Name:     Makefile
## Purpose:  Build script for SortLab.
## Author:   M. J. Fromberger <http://www.dartmouth.edu/~sting/>
## Info:     $Id: Makefile,v 1.7 2006/02/28 06:13:58 sting Exp $
##
## Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
##
.SUFFIXES: .java .class
.PHONY: clean distclean jar

JCC = javac
JFLAGS = -source 1.6

RAW = $(subst +,"$$",\
	BubbleSort. BidirectionalBubbleSort. Clock. CountingSort. \
	CustomOrder. HeapSort. InsertionSort. MergeSort. MergeSortLR. \
	OrderPanel. QuickSort. QuickSortM3. QuickSortRand. QuickSortHybrid. \
	SelectionSort. ShellSort. Sort. SortLab. SortModel. SortingAlgorithm. \
	QuickSort+Range.i MergeSort+Range.i MergeSortLR+Range.i \
	SortLab+SortRunner.i)

EXTRA = Manifest Makefile img SortLab.shtml

SRCS = $(filter-out %.i,$(RAW:.=.java))
OBJS = $(patsubst %.,%.class,$(patsubst %.i,%.class,$(RAW)))
REAL = $(filter-out %.i,$(RAW))
REALOBJS = $(patsubst %.,%.class,$(patsubst %.i,%.class,$(REAL)))

.java.class:
	$(JCC) $(JFLAGS) $<

jar: $(REALOBJS) Manifest
	jar cmf Manifest sortlab.jar $(OBJS)

$(REALOBJS): %.class: %.java
	$(JCC) $(JFLAGS) SortLab.java

clean:
	rm -f *~ core core.[0-9][0-9]*

distclean: clean
	rm -f $(OBJS) sortlab.jar 

dist: distclean
	if [ -f sortlab.zip ] ; then \
		mv -f sortlab.zip sortlab-old.zip ; fi
	tar -cvf _temp.tar $(SRCS) $(EXTRA)
	mkdir sortlab
	( cd sortlab && tar -xvf ../_temp.tar )
	rm -f _temp.tar
	zip -9r sortlab.zip sortlab
	rm -rf sortlab

# Here there be dragons