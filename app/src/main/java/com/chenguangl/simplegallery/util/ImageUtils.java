package com.chenguangl.simplegallery.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.media.ExifInterface;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static int getRotationInDegrees(Context context, String imageUri) {
        InputStream inputStream = null;
        try {
            AssetManager assetManager = context.getAssets();
            int index = imageUri.indexOf("file:///android_asset/");
            if (index != -1) {
                imageUri = imageUri.substring(index + 22);
            }
            inputStream = assetManager.open(imageUri);
            ExifInterface exifInterface = new ExifInterface(inputStream);
            int exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
