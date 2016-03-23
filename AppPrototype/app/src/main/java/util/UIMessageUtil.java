package util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ethan on 3/22/2016.
 */
public class UIMessageUtil {
    private static UIMessageUtil inst;

    private Toast toast;
    private UIMessageUtil(){}

    private static UIMessageUtil instance(){
        if(inst == null){
            inst = new UIMessageUtil();
        }
        return inst;
    }
    private void _showResultMessage(Context context, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showResultMessage(Context context, String message){
        instance()._showResultMessage(context, message);
    }

}
