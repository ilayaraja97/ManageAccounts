package com.example.raja.manageaccounts;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddMoneyDialogFragment.MoneyDialogListener, AddPersonDialogFragment.PersonDialogListener {

    ListView lv_accounts1;
    //String accounts[]={"raja","deva","dad","amma"};
    int selectedPid=0;
    float selectedAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_accounts1=(ListView)findViewById(R.id.lv_1);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_activated_1,accounts);
//        lv_accounts1.setAdapter(adapter);
        DbHandler db=new DbHandler(this);
        Log.d("ilaya","adapter gonna be made");
        ArrayList<Person> people= db.getPeopleInOrder();
        PersonAdapter adapter = new PersonAdapter(this,R.layout.person_row_view, people);
        Log.d("ilaya","adapter made");
        lv_accounts1.setAdapter(adapter);
        registerForContextMenu(lv_accounts1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add:
                Toast.makeText(getApplicationContext(), "add a new person", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                Toast.makeText(getApplicationContext(), "help is on ur way!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");

        menu.add(0, v.getId(), 0, "take or give money");//groupId, itemId, order, title
        menu.add(0,v.getId(),0,"view history");
        menu.add(0, v.getId(), 0, "delete");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Person person = ((Person)lv_accounts1.getItemAtPosition(info.position)); // who the hell knew this!!
        selectedAmount=person.getCumulative_value();
        selectedPid=person.getPid();
        Log.d("ilaya","pid "+selectedPid);
        if(item.getTitle()=="take or give money"){
            Toast.makeText(getApplicationContext(),"take or give money code",Toast.LENGTH_LONG).show();
            DialogFragment dialog = new AddMoneyDialogFragment();
            dialog.show(getFragmentManager(), "AddMoneyDialogFragment");
        }
        else if(item.getTitle()=="delete"){
            Toast.makeText(getApplicationContext(),"deleting...",Toast.LENGTH_LONG).show();
            DbHandler db=new DbHandler(this);
            db.deletePerson(selectedPid);
            finish();
            startActivity(getIntent());
        }else{
            Intent intent=new Intent(this,Main2Activity.class);
            intent.putExtra("pid",selectedPid);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog,String description,Float amount){
        android.widget.Toast.makeText(getApplicationContext(), "ok from main", Toast.LENGTH_SHORT).show();
//        Log.d("ilaya","pos click");
        DbHandler db= new DbHandler(this);
//        Log.d("ilaya_add","description "+amount);
        db.addMoneyTo(selectedPid,
                amount,
                description,
                selectedAmount);
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        DbHandler db= new DbHandler(this);
        db.addPerson(name);
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        android.widget.Toast.makeText(getApplicationContext(), "cancel from main", Toast.LENGTH_SHORT).show();
    }

}

