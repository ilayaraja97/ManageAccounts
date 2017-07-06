package com.example.raja.manageaccounts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static int db_version=2;

    public DbHandler(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.d("ilaya","start create");
        db.execSQL("create table if not exists people(" +
                "pid integer primary key,"+
                "person varchar(50) not null,"+
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
        }
    }

    public void addMoneyTo(int pid,float amount,String description,float pastCumulative)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into transactions(pid,amount,description) values("+pid+","+amount+",'"+description+"');");
        db.execSQL("update people set cumulative_value="+(pastCumulative+amount)+",last_update= current_timestamp where pid="+pid+";");
    }

    public void addPerson(String person)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into people(person) values ('"+person+"');");
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
        Log.d("ilaya","query sucess "+cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
                Log.d("ilaya","pid "+cursor.getString(0));
                Log.d("ilaya",cursor.getString(1));
                Log.d("ilaya",cursor.getString(2));
                Person person = new Person();
                // exception solved
                person.setPid(Integer.parseInt(cursor.getString(0)));
                person.setName(cursor.getString(1));
                person.setCumulative_value(Float.parseFloat(cursor.getString(2)));
                peopleList.add(person);
                Log.d("ilaya","taken 1 row "+cursor.toString());
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
        String query="select tid,pid,time_of_transaction,amount,description from transactions where pid="+pid+" order by tid desc;";
        Cursor cursor= db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                Transactions trans = new Transactions();
                trans.setTid(Integer.parseInt(cursor.getString(0)));
                trans.setPid(Integer.parseInt(cursor.getString(1)));
                Log.d("ilaya","going to insert date");
                DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
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
