package com.example.fixmycar;

import java.io.Serializable;

/**
 * Created by tanzhongyi on 2015/4/4.
 */
public class ShopListItem implements Serializable {
    private String shopName;
    private int shopImageId;
    private String shopAddr;
    private String shopPhone;
    private String shopRank;
    private String bidPrice;
    private String bidComment;
    private String distance;

    public ShopListItem(String shopName, int shopImageId, String shopAddr, String shopPhone,
                        String shopPrice, String shopComment,String shopRank,String theDistance) {
        this.shopName = shopName;
        this.shopImageId = shopImageId;
        this.shopAddr = shopAddr;
        this.shopPhone = shopPhone;
        this.bidPrice = shopPrice;
        this.bidComment = shopComment;
        this.shopRank = shopRank;
        this.distance = theDistance;
    }

    public String getDistance() {
        return distance;
    }
    public String getShopRank() {
        return shopRank;
    }
    public String getBidComment() {
        return bidComment;
    }
    public String getShopName() {
        return shopName;
    }
    public String getShopAddr() {
        return shopAddr;
    }
    public String getShopPhone() {
        return shopPhone;
    }
    public String getBidPrice() {
        return bidPrice;
    }
    public int getShopImageId() {
        return shopImageId;
    }

}
