package com.example.foxykat;

import android.content.Context;
import android.content.SharedPreferences;

//Shared Preference class
public class userbio {
    String Phone = "com.example.foxykat.Phone";
    Context context;
    public userbio(Context context) {

        this.context = context;

    }

    public String getMob() {
        try {


            SharedPreferences sharedPref = context.getSharedPreferences(Phone, Context.MODE_PRIVATE);

            String defaultValue = "DefaultName";

            String name = sharedPref.getString("phone", defaultValue);
            return name;



        }catch(Exception e){
            e.printStackTrace();
            return "";
        }


    }
    public String getName() {
        try {


            SharedPreferences sharedPref = context.getSharedPreferences(Phone, Context.MODE_PRIVATE);

            String defaultValue = "DefaultName";

            String name = sharedPref.getString("name", defaultValue);
            return name;



        }catch(Exception e){
            e.printStackTrace();
            return "";
        }


    }



    public void save(String mob, String name) {

        SharedPreferences sharedPref = context.getSharedPreferences(Phone,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("name", name);
        editor.putString("phone", mob);

        editor.commit();
    }

}

