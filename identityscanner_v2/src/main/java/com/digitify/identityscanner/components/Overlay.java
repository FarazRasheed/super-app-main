package com.digitify.identityscanner.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Overlay extends View {
    Paint paint;
    int x1 = 0, x2 = 0, y1 = 0, y2 = 0;

    public Overlay(Context context) {
        super(context);
        init();
    }

    public Overlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Overlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        canvas.drawRect(x1, y1, x2, y2, paint);
    }

    public void drawRect(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        invalidate();
    }

    public void clear() {
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
        invalidate();
    }

}
