package com.healthclock.healthclock.network.model.indent;

import java.util.List;

public class AddressModel {


    /**
     * total : 2
     * list : [{"address":"测试地址","city":"长沙","country":"","district":"长沙","id":1,"isdefault":false,"isvalid":true,"province":"湖南","remark":"","userId":6},{"address":"测试地址","city":"长沙","country":"","district":"株洲","id":2,"isdefault":true,"isvalid":true,"province":"湖南","remark":"","userId":6}]
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
         * address : 测试地址
         * city : 长沙
         * country :
         * district : 长沙
         * id : 1
         * isdefault : false
         * isvalid : true
         * province : 湖南
         * remark :
         * userId : 6
         */

        private String address;
        private String city;
        private String country;
        private String district;
        private int id;
        private boolean isdefault;
        private boolean isvalid;
        private String province;
        private String remark;
        private int userId;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsdefault() {
            return isdefault;
        }

        public void setIsdefault(boolean isdefault) {
            this.isdefault = isdefault;
        }

        public boolean isIsvalid() {
            return isvalid;
        }

        public void setIsvalid(boolean isvalid) {
            this.isvalid = isvalid;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
