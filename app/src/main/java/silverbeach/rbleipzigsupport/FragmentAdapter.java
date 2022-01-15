package silverbeach.rbleipzigsupport;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import silverbeach.rbleipzigsupport.ui.match.MatchFragment;
import silverbeach.rbleipzigsupport.ui.posts.PostsFragment;
import silverbeach.rbleipzigsupport.ui.profile.SettingsFragment;
import silverbeach.rbleipzigsupport.ui.survey.SurveyFragment;
import silverbeach.rbleipzigsupport.ui.transfers.TransfersFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 1: return new TransfersFragment();
            case 2: return new MatchFragment();
            case 3: return new PostsFragment();
            case 4: return new SettingsFragment();

        }

        return new SurveyFragment();
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
