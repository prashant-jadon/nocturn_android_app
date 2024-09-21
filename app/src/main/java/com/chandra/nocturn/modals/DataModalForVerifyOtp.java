package com.chandra.nocturn.modals;

public class DataModalForVerifyOtp {
    String otp;
    String phoneNumber;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DataModalForVerifyOtp(String otp, String phoneNumber){
        this.otp = otp;
        this.phoneNumber = phoneNumber;
    }
}
