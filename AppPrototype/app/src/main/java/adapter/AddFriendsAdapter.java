package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
public class AddFriendsAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> users;
    private View.OnClickListener listener;

    public AddFriendsAdapter(Context c, ArrayList<User> users, View.OnClickListener listener){
        super(c,  R.layout.list_row_friends, R.id.friend_name, users);
        this.context = c;
        this.users = users;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_friends_add, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.friend_image);
        //ImageView addIcon = (ImageView)row.findViewById(R.id.add_friend_icon);
        Button addBtn = (Button)row.findViewById(R.id.add_friend_icon);
        TextView name = (TextView)row.findViewById(R.id.friend_name);
        TextView id = (TextView)row.findViewById(R.id.friend_id);
        TextView pos = (TextView)row.findViewById(R.id.friend_position_id);

        if(this.users.get(position).getImageURL().length() == 0){
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 0);
        }
        else {
            // load users profile pic
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(this.users.get(position).getImageURL(), image, 550);
        }

        // set onclick listener for the icon
       // Bitmap addIconImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add_friend);
       // addIcon.setImageBitmap(addIconImg);
       // addIcon.setOnClickListener(this.listener);
        addBtn.setOnClickListener(this.listener);

        // set username
        name.setTextColor(Color.BLACK);

        String username = this.users.get(position).getUsername();
        if(username.length() > 10){
            username = username.substring(0, 9) + "...";
        }
        name.setText(username);

        // set user id
        id.setText(this.users.get(position).getUserID());
        pos.setText(String.valueOf(position));

        return row;
    }
}
