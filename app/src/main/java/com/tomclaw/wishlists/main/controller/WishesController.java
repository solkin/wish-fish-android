package com.tomclaw.wishlists.main.controller;

import com.tomclaw.wishlists.core.MainExecutor;
import com.tomclaw.wishlists.main.dto.WishItem;

import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by solkin on 26.02.17.
 */
@EBean(scope = EBean.Scope.Singleton)
public class WishesController extends AbstractController<WishesController.WishListCallback> {

    private static final String API_URL = "http://appsend.store/api/list.php";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private List<WishItem> list;
    private boolean isError = false;

    private Future<?> future;

    @Override
    void onAttached(WishListCallback callback) {
        if (isLoaded()) {
            callback.onLoaded(list);
        } else if (isError) {
            callback.onError();
        } else {
            callback.onProgress();
        }
    }

    public boolean isLoaded() {
        return list != null;
    }

    public boolean isStarted() {
        return future != null;
    }

    public void load() {
        list = null;
        isError = false;
        future = executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    loadInternal();
                } catch (Throwable ignored) {
                    onError();
                }
            }
        });
    }

    @Override
    void onDetached(WishListCallback callback) {

    }

    private void onProgress() {
        MainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                operateCallbacks(new CallbackOperation<WishListCallback>() {
                    @Override
                    public void invoke(WishListCallback callback) {
                        callback.onProgress();
                    }
                });
            }
        });
    }

    private void onLoaded(final List<WishItem> list) {
        this.list = list;
        MainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                operateCallbacks(new CallbackOperation<WishListCallback>() {
                    @Override
                    public void invoke(WishListCallback callback) {
                        callback.onLoaded(list);
                    }
                });
            }
        });
    }

    private void onError() {
        this.isError = true;
        MainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                operateCallbacks(new CallbackOperation<WishListCallback>() {
                    @Override
                    public void invoke(WishListCallback callback) {
                        callback.onError();
                    }
                });
            }
        });
    }

    private void loadInternal() {
        onProgress();
    }

    public interface WishListCallback extends AbstractController.ControllerCallback {

        void onLoaded(List<WishItem> list);

        void onError();

        void onProgress();
    }
}
