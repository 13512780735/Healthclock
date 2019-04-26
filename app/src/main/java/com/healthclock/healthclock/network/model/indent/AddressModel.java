package com.healthclock.healthclock.network.model.indent;

import java.io.Serializable;
import java.util.List;

public class AddressModel {
    /**
     * total : 3
     * list : [{"id":"777","realname":"aaa","province":"北京","city":"石家庄市","area":"井陉矿区","address":"31231","mobile":"12312312123"},{"id":"758","realname":"李四","province":"广东","city":"中山","area":"东区","address":"测试1","mobile":"12345678978"},{"id":"778","realname":"111","province":"天津","city":"唐山市","area":"井陉矿区","address":"2222","mobile":"1111"}]
     */

    private String total;
    private List<ListBean> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * id : 777
         * realname : aaa
         * province : 北京
         * city : 石家庄市
         * area : 井陉矿区
         * address : 31231
         * mobile : 12312312123
         */
        public ListBean() {
        }

        private String id;
        private String realname;
        private String province;
        private String city;
        private String area;
        private String address;
        private String mobile;
        /**
         * 0 为被选中
         * 1 是默认选中的
         */
        private String isdefault = "0";

        public ListBean(String id, String realname, String province, String isdefault, String city, String area, String address, String mobile) {
            this.id = id;
            this.realname = realname;
            this.mobile = mobile;
            this.isdefault = isdefault;
            this.address = address;
            this.province = province;
            this.city = city;
            this.area = area;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", realname='" + realname + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", area='" + area + '\'' +
                    ", address='" + address + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", isdefault='" + isdefault + '\'' +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(String isdefault) {
            this.isdefault = isdefault;
        }
    }
}
