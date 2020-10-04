package com.example.foxykat.ui.home;

import android.app.Person;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.foxykat.message;
import com.example.foxykat.person;

import java.util.ArrayList;


public class dbchat {
    myDbHelper myhelper;
    ArrayList<person> people ;
    Context context;
    ArrayList<message> letter;
    public dbchat(Context context)
    {
        this.context = context;
        myhelper = new myDbHelper(context);
        people = new ArrayList<person>();
       letter = new ArrayList<message>();
    }

    public long insertData(String name, String phone,String msg,String img,String time)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.Phone, phone);
        contentValues.put(myDbHelper.Msg, msg);
        contentValues.put(myDbHelper.Img, img);
        contentValues.put(myDbHelper.Time, time);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return id;
    }
    public int checkUser(String pno){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID, myDbHelper.NAME, myDbHelper.Phone, myDbHelper.Msg, myDbHelper.Img, myDbHelper.Time};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));

            String  phone =cursor.getString(cursor.getColumnIndex(myDbHelper.Phone));
            if (phone.equals(pno)){
                return 1;
            }
        }
        return 0;
    }

    public ArrayList<person> getData()
        {

            SQLiteDatabase db = myhelper.getWritableDatabase();
            String[] columns = {myDbHelper.UID, myDbHelper.NAME, myDbHelper.Phone, myDbHelper.Msg, myDbHelper.Img, myDbHelper.Time};
            Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
            StringBuffer buffer= new StringBuffer();
            while (cursor.moveToNext())
            {
                int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
                String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
                String  phone =cursor.getString(cursor.getColumnIndex(myDbHelper.Phone));
                String  msg =cursor.getString(cursor.getColumnIndex(myDbHelper.Msg));
                String  img =cursor.getString(cursor.getColumnIndex(myDbHelper.Img));
                String  time =cursor.getString(cursor.getColumnIndex(myDbHelper.Time));
                people.add(0,new person(phone,img,msg,name,time));
            }
            return people;
    }

    public  int delete(String uname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete(myDbHelper.TABLE_NAME , myDbHelper.NAME+" = ?",whereArgs);
        return  count;
    }

    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
        return count;
    }


    public int updatelastMessage(String mob , String m)
    {

        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.Msg,m);
        String[] whereArgs= {mob};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.Phone+" = ?",whereArgs );
        return count;
    }







    //Defining function for chat table:


    //Insertion data

    public long insertMsgData(String mid,String name, String phone,String msg,String img,int token ,int seen,String time)
    {

        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.Mid, mid);
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.Phone, phone);
        contentValues.put(myDbHelper.Msg, msg);
        contentValues.put(myDbHelper.Img, img);
        contentValues.put(myDbHelper.Token, token);
        contentValues.put(myDbHelper.Seen, seen);
        contentValues.put(myDbHelper.Time, time);
        long id = dbb.insert(myDbHelper.MSG_TABLE_NAME, null , contentValues);
        return id;
    }
    //getMSgDAta

    public ArrayList<message> getMsgData(String mob)
    {
        letter.clear();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID ,myDbHelper.Mid,myDbHelper.NAME, myDbHelper.Phone, myDbHelper.Msg, myDbHelper.Img,myDbHelper.Token,myDbHelper.Seen, myDbHelper.Time};
        Cursor cursor =db.query(myDbHelper.MSG_TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
         //   Toast.makeText(context,"getting data for " + mob , Toast.LENGTH_SHORT).show();
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String mid = cursor.getString(cursor.getColumnIndex(myDbHelper.Mid));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            String  phone =cursor.getString(cursor.getColumnIndex(myDbHelper.Phone));
            String  msg =cursor.getString(cursor.getColumnIndex(myDbHelper.Msg));
            String  img =cursor.getString(cursor.getColumnIndex(myDbHelper.Img));
            String  time =cursor.getString(cursor.getColumnIndex(myDbHelper.Time));
            int token =cursor.getInt(cursor.getColumnIndex(myDbHelper.Token));
            int seen =cursor.getInt(cursor.getColumnIndex(myDbHelper.Seen));
            if(mob.equals(phone)) {
                letter.add(0, new message(cid,phone, img, msg, name, token, seen, time));
               // int id ,String phone, String img , String msg, String name  , int token, int seen,String time)
            }
        }
        return letter;
    }

//update seen param


    public int updateSeen(int id , int value)
    {

        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.Seen,value);
        String[] whereArgs= {Integer.toString(id)};
        int count =db.update(myDbHelper.MSG_TABLE_NAME,contentValues, myDbHelper.UID+" = ?",whereArgs );
        return count;
    }

    public int checkMsg(String did){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID ,myDbHelper.Mid,myDbHelper.NAME, myDbHelper.Phone, myDbHelper.Msg, myDbHelper.Img,myDbHelper.Token,myDbHelper.Seen, myDbHelper.Time};
        Cursor cursor =db.query(myDbHelper.MSG_TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));

            String  mid =cursor.getString(cursor.getColumnIndex(myDbHelper.Mid));
            if (mid.equals(did)){
                return 1;
            }
        }
        return 0;
    }







    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "FoxyKat";    // Database Name
        private static final String TABLE_NAME = "users";   // Table Name

        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String NAME = "Name";    //Column II
        private static final String Phone= "Phone";    // Column III
        private static final String Msg= "Msg";// Column IV
        private static final String Img= "Img";// Column V
        private static final String Time= "Time";// Column VI

        private static final String MSG_TABLE_NAME = "chats";   //  chats Table Name
        private static final String Token= "Token";// Column VI
        private static final String Seen= "Seen";// Column VI

        private static final String Mid= "Mid";// Column VI
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"+Phone+" VARCHAR(255) ,"+Msg+" VARCHAR(255) ,"+ Img+" VARCHAR(225) ," + Time + " VARCHAR(225));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        private static final String CREATE_MSG_TABLE = "CREATE TABLE "+ MSG_TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Mid+" VARCHAR(255) ,"+NAME+" VARCHAR(255) ,"+Phone+" VARCHAR(255) ,"+Msg+" VARCHAR(255) ,"+ Img+" VARCHAR(225) ,"+ Token+" Integer ,"+ Seen +" Integer ," + Time + " VARCHAR(225));";



        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {

                //eror
            }

            try {
                db.execSQL(CREATE_MSG_TABLE);
            } catch (Exception e) {

                //eror
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {

                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
               // Message.message(context,""+e);
            }
        }
    }
}