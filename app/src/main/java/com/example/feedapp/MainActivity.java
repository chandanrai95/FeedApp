package com.example.feedapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFireAuth;
    private Toolbar mToolbar;
    private TextInputLayout memail,mpassword;
    private Button mLogin, mCreate;
    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=memail.getEditText().getText().toString();
                String pass=mpassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
                {
                    mLoginProgress.setTitle("Logging in");
                    mLoginProgress.setMessage("Please wait while we checking your credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    LoginWithEmail(email,pass);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Fill Details Properly",Toast.LENGTH_SHORT).show();
                }

            }
        });

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateLoginActivity.class));
            }
        });
    }


    public void init()
    {
        memail=(TextInputLayout) findViewById(R.id.textInputLayout);
        mpassword=(TextInputLayout) findViewById(R.id.textInputLayout2);
        mLogin=(Button) findViewById(R.id.login_bt);
        mCreate=(Button) findViewById(R.id.create_bt);
        mLoginProgress=new ProgressDialog(MainActivity.this);
        mToolbar=(Toolbar)findViewById(R.id.login_toolbar);
        mFireAuth=FirebaseAuth.getInstance();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
    }

    private void LoginWithEmail(String email, String pass)
    {
        mFireAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        mLoginProgress.dismiss();
                        Toast.makeText(MainActivity.this,"Login Succesfully",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        mLoginProgress.dismiss();
                        Toast.makeText(MainActivity.this,"Something went wrong Please check your credentials", Toast.LENGTH_LONG).show();

                    }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser =mFireAuth.getCurrentUser();
        if (currentUser!=null)
        {
            Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
