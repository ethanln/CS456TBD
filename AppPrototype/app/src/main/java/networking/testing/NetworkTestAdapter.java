package networking.testing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tbd.appprototype.R;

import java.util.ArrayList;

/**
 * Created by mitch10e on 12 March 2016.
 */
public class NetworkTestAdapter extends ArrayAdapter<NetworkTest> {
    private Context context;
    private ArrayList<NetworkTest> tests;

    public NetworkTestAdapter (Context context, ArrayList<NetworkTest> tests) {
        super(context, R.layout.list_item_network_test, tests);
        this.context = context;
        this.tests = tests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_network_test, parent, false);
        TextView testNameView = (TextView) rowView.findViewById(R.id.test_name);

        testNameView.setText(tests.get(position).getName());

        return rowView;
    }

}
