package com.tomclaw.wishlists.main.adapter.view;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tomclaw.wishlists.R;
import com.tomclaw.wishlists.core.GlideApp;
import com.tomclaw.wishlists.main.dto.Image;
import com.tomclaw.wishlists.main.dto.Size;
import com.tomclaw.wishlists.main.view.AspectRatioImageView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by solkin on 26.02.17.
 */
@EViewGroup(R.layout.image_wish_view)
public class ImageWishView extends FrameLayout {

    @ViewById
    AspectRatioImageView imageView;

    @ViewById
    TextView titleView;

    public ImageWishView(Context context) {
        super(context);
    }

    public void showImage(Image image) {
        Size remoteImageSize = image.getSize();
        float aspectRatio = (float) remoteImageSize.getHeight() / (float) remoteImageSize.getWidth();
        imageView.setAspectRatio(aspectRatio);

        GlideApp.with(getContext())
                .load(image.getUrl())
                .placeholder(R.drawable.gift)
                .override(remoteImageSize.getWidth(), remoteImageSize.getHeight())
                .centerCrop()
                .into(imageView);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }
}
