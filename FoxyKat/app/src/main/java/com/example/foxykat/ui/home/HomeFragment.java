package com.example.foxykat.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foxykat.R;
import com.example.foxykat.botnav;
import com.example.foxykat.person;
import com.example.foxykat.personAdapter;
import com.example.foxykat.userdet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment  {
    SwipeRefreshLayout mySwipeRefreshLayout;
    DatabaseReference myRef ;
    RecyclerView recyclerView;
    private static final int ACTIVITYuserdet = 2 ,ACTIVITYchat =3;
    dbchat helper;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutmanager;
     View root2 ;
    int i;
    ViewGroup container2;
    LayoutInflater inflater2;
    ArrayList<person> people ,people2;
    String cn ,pno;
    String tok;// to know the login pages intent;

    private static final int PRRCon = 100;
    Cursor c;
    ArrayList<String> contacts;

    Contact obj;


    public interface Contact {
        ArrayList<person>  contacts() ;
    }



    private HomeViewModel homeViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        // its doing something which i dont understand
        inflater2 = inflater;
        container2 = container;




        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        recyclerView = root.findViewById(R.id.list);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new SpeedyLinearLayoutManager(getActivity(), SpeedyLinearLayoutManager.VERTICAL, false) );

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Clicked !!", Toast.LENGTH_SHORT).show();
                    }
                });







                textView.setText("Swipe Down to Refresh");

            }
        });

        //Get dat from database
        people2 = new ArrayList<person>();
        helper = new dbchat(getActivity());
        tok = getActivity().getIntent().getStringExtra("tok");
        if (tok.equals("login")) {
            int a,h,m;
            tok = "";
            String time;

            h= LocalDateTime.now().getHour() ;
            m = LocalDateTime.now().getMinute();
            time = Integer.toString(h) + " : " + Integer.toString(m) ;
            if(helper.checkUser("0000000000") == 0) {
                a = (int) helper.insertData("FoxyKat", "0000000000", "Swipe To Refresh", "logo", time);
            }

                people2 = helper.getData();
                myAdapter = new personAdapter(people2, getActivity());
                recyclerView.setAdapter(myAdapter);



        }








        mySwipeRefreshLayout =  root.findViewById(R.id.swipe) ;
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        int Pcheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
                        if(Pcheck == PackageManager.PERMISSION_GRANTED ) {
                            update();
                        }else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    PRRCon );
                            if(Pcheck == PackageManager.PERMISSION_GRANTED ) {
                                update();

                            }
                        }

                        mySwipeRefreshLayout.setRefreshing(false);

                    }

                }
        );






        //  1----------------   myAdapter.notifyDataSetChanged();




















        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

            people2 = helper.getData();

            myAdapter = new personAdapter(people2, getActivity());
            recyclerView.setAdapter(myAdapter);


    }

    /*
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            root2 = inflater2.inflate(R.layout.fragment_home, container2, false);
            //changes text
                   recyclerView = root2.findViewById(R.id.list);
                    recyclerView.setHasFixedSize(true);
                    layoutmanager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutmanager);
                    people = new ArrayList<person>();

                    people.add(new person( "/chandu.jpg","i Love you 3000","Chandrika","21:00"));
                    people.add(new person( "/sai.jpg","i Love you 3000","Sai","22:00"));
                    myAdapter = new personAdapter(people,getActivity());
                    recyclerView.setAdapter(myAdapter);









        }


    */
public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    obj = (Contact) context;
}
///////////////////////
        public void update() {


          people = obj.contacts();

            process1 k = new process1();
            try {
                AsyncTask<Integer, Integer, String> p = k.execute(people.size());
            }catch (Exception e) {
                e.printStackTrace();
            }

            // 1 layoutmanager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL,false);
            //2 recyclerView.setLayoutManager(layoutmanager);

            //myAdapter = new personAdapter(people, getActivity());
           // recyclerView.setAdapter(myAdapter);
        //    mySwipeRefreshLayout.setRefreshing(false);

        }

        ////////////////////-------------------------thread start


    class MultithreadingDemo implements Runnable
    {   String tname;
        int index;

        public MultithreadingDemo(String tname,int index) {
            this.tname = tname;
            this.index = index;
        }

        public void run()
        {
            try
            {
                tname   = tname.replaceAll("\\s", "");
                tname = tname.replaceAll("\\+", "");
                if(tname.length() > 10) {
                    int j;
                    int i = tname.length() - 1;
                    String buf = "";
                    for(j=1;j<=10 ;j++) {
                        buf = tname.charAt(i) + buf ;
                        i--;
                    }
                    tname = buf;
                }

                myRef.child(tname).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        String value = dataSnapshot.getValue(String.class);
                        if (value == null) {
                            if(helper.checkUser(tname) == 1) {
                                helper.delete(people.get(index).getName());
                            }
                            Log.d("1FAiled", "NULL   For  " + tname  );
                            Log.d("2FAiled", "Currently fail  " + people.get(index).getName() );
                        }else {
                            Log.d("Success", " for " + tname );
                            Log.d("Suc2", "Currently  at  " + people.get(index).getName()  );
                            try {
                                int a;
                                if(helper.checkUser(tname) == 0) {
                                    a = (int) helper.insertData(people.get(index).getName(), tname, people.get(index).getMsg(), people.get(index).getImg(), "22:00");
                                }
                                    people2.clear();
                                    people2 = helper.getData();
                                    myAdapter = new personAdapter(people2, getActivity());
                                    recyclerView.setAdapter(myAdapter);


//                                people2.add(0,people.get(index));
  //                              myAdapter = new personAdapter(people2, getActivity());
    //                            recyclerView.setAdapter(myAdapter);

                            }catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Failed to read value
                        Toast.makeText(getActivity(),"Failed to read data base",Toast.LENGTH_SHORT).show();
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                if(index == people.size() - 1) {

                }

            }
            catch (Exception e)
            {
                // Throwing an exception
                System.out.println ("Exception is caught");
            }
        }
    }


//////////------------------asynctask

    public class process1 extends AsyncTask<Integer,Integer,String> {


        public process1() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"On pre execute",Toast.LENGTH_SHORT).show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("mob");
        }


        @Override
        protected String doInBackground(Integer... integers) {
            i = 0;

            while (i < integers[0]){
               cn = people.get(i).getName();
               pno = people.get(i).getMsg();
                Thread object = new Thread(new MultithreadingDemo(pno,i));
                object.start();
                //publishProgress(i);
                i++;


            }
            //--------------------
            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            people2.add(people.get(values[0]));
            myAdapter = new personAdapter(people2, getActivity());
            recyclerView.setAdapter(myAdapter);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //myAdapter = new personAdapter(people2, getActivity());
            //recyclerView.setAdapter(myAdapter);


            Toast.makeText(getActivity(),"On POST execute",Toast.LENGTH_SHORT).show();
        }

    }







































    public void add() {
        myAdapter = new personAdapter(people,getActivity());
        recyclerView.setAdapter(myAdapter);

    }
    public class SpeedyLinearLayoutManager extends LinearLayoutManager {

        private static final float MILLISECONDS_PER_INCH = 5f; //default is 25f (bigger = slower)

        public SpeedyLinearLayoutManager(Context context) {
            super(context);
        }

        public SpeedyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public SpeedyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

            final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

                @Override
                public PointF computeScrollVectorForPosition(int targetPosition) {
                    return super.computeScrollVectorForPosition(targetPosition);
                }

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                }
            };

            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        people2.clear();
        people2 = helper.getData();
        myAdapter = new personAdapter(people2, getActivity());
        recyclerView.setAdapter(myAdapter);

    }


}