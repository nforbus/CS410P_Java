package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.AbstractPhoneCall;

public class PhoneCall extends AbstractPhoneCall {

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
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getCallee() {

    return callee;
    //return "This method is not implemented yet";
  }

  @Override
  public String getStartTimeString() {

    return startTimeAndDate;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getEndTimeString() {

    return endTimeAndDate;
    //throw new UnsupportedOperationException("This method is not implemented yet");
  }
}
