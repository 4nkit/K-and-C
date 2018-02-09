package com.example.ankit.KandC;

import android.widget.ImageView;

/**
 * Created by Ankit on 31-01-2018.
 */

public class Product {
    String pId;
    String pName;
    String pPrice;
    String pDescription;
    String Imgurl;
    public void Product(){

    }

    public Product(String pId,String pName, String pPrice, String pDescription, String Imgurl) {
        this.pId = pId;
        this.pName = pName;
        this.pPrice = pPrice;
        this.pDescription = pDescription;
        this.Imgurl = Imgurl;
    }
    public String getpId() {return pId;}

    public String getpName() {
        return pName;
    }

    public String getpPrice() {
        return pPrice;
    }

    public String getpDescription() {
        return pDescription;
    }

    public String getImgurl(){return Imgurl;}
}
