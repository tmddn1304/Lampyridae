package com.hackathon.lampyridaeclient;

class UserData {
    private String userName;
    private String userGender;
    private String userAge;
    private String userPhone;

    private String systemFemaleText;

    UserData(String systemFemaleText) {
        this.systemFemaleText = systemFemaleText;
    }

    String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    boolean isFemale() {
        return userGender.equals(systemFemaleText);
    }

    void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    String getUserAge() {
        return userAge;
    }

    void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    String getUserPhone() {
        return userPhone;
    }

    void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
