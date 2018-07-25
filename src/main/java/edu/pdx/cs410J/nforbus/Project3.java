package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * The main class.
 */
public class Project3 {

  //Calls textparser in order to populate a phonebill object with contents from file
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

  //Checks args to see if pretty option is selected
  private static String checkForPretty(String[] args) {

    int lengthCheck = args.length;

    for(int i = 0; i < lengthCheck; ++i) {
      if(args[i].contains("-pretty")) {
        if(args[i+1].endsWith(".txt")) {
          return args[i+1];
        }

        return "STDOUT";
      }
    }

    return null;
  }

  //Checks args to see if file options are selected
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

  //Checks args to see if readme option is selected
  private static int checkForReadMe(String[] args) {
    int lengthCheck = args.length;

    for(int i = 0; i < lengthCheck; ++i) {
      if(args[i].equals("-README")) {
        return 0;
      }
    }

    return 1;
  }

  //Checks args to see if print option is selected
  private static int checkForPrint(String[] args) {
    int lengthCheck = args.length;

    for(int i = 0; i < lengthCheck; ++i) {
      if(args[i].equals("-print")) {
        return 0;
      }
    }

    return 1;
  }

  //Verifies format matches
  private static int checkFormat(String toCheck, SimpleDateFormat formatCheck) {

    try{formatCheck.parse(toCheck);
    }catch(ParseException pe){
      return 1;
    }

    return 0;
  }

  //Creates date strings with date/time/am-pm args
  private static String makeDateTime(String theDate, String theTime, String theSuffix) {

    int dateSuccess1, dateSuccess2, dateSuccess3, dateSuccess4 = 1;
    int timeSuccess1, timeSuccess2 = 1;

    SimpleDateFormat dateCheck1 = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat dateCheck2 = new SimpleDateFormat("M/dd/yyyy");
    SimpleDateFormat dateCheck3 = new SimpleDateFormat("MM/d/yyyy");
    SimpleDateFormat dateCheck4 = new SimpleDateFormat("M/d/yyyy");

    dateSuccess1 = checkFormat(theDate, dateCheck1);
    dateSuccess2 = checkFormat(theDate, dateCheck2);
    dateSuccess3 = checkFormat(theDate, dateCheck3);
    dateSuccess4 = checkFormat(theDate, dateCheck4);

    if((dateSuccess1 != 0)&&(dateSuccess2 != 0)&&(dateSuccess3 != 0)&&(dateSuccess4 != 0)) {
      System.out.println("Improper date format detected.");
      System.exit(1);
    }

    SimpleDateFormat timeCheck1 = new SimpleDateFormat("hh:mm");
    SimpleDateFormat timeCheck2 = new SimpleDateFormat("h:mm");

    timeSuccess1 = checkFormat(theTime, timeCheck1);
    timeSuccess2 = checkFormat(theTime, timeCheck2);

    if((timeSuccess1 != 0)&&(timeSuccess2 != 0)) {
      System.out.println("Improper time format detected.");
      System.exit(1);
    }

    if((!theSuffix.equals("AM")) && (!theSuffix.equals("PM"))) {
      System.out.println("Improper suffix selected.  Should be AM or PM.");
      System.exit(1);
    }

    Date myDate = null;
    String myString = (theDate + " " + theTime + " " + theSuffix);
    SimpleDateFormat dateTimeCheck = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    try{myDate = dateTimeCheck.parse(myString);
    }catch(ParseException pe){
      System.out.println("Parse failed.");
    }

    myString = df.format(myDate);
    myString = myString.replaceAll(",", "");

    return myString;
  }

  //Filters arguments to make sure they are accurate.  Exits when there are problems.
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

    //Processes startTimeDate and endTimeDate
    processedArray[3] = makeDateTime(strippedArgs[3], strippedArgs[4], strippedArgs[5]);
    processedArray[4] = makeDateTime(strippedArgs[6], strippedArgs[7], strippedArgs[8]);

    return processedArray;
  }

  //Main method
  public static void main(String[] args) {

    if(args.length < 9) {
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
    int optionCount = 0;
    int readMeRequested = checkForReadMe(args);
    int printRequested = checkForPrint(args);
    String fileName = checkForFile(args);
    String prettyName = checkForPretty(args);

    //Tally up how many options were utilized
    if(readMeRequested == 0) { ++optionCount; }
    if(printRequested == 0) { ++optionCount; }
    if(fileName != null && fileName.length() > 4) { ++optionCount; ++optionCount; }
    if((prettyName != null) && (prettyName.equals("STDOUT"))) { ++optionCount; }
    else if((prettyName != null) && (!prettyName.equals("STDOUT"))) { ++optionCount; ++optionCount; }

    //Strip out the options from the args
    int strippedLength = (args.length - optionCount);

    //Crashes the program when too many arguments are passed, for preventing bullshit arguments from screwing things up
    if(9 + optionCount != args.length) {
      System.out.println("Nonsense arguments have been passed.  Closing.");
      System.exit(1);
    }

    String[] strippedArray = new String[strippedLength];

    for(int i = 0; i < strippedLength; i++) {
      strippedArray[i] = args[i + optionCount];
    }

    //capture args and parse them
    String[] parsedArray = parseArgs(strippedArray);

    //Onto execution!
    PhoneBill bill;
    File myFile = null;
    File prettyFile = null;

    //if file work needs to be done, start here.
    if(fileName != null) {

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

    //Second half of the -pretty option, saves phonebill info to external file in pretty form
    if(prettyName != null) {
      if(prettyName.equals("STDOUT")) {
        PrettyPrinter prettyPr = new PrettyPrinter(parsedArray[0]);
        prettyPr.print(bill);
        System.out.println();
      }

      else {
        System.out.println("Saving a pretty print to file: " + prettyName);
        prettyFile = new File(prettyName);
        PrettyPrinter prettyPr = new PrettyPrinter(parsedArray[0], prettyFile);

        try {
          prettyPr.dump(bill);
        }catch(IOException ex) {
          System.out.println("IO Exception from Pretty Printer.");
        }
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