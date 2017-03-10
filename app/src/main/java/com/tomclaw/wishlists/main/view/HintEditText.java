package com.tomclaw.wishlists.main.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by solkin on 10.03.17.
 */
public class HintEditText extends AppCompatEditText {

    private String hintText;
    private float textOffset;
    private float spaceSize;
    private float numberSize;
    private Paint paint = new Paint();
    private Rect rect = new Rect();

    public HintEditText(Context context) {
        super(context);
        paint.setColor(0xff979797);
    }

    public HintEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(0xff979797);
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String value) {
        hintText = value;
        onTextChange();
        setText(getText());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onTextChange();
    }

    public void onTextChange() {
        textOffset = (length() > 0 ? getPaint().measureText(getText(), 0, length()) : 0);
        spaceSize = getPaint().measureText(" ");
        numberSize = getPaint().measureText("1");
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (hintText != null && length() < hintText.length()) {
            int top = getMeasuredHeight() / 2;
            float offsetX = textOffset;
            for (int a = length(); a < hintText.length(); a++) {
                if (hintText.charAt(a) == ' ') {
                    offsetX += spaceSize;
                } else {
                    rect.set((int) (offsetX + dp(1)), top, (int) (offsetX + numberSize - dp(1)), (int) (top + dp(2)));
                    canvas.drawRect(rect, paint);
                    offsetX += numberSize;
                }
            }
        }
    }

    public static float dp(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
