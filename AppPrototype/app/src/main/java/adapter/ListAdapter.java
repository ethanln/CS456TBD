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

    private boolean owner;

    private View.OnClickListener listenerEdit;
    private View.OnClickListener listenerRemove;

    public ListAdapter(Context c, ArrayList<InventoryList> lists, boolean owner, View.OnClickListener listenerEdit, View.OnClickListener listenerRemove){
        super(c,  R.layout.list_row_list, R.id.list_name, lists);
        this.context = c;
        this.listsList = lists;
        this.owner = owner;

        this.listenerEdit = listenerEdit;
        this.listenerRemove = listenerRemove;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row_list, parent, false);

        ImageView image = (ImageView)row.findViewById(R.id.list_image);
        TextView name = (TextView)row.findViewById(R.id.list_name);
        TextView description = (TextView)row.findViewById(R.id.list_type);
        TextView id = (TextView) row.findViewById(R.id.list_id);

        try {
            String type = this.listsList.get(position).getType();
            ImageLoaderUtil imageLoader = new ImageLoaderUtil();
            if(type.equals("Movie")){
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.movie_icon);
                image.setImageBitmap(bm);
            }
            else if(type.equals("Book")){
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.book_icon);
                image.setImageBitmap(bm);
            }
            else if(type.equals("Board Game")){
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.board_game_icon);
                image.setImageBitmap(bm);
            }
            else if(type.equals( "Video Game")){
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.video_game_icon);
                image.setImageBitmap(bm);
            }

        }
        catch(Exception e){
            String error = e.getMessage();
        }


        name.setTextColor(Color.BLACK);
        name.setText(this.listsList.get(position).getTitle());

        description.setText(this.listsList.get(position).getType());


        if(!owner){
            ImageView edit_icon = (ImageView)row.findViewById(R.id.edit_list_icon);
            ImageView remove_icon = (ImageView)row.findViewById(R.id.delete_list_icon);
            edit_icon.setVisibility(View.GONE);
            remove_icon.setVisibility(View.GONE);
        }
        else{
            // add listeners to the list icons
            ImageView edit_icon = (ImageView)row.findViewById(R.id.edit_list_icon);
            ImageView remove_icon = (ImageView)row.findViewById(R.id.delete_list_icon);
            if(this.listenerEdit != null &&
                this.listenerRemove != null){
                edit_icon.setOnClickListener(this.listenerEdit);
                remove_icon.setOnClickListener(this.listenerRemove);
            }
        }

        // set list id
        id.setText(listsList.get(position).getListID());

        return row;
    }
}
