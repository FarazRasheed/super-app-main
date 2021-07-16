package co.yap.widgets.pieview;

import android.annotation.SuppressLint;

/**
 * @author Mirza Adil
 */
@SuppressLint("ParcelCreator")
public class RadarEntry extends Entry {

    public RadarEntry(float value) {
        super(0f, value);
    }

    public RadarEntry(float value, Object data) {
        super(0f, value, data);
    }

    /**
     * This is the same as getY(). Returns the value of the RadarEntry.
     *
     * @return
     */
    public float getValue() {
        return getY();
    }

    public RadarEntry copy() {
        RadarEntry e = new RadarEntry(getY(), getData());
        return e;
    }

    @Deprecated
    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Deprecated
    @Override
    public float getX() {
        return super.getX();
    }
}
