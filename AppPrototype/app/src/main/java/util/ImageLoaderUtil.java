package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Ethan on 3/14/2016.
 */
public class ImageLoaderUtil {

    //private static ImageLoaderUtil imageLoader;

    private ImageView img;

    public ImageLoaderUtil(){}

    public void loadImage(String url, ImageView img, int radius){
        String radiusStr = String.valueOf(radius);
        this.img = img;
        Loader loader = new Loader();
        loader.execute(url, radiusStr);
    }

    private class Loader extends AsyncTask<String, String, Bitmap> {
        private int radius;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args) {
            Bitmap image = null;
            try {
                this.radius = Integer.parseInt(args[1]);
                image = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap image) {
            Bitmap roundedImage = this.getRoundedCornerBitmap(image, this.radius);
            if (image != null) {
                img.setImageBitmap(roundedImage);
            }
        }

        private Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
    }
}

