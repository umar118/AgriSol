package com.example.agrisol;

import androidx.lifecycle.ViewModel;

public class ExpertDetialsViewModel  {

    ExpertDetialsViewModel(){}

    private int profile;
    private String Expert_Fullname,Expert_Email,Expert_Contact,Expert_Qualification,Expert_Experties,Expert_City,Expert_Province,Expert_Country;

    public ExpertDetialsViewModel(int profile, String expert_Fullname, String expert_Email, String expert_Contact, String expert_Qualification, String expert_Experties, String expert_City, String expert_Province, String expert_Country) {
        this.profile = profile;
        Expert_Fullname = expert_Fullname;
        Expert_Email = expert_Email;
        Expert_Contact = expert_Contact;
        Expert_Qualification = expert_Qualification;
        Expert_Experties = expert_Experties;
        Expert_City = expert_City;
        Expert_Province = expert_Province;
        Expert_Country = expert_Country;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getExpert_Fullname() {
        return Expert_Fullname;
    }

    public void setExpert_Fullname(String expert_Fullname) {
        Expert_Fullname = expert_Fullname;
    }

    public String getExpert_Email() {
        return Expert_Email;
    }

    public void setExpert_Email(String expert_Email) {
        Expert_Email = expert_Email;
    }

    public String getExpert_Contact() {
        return Expert_Contact;
    }

    public void setExpert_Contact(String expert_Contact) {
        Expert_Contact = expert_Contact;
    }

    public String getExpert_Qualification() {
        return Expert_Qualification;
    }

    public void setExpert_Qualification(String expert_Qualification) {
        Expert_Qualification = expert_Qualification;
    }

    public String getExpert_Experties() {
        return Expert_Experties;
    }

    public void setExpert_Experties(String expert_Experties) {
        Expert_Experties = expert_Experties;
    }

    public String getExpert_City() {
        return Expert_City;
    }

    public void setExpert_City(String expert_City) {
        Expert_City = expert_City;
    }

    public String getExpert_Province() {
        return Expert_Province;
    }

    public void setExpert_Province(String expert_Province) {
        Expert_Province = expert_Province;
    }

    public String getExpert_Country() {
        return Expert_Country;
    }

    public void setExpert_Country(String expert_Country) {
        Expert_Country = expert_Country;
    }
}
