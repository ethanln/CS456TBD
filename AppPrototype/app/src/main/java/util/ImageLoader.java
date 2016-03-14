package util;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbd.appprototype.MainActivity;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Ethan on 3/14/2016.
 */
public class ImageLoader  {

    private static ImageLoader imageLoader;

    private ImageView img;

    private ImageLoader(){}

    private static ImageLoader inst(){
        if(imageLoader == null){
            imageLoader = new ImageLoader();
        }
        return imageLoader;
    }

    private void _loadImage(String url, ImageView img){
        this.img = img;
        Loader loader = new Loader();
        loader.execute(url);
    }

    public static void loadImage(String url, ImageView img){
        inst()._loadImage(url, img);
    }
    private class Loader extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args) {
            Bitmap image = null;
            try {
                image = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap image) {

            if (image != null) {
                img.setImageBitmap(image);
            }
        }
    }
}

