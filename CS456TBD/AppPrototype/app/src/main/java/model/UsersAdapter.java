package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tbd.appprototype.R;

import java.util.ArrayList;

import networking.testing.NetworkTest;

/**
 * Created by mitch10e on 12 March 2016.
 */
public class UsersAdapter extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> users;

    public UsersAdapter (Context context, ArrayList<User> users) {
        super(context, R.layout.list_item_generic, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_generic, parent, false);
        TextView testNameView = (TextView) rowView.findViewById(R.id.item_name);

        testNameView.setText(users.get(position).getUsername());

        return rowView;
    }

}
