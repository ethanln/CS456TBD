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
import android.widget.TextView;

import com.tbd.appprototype.R;

import java.util.ArrayList;

import model.InventoryItem;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;

/**
 * Created by Ethan on 4/1/2016.
 */
public class ItemBorrowedAdapter extends ArrayAdapter<InventoryItem> {

    private Context context;

    private ArrayList<InventoryItem> borrowedItems;


    public ItemBorrowedAdapter(Context c, ArrayList<InventoryItem> borrowedItems){
        super(c,  R.layout.list_row_borrowed, R.id.friend_name, borrowedItems);
        this.context = c;

        this.borrowedItems = borrowedItems;
    }

    public void remove(int position){
        remove(getItem(position));
    }

    public boolean isEmpty(){
        return this.borrowedItems.size() == 0;
    }

    public InventoryItem get(int position){
        return this.borrowedItems.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_borrowed, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.item_borrowed_image);

        TextView name = (TextView)row.findViewById(R.id.item_borrowed_name);
        TextView ownerName = (TextView)row.findViewById(R.id.item_borrowed_owner_name);
        TextView id = (TextView)row.findViewById(R.id.item_borrowed_id);
        TextView pos = (TextView)row.findViewById(R.id.item_borrowed_position_id);


        if(this.borrowedItems.get(position).getImageURL().equals("")){
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 0);
        }
        else {
            // load users profile pic
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(this.borrowedItems.get(position).getImageURL(), image, 550);
        }

        // set item title
        name.setTextColor(Color.BLACK);
        name.setText(this.borrowedItems.get(position).getTitle());

        //getOwner ID
        ownerName.setTextColor(Color.BLACK);
        ownerName.setText(this.borrowedItems.get(position).getOwnerName());

        // set item id
        id.setText(this.borrowedItems.get(position).getItemID());

        // set position id
        pos.setText(String.valueOf(position));

        return row;
    }
}
