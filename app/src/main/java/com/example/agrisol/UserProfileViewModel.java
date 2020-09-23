package com.example.agrisol;

import androidx.lifecycle.ViewModel;

public class UserProfileViewModel {

    int UserProfile;
    String UserFullName,UserEmail,UserMobile,UserOccup,UserCity,UserProvince,UserCountry;

    public UserProfileViewModel(){}

    public UserProfileViewModel(int userProfile, String userFullName, String userEmail, String userMobile, String userOccup, String userCity, String userProvince, String userCountry) {
        UserProfile = userProfile;
        UserFullName = userFullName;
        UserEmail = userEmail;
        UserMobile = userMobile;
        UserOccup = userOccup;
        UserCity = userCity;
        UserProvince = userProvince;
        UserCountry = userCountry;
    }

    public int getUserProfile() {
        return UserProfile;
    }

    public String getUserFullName() {
        return UserFullName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public String getUserOccup() {
        return UserOccup;
    }

    public String getUserCity() {
        return UserCity;
    }

    public String getUserProvince() {
        return UserProvince;
    }

    public String getUserCountry() {
        return UserCountry;
    }

    public void setUserProfile(int userProfile) {
        UserProfile = userProfile;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public void setUserOccup(String userOccup) {
        UserOccup = userOccup;
    }

    public void setUserCity(String userCity) {
        UserCity = userCity;
    }

    public void setUserProvince(String userProvince) {
        UserProvince = userProvince;
    }

    public void setUserCountry(String userCountry) {
        UserCountry = userCountry;
    }
}
