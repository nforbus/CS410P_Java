package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.AbstractPhoneBill;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A class for phonebill objects.  Phone calls will be appended to these.
 */
public class PhoneBill extends AbstractPhoneBill<PhoneCall> {

    private Collection<PhoneCall> calls = new ArrayList<>();
    String customer;

    //Default constructor
    PhoneBill() {}

    //Overloaded constructor
    PhoneBill(String customerNameToAdd) {

        this.customer = customerNameToAdd;
    }

    //Returns customer name string
    @Override
    public String getCustomer() {

        return customer;
    }

    //Adds a phone call to the bill
    @Override
    public void addPhoneCall(PhoneCall call) {

        this.calls.add(call);
    }

    //Returns a sorted list of all phone calls on a phonebill
    @Override
    public Collection<PhoneCall> getPhoneCalls() {

        Collections.sort((ArrayList)this.calls);

        return this.calls;
    }
}
