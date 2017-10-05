package com.example.raja.manageaccounts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Help");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ExpandableListView expandableListView=(ExpandableListView) findViewById(R.id.exlv_1);
        List<String> headers = new ArrayList<>();
        HashMap<String, List<String>> items = new HashMap<>();

        //awesome hardcoding
        headers.add("Feedback and support");
        headers.add("Credits");
        headers.add("What is a convention");
        headers.add("How to migrate conventions");
        headers.add("How to create backups");
        headers.add("How to import");
        headers.add("Using expressions when adding money");
        headers.add("What is a transaction");
        headers.add("Can I delete transactions");
        headers.add("How to change a transaction");
        headers.add("What is an encryption and decryption key");

        for (String e:headers
             ) {
            items.put(e, new ArrayList<String>());
        }

        items.get(headers.get(0))
                .add("For any queries outside the ones covered in this help section and to propose" +
                        " any idea drop an email on ilayaraja97@gmail.com Subject shall be" +
                        " Manage Accounts.");
        items.get(headers.get(1))
                .add("Some of the artwork was taken from " +
                        "http://www.flaticon.com/authors/madebyoliver check flaticon for more.");
        items.get(headers.get(2))
                .add("A convention is the interpretation which you follow. In this app it's " +
                        "positive for borrowing for some people and positive for lending for others " +
                        "the app asks you for this in the beginning and can't be changed easily.");
        items.get(headers.get(3))
                .add("Create a backup then reinstall the app. On first open select convention you want" +
                        " then import all you data.");
        items.get(headers.get(4))
                .add("Just go to settings to create a backup. The backup will be stored in your " +
                        "default downloads folder by the name manageaccounts.backup it is " +
                        "encrypted with AES so don't worry about letting others have it, as they " +
                        "can't use it.\n\nIf you close the app also it will prompt you to backup & close." +
                        " This is by far the easiest way.\n\nDon't worry though backups are " +
                        "created really fast and don't consume extra memory. ");
        items.get(headers.get(5))
                .add("To import you need to go to the settings. It might take some time so be patient " +
                        "as even the UI will freeze. To import a file successfully the decryption " +
                        "key of the import must be same as the encryption key with which the backup" +
                        " was created." +
                        "\n\nThere should be a file named manageaccounts.backup in your default " +
                        "downloads folder.");
        items.get(headers.get(6))
                .add("The expressions support when adding money is huge. It supports all types of " +
                        "meaning full expressions. Powered by the exp4j mathematical expression " +
                        "parser.");
        items.get(headers.get(7))
                .add("When you record an action of borrowing or lending. That is a transaction.");
        items.get(headers.get(8))
                .add("For integrity, you can't delete or modify a transaction in anyway.");
        items.get(headers.get(9))
                .add("For integrity, you can't delete or modify a transaction in anyway. \n\nIf " +
                        "you do want to change the total amount make another transaction to " +
                        "correct the final amount.\n\nIf you want to change time specify that in description.");
        items.get(headers.get(10))
                .add("This is the key with which the backup is encrypted or decrypted");
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(getApplicationContext(),headers,items);
        expandableListView.setAdapter(listAdapter);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
