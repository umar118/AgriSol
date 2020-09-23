package com.example.agrisolutions;

import androidx.lifecycle.ViewModel;

public class ExpertDetailsViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    String Name,Email,Education,Contact,Experties;

    public ExpertDetailsViewModel(String name, String email, String education, String contact, String experties) {
        Name = name;
        Email = email;
        Education = education;
        Contact = contact;
        Experties = experties;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String education) {
        Education = education;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getExperties() {
        return Experties;
    }

    public void setExperties(String experties) {
        Experties = experties;
    }


}
