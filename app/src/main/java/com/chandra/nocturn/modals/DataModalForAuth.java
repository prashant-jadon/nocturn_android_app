package com.chandra.nocturn.modals;

public class DataModalForAuth {
    public String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DataModalForAuth(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
}
