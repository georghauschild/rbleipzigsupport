package silverbeach.rbleipzigsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import silverbeach.rbleipzigsupport.ui.MatchActivity;
import silverbeach.rbleipzigsupport.ui.match.MatchFragment;
import silverbeach.rbleipzigsupport.ui.news.News1Fragment;
import silverbeach.rbleipzigsupport.ui.news.News2Fragment;
import silverbeach.rbleipzigsupport.ui.news.News3Fragment;
import silverbeach.rbleipzigsupport.ui.news.News4Fragment;
import silverbeach.rbleipzigsupport.ui.news.News5Fragment;
import silverbeach.rbleipzigsupport.ui.news.NewsFragment;
import silverbeach.rbleipzigsupport.ui.posts.PostsFragment;
import silverbeach.rbleipzigsupport.ui.profile.SettingsFragment;
import silverbeach.rbleipzigsupport.ui.survey.SurveyFragment;
import silverbeach.rbleipzigsupport.ui.transfers.TransfersFragment;

public class MainScreen extends AppCompatActivity {

    ViewPager2 myViewPager2, bottomViewPager2;
    Adapter myAdapter, myAdapter2;
    private ImageView ImageHome, ImageAway;
    public TextView countdownTV, clubsTV, competitionTV;
    private DatabaseReference mDatabase, mUserDatabase;
    private Comment2 comment2;
    private ListView listView;
    private ArrayList<String> list, uid_list;
    private ArrayAdapter<String> adapter;
    private Long diff, oldLong, NewLong;
    private MyCount2 counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        myViewPager2 = findViewById(R.id.myViewPager2);
        bottomViewPager2 = findViewById(R.id.bottomViewPager2);
        listView = findViewById(R.id.listView);

        if (FirebaseAuth.getInstance().getCurrentUser()==null){

            Intent startIntent = new Intent(MainScreen.this, StartActivity.class);
            startActivity(startIntent);
            finish();        }else{
        }
        setImagesAndInfos();
        setCountdown();

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        ImageHome = (ImageView) findViewById(R.id.ImageHome);
        ImageAway = (ImageView) findViewById(R.id.ImageAway);
        countdownTV = (TextView) findViewById(R.id.countdownTextView);
        clubsTV = (TextView) findViewById(R.id.clubsTextView);
        competitionTV = (TextView) findViewById(R.id.competitionTextView);

