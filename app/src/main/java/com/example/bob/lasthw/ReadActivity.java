package com.example.bob.lasthw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReadActivity extends Activity implements View.OnClickListener{

    private Button buttonRefresh;
    private TextView post1;
    private TextView post2;
    private TextView post3;
    private TextView post4;
    private TextView post5;
    private Integer i;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        post1 = (TextView) findViewById(R.id.post1);
        post2 = (TextView) findViewById(R.id.post2);
        post3 = (TextView) findViewById(R.id.post3);
        post4 = (TextView) findViewById(R.id.post4);
        post5 = (TextView) findViewById(R.id.post5);
        buttonRefresh.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(ReadActivity.this, "User signed in: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReadActivity.this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReadActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataTweets = database.getReference();
        i=0;
        dataTweets.child("tweets").orderByKey().limitToLast(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                i=i+1;
                Tweets tweets = dataSnapshot.getValue(Tweets.class);
                if (i==1) {
                        post1.setText("Time: "+tweets.timestamp+" - User: "+tweets.user+"\n"+tweets.message);
                } else if (i==2) {
                        post2.setText("Time: "+tweets.timestamp+" - User: "+tweets.user+"\n"+tweets.message);
                } else if (i==3) {
                        post3.setText("Time: "+tweets.timestamp+" - User: "+tweets.user+"\n"+tweets.message);
                } else if (i==4) {
                        post4.setText("Time: "+tweets.timestamp+" - User: "+tweets.user+"\n"+tweets.message);
                } else if (i==5) {
                        post5.setText("Time: "+tweets.timestamp+" - User: "+tweets.user+"\n"+tweets.message);
                }
                }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Intent intentMonitor = new Intent(MonitorActivity.this, MonitorActivity.class);
        Intent intentPurchase = new Intent(ReadActivity.this, PostActivity.class);
        if (mAuth.getCurrentUser() != null ) {
            if (item.getItemId() == R.id.menuLogout) {
                mAuth.signOut();

            } else if (item.getItemId() == R.id.menuRead) {
                Toast.makeText(this, "You are in Tweet Page Already", Toast.LENGTH_SHORT).show();

            } else if (item.getItemId() == R.id.menuPost) {
                startActivity(intentPurchase);

            }
        } else {
            Toast.makeText(this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

