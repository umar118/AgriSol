package com.example.agrisol;

public class Market {
    private String CropPrice;
    private String CropName,District;

    public Market(){}

    public Market(String cropPrice, String cropName, String district) {
        CropPrice = cropPrice;
        CropName = cropName;
        District = district;
    }

    public String getCropPrice() {
        return CropPrice;
    }

    public void setCropPrice(String cropPrice) {
        CropPrice = cropPrice;
    }

    public String getCropName() {
        return CropName;
    }

    public void setCropName(String cropName) {
        CropName = cropName;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }
}
