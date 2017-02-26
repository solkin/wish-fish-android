package com.tomclaw.wishlists.main.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tomclaw.wishlists.main.dto.WishItem;

/**
 * Created by solkin on 27.02.17.
 */

public abstract class AbstractWishHolder extends RecyclerView.ViewHolder {

    public AbstractWishHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(WishItem item);
}
