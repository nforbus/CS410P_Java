

package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.PhoneBillDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * A class that takes a phonebill and creates/replaces a text file with the contents.
 */
public class TextDumper implements PhoneBillDumper<PhoneBill> {

    private String customerName;
    private File myFile;

    //Default and only OL constructor
    TextDumper(String custName, File custFile) {
        customerName = custName;
        myFile = custFile;
    }

    //Creates a text file and populates it with phonebill information.  Overwrites existing info in file.
    @Override
    public void dump(PhoneBill phoneBill) throws IOException {

        StringBuilder callString = new StringBuilder();
        Collection<PhoneCall> phoneCalls = phoneBill.getPhoneCalls();


        try {
            if(this.myFile == null) {
                throw new IOException("Could not find file.");
            }

            FileWriter file_out = new FileWriter(this.myFile);

            for(PhoneCall call : phoneCalls) {
                callString.append(this.customerName + ";");
                callString.append(call.getCaller() + ";");
                callString.append(call.getCallee() + ";");
                callString.append(call.getStartTimeString() + ";");
                callString.append(call.getEndTimeString() + System.getProperty("line.separator"));
                String thisString = callString.toString();

                file_out.write(thisString);

                callString.setLength(0);
            }

            file_out.close();

        }catch(IOException ex) {
            throw ex;
        }
    }
}
