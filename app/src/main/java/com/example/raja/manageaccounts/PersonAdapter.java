package com.example.raja.manageaccounts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by raja on 10/06/17.
 */

public class PersonAdapter extends ArrayAdapter {

    private static ArrayList<Person> searchArrayList;

    private LayoutInflater mInflater;

    public PersonAdapter(Context context, int textViewResourceId, ArrayList<Person> objects) {
        super(context, textViewResourceId, objects);
        searchArrayList = objects;
        mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Log.d("ilaya","constructed");
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
//            Log.d("ilaya","inflate start");
            //exception
            convertView = mInflater.inflate(R.layout.person_row_view,null);
//            Log.d("ilaya","inflate done");
            holder = new ViewHolder();
            holder.txtId = (TextView) convertView.findViewById((R.id.id));
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        Log.d("ilaya",searchArrayList.get(position).getName());
        holder.txtId.setText(String.valueOf(searchArrayList.get(position).getPid()));
        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtAmount.setText(String.valueOf(searchArrayList.get(position).getCumulative_value()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ilaya","clicked on ");
                view.performLongClick();
            }
        });
//        Log.d("ilaya","done setting text in list");
        return convertView;
    }

    private static class ViewHolder {
        TextView txtId;
        TextView txtName;
        TextView txtAmount;
    }
}
