package com.example.raja.manageaccounts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
            holder.img = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (TransactionAdapter.ViewHolder) convertView.getTag();
        }
//        Log.d("ilaya",searchArrayList.get(position).getDescription());
        holder.txtId.setText(String.valueOf(searchArrayList.get(position).getPid()));
        holder.txtName.setText(searchArrayList.get(position).getDescription());
        float val = searchArrayList.get(position).getAmount();
        holder.txtAmount.setText(String.valueOf(val));
//        Log.d("ilaya","before time");
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Regular.ttf");
        holder.txtName.setTypeface(tf);
        tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Medium.ttf");
        holder.txtAmount.setTypeface(tf);
        convertView.setBackgroundResource(R.drawable.cardshape);
        if(val<0)
            holder.txtAmount.setTextColor(Color.parseColor("#AA3939"));
        else if(val>0)
            holder.txtAmount.setTextColor(Color.parseColor("#2D882D"));
        else
            holder.txtAmount.setTextColor(Color.GRAY);
        holder.img.setVisibility(View.VISIBLE);
        if(Math.abs(val)<5)
            holder.img.setVisibility(View.INVISIBLE);
        if(Math.abs(val)<100)
            holder.img.setImageResource(R.mipmap.coin);
        else if(Math.abs(val)<500)
            holder.img.setImageResource(R.mipmap.coins);
        else
            holder.img.setImageResource(R.mipmap.notes);
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
        ImageView img;
    }
}
