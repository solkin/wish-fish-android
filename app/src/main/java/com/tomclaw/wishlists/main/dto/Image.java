package com.tomclaw.wishlists.main.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by solkin on 27.02.17.
 */

public class Image implements Parcelable {

    private String url;
    private Size size;

    public Image(String url, Size size) {
        this.url = url;
        this.size = size;
    }

    protected Image(Parcel in) {
        url = in.readString();
        size = in.readParcelable(Size.class.getClassLoader());
    }

    public String getUrl() {
        return url;
    }

    public Size getSize() {
        return size;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeParcelable(size, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
