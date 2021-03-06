package com.example.feedapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PostCreateActivity extends AppCompatActivity {

    private FirebaseAuth mFireAuth;
    private Toolbar mToolbar;
    private TextInputLayout mFeedback;
    private Button mPost;
    private DatabaseReference mDataBaseReference;
    private FirebaseUser currentUser;
    private String name,email;
    private int count;
    ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);
        init();

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feed=mFeedback.getEditText().getText().toString();
                if (!TextUtils.isEmpty(feed) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(email))
                {
                    mprogress.show();
                    mprogress.setTitle("Adding Feedbacks");
                    mprogress.setMessage("Please wait while we Adding Your Feedbacks");
                    mprogress.setCanceledOnTouchOutside(false);
                    if (Util.isInternetConnection(getApplicationContext())==true) {
                        createPost(feed,name,email);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Network Not Available",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Fill Details Properly",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void init()
    {
        Intent intent=getIntent();

        name=intent.getStringExtra("name");
        email=intent.getStringExtra("email");
        count=intent.getIntExtra("count",0);

        mFireAuth=FirebaseAuth.getInstance();
        mToolbar=(Toolbar) findViewById(R.id.post_toolbar);
        mFeedback=(TextInputLayout) findViewById(R.id.textInputLayout_feedback);
        mPost=(Button) findViewById(R.id.post_btn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle("Create Feedback");
        mToolbar.setTitleTextColor(getApplicationContext().getResources().getColor(R.color.white));

        mprogress=new ProgressDialog(PostCreateActivity.this);
    }

    private void createPost(String Feed,String name, String email)
    {
        currentUser=mFireAuth.getCurrentUser();
        mDataBaseReference= FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(currentUser.getUid()+count);
        HashMap<String,String> dataMap=new HashMap<>();
        dataMap.put("feed",Feed);
        dataMap.put("createdby",name);
        mDataBaseReference.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mprogress.dismiss();
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Feedbacks Added Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PostCreateActivity.this,DashboardActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}
