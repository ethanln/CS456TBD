package util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Ethan on 3/28/2016.
 */
public class LoadingScreenUtil {

    private static LoadingScreenUtil instance;
    private ProgressDialog dialog;
    private String endMessage;
    private Context context;

    private LoadingScreenUtil()
    {
        this.dialog = null;
        this.endMessage = "";
        this.context = null;
    }

    private static LoadingScreenUtil inst(){
        if(instance == null){
            instance = new LoadingScreenUtil();
        }
        return instance;
    }

    private void _start(Context context, String message){
        this.dialog = new ProgressDialog(context); // this = YourActivity
        this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.dialog.setMessage(message);
        this.dialog.setIndeterminate(true);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.show();
    }

    public static void start(Context context, String message){
        inst()._start(context, message);
    }

    private void _setEndMessage(Context context, String endMessage){
        this.context = context;
        this.endMessage = endMessage;
    }

    public static void setEndMessage(Context context, String endMessage){
        inst()._setEndMessage(context, endMessage);
    }

    private void _end(){
        if(this.dialog != null) {
            this.dialog.dismiss();
            this.dialog = null;
            if(this.context != null &&
                    !this.endMessage.equals("")) {
                UIMessageUtil.showResultMessage(this.context, this.endMessage);
                this.context = null;
                this.endMessage = "";
            }
        }
    }

    public static void end(){
        instance._end();
    }
}
