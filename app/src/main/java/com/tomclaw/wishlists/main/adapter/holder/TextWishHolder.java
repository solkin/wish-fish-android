package com.tomclaw.wishlists.main.adapter.holder;

import com.tomclaw.wishlists.main.adapter.view.TextWishView;
import com.tomclaw.wishlists.main.dto.WishItem;

/**
 * Created by solkin on 27.02.17.
 */
public class TextWishHolder extends AbstractWishHolder {

    private TextWishView view;

    public TextWishHolder(TextWishView itemView) {
        super(itemView);
        view = itemView;
    }

    @Override
    public void bind(WishItem item) {
        view.setTitle(item.getTitle());
    }
}
