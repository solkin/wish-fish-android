package com.tomclaw.wishlists.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tomclaw.wishlists.main.adapter.holder.CountryHolder;
import com.tomclaw.wishlists.main.adapter.view.CountryView;
import com.tomclaw.wishlists.main.adapter.view.CountryView_;
import com.tomclaw.wishlists.util.Country;
import com.tomclaw.wishlists.util.RecyclerViewFastScroller.FastScrollAdapter;

import java.util.List;

/**
 * Created by solkin on 10.03.17.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryHolder> implements FastScrollAdapter {

    private
    @NonNull
    Context context;
    private
    @NonNull
    final List<Country> countries;
    private
    @Nullable
    CountryClickListener listener;

    public CountryAdapter(@NonNull Context context, @NonNull List<Country> countries) {
        this.context = context;
        this.countries = countries;
    }

    public void setListener(@Nullable CountryClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CountryView view = CountryView_.build(context);
        return new CountryHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryHolder holder, int position) {
        Country country = countries.get(position);
        holder.bind(country, listener);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public CharSequence getBubbleText(int position) {
        return countries.get(position).getAlphabetIndex();
    }

    public interface CountryClickListener {

        void onCountryClicked(Country country);

    }
}
