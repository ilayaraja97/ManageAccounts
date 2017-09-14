package com.example.raja.manageaccounts;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddMoneyDialogFragment.MoneyDialogListener, AddPersonDialogFragment.PersonDialogListener {

    ListView lv_accounts1;
    //String accounts[]={"raja","deva","dad","amma"};
    PersonAdapter adapter;
    int selectedPid=0;
    float selectedAmount;
    String selectedName;
    ArrayList<Person> people;
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.d("ilaya_version","current "+BuildConfig.VERSION_NAME);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject jsonObject = getJSONObjectFromURL("http://ilayaraja97.000webhostapp.com/version.json");
                    String latest= jsonObject.getString("versionName");
//                    Log.d("ilaya_version","latest "+latest);
//                    Log.d("ilaya_version","current "+BuildConfig.VERSION_NAME);
                    String current=BuildConfig.VERSION_NAME;
                    current.split(".");
                    if(!BuildConfig.VERSION_NAME.equals(latest)){
                        new Thread(){
                            public void run()
                            {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Update your app", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }.start();


                    }
                } catch (JSONException e) {
                    Log.d("ilaya_version","exception");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("ilaya_version","IO");
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

        lv_accounts1=(ListView)findViewById(R.id.lv_1);

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_activated_1,accounts);
//        lv_accounts1.setAdapter(adapter);
        DbHandler db=new DbHandler(this);
//        Log.d("ilaya","adapter gonna be made");
        people= db.getPeopleInOrder();
        adapter = new PersonAdapter(this,R.layout.person_row_view, people);
//        Log.d("ilaya","adapter made");
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
    private ProgressBar spinner;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.add:
                DialogFragment dialog = new AddPersonDialogFragment();
                dialog.show(getFragmentManager(), "AddPersonDialogFragment");
                return true;
            case R.id.export:
                ExportImport.export(getApplicationContext());
                Toast.makeText(this, "Backup created in Downloads folder", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.importDb:
                new ProgressTask().execute();
                return true;
            case R.id.settings:
                intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
//                Toast.makeText(getApplicationContext(), "help is on ur way!", Toast.LENGTH_SHORT).show();
                intent=new Intent(this,HelpActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class ProgressTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ExportImport.importDb(getApplicationContext());

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            DbHandler db=new DbHandler(MainActivity.this);
            people = db.getPeopleInOrder();
            spinner.setVisibility(View.GONE);
            db.close();
            Toast.makeText(MainActivity.this, "Restored from backup in Downloads folder", Toast.LENGTH_SHORT).show();
            adapter.clear();
            adapter.addAll(people);
            adapter.notifyDataSetChanged();
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
        selectedName=person.getName();
//        Log.d("ilaya","pid "+selectedPid);
        if(item.getTitle()=="take or give money"){
//            Toast.makeText(getApplicationContext(),"take or give money code",Toast.LENGTH_LONG).show();
            DialogFragment dialog = new AddMoneyDialogFragment();
            dialog.show(getFragmentManager(), "AddMoneyDialogFragment");
        }
        else if(item.getTitle()=="delete"){
            Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_LONG).show();
            DbHandler db=new DbHandler(this);
            db.deletePerson(selectedPid);
            people = db.getPeopleInOrder();
            adapter.clear();
            adapter.addAll(people);
            adapter.notifyDataSetChanged();
//            finish();
//            startActivity(getIntent());
        }else{
            Intent intent=new Intent(this,Main2Activity.class);
            intent.putExtra("pid",selectedPid);
            intent.putExtra("name",selectedName);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String description, double amount){
//        android.widget.Toast.makeText(getApplicationContext(), "ok from main", Toast.LENGTH_SHORT).show();
//        Log.d("ilaya","pos click");
        DbHandler db= new DbHandler(this);
//        Log.d("ilaya_add","description "+amount);
        db.addMoneyTo(selectedPid,
                amount,
                description,
                selectedAmount);
        people = db.getPeopleInOrder();
        adapter.clear();
        adapter.addAll(people);
        adapter.notifyDataSetChanged();
//        finish();
//        startActivity(getIntent());
    }
    @Override
    public void onDialogNeutralClick(DialogFragment dialog){
//        android.widget.Toast.makeText(getApplicationContext(), "ok from main", Toast.LENGTH_SHORT).show();
//        Log.d("ilaya","pos click");
        DbHandler db= new DbHandler(this);
//        Log.d("ilaya_add","description "+amount);

        db.addMoneyTo(selectedPid,
                -selectedAmount,
                "settled",
                selectedAmount);
        people = db.getPeopleInOrder();
        adapter.clear();
        adapter.addAll(people);
        adapter.notifyDataSetChanged();
//        finish();
//        startActivity(getIntent());
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        DbHandler db= new DbHandler(this);
        try {
            db.addPerson(name);
        }catch (SQLiteConstraintException sce){
            Toast.makeText(this, "person with name already exists", Toast.LENGTH_SHORT).show();
        }
        people = db.getPeopleInOrder();
        db.close();
        adapter.clear();
        adapter.addAll(people);
        adapter.notifyDataSetChanged();
//        finish();
//        startActivity(getIntent());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        android.widget.Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();
//        Log.d("ilaya_version","connected");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
//        Log.d("ilaya_version","JSON: " + jsonString);

        return new JSONObject(jsonString);
    }
}

