package com.firstapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;

/**
 * Created by K on 2014/11/24.
 */
public class UserPhotoAdapter extends ResourceCursorAdapter {
    /**
     * 用來做圖片Caching
     */
    private LruCache<String, Bitmap> memoryCache;
    BitmapFactory.Options options;
    private static final int PHOTO_WIDTH = 120;
    private static final int PHOTO_HEIGHT = 120;
    public UserPhotoAdapter(Context context, Cursor c) {
        super(context, R.layout.item_grid_photo_user, c, 0);
        // 取得app可用的最大記憶體
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 圖片暫存大小為app可用最大記憶體的1/8
        int cacheSize = maxMemory / 8;
        this.memoryCache = new LruCache<String, Bitmap>( cacheSize )
        {
            @Override
            protected int sizeOf( String key, Bitmap bitmap )
            {
                return bitmap.getByteCount();
            }
        };

        options = new BitmapFactory.Options();
        options.inSampleSize=8;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));

        Bitmap bm = getBitmapFromMemoryCache(path);
        if (bm == null) {
            bm = loadBitmapFromFile(path,PHOTO_WIDTH,PHOTO_HEIGHT);
            addBitmapToMemoryCache(path, bm);
        } else {
            bm = getBitmapFromMemoryCache(path);
        }
        img.setImageBitmap(bm);
    }
    /**
     * 從手機儲存載入圖片。
     *
     * @param photoPath
     * @param width  Bitmap要呈現的寬度
     * @param height Bitmap要呈現的高度
     * @return
     */
    public static Bitmap loadBitmapFromFile( String photoPath, int width, int height )
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( photoPath, options );
        options.inSampleSize = 8;//calculateImageSampleSize( options, width, height );
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ALPHA_8;
        bitmap = BitmapFactory.decodeFile( photoPath, options );
        return bitmap;
    }
    /**
     * 依照要顯示的長寬來計算Bitmap要sample的比例
     *
     * @param options
     * @param reqWidth 需要的寬度
     * @param reqHeight 需要的高度
     * @return
     */
    public static int calculateImageSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight )
    {
        final int rawH = options.outHeight;
        final int rawW = options.outWidth;
        int imageSampleSize = 1;

        if ( rawH > reqHeight || rawW > reqWidth )
        {
            final int halfH = rawH / 2;
            final int halfW = rawW / 2;

            while ( ( ( halfH / imageSampleSize ) > reqHeight ) && ( ( halfW / imageSampleSize ) > reqWidth ) )
                imageSampleSize *= 2;
        }
        return imageSampleSize;
    }
    /**
     * 把圖片做caching
     *
     * @param key    LruCache的鍵，這裡是圖片的URL位址
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (this.getBitmapFromMemoryCache(key) == null)
            this.memoryCache.put(key, bitmap);
    }

    /**
     * 從LruCache中取回圖片
     *
     * @param key 用圖片URL位址從cache中取回圖片
     * @return 圖片Bitmap或null
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        Bitmap bitmap = this.memoryCache.get(key);
        return bitmap;
    }

}
