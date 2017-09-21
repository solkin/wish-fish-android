package com.tomclaw.wishlists.main.adapter.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomclaw.wishlists.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by solkin on 10.03.17.
 */
@EViewGroup(R.layout.country_view)
public class CountryView extends RelativeLayout {

    @ViewById
    TextView countryName;

    @ViewById
    TextView countryCode;

    public CountryView(Context context) {
        super(context);
    }

    public void setCountryName(String name) {
        countryName.setText(name);
    }

    @SuppressLint("DefaultLocale")
    public void setCountryCode(int code) {
        countryCode.setText(String.format("+%d", code));
    }
}
