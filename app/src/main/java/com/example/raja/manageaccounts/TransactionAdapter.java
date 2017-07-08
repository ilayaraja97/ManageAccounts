package com.example.raja.manageaccounts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
/**
 * Created by raja on 22/06/17.
 */

public class TransactionAdapter extends ArrayAdapter {
    private static ArrayList<Transactions> searchArrayList;

    private LayoutInflater mInflater;

    public TransactionAdapter(Context context, int textViewResourceId, ArrayList<Transactions> objects) {
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
        TransactionAdapter.ViewHolder holder;
        if (convertView == null) {
//            Log.d("ilaya","inflate start");
            //exception
            convertView = mInflater.inflate(R.layout.transaction_row_view,null);
//            Log.d("ilaya","inflate done");
            holder = new TransactionAdapter.ViewHolder();
            holder.txtId = (TextView) convertView.findViewById((R.id.id));
            holder.txtName = (TextView) convertView.findViewById(R.id.description);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.amount);
            holder.txtTime = (TextView) convertView.findViewById(R.id.date_time);
            convertView.setTag(holder);
        } else {
            holder = (TransactionAdapter.ViewHolder) convertView.getTag();
        }
//        Log.d("ilaya",searchArrayList.get(position).getDescription());
        holder.txtId.setText(String.valueOf(searchArrayList.get(position).getPid()));
        holder.txtName.setText(searchArrayList.get(position).getDescription());
        holder.txtAmount.setText(String.valueOf(searchArrayList.get(position).getAmount()));
//        Log.d("ilaya","before time");

//        Log.d("ilaya","after time"+searchArrayList.get(position).getTime_of_transaction());
        holder.txtTime.setText(DateDifference.relativeDate(searchArrayList.get(position).getTime_of_transaction()));
//        Log.d("ilaya","after adding to txtv");
//        Log.d("ilaya","done setting text in list");
        return convertView;
    }

    private static class ViewHolder {
        TextView txtId;
        TextView txtName;
        TextView txtAmount;
        TextView txtTime;
    }
}
