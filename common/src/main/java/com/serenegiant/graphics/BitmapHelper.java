//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.graphics;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.serenegiant.utils.BitsHelper;
import com.serenegiant.utils.UriHelper;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class BitmapHelper {
    private static final int OPTIONS_SCALE_UP = 1;
    public static final int OPTIONS_RECYCLE_INPUT = 2;

    public BitmapHelper() {
    }

    public static byte[] BitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = null;
        if (bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream)) {
            bytes = byteArrayOutputStream.toByteArray();
        }

        return bytes;
    }

    public static Bitmap asBitmap(byte[] bytes) {
        Bitmap bitmap = null;
        if (bytes != null) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        return bitmap;
    }

    public static Bitmap asBitmap(byte[] bytes, int requestWidth, int requestHeight) {
        Bitmap bitmap = null;
        if (bytes != null) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calcSampleSize(options, requestWidth, requestHeight);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }

        return bitmap;
    }

    public static Bitmap asBitmapStrictSize(byte[] bytes, int requestWidth, int requestHeight) {
        Bitmap bitmap = null;
        if (bytes != null) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            int calcedSampleSize = calcSampleSize(options, requestWidth, requestHeight);
            int inSampleSize = 1 << BitsHelper.MSB(calcedSampleSize);
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            if (inSampleSize != calcedSampleSize || bitmap.getWidth() != requestWidth || bitmap.getHeight() != requestHeight) {
                Bitmap newBitmap = scaleBitmap(bitmap, requestWidth, requestHeight);
                bitmap.recycle();
                bitmap = newBitmap;
            }
        }

        return bitmap;
    }

    public static Bitmap asBitmap(String filePath) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(filePath)) {
            bitmap = BitmapFactory.decodeFile(filePath);
        }

        return bitmap;
    }

    public static Bitmap asBitmap(String filePath, int requestWidth, int requestHeight) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(filePath)) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calcSampleSize(options, requestWidth, requestHeight);
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }

        return bitmap;
    }

    public static Bitmap asBitmapStrictSize(String filePath, int requestWidth, int requestHeight) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(filePath)) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            int calcedSampleSize = calcSampleSize(options, requestWidth, requestHeight);
            int inSampleSize = 1 << BitsHelper.MSB(calcedSampleSize);
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(filePath, options);
            if (inSampleSize != calcedSampleSize || bitmap.getWidth() != requestWidth || bitmap.getHeight() != requestHeight) {
                Bitmap newBitmap = scaleBitmap(bitmap, requestWidth, requestHeight);
                bitmap.recycle();
                bitmap = newBitmap;
            }
        }

        return bitmap;
    }

    public static Bitmap asBitmap(FileDescriptor fd) {
        Bitmap bitmap = null;
        if (fd != null && fd.valid()) {
            bitmap = BitmapFactory.decodeFileDescriptor(fd);
        }

        return bitmap;
    }

    public static Bitmap asBitmap(FileDescriptor fd, int requestWidth, int requestHeight) {
        Bitmap bitmap = null;
        if (fd != null && fd.valid()) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, (Rect)null, options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calcSampleSize(options, requestWidth, requestHeight);
            bitmap = BitmapFactory.decodeFileDescriptor(fd, (Rect)null, options);
        }

        return bitmap;
    }

    public static Bitmap asBitmapStrictSize(FileDescriptor fd, int requestWidth, int requestHeight) {
        Bitmap bitmap = null;
        if (fd != null && fd.valid()) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, (Rect)null, options);
            int calcedSampleSize = calcSampleSize(options, requestWidth, requestHeight);
            int inSampleSize = 1 << BitsHelper.MSB(calcedSampleSize);
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFileDescriptor(fd, (Rect)null, options);
            if (inSampleSize != calcedSampleSize || bitmap.getWidth() != requestWidth || bitmap.getHeight() != requestHeight) {
                Bitmap newBitmap = scaleBitmap(bitmap, requestWidth, requestHeight);
                bitmap.recycle();
                bitmap = newBitmap;
            }
        }

        return bitmap;
    }

    public static Bitmap asBitmap(ContentResolver cr, Uri uri) throws FileNotFoundException, IOException {
        Bitmap bitmap = null;
        if (uri != null) {
            ParcelFileDescriptor pfd = cr.openFileDescriptor(uri, "r");
            if (pfd != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                int orientation = getOrientation(cr, uri);
                if (orientation != 0) {
                    Bitmap newBitmap = rotateBitmap(bitmap, orientation);
                    bitmap.recycle();
                    bitmap = newBitmap;
                }
            }
        }

        return bitmap;
    }

    public static Bitmap asBitmap(ContentResolver cr, Uri uri, int requestWidth, int requestHeight) throws FileNotFoundException, IOException {
        Bitmap bitmap = null;
        if (uri != null) {
            ParcelFileDescriptor pfd = cr.openFileDescriptor(uri, "r");
            if (pfd != null) {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor(), (Rect)null, options);
                options.inJustDecodeBounds = false;
                options.inSampleSize = calcSampleSize(options, requestWidth, requestHeight);
                bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor(), (Rect)null, options);
                int orientation = getOrientation(cr, uri);
                if (orientation != 0) {
                    Bitmap newBitmap = rotateBitmap(bitmap, orientation);
                    bitmap.recycle();
                    bitmap = newBitmap;
                }
            }
        }

        return bitmap;
    }

    public static Bitmap asBitmapStrictSize(ContentResolver cr, Uri uri, int requestWidth, int requestHeight) throws FileNotFoundException, IOException {
        Bitmap bitmap = null;
        if (uri != null) {
            ParcelFileDescriptor pfd = cr.openFileDescriptor(uri, "r");
            if (pfd != null) {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor(), (Rect)null, options);
                int calcedSampleSize = calcSampleSize(options, requestWidth, requestHeight);
                int inSampleSize = 1 << BitsHelper.MSB(calcedSampleSize);
                options.inSampleSize = inSampleSize;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor(), (Rect)null, options);
                int orientation = getOrientation(cr, uri);
                if (inSampleSize != calcedSampleSize || orientation != 0 || bitmap.getWidth() != requestWidth || bitmap.getHeight() != requestHeight) {
                    Bitmap newBitmap = scaleRotateBitmap(bitmap, requestWidth, requestHeight, orientation);
                    bitmap.recycle();
                    bitmap = newBitmap;
                }
            }
        }

        return bitmap;
    }

    public static Bitmap asBitmap(InputStream in) {
        Bitmap bitmap = null;
        if (in != null) {
            bitmap = BitmapFactory.decodeStream(in);
        }

        return bitmap;
    }

    public static Bitmap asBitmap(InputStream in, int requestWidth, int requestHeight) {
        Bitmap bitmap = null;
        if (in != null) {
            Options options = new Options();
            Rect outPadding = new Rect();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, outPadding, options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calcSampleSize(options, requestWidth, requestHeight);
            bitmap = BitmapFactory.decodeStream(in, outPadding, options);
        }

        return bitmap;
    }

    public static Bitmap asBitmapStrictSize(InputStream in, int requestWidth, int requestHeight) {
        Bitmap bitmap = null;
        if (in != null) {
            Options options = new Options();
            Rect outPadding = new Rect();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, outPadding, options);
            int calcedSampleSize = calcSampleSize(options, requestWidth, requestHeight);
            int inSampleSize = 1 << BitsHelper.MSB(calcedSampleSize);
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(in, outPadding, options);
            if (inSampleSize != calcedSampleSize || bitmap.getWidth() != requestWidth || bitmap.getHeight() != requestHeight) {
                Bitmap newBitmap = scaleBitmap(bitmap, requestWidth, requestHeight);
                bitmap.recycle();
                bitmap = newBitmap;
            }
        }

        return bitmap;
    }

    public static int getOrientation(ContentResolver cr, Uri uri) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(UriHelper.getAbsolutePath(cr, uri));
        } catch (Exception var5) {
            return 0;
        }

        int exifR = exifInterface.getAttributeInt("Orientation", 0);
        short orientation;
        switch(exifR) {
            case 3:
                orientation = 180;
                break;
            case 6:
                orientation = 90;
                break;
            case 8:
                orientation = 270;
                break;
            default:
                orientation = 0;
        }

        return orientation;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int requestWidth, int requestHeight) {
        Bitmap newBitmap = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale((float)width / (float)requestWidth, (float)height / (float)requestHeight);
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }

        return newBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotation) {
        Bitmap newBitmap = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postRotate((float)rotation);
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }

        return newBitmap;
    }

    public static Bitmap scaleRotateBitmap(Bitmap bitmap, int requestWidth, int requestHeight, int rotation) {
        Bitmap newBitmap = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale((float)width / (float)requestWidth, (float)height / (float)requestHeight);
            matrix.postRotate((float)rotation);
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }

        return newBitmap;
    }

    public static Bitmap extractBitmap(Bitmap source, int width, int height) {
        Bitmap newBitmap = null;
        if (source != null) {
            float scale;
            if (source.getWidth() < source.getHeight()) {
                scale = (float)width / (float)source.getWidth();
            } else {
                scale = (float)height / (float)source.getHeight();
            }

            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            newBitmap = transform(matrix, source, width, height, 3);
        }

        return newBitmap;
    }

    private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, int options) {
        boolean scaleUp = (options & 1) != 0;
        boolean recycle = (options & 2) != 0;
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        int dx1;
        int dy1;
        if (scaleUp || deltaX >= 0 && deltaY >= 0) {
            float bitmapWidthF = (float)source.getWidth();
            float bitmapHeightF = (float)source.getHeight();
            float bitmapAspect = bitmapWidthF / bitmapHeightF;
            float viewAspect = (float)targetWidth / (float)targetHeight;
            float scale;
            if (bitmapAspect > viewAspect) {
                scale = (float)targetHeight / bitmapHeightF;
                if (scale >= 0.9F && scale <= 1.0F) {
                    scaler = null;
                } else {
                    scaler.setScale(scale, scale);
                }
            } else {
                scale = (float)targetWidth / bitmapWidthF;
                if (scale >= 0.9F && scale <= 1.0F) {
                    scaler = null;
                } else {
                    scaler.setScale(scale, scale);
                }
            }

            Bitmap b1;
            if (scaler != null) {
                b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
            } else {
                b1 = source;
            }

            if (recycle && b1 != source) {
                source.recycle();
            }

            dx1 = Math.max(0, b1.getWidth() - targetWidth);
            dy1 = Math.max(0, b1.getHeight() - targetHeight);
            Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);
            if (b2 != b1 && (recycle || b1 != source)) {
                b1.recycle();
            }

            return b2;
        } else {
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Config.ARGB_8888);
            Canvas c = new Canvas(b2);
            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf + Math.min(targetWidth, source.getWidth()), deltaYHalf + Math.min(targetHeight, source.getHeight()));
            dx1 = (targetWidth - src.width()) / 2;
            dy1 = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dx1, dy1, targetWidth - dx1, targetHeight - dy1);
            c.drawBitmap(source, src, dst, (Paint)null);
            if (recycle) {
                source.recycle();
            }

            c.setBitmap((Bitmap)null);
            return b2;
        }
    }

    public static int calcSampleSize(Options options, int requestWidth, int requestHeight) {
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        int inSampleSize = 1;
        if (imageHeight > requestHeight || imageWidth > requestWidth) {
            if (imageWidth > imageHeight) {
                inSampleSize = (int)Math.floor((double)((float)imageHeight / (float)requestHeight));
            } else {
                inSampleSize = (int)Math.floor((double)((float)imageWidth / (float)requestWidth));
            }
        }

        return inSampleSize;
    }

    public static Bitmap copyBitmap(Bitmap src, Bitmap dest) {
        if (src == null) {
            throw new NullPointerException("src bitmap should not be null.");
        } else {
            if (dest == null) {
                dest = Bitmap.createBitmap(src);
            } else if (!src.equals(dest)) {
                Canvas canvas = new Canvas(dest);
                canvas.setBitmap(src);
            }

            return dest;
        }
    }

    public static Bitmap makeCheckBitmap() {
        Bitmap bm = Bitmap.createBitmap(40, 40, Config.RGB_565);
        Canvas c = new Canvas(bm);
        c.drawColor(-1);
        Paint p = new Paint();
        p.setColor(-3355444);
        c.drawRect(0.0F, 0.0F, 20.0F, 20.0F, p);
        c.drawRect(20.0F, 20.0F, 40.0F, 40.0F, p);
        return bm;
    }
}
