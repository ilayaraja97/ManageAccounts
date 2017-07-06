package com.example.raja.manageaccounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by raja on 16/06/17.
 */

public class AddMoneyDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.dialog_add_money,null);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //something good must happen
                        String descri=((EditText)view.findViewById(R.id.description)).getText().toString();
                        float amount;
                        if(descri.equals(""))
                        {
                            descri="no comment about this transaction";
                        }
                        try {
                            amount = Float.valueOf(((EditText) view.findViewById(R.id.amount)).getText().toString());
                        }catch(Exception e){
                            amount=0f;
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
        return builder.create();
    }
    public interface MoneyDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog,String description,Float amount);
        public void onDialogNegativeClick(DialogFragment dialog);
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

}
