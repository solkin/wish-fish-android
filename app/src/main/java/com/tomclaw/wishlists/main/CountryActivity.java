package com.tomclaw.wishlists.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tomclaw.wishlists.R;
import com.tomclaw.wishlists.main.adapter.CountryAdapter;
import com.tomclaw.wishlists.util.CountriesProvider;
import com.tomclaw.wishlists.util.Country;
import com.tomclaw.wishlists.util.RecyclerViewFastScroller;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by solkin on 10.03.17.
 */
@EActivity(R.layout.activity_country)
public class CountryActivity extends AppCompatActivity implements CountryAdapter.CountryClickListener {

    public static final String EXTRA_COUNTRY = "country";

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView countriesView;

    @ViewById
    RecyclerViewFastScroller fastScroller;

    @Bean
    CountriesProvider countriesProvider;

    @AfterViews
    void init() {
        final List<Country> countries = countriesProvider.getCountries();
        CountryAdapter adapter = new CountryAdapter(this, countries);
        adapter.setListener(this);
        countriesView.setAdapter(adapter);
        countriesView.setHasFixedSize(true);
        countriesView.setLayoutManager(new LinearLayoutManager(this));
        fastScroller.setRecyclerView(countriesView);
    }

    @AfterViews
    void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle(R.string.choose_country);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OptionsItem(android.R.id.home)
    boolean actionHome() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCountryClicked(Country country) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_COUNTRY, country);
        setResult(RESULT_OK, intent);
        finish();
    }
}
