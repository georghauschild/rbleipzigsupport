package silverbeach.rbleipzigsupport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import silverbeach.rbleipzigsupport.ui.profile.MyProfileActivity;
import silverbeach.rbleipzigsupport.ui.profile.SettingsFragment;

public class VariousFragment extends Fragment {

    private View mMainView4;
    private Button profileButton, rateAppButton;

    public VariousFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView4 = inflater.inflate(R.layout.fragment_various, container, false);

        profileButton = (Button) mMainView4.findViewById(R.id.button_profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(startIntent);

            }
        });

        rateAppButton = (Button) mMainView4.findViewById(R.id.rateButton);
        rateAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppRate.with(getContext())
                        .setDebug(true) // default false
                        .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                            @Override
                            public void onClickButton(int which) {
                                Log.d(MainActivity.class.getName(), Integer.toString(which));
                            }
                        })
                        .monitor();

                // Show a dialog if meets conditions
                AppRate.showRateDialogIfMeetsConditions(getActivity());
            }
        });

        return mMainView4;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}

