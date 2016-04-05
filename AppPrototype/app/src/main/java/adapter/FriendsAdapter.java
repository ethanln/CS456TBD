package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbd.appprototype.R;

import java.util.ArrayList;

import model.User;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.ImageLoaderUtil;

/**
 * Created by Ethan on 3/14/2016.
 */
public class FriendsAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> users;
    private View.OnClickListener removeListener;

    public FriendsAdapter(Context c, ArrayList<User> users, View.OnClickListener removeListener){
        super(c,  R.layout.list_row_friends, R.id.friend_name, users);
        this.context = c;
        this.users = users;
        this.removeListener = removeListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_friends, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.friend_image);
        ImageView removeIcon = (ImageView)row.findViewById(R.id.delete_friend_icon);
        TextView name = (TextView)row.findViewById(R.id.friend_name);
        TextView pos = (TextView)row.findViewById(R.id.friend_pos);

        if(this.users.get(position).getImageURL().length() == 0){
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 0);
        }
        else {
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(this.users.get(position).getImageURL(), image, 550);
        }

        name.setTextColor(Color.BLACK);
        if(this.users.get(position).getUsername().length() > 20) {
            name.setText(this.users.get(position).getUsername().substring(0, 19) + "...");
        }
        else{
            name.setText(this.users.get(position).getUsername());
        }
        removeIcon.setOnClickListener(this.removeListener);
        pos.setText(String.valueOf(position));

        return row;
    }
}
