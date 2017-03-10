package com.tomclaw.wishlists.main.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tomclaw.wishlists.main.adapter.view.CountryView;
import com.tomclaw.wishlists.util.Country;

/**
 * Created by solkin on 10.03.17.
 */

public class CountryHolder extends RecyclerView.ViewHolder {

    private CountryView countryView;

    public CountryHolder(CountryView itemView) {
        super(itemView);
        countryView = itemView;
    }

    public void bind(Country country) {
        countryView.setCountryName(country.getName());
        countryView.setCountryCode(country.getCode());
    }
}
