package com.example.raja.manageaccounts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    ListView lv_history;
    //String accounts[]={"raja","deva","dad","amma"};
    int selectedPid=0;
    float selectedAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent=getIntent();

        selectedPid=intent.getIntExtra("pid",0);
        setTitle(intent.getStringExtra("name"));
        lv_history=(ListView)findViewById(R.id.lv_2);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_activated_1,accounts);
//        lv_accounts1.setAdapter(adapter);
        DbHandler db=new DbHandler(this);
//        Log.d("ilaya","adapter gonna be made");
        ArrayList<Transactions> people= db.getTransactionsFor(selectedPid);
        TransactionAdapter adapter = new TransactionAdapter(this,R.layout.transaction_row_view, people);
//        Log.d("ilaya","adapter made");
        lv_history.setAdapter(adapter);
        registerForContextMenu(lv_history);
    }
}

