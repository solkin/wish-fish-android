package com.tomclaw.wishlists.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tomclaw.wishlists.main.adapter.holder.CountryHolder;
import com.tomclaw.wishlists.main.adapter.view.CountryView;
import com.tomclaw.wishlists.main.adapter.view.CountryView_;
import com.tomclaw.wishlists.util.Country;
import com.tomclaw.wishlists.util.RecyclerViewFastScroller.BubbleTextGetter;

import java.util.List;

/**
 * Created by solkin on 10.03.17.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryHolder> implements BubbleTextGetter {

    private Context context;
    private final List<Country> countries;

    public CountryAdapter(Context context, List<Country> countries) {
        this.context = context;
        this.countries = countries;
    }

    @Override
    public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CountryView view = CountryView_.build(context);
        return new CountryHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryHolder holder, int position) {
        Country country = countries.get(position);
        holder.bind(country);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public String getTextToShowInBubble(int position) {
        return countries.get(position).getAlphabetIndex();
    }
}
