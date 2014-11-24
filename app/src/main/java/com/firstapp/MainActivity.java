package com.firstapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    static final int RESULT_CAMERA = 101;
    static final String SAVE_PHOTO_URI = "camera_photo_uri";

    static final String LOADER_PHOTOS_BUCKETS_PARAM = "bucket_id";
    static final int LOADER_USER_PHOTOS_EXTERNAL = 0x01;
    private UserPhotoAdapter mPhotoAdapter;
    private GridView mPhotoGrid;

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(1, null, this);

        // Load buckets

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhotoGrid = (GridView) findViewById(R.id.gv_photos);
        mPhotoAdapter = new UserPhotoAdapter(this, null);
        mPhotoGrid.setAdapter(mPhotoAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        CursorLoader cursorLoader = null;

        switch (id) {
            case LOADER_USER_PHOTOS_EXTERNAL:
                String selection = null;
                String[] selectionArgs = null;

                if (null != bundle && bundle.containsKey(LOADER_PHOTOS_BUCKETS_PARAM)) {
                    selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
                    selectionArgs = new String[]{bundle.getString(LOADER_PHOTOS_BUCKETS_PARAM)};
                }

                cursorLoader = new PhotupCursorLoader(this,
                        MediaStoreCursorHelper.MEDIA_STORE_CONTENT_URI,
                        MediaStoreCursorHelper.PHOTOS_PROJECTION, selection, selectionArgs,
                        MediaStoreCursorHelper.PHOTOS_ORDER_BY, false);
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_USER_PHOTOS_EXTERNAL:
                mPhotoAdapter.swapCursor(data);
                mPhotoGrid.setSelection(0);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_USER_PHOTOS_EXTERNAL:
                mPhotoAdapter.swapCursor(null);
                break;
        }
    }
}
