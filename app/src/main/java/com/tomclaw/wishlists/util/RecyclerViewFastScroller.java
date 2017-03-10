package com.tomclaw.wishlists.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomclaw.wishlists.R;

public class RecyclerViewFastScroller extends LinearLayout {

    private static final int BUBBLE_ANIMATION_DURATION = 200;
    private static final int TRACK_SNAP_RANGE = 5;

    private
    @NonNull
    TextView bubble;
    private
    @NonNull
    View handle;
    private
    @Nullable
    RecyclerView recyclerView;

    private int height;
    private ObjectAnimator currentAnimator;

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            if (handle.isSelected()) return;
            final int offset = recyclerView.computeVerticalScrollOffset();
            final int range = recyclerView.computeVerticalScrollRange();
            final int extent = recyclerView.computeVerticalScrollExtent();
            final int offsetRange = Math.max(range - extent, 1);
            setBubbleAndHandlePosition((float) clamp(offset, 0, offsetRange) / offsetRange);
        }
    };

    public interface FastScrollAdapter {
        CharSequence getBubbleText(int position);
    }

    public RecyclerViewFastScroller(final Context context) {
        this(context, null);
    }

    public RecyclerViewFastScroller(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setClipChildren(false);
        setScrollContainer(true);
        inflate(context, R.layout.fast_scroller, this);
        bubble = findById(this, R.id.fast_scroller_bubble);
        handle = findById(this, R.id.fast_scroller_handle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
    }

    @Override
    @TargetApi(11)
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < getX(handle) - handle.getPaddingLeft() ||
                        event.getY() < getY(handle) - handle.getPaddingTop() ||
                        event.getY() > getY(handle) + handle.getHeight() + handle.getPaddingBottom()) {
                    return false;
                }
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }
                if (bubble.getVisibility() != VISIBLE) {
                    showBubble();
                }
                handle.setSelected(true);
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                setBubbleAndHandlePosition(y / height);
                setRecyclerViewPosition(y);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handle.setSelected(false);
                hideBubble();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setRecyclerView(final @NonNull RecyclerView recyclerView) {
        if (this.recyclerView != null) {
            this.recyclerView.removeOnScrollListener(onScrollListener);
        }
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (handle.isSelected()) return true;
                final int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
                final int verticalScrollRange = recyclerView.computeVerticalScrollRange();
                float proportion = (float) verticalScrollOffset / ((float) verticalScrollRange - height);
                setBubbleAndHandlePosition(height * proportion);
                return true;
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(onScrollListener);
        }
    }

    private void setRecyclerViewPosition(float y) {
        if (recyclerView != null) {
            final int itemCount = recyclerView.getAdapter().getItemCount();
            float proportion;
            if (getY(handle) == 0) {
                proportion = 0f;
            } else if (getY(handle) + handle.getHeight() >= height - TRACK_SNAP_RANGE) {
                proportion = 1f;
            } else {
                proportion = y / (float) height;
            }
            final int targetPos = clamp((int) (proportion * (float) itemCount), 0, itemCount - 1);
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(targetPos, 0);
            final CharSequence bubbleText = ((FastScrollAdapter) recyclerView.getAdapter()).getBubbleText(targetPos);
            bubble.setText(bubbleText);
        }
    }

    private void setBubbleAndHandlePosition(float y) {
        final int handleHeight = handle.getHeight();
        final int bubbleHeight = bubble.getHeight();
        final int handleY = clamp((int) ((height - handleHeight) * y), 0, height - handleHeight);
        setY(handle, handleY);
        setY(bubble, clamp(handleY - bubbleHeight - bubble.getPaddingBottom() + handleHeight,
                0,
                height - bubbleHeight));
    }

    @TargetApi(11)
    private void showBubble() {
        bubble.setVisibility(VISIBLE);
        if (currentAnimator != null) currentAnimator.cancel();
        currentAnimator = ObjectAnimator.ofFloat(bubble, "alpha", 0f, 1f).setDuration(BUBBLE_ANIMATION_DURATION);
        currentAnimator.start();
    }

    @TargetApi(11)
    private void hideBubble() {
        if (currentAnimator != null) currentAnimator.cancel();
        currentAnimator = ObjectAnimator.ofFloat(bubble, "alpha", 1f, 0f).setDuration(BUBBLE_ANIMATION_DURATION);
        currentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bubble.setVisibility(INVISIBLE);
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                bubble.setVisibility(INVISIBLE);
                currentAnimator = null;
            }
        });
        currentAnimator.start();
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static void setY(final @NonNull View v, final int y) {
        ViewCompat.setY(v, y);
    }

    public static float getX(final @NonNull View v) {
        return ViewCompat.getX(v);
    }

    public static float getY(final @NonNull View v) {
        return ViewCompat.getY(v);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findById(@NonNull View parent, @IdRes int resId) {
        return (T) parent.findViewById(resId);
    }
}
