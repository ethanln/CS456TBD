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
import model.InventoryList;
import util.ImageLoaderUtil;

/**
 * Created by Ethan on 3/15/2016.
 */
public class ListAdapter extends ArrayAdapter<InventoryList> {

    private Context context;
    private ArrayList<InventoryList> listsList;

    private final static String videoGameIcon = "https://cdn3.iconfinder.com/data/icons/computer-network-icons/512/Controller-512.png";
    private final static String movieIcon = "http://uxrepo.com/static/icon-sets/mfg-labs/svg/movie.svg";
    private final static String bookIcon = "http://simpleicon.com/wp-content/uploads/book-1.png";
    private final static String boardgameIcon = "https://cdn2.iconfinder.com/data/icons/app-icons-2/100/icon_66244-512.png";


    public ListAdapter(Context c, ArrayList<InventoryList> lists){
        super(c,  R.layout.list_row_list, R.id.list_name, lists);
        this.context = c;
        this.listsList = lists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_list, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.list_image);
        TextView name = (TextView)row.findViewById(R.id.list_name);
        TextView description = (TextView)row.findViewById(R.id.list_type);

        if(this.listsList.get(position).getImageURL().length() > 0) {
            try {
                String type = this.listsList.get(position).getType();
                ImageLoaderUtil imageLoader = new ImageLoaderUtil();
                if(type.equals("Movie")){
                    imageLoader.loadImage(movieIcon, image, 550);
                }
                else if(type.equals("Book")){
                    imageLoader.loadImage(bookIcon, image, 550);
                }
                else if(type.equals("Board Game")){
                    imageLoader.loadImage(boardgameIcon, image, 550);
                }
                else if(type.equals( "Video Game")){
                    imageLoader.loadImage(videoGameIcon, image, 550);
                }

            }
            catch(Exception e){
                String error = e.getMessage();
            }
        }

        name.setTextColor(Color.BLACK);
        name.setText(this.listsList.get(position).getTitle());

        description.setText(this.listsList.get(position).getType());

        return row;
    }
}
