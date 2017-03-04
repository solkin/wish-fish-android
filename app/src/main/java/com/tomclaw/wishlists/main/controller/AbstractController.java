package com.tomclaw.wishlists.main.controller;

import android.os.Bundle;

import com.tomclaw.wishlists.core.MainExecutor;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by ivsolkin on 11.01.17.
 */
public abstract class AbstractController<C extends AbstractController.ControllerCallback> {

    private Set<C> weakCallbacks = Collections.newSetFromMap(
            new WeakHashMap<C, Boolean>());

    public final void onAttach(final C callback) {
        MainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                weakCallbacks.add(callback);
                onAttached(callback);
            }
        });
    }

    abstract void onAttached(C callback);

    public void detachAll() {
        MainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (C callback : weakCallbacks) {
                    onDetach(callback);
                }
            }
        });
    }

    public final void onDetach(final C callback) {
        MainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (weakCallbacks.remove(callback)) {
                    onDetached(callback);
                }
            }
        });
    }

    abstract void onDetached(C callback);

    void operateCallbacks(final CallbackOperation<C> operation) {
        MainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (C callback : weakCallbacks) {
                    operation.invoke(callback);
                }
            }
        });
    }

    public abstract Bundle onSaveState();

    public abstract void onRestoreState(Bundle bundle);

    public interface CallbackOperation<C> {

        void invoke(C callback);
    }

    public interface ControllerCallback {
    }
}
