package com.example.foxykat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foxykat.ui.home.HomeFragment;
import com.example.foxykat.ui.home.dbchat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.foxykat.R.raw.tick;

public class MessageListActivity extends AppCompatActivity implements MessageListAdapter.ItemClicked , MessageListAdapter.onLongItemClickListener {
    RecyclerView recyclerView;
    MediaPlayer mp = null;
    dbchat helper;
    int onpause = 0;
    private final String CHANNEL_ID = "big_text_style_notification";
    private final int NOTIFICATION_ID = 02;


    private int mCurrentItemPosition;









    String username, userphone;

    FirebaseFirestore db;//firestore database
    private static final String TAG = "FireStore";
    Button send;
    EditText et;
    String pno;
    RecyclerView.Adapter myAdapter;
    ArrayList<message> letter;
    RecyclerView.LayoutManager layoutmanager;
    private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        userbio dat = new userbio(this);
        username = dat.getName();
        userphone = dat.getMob();
        setup();//firestore setup
        pno = getIntent().getStringExtra("pno");
        String nam = getIntent().getStringExtra("nam");
        getSupportActionBar().setTitle(nam);


        listenformsgs();


        //Toast.makeText(this,"you are "+username,Toast.LENGTH_LONG).show();
        letter = new ArrayList<message>();

        recyclerView = findViewById(R.id.reyclerview_message_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new SpeedyLinearLayoutManager(MessageListActivity.this, SpeedyLinearLayoutManager.VERTICAL, true));
        send = findViewById(R.id.button_chatbox_send);
        et = findViewById(R.id.edittext_chatbox);
        helper = new dbchat(this);


