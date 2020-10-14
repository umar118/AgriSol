package com.example.agrisol.Market;

public class Market  {

    private String Name,Price,District,Date,uid;

    public Market(){}

    public Market(String name, String price, String district, String date,String uid) {
        Name = name;
        Price = price;
        District = district;
        Date = date;
        uid=uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