        myAdapter = new Adapter(getSupportFragmentManager(), getLifecycle());
        myAdapter2 = new Adapter(getSupportFragmentManager(), getLifecycle());
        myViewPager2.setOffscreenPageLimit(1);
        bottomViewPager2.setOffscreenPageLimit(1);
        int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.fab_margin);
        int peekMarginPx = getResources().getDimensionPixelOffset(R.dimen.text_margin);

        RecyclerView rv = (RecyclerView) myViewPager2.getChildAt(0);
        RecyclerView rv2 = (RecyclerView) bottomViewPager2.getChildAt(0);
        rv.setClipToPadding(false);
        rv2.setClipToPadding(false);
        int padding = peekMarginPx + pageMarginPx;
        rv.setPadding(padding, 0, padding, 0);
        rv2.setPadding(padding, 0, padding, 0);

        // add Fragments in your ViewPagerFragmentAdapter class
      //  myAdapter.addFragment(new News1Fragment());
      //  myAdapter.addFragment(new News2Fragment());
      //  myAdapter.addFragment(new News3Fragment());
      //  myAdapter.addFragment(new News4Fragment());
      //  myAdapter.addFragment(new News5Fragment());

        myAdapter2.addFragment(new NewsFragment());
        myAdapter2.addFragment(new TransfersFragment());
        myAdapter2.addFragment(new PostsFragment());
        myAdapter2.addFragment(new SurveyFragment());
        myAdapter2.addFragment(new VariousFragment());


        // set Orientation in your ViewPager2
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);
        myViewPager2.getOffscreenPageLimit();
        bottomViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        bottomViewPager2.setAdapter(myAdapter2);
        bottomViewPager2.getOffscreenPageLimit();

        bottomViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(1,75));
            }
        });

        setComments();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (counter!=null){
            counter.cancel();
        }
        setCountdown();
    }

    private void setComments(){
        //comments
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("comments");
        mDatabase.keepSynced(true);
        comment2 = new Comment2();
        list = new ArrayList<>();
        uid_list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.comment, R.id.commentText, list);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    comment2 = ds.getValue(Comment2.class);
                    list.add(comment2.getCommentText().toString() + " -" + comment2.getUser().toString());
                    uid_list.add(comment2.getUid());

                }

                listView.setAdapter(adapter);
                //scroll to bottom
                listView.setSelection(adapter.getCount() - 1);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(VibrationEffect.createOneShot(1,75));
                        Intent startIntent = new Intent(MainScreen.this, MatchActivity.class);
                        startActivity(startIntent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setImagesAndInfos(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Match");
        mDatabase.addValueEventListener(new ValueEventListener() {
            String HomeTeam = null;
            String AwayTeam = null;
            String Info2 = null;
            String Info3 = null;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HomeTeam = dataSnapshot.child("Home").getValue().toString();
                AwayTeam = dataSnapshot.child("Away").getValue().toString();
                Info2 = dataSnapshot.child("Info2").getValue().toString();
                Info3 = dataSnapshot.child("Info3").getValue().toString();
                clubsTV.setText("" + Info2);
                competitionTV.setText("" + Info3);

                switch (HomeTeam) {
                    case "0":
                        ImageHome.setImageResource(R.drawable.wno);
                        break;
                    case "1":
                        ImageHome.setImageResource(R.drawable.wleipzignew);
                        break;
                    case "2":
                        ImageHome.setImageResource(R.drawable.wberlin);
                        break;
                    case "3":
                        ImageHome.setImageResource(R.drawable.wmainz);
                        break;
                    case "4":
                        ImageHome.setImageResource(R.drawable.wdortmund);
                        break;
                    case "5":
                        ImageHome.setImageResource(R.drawable.wnurnberg);
                        break;
                    case "6":
                        ImageHome.setImageResource(R.drawable.whoffenheim);
                        break;
                    case "7":
                        ImageHome.setImageResource(R.drawable.wleverkusen);
                        break;
                    case "8":
                        ImageHome.setImageResource(R.drawable.wfreiburg);
                        break;
                    case "9":
                        ImageHome.setImageResource(R.drawable.wfrankfurt);
                        break;
                    case "10":
                        ImageHome.setImageResource(R.drawable.wwolfsburg);
                        break;
                    case "11":
                        ImageHome.setImageResource(R.drawable.waugsburg);
                        break;
                    case "12":
                        ImageHome.setImageResource(R.drawable.wdusseldorf);
                        break;
                    case "13":
                        ImageHome.setImageResource(R.drawable.wstuttgart);
                        break;
                    case "14":
                        ImageHome.setImageResource(R.drawable.whannover);
                        break;
                    case "15":
                        ImageHome.setImageResource(R.drawable.wbremen);
                        break;
                    case "16":
                        ImageHome.setImageResource(R.drawable.wgelsenkirchen);
                        break;
                    case "17":
                        ImageHome.setImageResource(R.drawable.wmunchen);
                        break;
                    case "18":
                        ImageHome.setImageResource(R.drawable.wgladbach);
                        break;
                    case "19":
                        ImageHome.setImageResource(R.drawable.wsalzburg);
                        break;
                    case "20":
                        ImageHome.setImageResource(R.drawable.wunion);
                        break;
                    case "21":
                        ImageHome.setImageResource(R.drawable.wpaderborn);
                        break;
                    case "22":
                        ImageHome.setImageResource(R.drawable.wkoln);
                        break;
                    case "23":
                        ImageHome.setImageResource(R.drawable.whsv);
                        break;
                    case "24":
                        ImageHome.setImageResource(R.drawable.wpauli);
                        break;
                    case "25":
                        ImageHome.setImageResource(R.drawable.wbielefeld);
                        break;
                    case "26":
                        ImageHome.setImageResource(R.drawable.waue);
                        break;
                    case "27":
                        ImageHome.setImageResource(R.drawable.wdresden);
                        break;
                    case "28":
                        ImageHome.setImageResource(R.drawable.wparis);
                        break;
                    case "29":
                        ImageHome.setImageResource(R.drawable.wrealmadrid);
                        break;
                    case "30":
                        ImageHome.setImageResource(R.drawable.watleticomadrid);
                        break;
                    case "31":
                        ImageHome.setImageResource(R.drawable.wbarcelona);
                        break;
                    case "32":
                        ImageHome.setImageResource(R.drawable.wtottenham);
                        break;
                    case "33":
                        ImageHome.setImageResource(R.drawable.wmancity);
                        break;
                    case "34":
                        ImageHome.setImageResource(R.drawable.wliverpool);
                        break;
                    case "35":
                        ImageHome.setImageResource(R.drawable.wchelsea);
                        break;
                    case "36":
                        ImageHome.setImageResource(R.drawable.wturin);
                        break;
                    case "37":
                        ImageHome.setImageResource(R.drawable.wneapel);
                        break;
                    case "38":
                        ImageHome.setImageResource(R.drawable.wintermailand);
                        break;
                    case "39":
                        ImageHome.setImageResource(R.drawable.wajax);
                        break;
                    case "40":
                        ImageHome.setImageResource(R.drawable.wdonezk);
                        break;
                    case "41":
                        ImageHome.setImageResource(R.drawable.wbochum);
                        break;
                    case "42":
                        ImageHome.setImageResource(R.drawable.wceltic);
                        break;

                    default:
                        ImageHome.setImageResource(R.drawable.wno);
                        break;
                }

                switch (AwayTeam) {
                    case "0":
                        ImageAway.setImageResource(R.drawable.wno);
                        break;
                    case "1":
                        ImageAway.setImageResource(R.drawable.wleipzignew);
                        break;
                    case "2":
                        ImageAway.setImageResource(R.drawable.wberlin);
                        break;
                    case "3":
                        ImageAway.setImageResource(R.drawable.wmainz);
                        break;
                    case "4":
                        ImageAway.setImageResource(R.drawable.wdortmund);
                        break;
                    case "5":
                        ImageAway.setImageResource(R.drawable.wnurnberg);
                        break;
                    case "6":
                        ImageAway.setImageResource(R.drawable.whoffenheim);
                        break;
                    case "7":
                        ImageAway.setImageResource(R.drawable.wleverkusen);
                        break;
                    case "8":
                        ImageAway.setImageResource(R.drawable.wfreiburg);
                        break;
                    case "9":
                        ImageAway.setImageResource(R.drawable.wfrankfurt);
                        break;
                    case "10":
                        ImageAway.setImageResource(R.drawable.wwolfsburg);
                        break;
                    case "11":
                        ImageAway.setImageResource(R.drawable.waugsburg);
                        break;
                    case "12":
                        ImageAway.setImageResource(R.drawable.wdusseldorf);
                        break;
                    case "13":
                        ImageAway.setImageResource(R.drawable.wstuttgart);
                        break;
                    case "14":
                        ImageAway.setImageResource(R.drawable.whannover);
                        break;
                    case "15":
                        ImageAway.setImageResource(R.drawable.wbremen);
                        break;
                    case "16":
                        ImageAway.setImageResource(R.drawable.wgelsenkirchen);
                        break;
                    case "17":
                        ImageAway.setImageResource(R.drawable.wmunchen);
                        break;
                    case "18":
                        ImageAway.setImageResource(R.drawable.wgladbach);
                        break;
                    case "19":
                        ImageAway.setImageResource(R.drawable.wsalzburg);
                        break;
                    case "20":
                        ImageAway.setImageResource(R.drawable.wunion);
                        break;
                    case "21":
                        ImageAway.setImageResource(R.drawable.wpaderborn);
                        break;
                    case "22":
                        ImageAway.setImageResource(R.drawable.wkoln);
                        break;
                    case "23":
                        ImageAway.setImageResource(R.drawable.whsv);
                        break;
                    case "24":
                        ImageAway.setImageResource(R.drawable.wpauli);
                        break;
                    case "25":
                        ImageAway.setImageResource(R.drawable.wbielefeld);
                        break;
                    case "26":
                        ImageAway.setImageResource(R.drawable.waue);
                        break;
                    case "27":
                        ImageAway.setImageResource(R.drawable.wdresden);
                        break;
                    case "28":
                        ImageAway.setImageResource(R.drawable.wparis);
                        break;
                    case "29":
                        ImageAway.setImageResource(R.drawable.wrealmadrid);
                        break;
                    case "30":
                        ImageAway.setImageResource(R.drawable.watleticomadrid);
                        break;
                    case "31":
                        ImageAway.setImageResource(R.drawable.wbarcelona);
                        break;
                    case "32":
                        ImageAway.setImageResource(R.drawable.wtottenham);
                        break;
                    case "33":
                        ImageAway.setImageResource(R.drawable.wmancity);
                        break;
                    case "34":
                        ImageAway.setImageResource(R.drawable.wliverpool);
                        break;
                    case "35":
                        ImageAway.setImageResource(R.drawable.wchelsea);
                        break;
                    case "36":
                        ImageAway.setImageResource(R.drawable.wturin);
                        break;
                    case "37":
                        ImageAway.setImageResource(R.drawable.wneapel);
                        break;
                    case "38":
                        ImageAway.setImageResource(R.drawable.wintermailand);
                        break;
                    case "39":
                        ImageAway.setImageResource(R.drawable.wajax);
                        break;
                    case "40":
                        ImageAway.setImageResource(R.drawable.wdonezk);
                        break;
                    case "41":
                        ImageAway.setImageResource(R.drawable.wbochum);
                        break;
                    case "42":
                        ImageAway.setImageResource(R.drawable.wceltic);
                        break;
                    default:
                        ImageAway.setImageResource(R.drawable.wrbl);
                        break;
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setCountdown(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Match");
        mDatabase.keepSynced(true);


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Info = null;
                Info = dataSnapshot.child("Info").getValue().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
                String oldTime = formatter.format(new Date());
                String NewTime = dataSnapshot.child("Info").getValue().toString();
                Date oldDate, newDate;
                try {
                    oldDate = formatter.parse(oldTime);
                    newDate = formatter.parse(NewTime);
                    oldLong = oldDate.getTime();
                    NewLong = newDate.getTime();
                    diff = NewLong - oldLong;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                counter = new MyCount2(diff, 1000);
                counter.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class MyCount2 extends CountDownTimer {
        MyCount2(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            countdownTV.setText("L\nI\nV\nE");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;


            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis));
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
            long sec = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));

            if (days==0 && hours!=0 && minutes!=0){
                countdownTV.setText(hours+"h\n"+minutes+"m\n"+sec+"s");
            }else if(days==0 && hours==0 && minutes!=0){
                countdownTV.setText(minutes+"m\n"+sec+"s");
            }else if(days==0 && hours==0 &&minutes==0){
                countdownTV.setText(sec+"s");
            }else {
                countdownTV.setText(days+"d\n"+hours+"h\n"+minutes+"m\n"+sec+"s");
            }

        }


    }


}

class Adapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}

