package com.digitify.identityscanner.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.digitify.identityscanner.R;

public class TransparentCardView extends View {

    private final static String TAG = "TransparentCardView";

    public static final float ID_CARD_RATIO = 1.586f;
    public static final float PASSPORT_RATIO = 1.432f;

    private float cardRatio = ID_CARD_RATIO;

    private int cardWidth = 0;
    private int cardHeight = 0;
    private int mainWidth = 0;
    private int mainHeight = 0;
    private int cardTop = -1;
    private int cardBottom = 0;
    private int cardLeft = 0;
    private int cardRight = 0;
    private int cardCorners = 8;
    private int cardMarginHorizontal = 10;
    private int cardBorderWidth = 1;

    private int backgroundColor;
    private int cardBorderColor;
    private int cardColor;
    private int cardBorderDashSpan = 20;
    private boolean cardBorderDashed = false;

    //Flag for checking whether view is drawn or not.
    public MutableLiveData<Boolean> isDrawn = new MutableLiveData<>(false);

    private OnLayoutListener layoutListener;

    public TransparentCardView(Context context) {
        super(context);
    }

    public TransparentCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TransparentCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TransparentCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TransparentCardView);
        backgroundColor = a.getColor(R.styleable.TransparentCardView_backgroundColor, getResources().getColor(R.color.semi_dark));
        cardColor = a.getColor(R.styleable.TransparentCardView_cardColor, getResources().getColor(R.color.transparent));
        cardBorderColor = a.getColor(R.styleable.TransparentCardView_cardBorderColor, getResources().getColor(R.color.white));
        cardBorderWidth = (int) a.getDimension(R.styleable.TransparentCardView_cardBorderWidth, cardBorderWidth);
        cardBorderDashSpan = (int) a.getDimension(R.styleable.TransparentCardView_cardBorderDashSpan, cardBorderDashSpan);
        cardBorderDashed = a.getBoolean(R.styleable.TransparentCardView_cardBorderDashed, cardBorderDashed);
        cardCorners = (int) a.getDimension(R.styleable.TransparentCardView_cardCorners, cardCorners);
        cardTop = (int) a.getDimension(R.styleable.TransparentCardView_cardMarginTop, cardTop);
        cardMarginHorizontal = (int) a.getDimension(R.styleable.TransparentCardView_cardMarginHorizontal, cardMarginHorizontal);
        a.recycle();
    }

    /**
     * Calculates required parameters for TransparentCardView creation
     */
    private void defaultAttributes() {
        mainWidth = getWidth();
        mainHeight = getHeight();
        cardWidth = mainWidth - (cardMarginHorizontal * 2);
        cardHeight = Math.round(cardWidth / getCardRatio());

        if (cardTop < 0) cardTop = (getHeight() / 2) - (cardHeight / 2); // center
        cardLeft = cardMarginHorizontal;
        cardBottom = cardTop + cardHeight;
        cardRight = cardLeft + cardWidth;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        defaultAttributes();
        if (isDrawn.getValue() != null) {
            if (this.layoutListener != null && !isDrawn.getValue())
                this.layoutListener.onLayout();
        }
        isDrawn.setValue(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawn.getValue() != null) {
            if (!isDrawn.getValue()) defaultAttributes();
        }
        isDrawn.setValue(true);
        Bitmap bitmap = bitmapDraw();
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * Creates a bitmap with transparent card.
     *
     * @return
     */
    private Bitmap bitmapDraw() {
        Bitmap bitmap = Bitmap.createBitmap(getMainWidth(), getMainHeight(), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);

        // Fill the canvas with background first
        Canvas canvasBitmap = new Canvas(bitmap);
        canvasBitmap.drawColor(backgroundColor);


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Draw card rect
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // adds color over background
        paint.setColor(cardColor);
        RectF cardRectangle = new RectF(cardLeft, cardTop, cardRight, cardBottom);
        canvasBitmap.drawRoundRect(cardRectangle, cardCorners, cardCorners, paint);


        // Draw border of card
        paint.setColor(cardBorderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(cardBorderWidth);
        if (cardBorderDashed) {
            paint.setPathEffect(new DashPathEffect(new float[]{cardBorderDashSpan, cardBorderDashSpan}, 0));
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // adds color over background
        canvasBitmap.drawRoundRect(cardRectangle, cardCorners, cardCorners, paint);

        return bitmap;
    }


    public int getCardWidth() {
        return cardWidth;
    }

    public void setCardWidth(int cardWidth) {
        this.cardWidth = cardWidth;
    }

    public int getCardHeight() {
        return cardHeight;
    }

    public void setCardHeight(int cardHeight) {
        this.cardHeight = cardHeight;
        invalidate();
    }

    public int getMainWidth() {
        return mainWidth;
    }

    public void setMainWidth(int mainWidth) {
        this.mainWidth = mainWidth;
    }

    public int getMainHeight() {
        return mainHeight;
    }

    public void setMainHeight(int mainHeight) {
        this.mainHeight = mainHeight;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getCardTop() {
        return cardTop;
    }

    public void setCardTop(int cardTop) {
        this.cardTop = cardTop;
    }

    public int getCardBottom() {
        return cardBottom;
    }

    public void setCardBottom(int cardBottom) {
        this.cardBottom = cardBottom;
    }

    public int getCardLeft() {
        return cardLeft;
    }

    public void setCardLeft(int cardLeft) {
        this.cardLeft = cardLeft;
    }

    public int getCardRight() {
        return cardRight;
    }

    public void setCardRight(int cardRight) {
        this.cardRight = cardRight;
    }

    public int getCardCorners() {
        return cardCorners;
    }

    public void setCardCorners(int cardCorners) {
        this.cardCorners = cardCorners;
    }

    public int getCardBorderWidth() {
        return cardBorderWidth;
    }

    public void setCardBorderWidth(int cardBorderWidth) {
        this.cardBorderWidth = cardBorderWidth;
    }

    public int getCardBorderColor() {
        return cardBorderColor;
    }

    public void setCardBorderColor(int cardBorderColor) {
        this.cardBorderColor = cardBorderColor;
        invalidate();
    }

    public Rect getCardRect() {
        return new Rect(getCardLeft(), getCardTop(), getCardRight(), getCardBottom());
    }

    public float getCardRatio() {
        return cardRatio;
    }

    public void setCardRatio(float cardRatio) {
        this.cardRatio = cardRatio;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }

    public void setOnLayoutListener(OnLayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    /**
     * Listener for notifying view layout is done.
     */
    public interface OnLayoutListener {
        void onLayout();
    }
}

