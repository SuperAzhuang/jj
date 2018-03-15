package com.feihua.jjcb.phone.utils;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class PhotoUtils
{
    public static Bitmap getSmallBitmap(String filePath)
    {
        L.w("PhotoUtils", "filePath = " + filePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(filePath, options);
//                options.inSampleSize = calculateInSampleSize(bitmap.getWidth(), bitmap.getHeight(), 480, 800);

        options.inSampleSize = 4;

        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null)
        {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        return bm;

    }

    private static int readPictureDegree(String path)
    {
        int degree = 0;
        try
        {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return degree;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate)
    {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private static int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        //        final int height = options.outHeight;
        //        final int width = options.outWidth;
        int inSampleSize = 1;//inSampleSize=1表示不缩放

        //        if (height > reqHeight || width > reqWidth)
        //        {
        //
        //            // Calculate ratios of height and width to requested height and
        //            // width
        //            final int heightRatio = Math.round((float) height
        //                    / (float) reqHeight);
        //            final int widthRatio = Math.round((float) width / (float) reqWidth);
        //
        //            // Choose the smallest ratio as inSampleSize value, this will
        //            // guarantee
        //            // a final image with both dimensions larger than or equal to the
        //            // requested height and width.
        //            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        //        }
        if (width > height && width > reqWidth)
        {//如果宽度大的话根据宽度固定大小缩放
            inSampleSize = (int) (width / reqWidth);
        }
        else if (width < height && height > reqHeight)
        {//如果高度高的话根据宽度固定大小缩放
            inSampleSize = (int) (height / reqHeight);
        }
        if (inSampleSize <= 0)
            inSampleSize = 1;
        L.w("PhotoUtils", "inSampleSize = " + inSampleSize + ",width = " + width + ",height = " + height);
        return inSampleSize;
    }

}
