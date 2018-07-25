package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.AbstractPhoneCall;

public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall> {

  String caller;
  String callee;
  String startTimeAndDate;
  String endTimeAndDate;

  PhoneCall() {}

  PhoneCall(String[] callInfo) {
    this.caller = callInfo[1];
    this.callee = callInfo[2];
    this.startTimeAndDate = callInfo[3];
    this.endTimeAndDate = callInfo[4];
  }

  @Override
  public String getCaller() {

    return caller;
  }

  @Override
  public String getCallee() {

    return callee;
  }

  @Override
  public String getStartTimeString() {

    return startTimeAndDate;
  }

  @Override
  public String getEndTimeString() {

    return endTimeAndDate;
  }

  @Override
  public int compareTo(PhoneCall toCompare) {

      String[] myDT = this.startTimeAndDate.split(":|/| ");
      String[] otherDT = toCompare.startTimeAndDate.split(":|/| ");

      if (myDT[5] == "PM") {
          myDT[5] = "1";
      } else {
          myDT[5] = "0";
      }

      if (otherDT[5] == "PM") {
          otherDT[5] = "1";
      } else {
          otherDT[5] = "0";
      }

      /* Straight up comparisons between the entire date string were resulting in bad results
      when the year changed.  So I had to break up the date string by each time aspect
      Then figure out the sort piece by piece, in the order;
      Year -> Month -> Day -> AM/PM -> Hour -> Minute -> Phone Number
       */
      int diff = myDT[2].compareTo(otherDT[2]);
      if (diff == 0) {
          diff = myDT[0].compareTo(otherDT[0]);
          if (diff == 0) {
              diff = myDT[1].compareTo(otherDT[1]);
              if (diff == 0) {
                  diff = myDT[5].compareTo(otherDT[5]);
                  if (diff == 0) {
                      diff = myDT[3].compareTo(otherDT[3]);
                      if (diff == 0) {
                          diff = myDT[4].compareTo(otherDT[4]);
                          if (diff == 0) {
                              return this.callee.compareTo(toCompare.caller);
                          }
                      }
                  }
              }
          }
      }

      return diff;
  }
}
