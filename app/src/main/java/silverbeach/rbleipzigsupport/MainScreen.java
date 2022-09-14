package silverbeach.rbleipzigsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import silverbeach.rbleipzigsupport.ui.match.MatchFragment;
import silverbeach.rbleipzigsupport.ui.news.NewsFragment;
import silverbeach.rbleipzigsupport.ui.posts.PostsFragment;
import silverbeach.rbleipzigsupport.ui.table.TableFragment;
import silverbeach.rbleipzigsupport.ui.transfers.TransfersFragment;

public class MainScreen extends AppCompatActivity {

    ViewPager2 myViewPager2;
    Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        myViewPager2 = findViewById(R.id.viewPager2);
        myAdapter = new Adapter(getSupportFragmentManager(), getLifecycle());
        myViewPager2.setOffscreenPageLimit(1);
        int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.fab_margin);
        int peekMarginPx = getResources().getDimensionPixelOffset(R.dimen.text_margin);

        RecyclerView rv = (RecyclerView) myViewPager2.getChildAt(0);
        rv.setClipToPadding(false);
        int padding = peekMarginPx + pageMarginPx;
        rv.setPadding(padding, 0, padding, 0);

        // add Fragments in your ViewPagerFragmentAdapter class
        myAdapter.addFragment(new NewsFragment());
        myAdapter.addFragment(new NewsFragment());
        myAdapter.addFragment(new NewsFragment());
        myAdapter.addFragment(new NewsFragment());
        myAdapter.addFragment(new NewsFragment());
        myAdapter.addFragment(new NewsFragment());
        myAdapter.addFragment(new NewsFragment());

        // set Orientation in your ViewPager2
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);
        myViewPager2.getOffscreenPageLimit();

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