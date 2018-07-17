package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * The main class for the CS410J Phone Bill Project
 */
public class Project1 {

  public static int checkForReadMe(String[] args) {
    int lengthCheck = args.length;

    for(int i = 7; i < lengthCheck; ++i) {
      if(args[i].equals("-README")) {
        return 0;
      }
    }

    return 1;
  }

  public static int checkForPrint(String[] args) {
    int lengthCheck = args.length;

    for(int i = 7; i < lengthCheck; ++i) {
      if(args[i].equals("-print")) {
        return 0;
      }
    }

    return 1;
  }

  public static int checkFormat(String toCheck, SimpleDateFormat formatCheck) {

    try{formatCheck.parse(toCheck);} catch(ParseException pe){ return 1; }

    return 0;

  }

  public static void checkTimeWrapper(String timeToCheck) {
    int checkSuccess1 = 1;
    int checkSuccess2 = 1;

    SimpleDateFormat timeCheck1 = new SimpleDateFormat("HH:mm");
    SimpleDateFormat timeCheck2 = new SimpleDateFormat("H:mm");

    checkSuccess1 = checkFormat(timeToCheck, timeCheck1);
    checkSuccess2 = checkFormat(timeToCheck, timeCheck2);

    if((checkSuccess1 == 0)||(checkSuccess2 == 0)) {}

    else {
      System.out.println("Improper time format detected.");
      System.exit(1);
    }

  }

  public static void checkDateWrapper(String dateToCheck) {

    int checkSuccess1 = 1;
    int checkSuccess2 = 1;
    int checkSuccess3 = 1;
    int checkSuccess4 = 1;

    SimpleDateFormat dateCheck1 = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat dateCheck2 = new SimpleDateFormat("M/dd/yyyy");
    SimpleDateFormat dateCheck3 = new SimpleDateFormat("MM/d/yyyy");
    SimpleDateFormat dateCheck4 = new SimpleDateFormat("M/d/yyyy");

    checkSuccess1 = checkFormat(dateToCheck, dateCheck1);
    checkSuccess2 = checkFormat(dateToCheck, dateCheck2);
    checkSuccess3 = checkFormat(dateToCheck, dateCheck3);
    checkSuccess4 = checkFormat(dateToCheck, dateCheck4);

    if((checkSuccess1 == 0)||(checkSuccess2 == 0)||(checkSuccess3 == 0)||(checkSuccess4 == 0)) {}

    else {
      System.out.println("Improper date format detected.");
      System.exit(1);
    }
  }

  public static String[] parseArgs(String[] args) {

    String[] processedArray = new String[5];
    int success = 0;

    //parses customerName
    String newCustomerName = args[0].replaceAll("[^a-zA-Z]+", "");
    processedArray[0] = newCustomerName;

    //parses callerNum and calleeNum; must be 12 chars, two -'s, 9 numbers 0-9
    if((args[1].length() != 12) || (args[2].length() != 12)) {
      System.out.println("Phone numbers must be 12 characters long, in the format of nnn-nnn-nnnn");
      System.exit(1);
    }

    char[] testArray1 = args[1].toCharArray();
    char[] testArray2 = args[2].toCharArray();

    for(int i = 0; i < 12; ++i) {
      if(i == 3 || i == 7) {
        if((testArray1[i] != '-')||(testArray2[i] != '-')) {
          System.out.println("Expected format of nnn-nnn-nnnn");
          System.exit(1);
        }
      }

      else if((Character.isLetter(testArray1[i]))||(Character.isLetter(testArray2[i]))) {
        System.out.println("Expected format of nnn-nnn-nnnn");
        System.exit(1);
      }
    }

    processedArray[1] = args[1];
    processedArray[2] = args[2];

    //parses startDate and endDate; D/M/YYYY and DD/MM/YYYY accepted
    checkDateWrapper(args[3]);
    checkDateWrapper(args[5]);

    //parses startTime and endTime;  HH:MM and H:MM accepted
    checkTimeWrapper(args[4]);
    checkTimeWrapper(args[6]);

    //Append time after date, then add to processedArray
    processedArray[3] = args[3] + " " + args[4];
    processedArray[4] = args[5] + " " + args[6];

    return processedArray;
  }

  public static void main(String[] args) {

    if(args.length < 7) {
      System.err.println("Missing command line arguments");
      System.exit(1);
    }

    else {
      System.out.println("Printing arguments.");
      for(String arg : args) {
        System.out.println(arg);
      }
    }

    String[] parsedArray = parseArgs(args);

    int readMeRequested = checkForReadMe(args);
    int printRequested = checkForPrint(args);

    PhoneCall call = new PhoneCall(parsedArray);
    PhoneBill bill = new PhoneBill(parsedArray[0]);

    bill.addPhoneCall(call);

    Collection<PhoneCall> phoneCalls = bill.getPhoneCalls();

    if(readMeRequested == 0) {
      System.out.println("This is where a README would go, IF I HAD ONE!");
    }

    if(printRequested == 0) {
      System.out.println("Printing out phonecall info.");

      for(PhoneCall myCall : phoneCalls) {
        System.out.println(myCall);
      }
    }

    System.exit(0);
  }
}