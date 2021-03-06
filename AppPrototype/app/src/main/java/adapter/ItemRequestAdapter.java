package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbd.appprototype.R;

import java.util.ArrayList;

import model.FriendRequest;
import model.InventoryItem;
import model.ItemRequest;
import model.User;
import networking.NetworkManager;
import networking.callback.ItemCallback;
import networking.callback.UserCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;

/**
 * Created by Ethan on 3/17/2016.
 */
public class ItemRequestAdapter extends ArrayAdapter<ItemRequest>{

    private Context context;

    private ArrayList<ItemRequest> itemRequests;

    private View.OnClickListener listenerDecline;
    private View.OnClickListener listenerAccept;

    public ItemRequestAdapter(Context c, ArrayList<ItemRequest> itemRequests, View.OnClickListener listenerDecline, View.OnClickListener listenerAccept ){
        super(c,  R.layout.list_row_item_request, R.id.friend_name, itemRequests);
        this.context = c;

        this.itemRequests = itemRequests;

        this.listenerDecline = listenerDecline;
        this.listenerAccept = listenerAccept;

    }


    public void remove(int position){
        remove(getItem(position));
    }

    public void remove(String itemID){
        ArrayList<ItemRequest> requestsToBeRemoved = new ArrayList<ItemRequest>();
        for(ItemRequest request : itemRequests){
            if(request.getItemID().equals(itemID)){
                requestsToBeRemoved.add(request);
            }
        }

        for(ItemRequest request : requestsToBeRemoved){
            remove(request);
        }
    }

    public boolean isEmpty(){
        return this.itemRequests.size() == 0;
    }

    public ItemRequest get(int position){
        return this.itemRequests.get(position);
    }

    public int getCount(){
        return this.itemRequests.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_item_request, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.item_request_image);

        Button acceptRequestClickView = (Button) row.findViewById(R.id.accept_item_request_click);
        Button declineRequestClickView = (Button) row.findViewById(R.id.decline_item_request_click);

        TextView name = (TextView)row.findViewById(R.id.item_request_name);
        TextView fromWhom = (TextView)row.findViewById(R.id.item_request_from_whom);
        TextView id = (TextView)row.findViewById(R.id.item_request_id);
        TextView pos = (TextView)row.findViewById(R.id.item_request_position_id);

        //getItemImage(this.itemRequests.get(position).getItemID());

        if(this.itemRequests.get(position).getImageCache().equals("")){
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 0);
        }
        else {
            // load users profile pic
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(this.itemRequests.get(position).getImageCache(), image, 550);
        }

        // set onclick listener for the icons
        acceptRequestClickView.setOnClickListener(this.listenerAccept);
        declineRequestClickView.setOnClickListener(this.listenerDecline);

        // set item title
        name.setTextColor(Color.BLACK);
        if(this.itemRequests.get(position).getItemName().length() > 12) {
            name.setText(this.itemRequests.get(position).getItemName().substring(0, 11) + "...");
        }
        else{
            name.setText(this.itemRequests.get(position).getItemName());
        }

        //getFromID(this.itemRequests.get(position).getFrom());
        fromWhom.setTextColor(Color.GRAY);
        fromWhom.setText(this.itemRequests.get(position).getFromName());

        // set user id
        id.setText(this.itemRequests.get(position).getID());

        // set position id
        pos.setText(String.valueOf(position));

        return row;
    }
}
