package com.firstapp;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * Created by K on 2014/11/24.
 */
public class PhotupCursorLoader extends CursorLoader {

    private final boolean mRequeryOnChange;

    public PhotupCursorLoader(Context context, Uri uri, String[] projection, String selection,
                              String[] selectionArgs,
                              String sortOrder, boolean requeryOnChange) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
        mRequeryOnChange = requeryOnChange;
    }

    @Override
    public void onContentChanged() {
        if (mRequeryOnChange) {
            super.onContentChanged();
        }
    }
}