        letter = helper.getMsgData(pno);
        try {
            mp = MediaPlayer.create(MessageListActivity.this, tick);
            //    mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //people.add(new message("url","Hey Dusky","prasu","22:00", 0, 0 )); people.add(new message("url","Hey Duffer","Oishi","21:00", 1, 0 ));
        // people.add(new message("url","Hey Dusky","Sai","22:00", 0, 0 ));
        ///people.add(new message("url","Hey Duffer","Arun","21:00", 1, 0 ));
        //people.add(new message("url","Hey Dusky","prasu","22:00", 0, 0 ));
        registerForContextMenu(recyclerView);
        myAdapter = new MessageListAdapter(letter, MessageListActivity.this);







        recyclerView.setAdapter(myAdapter);
        //Comntext menu


        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String st = et.getText().toString();
                if (st.isEmpty()) {

                } else {
                    String time;
                    int h, m;

                    h = LocalDateTime.now().getHour();
                    m = LocalDateTime.now().getMinute();
                    time = Integer.toString(h) + " : " + Integer.toString(m);
                    helper.insertMsgData("", "", pno, st, "", 0, 0, time);
                    // message(int id ,String phone, String img , String msg, String name  , int token, int seen,String time)
                    sendMsg(st);
                    helper.updatelastMessage(pno, st);
                    letter.clear();
                    letter = helper.getMsgData(pno);
                    myAdapter = new MessageListAdapter(letter, MessageListActivity.this);
                    recyclerView.setAdapter(myAdapter);
                    et.setText("");


                    mp.start();
                }
            }
        });


    }

    public void setup() {
        // [START get_firestore_instance]
        db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onpause = 0;
    }
    // we listen for input messages for our Account

    public void listenformsgs() {
        // [START listen_for_users]
        // Listen for users born before 1900.
        //
        // You will get a first snapshot with the initial results and a new
        // snapshot each time there is a change in the results.
        db.collection("Inbox").document(userphone)
                .collection("Buffer")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        Log.d(TAG, "Current  from the FireStore : " + snapshots);
                        for (DocumentSnapshot s : snapshots.getDocuments()) {
                            Log.d(TAG, "s  : " + s);
                            Log.d(TAG, "s Id : " + s.getId());
                            if (s.getId().toString().equals("root")) {
                                continue;
                            }
                            Log.d(TAG, "s data : " + s.getData());
                            Map<String, Object> data = new HashMap<>();
                            data = s.getData();

                            Log.d(TAG, "data value of Msg: " + data.get("Msg"));
                            String Name, Phone, Msg, Time;
                            Name = data.get("Name").toString();
                            Phone = data.get("Phone").toString();
                            Msg = data.get("Msg").toString();
                            Time = data.get("Time").toString();
                            //fill the msg in data base
                            if (helper.checkMsg(s.getId().toString()) == 0) {

                                // Custom Notification for messages
                                if (onpause == 1) {
                                    Intent intent = new Intent(MessageListActivity.this, MessageListActivity.class);
                                    intent.putExtra("pno", pno);
                                    createNotificationChannel();
                                    PendingIntent pIntent = PendingIntent.getActivity(MessageListActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                                    builder.setSmallIcon(R.drawable.logo);
                                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
                                    builder.setContentTitle(Name);
                                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(Msg));
                                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                    builder.setContentIntent(pIntent);
                                    builder.setAutoCancel(true);
                                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                    notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

                                }
                                try {
                                    helper.insertMsgData(s.getId().toString(), Name, Phone, Msg, "", 1, 0, Time);
                                    // message(int id ,String phone, String img , String msg, String name  , int token, int seen,String time)
                                    helper.updatelastMessage(Phone, Msg);
                                } catch (Exception ae) {
                                    ae.printStackTrace();
                                }
                            }
                            letter.clear();
                            letter = helper.getMsgData(pno);
                            myAdapter = new MessageListAdapter(letter, MessageListActivity.this);
                            recyclerView.setAdapter(myAdapter);
                            //Deleting message after its read ans saved in the data base
                            if (pno.equals("0000000000")) {

                            } else {
                                deleteDocument(s.getId());
                            }
                        }
                    }
                });
        // [END listen_for_users]


    }

    public void deleteDocument(final String docid) {
        // [START delete_document]
        db.collection("Inbox").document(userphone)
                .collection("Buffer").document(docid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted! with id " + docid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // [END delete_document]
    }


    ///////SEND MESSAGE

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMsg(String smsg) {
        CollectionReference Inbox = db.collection("Inbox");
        String time;
        int h, m;
        h = LocalDateTime.now().getHour();
        m = LocalDateTime.now().getMinute();
        time = Integer.toString(h) + " : " + Integer.toString(m);
        Map<String, Object> data1 = new HashMap<>();
        data1.put("Name", username);
        data1.put("Phone", userphone);
        data1.put("Msg", smsg);
        data1.put("Time", time);

        Inbox.document(pno).collection("Buffer").add(data1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // [END add_document]
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();

            }
        }
        mp.release();
        mp = null;
    }

    @Override
    public void onItemClicked(int index, ArrayList<message> people) {

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

    //create notification channel if you target android 8.0 or higher version
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "BigTextStyle Notification";
            String description = "Include all the BigTextStyle notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void ItemLongClicked(View v, int position) {
        mCurrentItemPosition = position;
        Toast.makeText(this,"Long Press",Toast.LENGTH_LONG).show();
        v.showContextMenu();
    }


 	    @Override
	    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        	        super.onCreateContextMenu(menu, v, menuInfo);
        	        menu.setHeaderTitle("Choose your option");
        	        getMenuInflater().inflate(R.menu.context_menu, menu);

        	    }

        @Override
	    public boolean onContextItemSelected(MenuItem item) {

        	        switch ((item.getItemId())){
        	            case R.id.option1:
        	                Toast.makeText(this, "option 1 selected", Toast.LENGTH_SHORT).show();
        	                return true;

        	            case R.id.option2:
        	                Toast.makeText(this, "option 2 selected", Toast.LENGTH_SHORT).show();
        	                return true;

        	            case R.id.option3:
        	                Toast.makeText(this, "option 3 selected", Toast.LENGTH_SHORT).show();
        	                return true;

        	            default:
        	                return super.onContextItemSelected(item);
        	        }
        	    }
        	}
