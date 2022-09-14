package silverbeach.rbleipzigsupport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.VibrationEffect;
import android.util.Log;
import android.widget.Toast;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    private String current_uid, rank, banned;
    private DatabaseReference mStatusDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.soccer));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.forum));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.transfer));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.newspaper));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.poll));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(1,75));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
       pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageSelected(int position){
               TabLayout.Tab tab = tabLayout.getTabAt(position);
               tab.select();
           }
       });





        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Anmeldung
        //FÜR score ini und rank
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        current_uid = "DEFAULT";
        if (mCurrentUser != null){
            current_uid = mCurrentUser.getUid();
            DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
            mDatabase2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rank = dataSnapshot.child("rank").getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            // BANNED ??
            mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
            mStatusDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    banned = dataSnapshot.child("banned").getValue().toString();
                    if (banned.equals("true")){

                        Toast.makeText(MainActivity.this, "Sie wurden von der App ausgeschlossen!",
                                Toast.LENGTH_LONG).show();
                        finish();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        AppRate.with(this)
                .setInstallDays(5) // default 10, 0 means install day. 5
                .setLaunchTimes(10) // default 10 . 10
                .setRemindInterval(3) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);

    }





    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


        if(mAuth.getCurrentUser() == null){
            sendToStart();
        }
    }

    private void sendToStart() {

        Intent startIntent = new Intent(MainActivity.this, IntroActivity.class);
        startActivity(startIntent);
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth.getCurrentUser() != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
        }

    }

    void selectPage(int pageIndex){
        tabLayout.getTabAt(pageIndex).select();
        pager2.setCurrentItem(pageIndex);

    }
}
