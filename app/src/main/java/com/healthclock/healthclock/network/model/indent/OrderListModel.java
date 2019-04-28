package com.healthclock.healthclock.network.model.indent;

import java.util.List;

public class OrderListModel {

    /**
     * total : 5
     * list : [{"createTime":1556378248000,"buyerRate":"1","orderInfo":{"commodityId":"1","description":"1","id":0,"num":1,"orderId":"1","picPath":"","price":"1.1","remark":"","shippingWay":"","title":"","totalFee":"1.1"},"payment":"1556378248443699","id":1,"userId":6,"status":0},{"createTime":1556423790000,"orderInfo":{"commodityId":"1","description":"1","id":0,"num":1,"orderId":"2","picPath":"","price":"1","remark":"","shippingWay":"","title":"","totalFee":"1"},"payment":"1556423790214650","id":2,"userId":6,"status":1},{"createTime":1556423804000,"orderInfo":{"commodityId":"1","description":"1","id":0,"num":1,"orderId":"3","picPath":"","price":"5","remark":"","shippingWay":"","title":"","totalFee":"5"},"payment":"1556423804103103","id":3,"userId":6,"status":2},{"createTime":1556423822000,"orderInfo":{"commodityId":"1","description":"1","id":0,"num":1,"orderId":"4","picPath":"","price":"10","remark":"","shippingWay":"","title":"","totalFee":"10"},"payment":"1556423821870440","id":4,"userId":6,"status":3},{"createTime":1556423840000,"orderInfo":{"commodityId":"1","description":"1","id":0,"num":1,"orderId":"5","picPath":"","price":"25","remark":"","shippingWay":"","title":"","totalFee":"25"},"payment":"1556423839918450","id":5,"userId":6,"status":4}]
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
         * createTime : 1556378248000
         * buyerRate : 1
         * orderInfo : {"commodityId":"1","description":"1","id":0,"num":1,"orderId":"1","picPath":"","price":"1.1","remark":"","shippingWay":"","title":"","totalFee":"1.1"}
         * payment : 1556378248443699
         * id : 1
         * userId : 6
         * status : 0
         */

        private long createTime;
        private String buyerRate;
        private OrderInfoBean orderInfo;
        private String payment;
        private int id;
        private int userId;
        private int status;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getBuyerRate() {
            return buyerRate;
        }

        public void setBuyerRate(String buyerRate) {
            this.buyerRate = buyerRate;
        }

        public OrderInfoBean getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(OrderInfoBean orderInfo) {
            this.orderInfo = orderInfo;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class OrderInfoBean {
            /**
             * commodityId : 1
             * description : 1
             * id : 0
             * num : 1
             * orderId : 1
             * picPath :
             * price : 1.1
             * remark :
             * shippingWay :
             * title :
             * totalFee : 1.1
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
    }
}
