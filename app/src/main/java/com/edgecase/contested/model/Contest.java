package com.edgecase.contested.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contest implements Parcelable {
    private String contestName;
    private String userOne;
    private String userTwo;
    private String thumbnailUrl;
    private String thumbnailUrlTwo;
    private String contestID;


    public Contest() {

    }


    public Contest(String contestName, String thumbnailUrl, String thumbnailUrlTwo, String userOne, String userTwo, String contestID) {
        this.contestName = contestName;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailUrlTwo = thumbnailUrlTwo;
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.contestID = contestID;
    }

    public Contest (Contest contest){
        this.contestName = contest.contestName;
        this.thumbnailUrl = contest.thumbnailUrl;
        this.thumbnailUrlTwo = contest.thumbnailUrlTwo;
        this.userOne = contest.userOne;
        this.userTwo = contest.userTwo;
        this.contestID = contest.contestID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(contestName);
        out.writeString(thumbnailUrl);
        out.writeString(thumbnailUrlTwo);
        out.writeString(userOne);
        out.writeString(userTwo);
        out.writeString(contestID);
    }

    private Contest (Parcel in) {
        contestName = in.readString();
        thumbnailUrl = in.readString();
        thumbnailUrlTwo = in.readString() ;
        userOne = in.readString();
        userTwo = in.readString();
        contestID = in.readString();
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getContestID() { return contestID; }

    public void setContestID(String contestID){ this.contestID = contestID;}

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