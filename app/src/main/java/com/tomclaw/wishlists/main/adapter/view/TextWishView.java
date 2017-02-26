package com.tomclaw.wishlists.main.adapter.view;

import android.content.Context;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tomclaw.wishlists.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by solkin on 26.02.17.
 */
@EViewGroup(R.layout.text_wish_view)
public class TextWishView extends FrameLayout {

    @ViewById
    TextView titleView;

    public TextWishView(Context context) {
        super(context);
    }

    public void setTitle(String title) {
        int titleLength = title.length();
        int multiplier = titleLength / 5;
        float textSize;
        switch (multiplier) {
            case 0:
            case 1:
                textSize = getResources().getDimension(R.dimen.extra_short_text);
                break;
            case 2:
                textSize = getResources().getDimension(R.dimen.short_text);
                break;
            case 3:
                textSize = getResources().getDimension(R.dimen.medium_text);
                break;
            default:
                textSize = getResources().getDimension(R.dimen.long_text);
                break;
        }
        titleView.setText(title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}
