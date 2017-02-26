package com.tomclaw.wishlists.main.adapter.holder;

import com.tomclaw.wishlists.main.adapter.view.ImageWishView;
import com.tomclaw.wishlists.main.dto.WishItem;

/**
 * Created by solkin on 26.02.17.
 */
public class ImageWishHolder extends AbstractWishHolder {

    private ImageWishView view;

    public ImageWishHolder(ImageWishView itemView) {
        super(itemView);
        view = itemView;
    }

    @Override
    public void bind(WishItem item) {
        view.showImage(item.getImage());
        view.setTitle(item.getTitle());
    }
}
