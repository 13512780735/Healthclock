package com.healthclock.healthclock.network.model.indent;

public class CreateOrderModel {

    /**
     * commodityId : 1
     * description : 1
     * id : 7
     * num : 2
     * orderId : 7
     * picPath : 20
     * price : 5
     * remark :
     * shippingWay :
     * title :
     * totalFee : 5
     */

    private String commodityId;
    private String description;
    private int id;
    private int num;
    private String orderId;
    private String picPath;
    private String price;
    private String remark;
    private String shippingWay;
    private String title;
    private String totalFee;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShippingWay() {
        return shippingWay;
    }

    public void setShippingWay(String shippingWay) {
        this.shippingWay = shippingWay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }
}
