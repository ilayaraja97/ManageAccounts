package com.example.raja.manageaccounts;

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
 * Created by raja on 06/07/17.
 */

public class AddPersonDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.dialog_add_person,null);

        builder.setView(view)
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //something good must happen
                        String name=((EditText)view.findViewById(R.id.add_name)).getText().toString();
                        if(name.equals(""))
                        {
                            mListener.onDialogNegativeClick(AddPersonDialogFragment.this);
                        }
                        mListener.onDialogPositiveClick(AddPersonDialogFragment.this,name);
                    }
                });
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel this shit
                        mListener.onDialogNegativeClick(AddPersonDialogFragment.this);
                    }
                });
        return builder.create();
    }
    public interface PersonDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog,String name);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddPersonDialogFragment.PersonDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddPersonDialogFragment.PersonDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
