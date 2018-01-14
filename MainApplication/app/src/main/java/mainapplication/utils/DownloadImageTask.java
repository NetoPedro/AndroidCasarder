package mainapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Luis Gouveia on 06/12/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    GifImageView bmImage;
    String url;
    int facilityId;
    boolean suggest = false;

    public DownloadImageTask(int id, GifImageView bmImage, String url) {
        this.bmImage = bmImage;
        this.url = url;
        this.facilityId = id;
    }


    public DownloadImageTask(GifImageView bmImage, String url) {
        this.bmImage = bmImage;
        this.url = url;
        suggest = true;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap mIcon = null;
        if(!suggest) {
            if (!ImageCache.contains(facilityId)) {
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    Bitmap originalIcon = BitmapFactory.decodeStream(in);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    originalIcon.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    mIcon = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                    ImageCache.addFacilityImage(facilityId, mIcon);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            } else {
                mIcon = ImageCache.getImage(facilityId);
            }
        }else{
            try{
                InputStream in = new java.net.URL(url).openStream();
                Bitmap originalIcon = BitmapFactory.decodeStream(in);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                originalIcon.compress(Bitmap.CompressFormat.JPEG, 50, out);
                mIcon = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}