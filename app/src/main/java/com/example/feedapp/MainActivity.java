package com.example.feedapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFireAuth;
    private Toolbar mToolbar;
    private TextInputLayout memail,mpassword;
    private Button mLogin;
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

                LoginWithEmail(email,pass);
            }
        });
    }


    public void init()
    {
        memail=(TextInputLayout) findViewById(R.id.textInputLayout);
        mpassword=(TextInputLayout) findViewById(R.id.textInputLayout2);
        mLogin=(Button) findViewById(R.id.login_bt);
        mLoginProgress=new ProgressDialog(MainActivity.this);
        mToolbar=(Toolbar)findViewById(R.id.login_toolbar);
        mFireAuth=FirebaseAuth.getInstance();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void LoginWithEmail(String email, String pass)
    {
        mFireAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {

                    }
                    else {

                    }
            }
        });
    }
}
