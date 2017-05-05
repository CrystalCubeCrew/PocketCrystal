package edu.temple.crystalcube11;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hongear on 5/5/17.
 */

public class ContactsArrayListAdapter extends ArrayAdapter<Contacts> {
    private final Context mContext;
    private final ArrayList<Contacts> data;
    private final int layoutResourceId;

    public ContactsArrayListAdapter(Context context, int resource, ArrayList<Contacts> data) {
        super(context, resource, data);
        this.mContext = context;
        this.data = data;
        this.layoutResourceId = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView) row.findViewById(R.id.contact_name);
            holder.textView2 = (TextView) row.findViewById(R.id.contact_number);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Contacts mContact = data.get(position);

        holder.textView1.setText(mContact.getName());
        holder.textView2.setText(mContact.getNumber());

        return row;
    }

    static class ViewHolder {
        TextView textView1;
        TextView textView2;
    }
}
