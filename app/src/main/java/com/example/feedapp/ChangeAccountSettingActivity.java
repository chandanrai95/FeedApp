package com.example.feedapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeAccountSettingActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private Toolbar mToolbar;
    private DatabaseReference mDatabaseRefer;
    private ProgressDialog mprogress;
    private String name,age;
    private Button submitBtn;
    private boolean flag=false;
    private EditText mName ,mAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account_setting);

        init();
        getUserData();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==false)
                {
                    flag=true;
                    submitBtn.setText("Submit");
                    mName.setFocusable(true);
                    mAge.setFocusable(true);
                    mName.setFocusableInTouchMode(true);
                    mAge.setFocusableInTouchMode(true);
                }
                else{
                    updateData();

                    flag=false;
                    submitBtn.setText("Edit");
                    mName.setFocusable(false);
                    mAge.setFocusable(false);
                }
            }
        });

    }

    public void init()
    {
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        mToolbar=(Toolbar) findViewById(R.id.setting_toolbar);
        submitBtn=(Button) findViewById(R.id.edit_button);
        mDatabaseRefer= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        mName=(EditText) findViewById(R.id.name_editText);
        mAge=(EditText) findViewById(R.id.age_editText);

        mprogress=new ProgressDialog(ChangeAccountSettingActivity.this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbar.setTitle("Account Info");
        mToolbar.setTitleTextColor(getApplicationContext().getResources().getColor(R.color.white));

    }

    private void getUserData()
    {
        if (Util.isInternetConnection(getApplicationContext())==true) {
            mprogress.setTitle("Fetching Details");
            mprogress.setMessage("Please Wait While We Fetching Your Details");
            mprogress.show();

            mDatabaseRefer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name=dataSnapshot.child("Name").getValue().toString();
                    age=dataSnapshot.child("DOB").getValue().toString();
                    mName.setText(name);
                    mAge.setText(age);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mprogress.dismiss();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Network Not Available",Toast.LENGTH_LONG).show();
        }
    }

    private void updateData()
    {
        if (Util.isInternetConnection(getApplicationContext())==true) {

            mprogress.setTitle("Updating Details");
            mprogress.setMessage("Please Wait While We Update Your Details");
            mprogress.show();
            name=mName.getText().toString();
            age=mAge.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age))
                {
                    mDatabaseRefer=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("Name");
                    mDatabaseRefer.setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                mprogress.dismiss();
                                Toast.makeText(getApplicationContext(),"Succesfully changed the name",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                mprogress.dismiss();
                                Toast.makeText(getApplicationContext(),"Error has been occured while changing the name",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    mDatabaseRefer=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("DOB");
                    mDatabaseRefer.setValue(age).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                mprogress.dismiss();
                                Toast.makeText(getApplicationContext(),"Succesfully changed the DOB ",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                mprogress.dismiss();
                                Toast.makeText(getApplicationContext(),"Error has been occured while changing the DOB",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                Toast.makeText(getApplicationContext(),"Please Fill Details Properly",Toast.LENGTH_SHORT).show();
            }

            mprogress.dismiss();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Network Not Available",Toast.LENGTH_LONG).show();
        }

    }
}
