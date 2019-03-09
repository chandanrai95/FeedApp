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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateLoginActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName,mDob,mEmail,mPassword;
    private Button mCreate;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private Toolbar mtoolbar;
    private DatabaseReference mdatabaseRefer;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login);
        init();
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=mDisplayName.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String pass=mPassword.getEditText().getText().toString();
                String dob=mDob.getEditText().getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(dob))
                {
                    mprogress.setTitle("Registering User");
                    mprogress.setMessage("Please wait while we registering ");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();
                    createUser(name,email,pass,dob);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Fill Details Properly",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void init()
    {
        mDisplayName=(TextInputLayout) findViewById(R.id.name);
        mDob=(TextInputLayout) findViewById(R.id.dob);
        mEmail=(TextInputLayout) findViewById(R.id.email);
        mPassword=(TextInputLayout) findViewById(R.id.password);
        mCreate=(Button) findViewById(R.id.create_id);
        mtoolbar=(Toolbar)findViewById(R.id.register_toolbar);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogress=new ProgressDialog(CreateLoginActivity.this);
    }

    public void createUser(final String Name, final String Email, String Pass, final String dob)
    {
        mAuth.createUserWithEmailAndPassword(Email,Pass)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                mprogress.dismiss();
                if(task.isSuccessful())
                {
                    FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
                    mdatabaseRefer= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

                    HashMap<String,String> dataMap =new HashMap<>();
                    dataMap.put("Name",Name);
                    dataMap.put("Email",Email);
                    dataMap.put("DOB",dob);

                    mdatabaseRefer.setValue(dataMap);

                    Toast.makeText(getApplicationContext(),"Registered Succesfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateLoginActivity.this,MainActivity.class));
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
