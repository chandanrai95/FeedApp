package com.example.feedapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private Toolbar mToolbar;
    private FloatingActionButton mCreatePost;
    private DatabaseReference mDatabaseRefer;
    private String Name,Email;private
    ProgressDialog mprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();

        mCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this,PostCreateActivity.class);
                intent.putExtra("name",Name);
                intent.putExtra("email",Email);
                startActivity(intent);
            }
        });
    }

    public void init()
    {
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        mToolbar=(Toolbar) findViewById(R.id.dashboard_toolbar);
        mCreatePost=(FloatingActionButton) findViewById(R.id.create_post_btn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_person_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mprogress=new ProgressDialog(DashboardActivity.this);
        mDatabaseRefer= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        getuserData();

    }

    public void getuserData()
    {
        mprogress.show();
        mprogress.setTitle("Fetching");
        mprogress.setMessage("Please wait while we Fetching Details");
        mprogress.setCanceledOnTouchOutside(false);

        mDatabaseRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mprogress.dismiss();
                getSupportActionBar().setTitle(dataSnapshot.child("Name").getValue().toString());
                Name=dataSnapshot.child("Name").getValue().toString();
                Email=dataSnapshot.child("Email").getValue().toString();

                mToolbar.setTitle(dataSnapshot.child("Name").getValue().toString());
                mToolbar.setTitleTextColor(getApplicationContext().getResources().getColor(R.color.white));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mprogress.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId())
        {
            case R.id.change_info:

                break;
            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(DashboardActivity.this,MainActivity.class));
                break;
        }

        return true;
    }
}
