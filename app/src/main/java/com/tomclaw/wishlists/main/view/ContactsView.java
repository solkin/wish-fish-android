package com.tomclaw.wishlists.main.view;

import android.content.Context;
import android.os.Bundle;

import org.androidannotations.annotations.EViewGroup;

/**
 * Created by ivsolkin on 08.01.17.
 */
@EViewGroup
public class ContactsView extends MainView {

    public ContactsView(Context context, Bundle state) {
        super(context, state);
    }

    @Override
    void activate() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void refresh() {
    }

    @Override
    public Bundle onSaveState() {
        return null;
    }
}
