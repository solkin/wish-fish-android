package com.tomclaw.wishlists.main.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by solkin on 26.02.17.
 */
public class WishItem implements Parcelable {

    private String title;
    private String description;
    private String url;
    private String barcode;
    private Image image;
    private long time;

    public WishItem(String title) {
        this.title = title;
        this.description = null;
        this.url = null;
        this.barcode = null;
        this.image = null;
        this.time = 0;
    }

    public WishItem(String title, Image image) {
        this.title = title;
        this.description = null;
        this.url = null;
        this.barcode = null;
        this.image = image;
        this.time = 0;
    }

    protected WishItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        url = in.readString();
        barcode = in.readString();
        image = in.readParcelable(Image.class.getClassLoader());
        time = in.readLong();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getBarcode() {
        return barcode;
    }

    public Image getImage() {
        return image;
    }

    public long getTime() {
        return time;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(barcode);
        dest.writeParcelable(image, flags);
        dest.writeLong(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WishItem> CREATOR = new Creator<WishItem>() {
        @Override
        public WishItem createFromParcel(Parcel in) {
            return new WishItem(in);
        }

        @Override
        public WishItem[] newArray(int size) {
            return new WishItem[size];
        }
    };
}
