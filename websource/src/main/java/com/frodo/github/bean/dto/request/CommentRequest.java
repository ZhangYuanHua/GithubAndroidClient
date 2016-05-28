package com.frodo.github.bean.dto.request;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentRequest implements Parcelable {

    public static final Creator<CommentRequest> CREATOR =
            new Creator<CommentRequest>() {
                public CommentRequest createFromParcel(Parcel source) {
                    return new CommentRequest(source);
                }

                public CommentRequest[] newArray(int size) {
                    return new CommentRequest[size];
                }
            };
    public String body;

    public CommentRequest(String body) {
        this.body = body;
    }

    protected CommentRequest(Parcel in) {
        this.body = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
    }
}
