package com.tomclaw.wishlists.main.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tomclaw.wishlists.R;
import com.tomclaw.wishlists.main.adapter.WishesAdapter;
import com.tomclaw.wishlists.main.controller.WishesController;
import com.tomclaw.wishlists.main.dto.WishItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by ivsolkin on 08.01.17.
 */
@EViewGroup(R.layout.wish_list_view)
public class WishesView extends MainView implements WishesController.WishListCallback {

    private static final String KEY_CONTROLLER = "controller";

    @ViewById
    ViewFlipper flipper;

    @ViewById
    RecyclerView recycler;

    @ViewById
    TextView errorText;

    @Bean
    WishesController controller;

    WishesAdapter adapter;

    private Bundle state;

    public WishesView(Context context, Bundle state) {
        super(context, state);
        this.state = state;
    }

    @AfterViews
    void init() {
        errorText.setText(R.string.wish_list_error);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recycler.setItemAnimator(itemAnimator);
        recycler.setHasFixedSize(true);

        adapter = new WishesAdapter(getContext());

        recycler.setAdapter(adapter);
    }

    @Click(R.id.button_retry)
    void onRetryClicked() {
        load();
    }

    @Override
    public void onLoaded(List<WishItem> list) {
        adapter.setItems(list);
        adapter.notifyDataSetChanged();
        flipper.setDisplayedChild(1);
    }

    @Override
    public void onError() {
        flipper.setDisplayedChild(2);
    }

    @Override
    public void onProgress() {
        flipper.setDisplayedChild(0);
    }

    @Override
    void activate() {
    }

    @Override
    public void start() {
        controller.onAttach(this);
    }

    @Override
    public void stop() {
        controller.onDetach(this);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void refresh() {
        load();
    }

    @Override
    public Bundle onSaveState() {
        Bundle bundle = new Bundle();
        bundle.putBundle(KEY_CONTROLLER, controller.onSaveState());
        return bundle;
    }

    @AfterInject
    void onRestoreState() {
        Bundle bundle = null;
        if (state != null) {
            bundle = state.getBundle(KEY_CONTROLLER);
        }
        controller.onRestoreState(bundle);
    }

    private void load() {
        controller.load();
    }
}
