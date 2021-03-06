package util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Created by Ethan on 3/22/2016.
 */
public class CustomImageUtil {

    private static CustomImageUtil inst;

    private CustomImageUtil(){}

    private static CustomImageUtil instance(){
        if(inst == null){
            inst = new CustomImageUtil();
        }
        return inst;
    }
    private Bitmap _squareImage(Bitmap imageBitmap) {
        if (imageBitmap.getWidth() >= imageBitmap.getHeight()){

            imageBitmap = Bitmap.createBitmap(
                    imageBitmap,
                    imageBitmap.getWidth()/2 - imageBitmap.getHeight()/2,
                    0,
                    imageBitmap.getHeight(),
                    imageBitmap.getHeight()
            );

        }else{

            imageBitmap = Bitmap.createBitmap(
                    imageBitmap,
                    0,
                    imageBitmap.getHeight()/2 - imageBitmap.getWidth()/2,
                    imageBitmap.getWidth(),
                    imageBitmap.getWidth()
            );
        }
        return imageBitmap;
    }

    public static Bitmap squareImage(Bitmap image){
        return instance()._squareImage(image);
    }

    private Bitmap _roundImage(Bitmap image, int pixels){
        Bitmap output = Bitmap.createBitmap(image.getWidth(), image
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, image.getWidth(), image.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(image, rect, rect, paint);

        return output;
    }

    public static Bitmap roundImage(Bitmap image, int pixels){
        return instance()._roundImage(image, pixels);
    }

    private Bitmap _addCircularBorder(Bitmap bitmap, int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }

    public static Bitmap addCircularBorder(Bitmap bitmap, int borderWidth){
        return instance()._addCircularBorder(bitmap, borderWidth);
    }
}
