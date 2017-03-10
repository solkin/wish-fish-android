package com.tomclaw.wishlists.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by Solkin on 22.10.2014.
 */
public class Country implements Comparable<Country>, Parcelable {

    private String name;
    private int code;
    private String shortName;
    private String alphabetIndex;
    private String phoneFormat;

    public Country(String name, int code, String shortName, String phoneFormat) {
        Locale locale = new Locale("", shortName);
        this.name = locale.getDisplayCountry();
        // Check for county not found.
        if (TextUtils.equals(this.name, shortName)) {
            this.name = name;
        }
        this.code = code;
        this.shortName = shortName;
        this.alphabetIndex = String.valueOf(StringUtil.getAlphabetIndex(this.name));
        this.phoneFormat = phoneFormat;
    }

    protected Country(Parcel in) {
        name = in.readString();
        code = in.readInt();
        shortName = in.readString();
        alphabetIndex = in.readString();
        phoneFormat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(code);
        dest.writeString(shortName);
        dest.writeString(alphabetIndex);
        dest.writeString(phoneFormat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getShortName() {
        return shortName;
    }

    public String getAlphabetIndex() {
        return alphabetIndex;
    }

    public String getPhoneFormat() {
        return phoneFormat;
    }

    @Override
    public int compareTo(@NonNull Country another) {
        return name.compareTo(another.name);
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='+" + code + '\'' +
                '}';
    }

    public boolean contains(CharSequence constraint) {
        return toString().toLowerCase().contains(constraint.toString().toLowerCase());
    }
}
