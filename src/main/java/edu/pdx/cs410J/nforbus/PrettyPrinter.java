package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.PhoneBillDumper;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A class for the Pretty Printer.  This offers formatted printing and formatted file writing.
 */
public class PrettyPrinter implements PhoneBillDumper<PhoneBill> {

    private String customerName;
    private File myFile;

    //Default constructor
    PrettyPrinter() {}

    //OL constructor for when user requests STD OUTPUT
    PrettyPrinter(String custName) {
        customerName = custName;
    }

    //OL constructor for when user requests file version
    PrettyPrinter(String custName, File custFile) {
        customerName = custName;
        myFile = custFile;
    }

    //Returns duration of a call in minutes
    public long getDuration(String startTime, String endTime) {
        SimpleDateFormat dateTimeCheck = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");

        Date start = null;
        Date end = null;

        try{
            start = dateTimeCheck.parse(startTime);
            end = dateTimeCheck.parse(endTime);

        }catch(ParseException pe){
            System.out.println("Parse failed.");
        }

        if((start != null) && (end != null)) {
            long duration = end.getTime() - start.getTime();
            return TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        }

        System.out.println("Could not get call duration.");
        return 0;
    }

    //Pretty prints a phonebill
    public void print(PhoneBill myBill) {
        Collection<PhoneCall> phoneCalls = myBill.getPhoneCalls();
        PrintStream out = System.out;
        int custLen = this.customerName.length();
        long callDuration;

        out.format("+ %-"+custLen+"s + %-12s + %-12s + %-16s + %-16s + %-11s +%n", "Customer", "Caller", "Callee",
                "Start Time", "End Time", "Duration");

        for(PhoneCall call : phoneCalls) {
            callDuration = getDuration(call.getStartTimeString(), call.getEndTimeString());
            out.format("| %-"+custLen+"s | %-12s | %-12s | %-16s | %-16s | %-11s |%n", this.customerName, call.getCaller(),
                    call.getCallee(), call.getStartTimeString(), call.getEndTimeString(), callDuration + " mins");
        }

    }

    //Creates a pretty-print text file
    @Override
    public void dump(PhoneBill phoneBill) throws IOException {

        Collection<PhoneCall> phoneCalls = phoneBill.getPhoneCalls();
        int custLen = this.customerName.length();
        long callDuration;

        PrintWriter out = new PrintWriter(this.myFile);
        out.format("+ %-"+custLen+"s + %-12s + %-12s + %-16s + %-16s + %-11s +%n", "Customer", "Caller", "Callee",
                "Start Time", "End Time", "Duration");

        try{
            if(this.myFile == null) {
                throw new IOException("Could not find file.");
            }

            for(PhoneCall call : phoneCalls) {
                callDuration = getDuration(call.getStartTimeString(), call.getEndTimeString());
                out.format("| %-"+custLen+"s | %-12s | %-12s | %-16s | %-16s | %-11s |%n", this.customerName, call.getCaller(),
                        call.getCallee(), call.getStartTimeString(), call.getEndTimeString(), callDuration + " mins");
            }

            out.close();

        }catch(IOException ex) {
            throw ex;
        }
    }
}
