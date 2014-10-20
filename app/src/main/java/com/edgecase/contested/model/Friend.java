package com.edgecase.contested.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable {
    private String friendName;
    private String friendImage;

    public Friend() {

    }

    public Friend(String friendName) {
        this.friendName = friendName;
    }

    public Friend(String friendName, String friendImage) {
        this.friendName = friendName;
        this.friendImage = friendImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.friendName);
        out.writeString(this.friendImage);
    }

    private Friend (Parcel in) {
        this.friendName = in.readString();
        this.friendImage = in.readString();
    }

    public String getFriendName() {
        return this.friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendImage() {
        return this.friendImage;
    }

    public void setFriendImage(String friendImage) {
        this.friendImage = friendImage;
    }
}