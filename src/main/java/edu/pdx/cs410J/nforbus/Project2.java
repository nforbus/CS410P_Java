package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * The main class for the CS410J Phone Bill Project
 */
public class Project2 {

  private static PhoneBill makePhoneBill(File myFile, String customerName) {

    PhoneBill newBill = null;
    TextParser myParser = new TextParser(customerName, myFile);

    try {
      newBill = myParser.parse();
    }catch(ParserException ex) {
      System.out.println("PARSER EXCEPTION: " + ex);
    }

    return newBill;

  }

  private static String checkForFile(String[] args) {

    int lengthCheck = args.length;

    for(int i = 0; i < lengthCheck; ++i) {
      if(args[i].contains("-textFile")) {

        if(args[i+1].endsWith(".txt")) {
          return args[i+1];
        }
      }
    }

    return null;
  }

  private static int checkForReadMe(String[] args) {
    int lengthCheck = args.length;

    for(int i = 0; i < lengthCheck; ++i) {
      if(args[i].equals("-README")) {
        return 0;
      }
    }

    return 1;
  }

  private static int checkForPrint(String[] args) {
    int lengthCheck = args.length;

    for(int i = 0; i < lengthCheck; ++i) {
      if(args[i].equals("-print")) {
        return 0;
      }
    }

    return 1;
  }

  private static int checkFormat(String toCheck, SimpleDateFormat formatCheck) {

    try{formatCheck.parse(toCheck);
    }catch(ParseException pe){
      return 1;
    }

    return 0;
  }

  private static void checkTimeWrapper(String timeToCheck) {
    int checkSuccess1, checkSuccess2 = 1;

    SimpleDateFormat timeCheck1 = new SimpleDateFormat("HH:mm");
    SimpleDateFormat timeCheck2 = new SimpleDateFormat("H:mm");

    checkSuccess1 = checkFormat(timeToCheck, timeCheck1);
    checkSuccess2 = checkFormat(timeToCheck, timeCheck2);

    if((checkSuccess1 != 0)&&(checkSuccess2 != 0)) {
      System.out.println("Improper time format detected.");
      System.exit(1);
    }
  }

  private static void checkDateWrapper(String dateToCheck) {

    int checkSuccess1, checkSuccess2, checkSuccess3, checkSuccess4 = 1;

    SimpleDateFormat dateCheck1 = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat dateCheck2 = new SimpleDateFormat("M/dd/yyyy");
    SimpleDateFormat dateCheck3 = new SimpleDateFormat("MM/d/yyyy");
    SimpleDateFormat dateCheck4 = new SimpleDateFormat("M/d/yyyy");

    checkSuccess1 = checkFormat(dateToCheck, dateCheck1);
    checkSuccess2 = checkFormat(dateToCheck, dateCheck2);
    checkSuccess3 = checkFormat(dateToCheck, dateCheck3);
    checkSuccess4 = checkFormat(dateToCheck, dateCheck4);

    if((checkSuccess1 != 0)&&(checkSuccess2 != 0)&&(checkSuccess3 != 0)&&(checkSuccess4 != 0)) {
      System.out.println("Improper date format detected.");
      System.exit(1);
    }
  }

