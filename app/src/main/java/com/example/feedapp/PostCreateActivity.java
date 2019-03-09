package com.example.feedapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PostCreateActivity extends AppCompatActivity {

    private FirebaseAuth mFireAuth;
    private Toolbar mToolbar;
    private TextInputLayout mFeedback;
    private Button mPost;
    private DatabaseReference mDataBaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);
        init();
    }

    public void init()
    {
        mFireAuth=FirebaseAuth.getInstance();
        mToolbar=(Toolbar) findViewById(R.id.post_toolbar);
        mFeedback=(TextInputLayout) findViewById(R.id.textInputLayout_feedback);
        mPost=(Button) findViewById(R.id.post_btn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Post");
    }

    private void createPost(String Feed,String name, String email)
    {

    }
}
