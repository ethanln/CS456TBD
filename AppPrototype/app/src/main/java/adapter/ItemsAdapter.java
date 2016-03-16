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

import model.InventoryItem;
import model.User;
import util.BlobImageLoaderUtil;
import util.ImageLoaderUtil;

/**
 * Created by Ethan on 3/15/2016.
 */
public class ItemsAdapter extends ArrayAdapter<InventoryItem> {

    private Context context;
    private ArrayList<InventoryItem> itemsList;

    public ItemsAdapter(Context c, ArrayList<InventoryItem> items){
        super(c,  R.layout.list_row_items, R.id.item_name, items);
        this.context = c;
        this.itemsList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_items, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.item_image);
        TextView name = (TextView)row.findViewById(R.id.item_name);
        TextView description = (TextView)row.findViewById(R.id.item_description);

        BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
        imageLoader.loadImage(this.itemsList.get(position).getImageURL(), image, 550);

        name.setTextColor(Color.BLACK);
        name.setText(this.itemsList.get(position).getTitle());

        description.setText(this.itemsList.get(position).getDescription());

        return row;
    }
}
