package com.edgecase.contested.model;

public class Contest {
    private String contestName;
    private String userOne;
    private String userTwo;
    private String thumbnailUrl;
    private String thumbnailUrlTwo;


    public Contest() {
    }

    public Contest(String contestName, String thumbnailUrl, String thumbnailUrlTwo, String userOne, String userTwo) {
        this.contestName = contestName;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailUrlTwo = thumbnailUrlTwo;
        this.userOne = userOne;
        this.userTwo = userTwo;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getUserOne() {
        return userOne;
    }

    public void setUserOne(String userOne) {
        this.userOne = userOne;
    }

    public String getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(String userTwo) {
        this.userTwo = userTwo;
    }

    public String getThumbnail() {
        return thumbnailUrl;
    }

    public void setThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailTwo() {
        return thumbnailUrlTwo;
    }

    public void setThumbnailTwo(String thumbnailUrlTwo) {
        this.thumbnailUrlTwo = thumbnailUrlTwo;
    }
}