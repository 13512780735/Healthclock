package com.healthclock.healthclock.network.model.other;

import java.util.List;

public class VideoModel {

    /**
     * total : 1
     * list : [{"allowComment":true,"content":"","description":"","duration":"","fileSize":"","id":1,"inputtime":null,"islink":false,"keywords":"1","listorder":0,"relation":"1","remark":"","status":1,"style":"1","thumb":"1","title":"1","typeid":1,"updateTime":null,"uploadId":1,"url":"1","username":"1","video":"1","videoCategory":"1","vision":"1"}]
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
         * allowComment : true
         * content :
         * description :
         * duration :
         * fileSize :
         * id : 1
         * inputtime : null
         * islink : false
         * keywords : 1
         * listorder : 0
         * relation : 1
         * remark :
         * status : 1
         * style : 1
         * thumb : 1
         * title : 1
         * typeid : 1
         * updateTime : null
         * uploadId : 1
         * url : 1
         * username : 1
         * video : 1
         * videoCategory : 1
         * vision : 1
         */

        private boolean allowComment;
        private String content;
        private String description;
        private String duration;
        private String fileSize;
        private int id;
        private Object inputtime;
        private boolean islink;
        private String keywords;
        private int listorder;
        private String relation;
        private String remark;
        private int status;
        private String style;
        private String thumb;
        private String title;
        private int typeid;
        private Object updateTime;
        private int uploadId;
        private String url;
        private String username;
        private String video;
        private String videoCategory;
        private String vision;

        public boolean isAllowComment() {
            return allowComment;
        }

        public void setAllowComment(boolean allowComment) {
            this.allowComment = allowComment;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getInputtime() {
            return inputtime;
        }

        public void setInputtime(Object inputtime) {
            this.inputtime = inputtime;
        }

        public boolean isIslink() {
            return islink;
        }

        public void setIslink(boolean islink) {
            this.islink = islink;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public int getListorder() {
            return listorder;
        }

        public void setListorder(int listorder) {
            this.listorder = listorder;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
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

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
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

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public int getUploadId() {
            return uploadId;
        }

        public void setUploadId(int uploadId) {
            this.uploadId = uploadId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getVideoCategory() {
            return videoCategory;
        }

        public void setVideoCategory(String videoCategory) {
            this.videoCategory = videoCategory;
        }

        public String getVision() {
            return vision;
        }

        public void setVision(String vision) {
            this.vision = vision;
        }
    }
}
