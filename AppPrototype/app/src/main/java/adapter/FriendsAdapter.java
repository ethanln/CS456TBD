package adapter;

import android.content.Context;
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
import util.ImageLoaderUtil;

/**
 * Created by Ethan on 3/14/2016.
 */
public class FriendsAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> users;

    public FriendsAdapter(Context c, ArrayList<User> users){
        super(c,  R.layout.list_row_friends, R.id.friend_name, users);
        this.context = c;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_friends, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.friend_image);
        TextView name = (TextView)row.findViewById(R.id.friend_name);

        BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
        imageLoader.loadImage(this.users.get(position).getImageURL(), image, 550);

        name.setTextColor(Color.BLACK);
        name.setText(this.users.get(position).getUsername());

        return row;
    }
}
