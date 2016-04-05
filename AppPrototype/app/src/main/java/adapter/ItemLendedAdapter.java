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
import model.ItemRequest;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;

/**
 * Created by Ethan on 4/1/2016.
 */
public class ItemLendedAdapter extends ArrayAdapter<InventoryItem> {

    private Context context;

    private View.OnClickListener returnListener;

    private ArrayList<InventoryItem> lendedItems;


    public ItemLendedAdapter(Context c, ArrayList<InventoryItem> lendedItems, View.OnClickListener returnListener){
        super(c,  R.layout.list_row_lended, R.id.friend_name, lendedItems);
        this.context = c;

        this.lendedItems = lendedItems;

        this.returnListener = returnListener;
    }

    public void remove(int position){
        remove(getItem(position));
    }

    public boolean isEmpty(){
        return this.lendedItems.size() == 0;
    }

    public InventoryItem get(int position){
        return this.lendedItems.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_lended, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.item_lended_image);

        Button returnBtn = (Button) row.findViewById(R.id.return_item_btn);

        TextView name = (TextView)row.findViewById(R.id.item_lended_name);
        TextView toWhom = (TextView)row.findViewById(R.id.item_lended_to_whom_name);
        TextView id = (TextView)row.findViewById(R.id.item_lended_id);
        TextView pos = (TextView)row.findViewById(R.id.item_lended_position_id);

        if(this.lendedItems.get(position).getImageURL().equals("")){
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 0);
        }
        else {
            // load users profile pic
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(this.lendedItems.get(position).getImageURL(), image, 550);
        }

        // set onclick listener for the icons
        returnBtn.setOnClickListener(returnListener);

        // set item title
        name.setTextColor(Color.BLACK);
        if(this.lendedItems.get(position).getTitle().length() > 12) {
            name.setText(this.lendedItems.get(position).getTitle().substring(0, 11) + "...");
        }
        else{
            name.setText(this.lendedItems.get(position).getTitle());
        }

        //getFromID
        toWhom.setTextColor(Color.GRAY);
        toWhom.setText(this.lendedItems.get(position).getLendedToName());

        // set item id
        id.setText(this.lendedItems.get(position).getItemID());

        // set position id
        pos.setText(String.valueOf(position));

        return row;
    }
}
