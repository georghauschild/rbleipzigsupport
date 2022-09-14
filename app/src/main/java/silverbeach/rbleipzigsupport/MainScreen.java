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

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import silverbeach.rbleipzigsupport.ui.news.News1Fragment;
import silverbeach.rbleipzigsupport.ui.news.News2Fragment;
import silverbeach.rbleipzigsupport.ui.news.News3Fragment;
import silverbeach.rbleipzigsupport.ui.news.News4Fragment;
import silverbeach.rbleipzigsupport.ui.news.News5Fragment;
import silverbeach.rbleipzigsupport.ui.posts.PostsFragment;

public class MainScreen extends AppCompatActivity {

    ViewPager2 myViewPager2;
    Adapter myAdapter;
    private ImageView ImageHome, ImageAway;
    public TextView countdownTV, clubsTV, competitionTV;
    private DatabaseReference mDatabase, mUserDatabase;
    private Comment2 comment2;
    private ListView listView;
    private ArrayList<String> list, uid_list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        myViewPager2 = findViewById(R.id.myViewPager2);


        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        ImageHome = (ImageView) findViewById(R.id.imageHomeClub);
        ImageAway = (ImageView) findViewById(R.id.imageAwayClub);
        countdownTV = (TextView) findViewById(R.id.countdownTextView);
        clubsTV = (TextView) findViewById(R.id.clubsTextView);
        competitionTV = (TextView) findViewById(R.id.competitionTextView);
        listView = (ListView) findViewById(R.id.listView);

        myAdapter = new Adapter(getSupportFragmentManager(), getLifecycle());
        myViewPager2.setOffscreenPageLimit(1);
        int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.fab_margin);
        int peekMarginPx = getResources().getDimensionPixelOffset(R.dimen.text_margin);

        RecyclerView rv = (RecyclerView) myViewPager2.getChildAt(0);
        rv.setClipToPadding(false);
        int padding = peekMarginPx + pageMarginPx;
        rv.setPadding(padding, 0, padding, 0);

        // add Fragments in your ViewPagerFragmentAdapter class
        myAdapter.addFragment(new News1Fragment());
        myAdapter.addFragment(new News2Fragment());
        myAdapter.addFragment(new News3Fragment());
        myAdapter.addFragment(new News4Fragment());
        myAdapter.addFragment(new News5Fragment());


        // set Orientation in your ViewPager2
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);
        myViewPager2.getOffscreenPageLimit();

        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new PostsFragment()).commit();

        setComments();

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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



