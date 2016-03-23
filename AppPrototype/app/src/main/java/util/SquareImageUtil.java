package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

/**
 * Created by Ethan on 3/22/2016.
 */
public class SquareImageUtil {

    private static SquareImageUtil inst;

    private SquareImageUtil(){}

    private static SquareImageUtil instance(){
        if(inst == null){
            inst = new SquareImageUtil();
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
}
