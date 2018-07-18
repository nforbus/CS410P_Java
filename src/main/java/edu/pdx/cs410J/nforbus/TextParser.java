package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class TextParser implements PhoneBillParser<PhoneBill> {

    private String customerName;
    private File myFile;

    TextParser(String custName, File custFile) {
        customerName = custName;
        myFile = custFile;
    }

    @Override
    public PhoneBill parse() throws ParserException {

        PhoneBill thisBill = new PhoneBill(this.customerName);

        if(this.myFile.exists()) {
            try{
                Scanner file_in = new Scanner(this.myFile).useDelimiter(";");

                if(file_in.hasNextLine()) {
                    String nextLine = file_in.nextLine();
                    String[] lineItems = nextLine.split(";");
                    if(lineItems[0].equals(this.customerName)) {
                        file_in.close();
                    }

                    else {
                        file_in.close();
                        throw new ParserException("Customer name does not match that in existing file.");
                    }
                }
            }catch(FileNotFoundException ex) {
                System.out.println("File not found.");
                return null;
            }

            //You only make it this far if the customer name is correct
            try {
                Scanner file_in = new Scanner(this.myFile).useDelimiter(";");

                while(file_in.hasNextLine()) {
                    String nextLine = file_in.nextLine();
                    String[] lineItems = nextLine.split(";");
                    PhoneCall aCall = new PhoneCall(lineItems);
                    thisBill.addPhoneCall(aCall);
                }

                file_in.close();

            } catch(FileNotFoundException ex) {
                System.out.println("Could not find file.");
            }
        }

        else {
            try {
                this.myFile.createNewFile();
            }catch(IOException ex) {
                System.out.println("File could not be made.");
            }
        }

        return thisBill;
    }
}
