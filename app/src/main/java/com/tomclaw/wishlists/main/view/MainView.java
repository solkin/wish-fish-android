package com.tomclaw.wishlists.main.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Created by ivsolkin on 08.01.17.
 */
public abstract class MainView extends FrameLayout {

    protected View view;
    private WeakReference<ActivityCallback> weakActivityCallback;

    public MainView(Context context) {
        super(context);
    }

    public final void activate(ActivityCallback activityCallback) {
        this.weakActivityCallback = new WeakReference<>(activityCallback);
        activate();
    }

    abstract void activate();

    public abstract void start();

    public abstract void stop();

    public abstract void destroy();

    public abstract void refresh();

    protected void startActivity(Intent intent) {
        ActivityCallback callback = weakActivityCallback.get();
        if (callback != null) {
            callback.startActivity(intent);
        }
    }

    public interface ActivityCallback {

        void startActivity(Intent intent);

    }
}
