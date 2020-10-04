package com.example.foxykat;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.foxykat.personAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foxykat.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class botnav extends AppCompatActivity  implements   personAdapter.ItemClicked , HomeFragment.Contact , SwipeRefreshLayout.OnRefreshListener{
    int backButtonCount = 0;
    private static final int ACTIVITYuserdet = 2 ,ACTIVITYchat =3;
    Cursor c ,k;
    int i=0 ,j = 0;
    Map.Entry map;
    Iterator iterator;
    String pno ,cn;







    private static final String TAG = "ContactReading";
    ArrayList<person> people;
    ArrayList<String> a1 ,a2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botnav);




        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



    }






  /* For Direct back  exit app
    @Override

    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


   */



    @Override
    public void onItemClicked(int index, ArrayList<person> people) {
        Intent intent = new Intent(botnav.this ,com.example.foxykat.MessageListActivity.class);
        intent.putExtra("nam",people.get(index).getName());
        intent.putExtra("pno",people.get(index).getPhone());
        startActivityForResult(intent,ACTIVITYchat);

//        Toast.makeText(this, "Chat with "  + people.get(index).getPhone(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImgClicked(int index, ArrayList<person> people) {
        Toast.makeText(this, "img Clicked !!", Toast.LENGTH_SHORT).show();
    }

//////--------------------------------------------
    @Override
    public ArrayList<person> contacts() {
        people = new ArrayList<person>();


        k= getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.Contacts.DISPLAY_NAME + " ASC " );
        k.moveToFirst();
        while(k.moveToNext()) {
            cn = k.getString(k.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            pno = k.getString(k.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


          people.add(new person(pno,"/chandu.jp", pno, cn, "21:00"));
        }
        Log.d("rETURN", " FROM BOTNAV "  );
        return people;

    }


    @Override
    public void onRefresh() {

    }




}