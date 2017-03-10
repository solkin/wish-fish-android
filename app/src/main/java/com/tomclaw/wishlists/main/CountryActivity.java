package com.tomclaw.wishlists.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tomclaw.wishlists.R;
import com.tomclaw.wishlists.main.adapter.CountryAdapter;
import com.tomclaw.wishlists.util.CountriesProvider;
import com.tomclaw.wishlists.util.Country;
import com.tomclaw.wishlists.util.RecyclerViewFastScroller;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by solkin on 10.03.17.
 */
@EActivity(R.layout.activity_country)
public class CountryActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView countriesView;

    @ViewById
    RecyclerViewFastScroller fastScroller;

    @Bean
    CountriesProvider countriesProvider;

    private CountryAdapter adapter;

    @AfterViews
    void init() {
        List<Country> countries = countriesProvider.getCountries();
        adapter = new CountryAdapter(this, countries);
        countriesView.setAdapter(adapter);
        countriesView.setHasFixedSize(true);
        countriesView.setLayoutManager(new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fastScroller.setRecyclerView(countriesView);
        fastScroller.setViewsToUse(R.layout.fast_scroller, R.id.fast_scroller_bubble, R.id.fast_scroller_handle);
    }

    private class CustomLinearLayoutManager extends LinearLayoutManager {

        private CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
            super.onLayoutChildren(recycler, state);
            //TODO if the items are filtered, considered hiding the fast scroller here
            final int firstVisibleItemPosition = findFirstVisibleItemPosition();
            if (firstVisibleItemPosition != 0) {
                // this avoids trying to handle un-needed calls
                if (firstVisibleItemPosition == -1)
                    //not initialized, or no items shown, so hide fast-scroller
                    fastScroller.setVisibility(View.GONE);
                return;
            }
            final int lastVisibleItemPosition = findLastVisibleItemPosition();
            int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
            //if all items are shown, hide the fast-scroller
            fastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
        }
    }
}
