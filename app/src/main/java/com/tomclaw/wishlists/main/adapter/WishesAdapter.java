package com.tomclaw.wishlists.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tomclaw.wishlists.core.exception.UnsupportedItemTypeException;
import com.tomclaw.wishlists.main.adapter.holder.AbstractWishHolder;
import com.tomclaw.wishlists.main.adapter.holder.ImageWishHolder;
import com.tomclaw.wishlists.main.adapter.holder.TextWishHolder;
import com.tomclaw.wishlists.main.adapter.view.ImageWishView;
import com.tomclaw.wishlists.main.adapter.view.ImageWishView_;
import com.tomclaw.wishlists.main.adapter.view.TextWishView;
import com.tomclaw.wishlists.main.adapter.view.TextWishView_;
import com.tomclaw.wishlists.main.dto.WishItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by solkin on 26.02.17.
 */
public class WishesAdapter extends RecyclerView.Adapter<AbstractWishHolder> {

    private static final int IMAGE_WISH_ITEM = 0x01;
    private static final int TEXT_WISH_ITEM = 0x02;

    private Context context;
    private final List<WishItem> items;

    public WishesAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @Override
    public AbstractWishHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case IMAGE_WISH_ITEM:
                ImageWishView imageWishView = ImageWishView_.build(context);
                return new ImageWishHolder(imageWishView);
            case TEXT_WISH_ITEM:
                TextWishView textWishView = TextWishView_.build(context);
                return new TextWishHolder(textWishView);
            default:
                throw new UnsupportedItemTypeException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        WishItem item = items.get(position);
        if (item.getImage() != null) {
            return IMAGE_WISH_ITEM;
        } else {
            return TEXT_WISH_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(AbstractWishHolder holder, int position) {
        WishItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<WishItem> items) {
        this.items.clear();
        this.items.addAll(items);
    }
}
