package com.example.pressnewspaper.Model;

public class ModelNewsPaper {
    private String newPaperId;
    private String newPaperName;
    private String newPaperType;
    private String releaseType;
    private String releaseTime;
    private String img;
    private String subscription_status;

    public String getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(String subscription_status) {
        this.subscription_status = subscription_status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNewPaperId() {
        return newPaperId;
    }

    public void setNewPaperId(String newPaperId) {
        this.newPaperId = newPaperId;
    }

    public String getNewPaperName() {
        return newPaperName;
    }

    public void setNewPaperName(String newPaperName) {
        this.newPaperName = newPaperName;
    }

    public String getNewPaperType() {
        return newPaperType;
    }

    public void setNewPaperType(String newPaperType) {
        this.newPaperType = newPaperType;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}
