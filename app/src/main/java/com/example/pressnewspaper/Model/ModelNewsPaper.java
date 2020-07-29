package com.example.pressnewspaper.Model;

public class ModelNewsPaper {
    private String newPaperId;
    private String newPaperName;
    private String newPaperType;
    private String releaseType;
    private String releaseTime;
    private String img;

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