  private static String[] parseArgs(String[] strippedArgs) {

    if(strippedArgs.length < 7) {
      System.out.println("Missing arguments.");
      System.exit(1);
    }

    String[] processedArray = new String[5];

    //parses customerName - NOTE: blank space is allowed due to the space between + and " in the regex.
    String newCustomerName = strippedArgs[0].replaceAll("[^a-zA-Z]+ ", "");
    processedArray[0] = newCustomerName;

    //parses callerNum and calleeNum; must be 12 chars, two -'s, 9 numbers 0-9
    if((strippedArgs[1].length() != 12) || (strippedArgs[2].length() != 12)) {
      System.out.println("Phone numbers must be 12 characters long, in the format of nnn-nnn-nnnn");
      System.exit(1);
    }

    char[] testArray1 = strippedArgs[1].toCharArray();
    char[] testArray2 = strippedArgs[2].toCharArray();

    for(int i = 0; i < 12; ++i) {
      if(i == 3 || i == 7) {
        if((testArray1[i] != '-')||(testArray2[i] != '-')) {
          System.out.println("Expected format of nnn-nnn-nnnn");
          System.exit(1);
        }
      }

      else if((!Character.isDigit(testArray1[i]) || (!Character.isDigit(testArray2[i])))) {
        System.out.println("Expected format of nnn-nnn-nnnn");
        System.exit(1);
      }
    }

    processedArray[1] = strippedArgs[1];
    processedArray[2] = strippedArgs[2];

    //parses startDate and endDate; D/M/YYYY and DD/MM/YYYY accepted
    checkDateWrapper(strippedArgs[3]);
    checkDateWrapper(strippedArgs[5]);

    //parses startTime and endTime;  HH:MM and H:MM accepted
    checkTimeWrapper(strippedArgs[4]);
    checkTimeWrapper(strippedArgs[6]);

    //Append time after date, then add to processedArray
    processedArray[3] = strippedArgs[3] + " " + strippedArgs[4];
    processedArray[4] = strippedArgs[5] + " " + strippedArgs[6];

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

    //check to see what options are selected
    int readMeRequested = checkForReadMe(args);
    int printRequested = checkForPrint(args);
    String fileName = checkForFile(args);

    //Tally up how many options were utilized
    int optionCount = 0;
    if(readMeRequested == 0) { ++optionCount; }
    if(printRequested == 0) { ++optionCount; }
    if(fileName != null && fileName.length() > 4) { ++optionCount; ++optionCount; }

    //Strip out the options from the args
    int strippedLength = (args.length - optionCount);
    String[] strippedArray = new String[strippedLength];

    for(int i = 0; i < strippedLength; i++) {
      strippedArray[i] = args[i + optionCount];
    }

    //capture args and parse them
    String[] parsedArray = parseArgs(strippedArray);

    //Onto execution!
    PhoneBill bill;
    File myFile = null;

    //if file work needs to be done, start here.
    if(fileName != null) {

      //File and directory management.  If bills folder doesn't exist, it will be created.
      /*
      File myDir = new File(System.getProperty("user.dir") + "\\" + "bills");
      if(!myDir.exists()) {
        myDir.mkdir();
      }
      */

      //Makes a new file in the bills directory
      //myFile = new File(myDir + "\\" + fileName);
      myFile = new File(fileName);

      //Handle the phonebill creation
      bill = makePhoneBill(myFile, parsedArray[0]);
      if(bill == null) {
        System.out.println("Bill not created.  Fatal error has occurred.");
        System.exit(1);
      }
    }

    else {
      System.out.println("WARNING: No file has been specified, so this information will not be saved.");
      bill = new PhoneBill(parsedArray[0]);
    }

    //create a new phone call
    PhoneCall call = new PhoneCall(parsedArray);
    bill.addPhoneCall(call);

    //Process the -README option
    if(readMeRequested == 0) {
      System.out.println("This is where a README would go, IF I HAD ONE!");
    }

    //Process the -print option
    if(printRequested == 0) {
      System.out.println("Printing out phonecall info...");

      Collection<PhoneCall> phoneCalls = bill.getPhoneCalls();
      for(PhoneCall myCall : phoneCalls) {
        System.out.println(myCall);
      }
    }

    //Second half of the -file option, saves new call info to external file
    if(myFile != null) {
      System.out.println("Saving new calls to file: " + fileName);
      TextDumper outputCall = new TextDumper(parsedArray[0], myFile);
      try {
        outputCall.dump(bill);
      } catch(IOException ex) {
        System.out.println("IO EXCEPTION:" + ex);
      }
    }

    System.exit(0);
  }
}