package com.example.feedapp;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
    private RecyclerView FeedbackList;


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
                intent.putExtra("count",FeedbackList.getAdapter().getItemCount());
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
        FeedbackList=(RecyclerView) findViewById(R.id.feedback_list);
        FeedbackList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_person_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mprogress=new ProgressDialog(DashboardActivity.this);
        mDatabaseRefer= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        if (Util.isInternetConnection(getApplicationContext())==true) {
            getuserData();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Network Not Available",Toast.LENGTH_LONG).show();
        }



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

                getSupportActionBar().setTitle(dataSnapshot.child("Name").getValue().toString());
                Name=dataSnapshot.child("Name").getValue().toString();
                Email=dataSnapshot.child("Email").getValue().toString();

                mToolbar.setTitle(dataSnapshot.child("Name").getValue().toString());
                mToolbar.setTitleTextColor(getApplicationContext().getResources().getColor(R.color.white));
                setFeedList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFeedList()
    {
        mDatabaseRefer= FirebaseDatabase.getInstance().getReference().child("Feedbacks");
        FirebaseRecyclerAdapter<Feedbacks,FeedAdapter> firebaseListAdapter=new FirebaseRecyclerAdapter<Feedbacks, FeedAdapter>(Feedbacks.class,R.layout.feedback_list_adapter,FeedAdapter.class,mDatabaseRefer) {
            @Override
            protected void populateViewHolder(final FeedAdapter viewHolder, final Feedbacks model, int position) {

                viewHolder.feed.setText(model.getFeed());
                viewHolder.createdby.setText("Created By : "+model.getCreatedby());
            }

        };
        FeedbackList.setAdapter(firebaseListAdapter);
        mprogress.dismiss();
        mprogress.dismiss();
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
                startActivity(new Intent(DashboardActivity.this,ChangeAccountSettingActivity.class));
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(DashboardActivity.this,MainActivity.class));
                break;
        }

        return true;
    }

    public static class FeedAdapter extends RecyclerView.ViewHolder{
        TextView feed,createdby;

        public FeedAdapter(@NonNull View itemView) {
            super(itemView);
            feed=(TextView) itemView.findViewById(R.id.feed_txt);
            createdby=(TextView) itemView.findViewById(R.id.creat_by_txt);
        }
    }


}
