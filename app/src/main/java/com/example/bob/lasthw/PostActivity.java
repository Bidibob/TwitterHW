package com.example.bob.lasthw;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class PostActivity extends Activity implements View.OnClickListener
{

        private Button buttonPost;
        private EditText editPost;

        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private FirebaseUser user;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_post);

            buttonPost = (Button) findViewById(R.id.buttonPost);
            editPost = (EditText) findViewById(R.id.editPost);

            buttonPost.setOnClickListener(this);

            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Toast.makeText(PostActivity.this, "User signed in: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PostActivity.this, "Nobody Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostActivity.this, LoginActivity.class);
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
        public void onClick(View v) {
            if (v==buttonPost) {
                String message = editPost.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ts = dateFormat.format(new Date()); // Finds current time UTC+-0
                Tweets tweets = new Tweets(message,user.getEmail(),ts);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dataTweets = database.getReference("tweets");
                DatabaseReference dataNewTweet = dataTweets.push();
                dataNewTweet.setValue(tweets);
                Toast.makeText(this, "Your tweet was posted.", Toast.LENGTH_SHORT).show();
                editPost.setText("");
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            Intent intentMonitor = new Intent(PostActivity.this, ReadActivity.class);
            if (mAuth.getCurrentUser() != null ) {
                if (item.getItemId() == R.id.menuLogout) {
                    mAuth.signOut();

                } else if (item.getItemId() == R.id.menuRead) {
                    startActivity(intentMonitor);

                } else if (item.getItemId() == R.id.menuPost) {

                    Toast.makeText(this, "You are in Tweet Page Already", Toast.LENGTH_SHORT).show();

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
