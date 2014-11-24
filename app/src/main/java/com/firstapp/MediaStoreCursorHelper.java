package com.firstapp;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by K on 2014/11/24.
 */
public class MediaStoreCursorHelper {
    public static final String[] PHOTOS_PROJECTION = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.MINI_THUMB_MAGIC,
            MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
    public static final String PHOTOS_ORDER_BY = MediaStore.Images.Media.DATE_ADDED + " desc";

    public static final Uri MEDIA_STORE_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
}
