package com.example.foxykat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;




import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView t1 ;
    private static final int ACTIVITYuserdet = 2 ,ACTIVITYbotnav =3;

    String UID;
    //Adding tag for logging and RC_SIGN_IN for an activity result
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    ImageButton sin;
    ImageView logo;
    Button sout ,sd,go;
    // Adding Google sign-in client
    GoogleSignInClient mGoogleSignInClient;

    //Creating member variable for FirebaseAuth
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Adding buttons to the OnClickListener

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        t1 = findViewById(R.id.t1);
        logo = findViewById(R.id.logo);
        findViewById(R.id.signOutButton).setOnClickListener((View.OnClickListener) this);
        findViewById(R.id.disconnectButton).setOnClickListener((View.OnClickListener) this);
        sin = findViewById(R.id.sign_in_button);
        sout =   findViewById(R.id.signOutButton);
        go =   findViewById(R.id.go);
       sd = findViewById(R.id.disconnectButton);

        mAuth = FirebaseAuth.getInstance();
        //----------visibily hide
        /*
        try {
            currentUser = mAuth.getCurrentUser();

            if (currentUser == null) {

                logo.setVisibility(View.GONE);
              t1.setVisibility(View.VISIBLE);
              sin.setVisibility(View.VISIBLE);
              sout.setVisibility(View.VISIBLE);
              sd.setVisibility(View.VISIBLE);


            }

        }catch(Exception e) {

        }

         */
        //Building Google sign-in and sign-up option.
// Configuring Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
// for the requestIdToken, use getString(R.string.default_web_client_id), this is in the values.xml file that
                // is generated from your google-services.json file (data from your firebase project), uses the google-sign-in method
                // web api key
                .requestIdToken(getString(R.string.default_web_client_id))//Default_web_client_id will be matched with the
                .requestEmail()
                .build();

// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

// Set the dimensions of the sign-in button.
      //  SignInButton signInButton = findViewById(R.id.sign_in_button);
       // signInButton.setSize(SignInButton.SIZE_WIDE);

// Initialize Firebase Auth
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    currentUser = mAuth.getCurrentUser();

                    if (currentUser != null) {

                        Intent intent = new Intent(MainActivity.this ,com.example.foxykat.botnav.class);
                        intent.putExtra("UID",UID);
                        intent.putExtra("tok","ok");
                        //   intent.putExtra("fm",people.get(index).getName());
                        startActivityForResult(intent,ACTIVITYbotnav);
                    }

                }catch(Exception e) {

                }

            }
        });


      // if the user in logge

       try {
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            Intent intent = new Intent(MainActivity.this ,com.example.foxykat.botnav.class);
            intent.putExtra("UID",UID);
            intent.putExtra("tok","ok");
            //   intent.putExtra("fm",people.get(index).getName());
            startActivityForResult(intent,ACTIVITYbotnav);
        }

        }catch(Exception e) {

        }

    }







    //Creating onStart() method.
    @Override
    public void onStart() {
        super.onStart();


// Checking if the user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            UID = mAuth.getUid().toString();
            t1.setText(mAuth.getUid().toString());
            Log.d(TAG, "Currently Signed in: " + currentUser.getEmail());
            Toast.makeText(MainActivity.this, "Currently Logged in: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();





        }
    }






    //Calling onActivityResult to use the information about the sign-in user contains in the object.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
// Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Google Sign in Succeeded",  Toast.LENGTH_LONG).show();
                firebaseAuthWithGoogle(account);


            } catch (ApiException e) {
// Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google Sign in Failed " + e,  Toast.LENGTH_LONG).show();
            }
        }













        }






    //Creating helper method FirebaseAuthWithGoogle().
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //Calling get credential from the oogleAuthProviderG
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    //Override th onComplete() to see we are successful or not.
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Update UI with the sign-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());
                            if( mAuth != null) {
                                UID = mAuth.getUid().toString();
                            }

                            Toast.makeText(MainActivity.this, "Firebase Authentication Succeeded ",  Toast.LENGTH_LONG).show();
                            // now call for user to enter his detailsdetails :
                            Intent intent = new Intent(MainActivity.this ,com.example.foxykat.userdet.class);
                            intent.putExtra("UID",UID);
                         //   intent.putExtra("fm",people.get(index).getName());
                            startActivityForResult(intent,ACTIVITYuserdet);



                        } else {
// If sign-in fails to display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Firebase Authentication failed:" + task.getException(),  Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signInToGoogle(){
        //Calling Intent and call startActivityForResult() method
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
// Firebase sign out
        FirebaseAuth.getInstance().signOut();

// Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
// Google Sign In failed, update UI appropriately
                        Toast.makeText(getApplicationContext(),"Signed out of google", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void revokeAccess() {
// Firebase sign out
        FirebaseAuth.getInstance().signOut();

// Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
// Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Revoked Access");
                    }
                });
    }




    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
        //    Adding signInToGoogle() method
            signInToGoogle();
        }
        else if (i == R.id.signOutButton) {
            signOut();
        }
        else if (i == R.id.disconnectButton) {
            revokeAccess();
        }
    }









    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    ///////////////////////////Some data base c




















}