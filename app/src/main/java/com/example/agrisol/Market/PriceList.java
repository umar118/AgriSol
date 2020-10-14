package com.example.agrisol.Market;

public class PriceList {

   private String CropName,CropPrice,CropDistrict,CropDate;
   public PriceList(){}

    public PriceList(String cropName, String cropPrice, String cropDistrict, String cropDate) {
        CropName = cropName;
        CropPrice = cropPrice;
        CropDistrict = cropDistrict;
        CropDate = cropDate;
    }

    public String getCropName() {
        return CropName;
    }

    public void setCropName(String cropName) {
        CropName = cropName;
    }

    public String getCropPrice() {
        return CropPrice;
    }

    public void setCropPrice(String cropPrice) {
        CropPrice = cropPrice;
    }

    public String getCropDistrict() {
        return CropDistrict;
    }

    public void setCropDistrict(String cropDistrict) {
        CropDistrict = cropDistrict;
    }

    public String getCropDate() {
        return CropDate;
    }

    public void setCropDate(String cropDate) {
        CropDate = cropDate;
    }
}
