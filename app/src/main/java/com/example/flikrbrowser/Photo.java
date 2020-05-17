package com.example.flikrbrowser;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    private String mTitle;
    private String mAuthor;
    private String mAuthorID;
    private String mLink;
    private String mTags;
    private String mImage;

    public Photo() {
    }

    public Photo (String title, String author, String authorID, String link, String tags, String image) {
        mTitle = title;
        mAuthor = author;
        mAuthorID = authorID;
        mLink = link;
        mTags = tags;
        mImage = image;
    }

    protected Photo(Parcel in) {
        mTitle = in.readString();
        mAuthor = in.readString();
        mAuthorID = in.readString();
        mLink = in.readString();
        mTags = in.readString();
        mImage = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getAuthorID() {
        return mAuthorID;
    }

    public void setAuthorID(String authorID) {
        mAuthorID = authorID;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getTags() {
        return mTags;
    }

    public void setTags(String tags) {
        mTags = tags;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorID='" + mAuthorID + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
        dest.writeString(mAuthorID);
        dest.writeString(mLink);
        dest.writeString(mTags);
        dest.writeString(mImage);
    }
}
