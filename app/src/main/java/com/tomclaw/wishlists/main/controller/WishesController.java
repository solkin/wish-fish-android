package com.tomclaw.wishlists.main.controller;

import android.os.Bundle;

import com.tomclaw.wishlists.core.ApiInterface;
import com.tomclaw.wishlists.core.ApiProvider;
import com.tomclaw.wishlists.main.dto.WishItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by solkin on 26.02.17.
 */
@EBean
public class WishesController extends AbstractController<WishesController.WishListCallback> {

    private static final String KEY_WISH_ITEMS = "wish_items";
    private static final java.lang.String KEY_IS_ERROR = "is_error";
    private static final java.lang.String KEY_IS_STARTED = "is_started";

    @Bean
    ApiProvider apiProvider;

    ApiInterface api;

    private List<WishItem> wishItems;
    private boolean isError = false;
    private boolean isStarted = true;

    @AfterInject
    void init() {
        api = apiProvider.getApi();
    }

    @Override
    void onAttached(WishListCallback callback) {
        if (isLoaded()) {
            callback.onLoaded(wishItems);
        } else if (isError) {
            callback.onError();
        } else {
            callback.onProgress();
        }
    }

    public boolean isLoaded() {
        return wishItems != null;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void load() {
        wishItems = null;
        isError = false;
        onProgress();
        api.getWishList("user_id").enqueue(new Callback<List<WishItem>>() {
            @Override
            public void onResponse(Call<List<WishItem>> call, Response<List<WishItem>> response) {
                onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<WishItem>> call, Throwable t) {
                onError();
            }
        });
    }

    @Override
    void onDetached(WishListCallback callback) {
    }

    @Override
    public Bundle onSaveState() {
        ArrayList<WishItem> items = new ArrayList<>(wishItems);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_WISH_ITEMS, items);
        bundle.putBoolean(KEY_IS_ERROR, isError);
        bundle.putBoolean(KEY_IS_STARTED, isStarted);
        return bundle;
    }

    @Override
    public void onRestoreState(Bundle bundle) {
        if (bundle != null) {
            wishItems = bundle.getParcelableArrayList(KEY_WISH_ITEMS);
            isError = bundle.getBoolean(KEY_IS_ERROR, isError);
            isStarted = bundle.getBoolean(KEY_IS_STARTED, isStarted);
        }
        if (isStarted) {
            load();
        }
    }

    private void onProgress() {
        isError = false;
        isStarted = true;
        operateCallbacks(new CallbackOperation<WishListCallback>() {
            @Override
            public void invoke(WishListCallback callback) {
                callback.onProgress();
            }
        });
    }

    private void onLoaded(final List<WishItem> list) {
        wishItems = list;
        isError = false;
        isStarted = false;
        operateCallbacks(new CallbackOperation<WishListCallback>() {
            @Override
            public void invoke(WishListCallback callback) {
                callback.onLoaded(list);
            }
        });
    }

    private void onError() {
        isError = true;
        isStarted = false;
        operateCallbacks(new CallbackOperation<WishListCallback>() {
            @Override
            public void invoke(WishListCallback callback) {
                callback.onError();
            }
        });
    }

    public interface WishListCallback extends AbstractController.ControllerCallback {

        void onLoaded(List<WishItem> list);

        void onError();

        void onProgress();
    }
}
