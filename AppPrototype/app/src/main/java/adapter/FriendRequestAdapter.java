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

import model.FriendRequest;
import model.ItemRequest;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;

/**
 * Created by Ethan on 3/22/2016.
 */
public class FriendRequestAdapter extends ArrayAdapter<FriendRequest> {

    private Context context;

    private ArrayList<FriendRequest> friendRequests;

    private View.OnClickListener listenerDecline;
    private View.OnClickListener listenerAccept;

    public FriendRequestAdapter(Context c, ArrayList<FriendRequest> friendRequests, View.OnClickListener listenerDecline, View.OnClickListener listenerAccept ){
        super(c,  R.layout.list_row_friend_request, R.id.friend_request_name, friendRequests);
        this.context = c;

        this.friendRequests = friendRequests;

        this.listenerDecline = listenerDecline;
        this.listenerAccept = listenerAccept;

    }


    public void remove(int position){
        remove(getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_friend_request, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.friend_request_image);

        TextView acceptRequestClickView = (TextView) row.findViewById(R.id.accept_friend_request_click);
        TextView declineRequestClickView = (TextView) row.findViewById(R.id.decline_friend_request_click);

        TextView name = (TextView)row.findViewById(R.id.friend_request_name);
        TextView id = (TextView)row.findViewById(R.id.friend_request_id);
        TextView pos = (TextView)row.findViewById(R.id.friend_request_position_id);

        //getItemImage(this.itemRequests.get(position).getItemID());

        if(this.friendRequests.get(position).getFromImage().equals("")){
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 0);
        }
        else {
            // load users profile pic
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(this.friendRequests.get(position).getFromImage(), image, 550);
        }

        // set onclick listener for the icons
        acceptRequestClickView.setOnClickListener(this.listenerAccept);
        declineRequestClickView.setOnClickListener(this.listenerDecline);

        // set item title
        name.setTextColor(Color.BLACK);
        if(this.friendRequests.get(position).getFromName().length() > 12) {
            name.setText(this.friendRequests.get(position).getFromName().substring(0, 11) + "...");
        }
        else{
            name.setText(this.friendRequests.get(position).getFromName());
        }

        // set user id
        id.setText(this.friendRequests.get(position).getID());

        // set position id
        pos.setText(String.valueOf(position));

        return row;
    }

}
