package com.example.raja.manageaccounts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by raja on 09/06/17.
 */

public class DbHandler extends SQLiteOpenHelper {

    private static String db_name="manage_accounts";
    private static int db_version=6;

    public DbHandler(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.d("ilaya","start create");
        db.execSQL("create table if not exists people(" +
                "pid integer primary key,"+
                "person varchar(50) not null unique,"+
                "cumulative_value float not null default 0,"+
                "last_update datetime default current_timestamp"+
                ");"
        );
        db.execSQL("create table if not exists transactions("+
                "tid integer primary key,"+
                "pid integer not null,"+
                "time_of_transaction datetime default current_timestamp,"+
                "amount float not null,"+
                "description varchar(144)"+
                ");"
        );
        //create dummy values
//        Log.d("ilaya","before inserted");
//        db.execSQL("insert into people(person,cumulative_value) values ('deva',200);");
//        db.execSQL("insert into people(person,cumulative_value) values ('amma',300);");
//        db.execSQL("insert into people(person,cumulative_value) values ('appu',100);");
//        Log.d("ilaya","inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.d("ilaya","update");
        switch(oldVersion)
        {
            case 1:
            case 2:
            case 3://backup
            case 4://prepared statements
            case 5:
            case 6:
                try {
                    db.execSQL("create unique index I_person on people(person);");
                } catch(Exception e) {
                    db.execSQL("insert into people(person,cumulative_value) values ('export and then reinstall after fixing name conflicts',0);");
                }
        }
    }

    public void importDatabase(JSONObject data) throws JSONException {
        if(data.get("purpose").equals("manageaccounts"))
        {
            int conventionFactor = 1;
            if(!data.get("convention").equals("plus-lending"))
            {
                conventionFactor = -1;
            }
            JSONArray peopleList = (JSONArray) data.get("people");

            for(int p=0;p<peopleList.length();p++)
            {
                JSONObject person = (JSONObject)peopleList.get(p);
                long pid;
                try {

                    pid = addPerson(
                            person.getString("name"),
                            (float) person.getDouble("amount") * conventionFactor,
                            person.getString("last")
                    );
                    Log.d("ilaya_import", "" + pid);

                    JSONArray transList = (JSONArray) person.get("transactions");
                    for (int t = 0; t < transList.length(); t++) {
                        JSONObject trans = (JSONObject) transList.get(t);
                        addMoneyTo(
                                pid,
                                trans.getDouble("amount") * conventionFactor,
                                trans.getString("description"),
                                trans.getString("datetime"));
                    }
                } catch (SQLiteConstraintException sce) {
                    sce.printStackTrace();
                }
            }
        }
    }

    public JSONObject exportDatabase() throws JSONException {
        JSONObject data=new JSONObject();
        data.put("purpose","manageaccounts");
        data.put("convention","plus-lending");
        SQLiteDatabase db = this.getWritableDatabase();
        JSONArray peopleList= new JSONArray();
        String query="select pid,person,cumulative_value,last_update from people order by last_update desc;";
        Cursor cursor= db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject o=new JSONObject();
                o.put("name",cursor.getString(1));
                o.put("amount",cursor.getString(2));
                o.put("last",cursor.getString(3));
                JSONArray transList= new JSONArray();
                String queryT="select tid,pid,datetime(time_of_transaction,'localtime'),amount,description from transactions where pid="+cursor.getString(0)+" order by tid desc;";
                Cursor cursorT= db.rawQuery(queryT,null);
                if (cursorT.moveToFirst()) {
                    do {
                        JSONObject t=new JSONObject();
                        t.put("datetime",cursorT.getString(2));
                        t.put("amount",cursorT.getString(3));
                        t.put("description",cursorT.getString(4));
                        transList.put(t);
                    } while (cursorT.moveToNext());
                }

                cursorT.close();
                o.put("transactions",transList);
                peopleList.put(o);
            } while (cursor.moveToNext());
            data.put("people",peopleList);
        }
        db.close();
        Log.d("ilaya_db",data.toString());
        cursor.close();
        return data;
    }

    public void addMoneyTo(int pid, double amount, String description, float pastCumulative)
    {
        addMoneyTo(pid,amount,description,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString());
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update people set cumulative_value="+(pastCumulative+amount)+",last_update= \'"+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString()+"\' where pid="+pid+";");
    }

    private void addMoneyTo(long pid, double amount, String description, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "insert into transactions(pid,amount,description,time_of_transaction) values(?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindLong(1, pid);
        statement.bindDouble(2,amount);
        statement.bindString(3,description);
        statement.bindString(4,date);
        statement.executeInsert();
        //db.execSQL("insert into transactions(pid,amount,description) values("+pid+","+amount+",'"+description+"');");
    }

    public void addPerson(String person) throws SQLiteConstraintException
    {
        addPerson(person,0f,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString());
    }

    private long addPerson(String person, float amount, String last) throws SQLiteConstraintException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "insert into people(person,cumulative_value,last_update) values (?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, person);
        statement.bindDouble(2,amount);
        statement.bindString(3, last);
        long i;
        i = statement.executeInsert();
        return i;

    }

    public void deletePerson(int pid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from people where pid="+pid+";");
        db.execSQL("delete from transactions where pid="+pid+";");
    }

    public ArrayList<Person> getPeopleInOrder()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Person> peopleList= new ArrayList<>();
        String query="select pid,person,cumulative_value from people order by last_update desc;";
        Cursor cursor= db.rawQuery(query,null);
//        Log.d("ilaya","query sucess "+cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
//                Log.d("ilaya","pid "+cursor.getString(0));
//                Log.d("ilaya",cursor.getString(1));
//                Log.d("ilaya",cursor.getString(2));
                Person person = new Person();
                // exception solved
                person.setPid(Integer.parseInt(cursor.getString(0)));
                person.setName(cursor.getString(1));
                person.setCumulative_value(Float.parseFloat(cursor.getString(2)));
                peopleList.add(person);
//                Log.d("ilaya","taken 1 row "+cursor.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return peopleList;
    }

    public ArrayList<Transactions> getTransactionsFor(int pid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Transactions> transList= new ArrayList<Transactions>();
//        Log.d("ilaya_db","db: pid = "+pid);
        String query="select tid,pid,datetime(time_of_transaction,'localtime'),amount,description from transactions where pid="+pid+" order by time_of_transaction desc;";
        Cursor cursor= db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                Transactions trans = new Transactions();
                trans.setTid(Integer.parseInt(cursor.getString(0)));
                trans.setPid(Integer.parseInt(cursor.getString(1)));
//                Log.d("ilaya","going to insert date"+cursor.getString(2));
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d=new Date();
                try {
                    d=(Date)formatter.parse(cursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                trans.setTime_of_transaction(d);
                trans.setAmount(Float.parseFloat(cursor.getString(3)));
                trans.setDescription(cursor.getString(4));
                transList.add(trans);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transList;
    }
}
