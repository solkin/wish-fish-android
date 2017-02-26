package com.tomclaw.wishlists.main.view;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tomclaw.wishlists.R;
import com.tomclaw.wishlists.main.adapter.WishesAdapter;
import com.tomclaw.wishlists.main.controller.WishesController;
import com.tomclaw.wishlists.main.dto.Image;
import com.tomclaw.wishlists.main.dto.Size;
import com.tomclaw.wishlists.main.dto.WishItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ivsolkin on 08.01.17.
 */
@EViewGroup(R.layout.wish_list_view)
public class WishesView extends MainView implements WishesController.WishListCallback {

    @ViewById
    ViewFlipper flipper;

    @ViewById
    RecyclerView recycler;

    @ViewById
    TextView errorText;

    @Bean
    WishesController controller;

    WishesAdapter adapter;

    public WishesView(Context context) {
        super(context);
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

        List<WishItem> items = Arrays.asList(
                new WishItem("Отличная сумка", new Image("http://snitsya-son.ru/wp-content/uploads/2015/06/56-потеря-сумки-во-сне.jpg", new Size(1280, 1080))),
                new WishItem("Надувная лодка", new Image("http://brakonyerov.net/upload/blogs/d6b7dfad6c854100e63bc88b3fc1aa0a.jpg", new Size(552, 400))),
                new WishItem("Мощный перфоратор для любимых соседей", null),
                new WishItem("Туфли-скороходы", new Image("http://region-gdo.ru/images/common/tip1/tufli_3.jpg", new Size(1200, 800))),
                new WishItem("Оранжевое платье", new Image("http://okean-opta.com/cache_files/images/kupit-Fabrika-Leda-platya-Plate-34Princessa34-naryadnye-platya-263-id8215.jpg", new Size(533, 800))),
                new WishItem("Фэтбайк", new Image("https://fatbiker.ru/img/fat-bike.jpg", new Size(665, 446))),
                new WishItem("Набор инструментов", null),
                new WishItem("Баночка мёда", new Image("http://img0.liveinternet.ru/images/attach/c/6/92/836/92836164_1WuHW_imDc.jpg", new Size(460, 460))),
                new WishItem("MacBook Pro 2016", new Image("https://cnet4.cbsistatic.com/img/LwGV_V7uTl2n_T-eg0db_6sEybA=/770x433/2016/10/27/e361002e-72c9-4b10-bfc1-2be21c508a42/apple-macbook-pro-13-inch-2016-1626-003.jpg", new Size(770, 433))),
                new WishItem("Мёд", null)
        );
        adapter.setItems(items);
        flipper.setDisplayedChild(1);

        recycler.setAdapter(adapter);
    }

    @Click(R.id.button_retry)
    void onRetryClicked() {
        load();
    }

    @Override
    public void onLoaded(List<WishItem> list) {
    }

    @Override
    public void onError() {
    }

    @Override
    public void onProgress() {
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

    private void load() {
        controller.load();
    }
}
