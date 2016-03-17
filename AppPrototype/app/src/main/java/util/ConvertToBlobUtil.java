package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.tbd.appprototype.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ethan on 3/16/2016.
 */
public class ConvertToBlobUtil {
    private static ConvertToBlobUtil inst;

    private ConvertToBlobUtil(){}

    private static ConvertToBlobUtil instance(){
        if(inst == null){
            inst = new ConvertToBlobUtil();
        }
        return inst;
    }

    private static String _convertToBlob(Bitmap image, String extension, Context context){

        Bitmap btmap = image == null ? BitmapFactory.decodeResource(context.getResources(), R.drawable.search_icon) : image;
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        switch(extension){
            case "png":
                btmap.compress(Bitmap.CompressFormat.PNG, 100, boas ); //bm is the bitmap object
                break;
            case "jpg":
                btmap.compress(Bitmap.CompressFormat.JPEG, 100, boas ); //bm is the bitmap object
                break;
            default:
                return "";
        }

        byte[] byteArrayImage = boas.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;
    }

    public static String convertToBlob(Bitmap image, String extension, Context context){
        return instance()._convertToBlob(image, extension, context);
    }
}

