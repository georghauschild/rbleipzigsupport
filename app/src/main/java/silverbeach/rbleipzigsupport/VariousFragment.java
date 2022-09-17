package silverbeach.rbleipzigsupport;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import silverbeach.rbleipzigsupport.ui.match.CreateMatchActivity;
import silverbeach.rbleipzigsupport.ui.profile.MyProfileActivity;
import silverbeach.rbleipzigsupport.ui.profile.SettingsFragment;

public class VariousFragment extends Fragment {

    private View mMainView4;
    private Button profileButton, rateAppButton, logoutButton;
    private String rank, current_uid,userName;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;


    public VariousFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView4 = inflater.inflate(R.layout.fragment_various, container, false);
        getRank();

        profileButton = (Button) mMainView4.findViewById(R.id.button_profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(startIntent);

            }
        });
        profileButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (rank.equals("admin")){
                    Intent newIntentA = new Intent(getActivity(), CreateMatchActivity.class);
                    startActivity(newIntentA);

                }
                return false;
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

        logoutButton = (Button) mMainView4.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Abmelden");
                alertDialog.setMessage("Möchtest du dich wirklich abmelden?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja, abmelden",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                FirebaseAuth.getInstance().signOut();
                                Intent startIntent = new Intent(getContext(), StartActivity.class);
                                startActivity(startIntent);
                                getActivity().finish();

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Abbrechen",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });

        return mMainView4;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void getRank(){
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            current_uid = mCurrentUser.getUid();
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
            mUserDatabase.keepSynced(true);
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userName = dataSnapshot.child("name").getValue().toString();
                    rank = dataSnapshot.child("rank").getValue().toString();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

        });
    } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



