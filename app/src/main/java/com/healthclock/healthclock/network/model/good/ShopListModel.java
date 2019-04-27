package com.healthclock.healthclock.network.model.good;

import java.util.List;

public class ShopListModel {

    /**
     * total : 1
     * list : [{"barcode":"","categoryId":0,"content":"","cost":"","createTime":null,"description":"","discount":"","id":1,"keywords":"","number":0,"pic":"","price":"","proNo":"","remark":"","status":0,"subTitle":"","thumb":"","title":"","titleDesc":"","updateTime":null,"url":""}]
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * barcode :
         * categoryId : 0
         * content :
         * cost :
         * createTime : null
         * description :
         * discount :
         * id : 1
         * keywords :
         * number : 0
         * pic :
         * price :
         * proNo :
         * remark :
         * status : 0
         * subTitle :
         * thumb :
         * title :
         * titleDesc :
         * updateTime : null
         * url :
         */

        private String barcode;
        private int categoryId;
        private String content;
        private String cost;
        private Object createTime;
        private String description;
        private String discount;
        private int id;
        private String keywords;
        private int number;
        private String pic;
        private String price;
        private String proNo;
        private String remark;
        private int status;
        private String subTitle;
        private String thumb;
        private String title;
        private String titleDesc;
        private Object updateTime;
        private String url;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getProNo() {
            return proNo;
        }

        public void setProNo(String proNo) {
            this.proNo = proNo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleDesc() {
            return titleDesc;
        }

        public void setTitleDesc(String titleDesc) {
            this.titleDesc = titleDesc;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
