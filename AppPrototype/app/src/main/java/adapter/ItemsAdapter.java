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

import org.w3c.dom.Text;

import java.util.ArrayList;

import model.InventoryItem;
import model.User;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.ImageLoaderUtil;

/**
 * Created by Ethan on 3/15/2016.
 */
public class ItemsAdapter extends ArrayAdapter<InventoryItem> {

    private Context context;
    private ArrayList<InventoryItem> itemsList;
    private boolean owner;

    private View.OnClickListener editItemListener;
    private View.OnClickListener removeItemListener;

    public ItemsAdapter(Context c, ArrayList<InventoryItem> items, boolean owner, View.OnClickListener editItemListener, View.OnClickListener removeItemListener){
        super(c,  R.layout.list_row_items, R.id.item_name, items);
        this.context = c;
        this.itemsList = items;
        this.owner = owner;

        this.editItemListener = editItemListener;
        this.removeItemListener = removeItemListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_items, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.item_image);
        TextView name = (TextView)row.findViewById(R.id.item_name);
        TextView description = (TextView)row.findViewById(R.id.item_description);
        TextView pos = (TextView)row.findViewById(R.id.item_pos);

        if(this.itemsList.get(position).getImageURL().length() == 0){
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 0);
        }
        else {
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(this.itemsList.get(position).getImageURL(), image, 550);
        }

        name.setTextColor(Color.BLACK);
        if(this.itemsList.get(position).getTitle().length() > 12) {
            name.setText(this.itemsList.get(position).getTitle().substring(0, 11) + "...");
        }
        else{
            name.setText(this.itemsList.get(position).getTitle());
        }

        description.setTextColor(Color.GRAY);
        description.setText(this.itemsList.get(position).getDescription());


        if(!owner){
            ImageView edit_icon = (ImageView)row.findViewById(R.id.edit_item_icon);
            ImageView delete_icon = (ImageView)row.findViewById(R.id.delete_item_icon);
            edit_icon.setVisibility(View.GONE);
            delete_icon.setVisibility(View.GONE);
        }
        else{
            ImageView edit_icon = (ImageView)row.findViewById(R.id.edit_item_icon);
            ImageView delete_icon = (ImageView)row.findViewById(R.id.delete_item_icon);
            if(this.editItemListener != null &&
                    this.removeItemListener != null){
                edit_icon.setOnClickListener(this.editItemListener);
                delete_icon.setOnClickListener(this.removeItemListener);
            }

        }

        pos.setText(String.valueOf(position));
        return row;
    }
}
