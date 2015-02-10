package com.burdysoft.debtr.resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.os.AsyncTask;

import android.widget.ImageView;

import java.io.File;

import helper.Event;

public final class LoadImageLV extends AsyncTask<Object, Void, Bitmap>{

    private ImageView imv;
    private String path;
    private File imgFile;

    public LoadImageLV(ImageView imv, Event event) {
        this.imv = imv;
        this.path = imv.getTag().toString();
        this.imgFile = new File(event.getPhotofile());
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        Bitmap bitmap = null;
        if(imgFile.exists()) {
            System.out.println("imgFile Exists!");
            bitmap = decodeSampledBitmapFromFile(imgFile.getAbsolutePath(), 50, 50);

        }

        return bitmap;
    }



    @Override
    protected void onPostExecute(Bitmap result) {
        System.out.println("We are in the onPostExecute " + path);
        System.out.println(imv.getTag().toString());
        System.out.println(path);
        System.out.println("Result is " + (result != null));

        if (!imv.getTag().toString().equals(path)) {
               /* The path is not same. This means that this
                  image view is handled by some other async task.
                  We don't do anything and return. */
            return;
        }

        if(result != null && imv != null){
            imv.setVisibility(View.VISIBLE);
            imv.setImageBitmap(result);
        }else{
            imv.setVisibility(View.GONE);
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String imgFile, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(imgFile, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgFile, options);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;


    }



}
