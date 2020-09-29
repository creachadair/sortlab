/*
 Name:     Clock.java
 Purpose:  Glue code for compatibility with JDK 1.3.x.
 Author:   M. J. Fromberger

 Copyright (C) 2003-2006 Michael J. Fromberger, All Rights Reserved.
*/

import java.util.*;

/*
 For some reason, older versions of the JDK (1.3.x) do not provide
 getTimeInMillis() as a public method of the Calendar class; it is
 protected.  So, for compatibility, I've written this wrapper class
 that just re-exports the functionality.

 This isn't necessary under JDK 1.4.x, but it works just fine.
*/

public class Clock extends GregorianCalendar {
  public Clock() {
    super();
  }

  public long getMilliseconds() {
    return super.getTimeInMillis();
  }
}

/* Here there be dragons */
