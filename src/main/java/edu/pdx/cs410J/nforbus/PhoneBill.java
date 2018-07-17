package edu.pdx.cs410J.nforbus;

import edu.pdx.cs410J.AbstractPhoneBill;
import java.util.ArrayList;
import java.util.Collection;

public class PhoneBill extends AbstractPhoneBill<PhoneCall> {

    private Collection<PhoneCall> calls = new ArrayList<>();
    String customer;

    PhoneBill() {}

    PhoneBill(String customerNameToAdd) {

        this.customer = customerNameToAdd;
    }

    @Override
    public String getCustomer() {

        //return null;
        return customer;
    }

    @Override
    public void addPhoneCall(PhoneCall call) {

        this.calls.add(call);
    }

    @Override
    public Collection<PhoneCall> getPhoneCalls() {

        return this.calls;
    }
}
