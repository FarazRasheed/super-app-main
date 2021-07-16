package co.yap.modules.qrcode;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Helper class for encoding barcodes as a Bitmap.
 * <p>
 * Adapted from QRCodeEncoder, from the zxing project:
 * https://github.com/zxing/zxing
 * <p>
 * Licensed under the Apache License, Version 2.0.
 */
public class BarcodeEncoder {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private static final int YAP_DARK = 0xFF272262;
    private static final int YAP_LIGHT = 0xFF5E35B1;


    public BarcodeEncoder() {
    }

    public Bitmap createBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
//                    pixels[offset + x] = matrix.get(x, y) ? YAP_DARK : WHITE;
                if (matrix.get(x, y)) {
                    if (y == x && y == x / 2) {
                        pixels[offset + x] = YAP_LIGHT;
                    } else
                        pixels[offset + x] = YAP_DARK;
                }
//                    if (matrix.getEnclosingRectangle() != null){
//                       int [] rect =  matrix.getEnclosingRectangle();
//                    }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        try {
            return new MultiFormatWriter().encode(contents, format, width, height);
        } catch (WriterException e) {
            throw e;
        } catch (Exception e) {
            // ZXing sometimes throws an IllegalArgumentException
            throw new WriterException(e);
        }
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        try {
            return new MultiFormatWriter().encode(contents, format, width, height, hints);
        } catch (WriterException e) {
            throw e;
        } catch (Exception e) {
            throw new WriterException(e);
        }
    }

    public Bitmap encodeBitmap(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        return createBitmap(encode(contents, format, width, height));
    }

    public Bitmap encodeBitmap(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        return createBitmap(encode(contents, format, width, height, hints));
    }
}
