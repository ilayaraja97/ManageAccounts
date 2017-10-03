package com.example.raja.manageaccounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Created by raja on 16/06/17.
 */

public class AddMoneyDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.dialog_add_money,null);

        if(!MainActivity.convention)
        {
            ((RadioButton)view.findViewById(R.id.plus)).setText(R.string.borrow, TextView.BufferType.EDITABLE);
            ((RadioButton)view.findViewById(R.id.minus)).setText(R.string.lend, TextView.BufferType.EDITABLE);
        }
        else
        {
            ((RadioButton)view.findViewById(R.id.plus)).setText(R.string.lend, TextView.BufferType.EDITABLE);
            ((RadioButton)view.findViewById(R.id.minus)).setText(R.string.borrow, TextView.BufferType.EDITABLE);
        }
        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //something good must happen
                        String descri=((EditText)view.findViewById(R.id.description)).getText().toString();
                        Expression expression;
                        try {
                            expression = new ExpressionBuilder(
                                    ((EditText) view.findViewById(R.id.amount)).getText().toString()
                            ).build();
                        }catch (Exception e){
                            mListener.onDialogNegativeClick(AddMoneyDialogFragment.this);
                            return;
                        }

                        double amount;
                        if(descri.equals(""))
                        {
                            descri="no comments";
                        }
                        try {
                            //amount = Float.valueOf(((EditText) view.findViewById(R.id.amount)).getText().toString());
//                            Log.d("ilaya_radio","hi");
                            amount=Math.round(expression.evaluate()*100D)/100D;
                            if(((RadioButton)view.findViewById(R.id.minus)).isChecked())
                            {
//                                Log.d("ilaya_radio","minus checked");
                                amount=-amount;
                            }
                        }catch(Exception e){
                            mListener.onDialogNegativeClick(AddMoneyDialogFragment.this);
                            return;
                        }
                        mListener.onDialogPositiveClick(AddMoneyDialogFragment.this,descri,amount);
                    }
                });
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel this shit
                        mListener.onDialogNegativeClick(AddMoneyDialogFragment.this);
                    }
                });
        builder.setView(view)
                .setNeutralButton("settle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                    mListener.onDialogNeutralClick(AddMoneyDialogFragment.this);
                }
        });
        return builder.create();
    }
    public interface MoneyDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String description, double amount);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onDialogNeutralClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    MoneyDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MoneyDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @SuppressWarnings("deparcation")
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddMoneyDialogFragment.MoneyDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

}
