package silverbeach.rbleipzigsupport.ui.transfers;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import silverbeach.rbleipzigsupport.R;


public class TransfersFragment extends Fragment {

    private View mMainView4;
    private String allText;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
    private Button p1,p2,p3,p4,p5,p6,p7,p8,p9,p10, n1,n2,n3,n4,n5,n6,n7,n8,n9,n10;
    int i1,i2,i3,i4,i5,i6,i7,i8,i9,i10;
    int o1,o2,o3,o4,o5,o6,o7,o8,o9,o10;
    private TableRow t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    private String [] parts;
    private DatabaseReference mDatabase;
    private String base1,base2,base3,base4,base5,base6,base7,base8,base9,base10;
    private SharedPreferences preferences;
    private Boolean isConnected;
    private LottieAnimationView lottieAnimationView;

    public TransfersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView4 = inflater.inflate(R.layout.fragment_transfers, container, false);


        lottieAnimationView = mMainView4.findViewById(R.id.animationView);

        tv1 = mMainView4.findViewById(R.id.transferTV1);
        tv2 = mMainView4.findViewById(R.id.transferTV2);
        tv3 = mMainView4.findViewById(R.id.transferTV3);
        tv4 = mMainView4.findViewById(R.id.transferTV4);
        tv5 = mMainView4.findViewById(R.id.transferTV5);
        tv6 = mMainView4.findViewById(R.id.transferTV6);
        tv7 = mMainView4.findViewById(R.id.transferTV7);
        tv8 = mMainView4.findViewById(R.id.transferTV8);
        tv9 = mMainView4.findViewById(R.id.transferTV9);
        tv10 = mMainView4.findViewById(R.id.transferTV10);

        t1 = mMainView4.findViewById(R.id.t1);
        t2 = mMainView4.findViewById(R.id.t2);
        t3 = mMainView4.findViewById(R.id.t3);
        t4 = mMainView4.findViewById(R.id.t4);
        t5 = mMainView4.findViewById(R.id.t5);
        t6 = mMainView4.findViewById(R.id.t6);
        t7 = mMainView4.findViewById(R.id.t7);
        t8 = mMainView4.findViewById(R.id.t8);
        t9 = mMainView4.findViewById(R.id.t9);
        t10 = mMainView4.findViewById(R.id.t10);

        parts = new String[10];


        downloadTransferNews();

        p1 = mMainView4.findViewById(R.id.p1);
        p2 = mMainView4.findViewById(R.id.p2);
        p3 = mMainView4.findViewById(R.id.p3);
        p4 = mMainView4.findViewById(R.id.p4);
        p5 = mMainView4.findViewById(R.id.p5);
        p6 = mMainView4.findViewById(R.id.p6);
        p7 = mMainView4.findViewById(R.id.p7);
        p8 = mMainView4.findViewById(R.id.p8);
        p9 = mMainView4.findViewById(R.id.p9);
        p10 = mMainView4.findViewById(R.id.p10);
        n1 = mMainView4.findViewById(R.id.n1);
        n2 = mMainView4.findViewById(R.id.n2);
        n3 = mMainView4.findViewById(R.id.n3);
        n4 = mMainView4.findViewById(R.id.n4);
        n5 = mMainView4.findViewById(R.id.n5);
        n6 = mMainView4.findViewById(R.id.n6);
        n7 = mMainView4.findViewById(R.id.n7);
        n8 = mMainView4.findViewById(R.id.n8);
        n9 = mMainView4.findViewById(R.id.n9);
        n10 = mMainView4.findViewById(R.id.n10);
        checkConnection();

        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base1).child("p");
                    mDatabase.setValue(++i1);
                    checkButtonWasAlreadyClicked(base1);
                }
            }
        });
        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base2).child("p");
                mDatabase.setValue(++i2);
                checkButtonWasAlreadyClicked(base2);
                }

            }
        });
        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base3).child("p");
                mDatabase.setValue(++i3);
                checkButtonWasAlreadyClicked(base3);
                    }

            }
        });
        p4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base4).child("p");
                mDatabase.setValue(++i4);
                checkButtonWasAlreadyClicked(base4);
                }

            }
        });
        p5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base5).child("p");
                mDatabase.setValue(++i5);
                checkButtonWasAlreadyClicked(base5);
                    }

            }
        });
        p6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base6).child("p");
                mDatabase.setValue(++i6);
                checkButtonWasAlreadyClicked(base6);
                }

            }
        });
        p7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base7).child("p");
                mDatabase.setValue(++i7);
                checkButtonWasAlreadyClicked(base7);
                    }

            }
        });
        p8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base8).child("p");
                mDatabase.setValue(++i8);
                checkButtonWasAlreadyClicked(base8);
                }

            }
        });
        p9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base9).child("p");
                mDatabase.setValue(++i9);
                checkButtonWasAlreadyClicked(base9);
                    }

            }
        });
        p10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base10).child("p");
                mDatabase.setValue(++i10);
                checkButtonWasAlreadyClicked(base10);
                }

            }
        });
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base1).child("n");
                mDatabase.setValue(++o1);
                checkButtonWasAlreadyClicked(base1);
                    }

            }
        });
        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base2).child("n");
                mDatabase.setValue(++o2);
                checkButtonWasAlreadyClicked(base2);
                }

            }
        });
        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base3).child("n");
                mDatabase.setValue(++o3);
                checkButtonWasAlreadyClicked(base3);
                    }

            }
        });
        n4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base4).child("n");
                mDatabase.setValue(++o4);
                checkButtonWasAlreadyClicked(base4);
                }

            }
        });
        n5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base5).child("n");
                mDatabase.setValue(++o5);
                checkButtonWasAlreadyClicked(base5);
                    }

            }
        });
        n6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base6).child("n");
                mDatabase.setValue(++o6);
                checkButtonWasAlreadyClicked(base6);
                }

            }
        });
        n7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base7).child("n");
                mDatabase.setValue(++o7);
                checkButtonWasAlreadyClicked(base7);
                    }

            }
        });
        n8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base8).child("n");
                mDatabase.setValue(++o8);
                checkButtonWasAlreadyClicked(base8);
                }

            }
        });
        n9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base9).child("n");
                mDatabase.setValue(++o9);
                checkButtonWasAlreadyClicked(base9);
                    }

            }
        });
        n10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (isConnected) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base10).child("n");
                mDatabase.setValue(++o10);
                checkButtonWasAlreadyClicked(base10);
                }
            }
        });



        // Inflate the layout for this fragment
        return mMainView4;
    }

    private void lockVotedButtons() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String alreadyVotedBases = preferences.getString("VotedBases", "");
        if (alreadyVotedBases.contains(base1)){
            t1.setAlpha(0.2f);
            p1.setClickable(false);
            n1.setClickable(false); }
        if (alreadyVotedBases.contains(base2)){
            t2.setAlpha(0.2f);
            p2.setClickable(false);
            n2.setClickable(false); }
        if (alreadyVotedBases.contains(base3)){
            t3.setAlpha(0.2f);
            p3.setClickable(false);
            n3.setClickable(false); }
        if (alreadyVotedBases.contains(base4)){
            t4.setAlpha(0.2f);
            p4.setClickable(false);
            n4.setClickable(false); }
        if (alreadyVotedBases.contains(base5)){
            t5.setAlpha(0.2f);
            p5.setClickable(false);
            n5.setClickable(false); }
        if (alreadyVotedBases.contains(base6)){
            t6.setAlpha(0.2f);
            p6.setClickable(false);
            n6.setClickable(false); }
        if (alreadyVotedBases.contains(base7)){
            t7.setAlpha(0.2f);
            p7.setClickable(false);
            n7.setClickable(false); }
        if (alreadyVotedBases.contains(base8)){
            t8.setAlpha(0.2f);
            p8.setClickable(false);
            n8.setClickable(false); }
        if (alreadyVotedBases.contains(base9)){
            t9.setAlpha(0.2f);
            p9.setClickable(false);
            n9.setClickable(false); }
        if (alreadyVotedBases.contains(base10)){
            t10.setAlpha(0.2f);
            p10.setClickable(false);
            n10.setClickable(false); }

    }

    private void checkButtonWasAlreadyClicked(String newBase) {

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String alreadyVotedBases = preferences.getString("VotedBases", "");
        SharedPreferences.Editor editor = preferences.edit();
        if (newBase != "x") {
            editor.putString("VotedBases", alreadyVotedBases + ", " + newBase);
            editor.apply();
        }else {
            editor.putString("VotedBases", alreadyVotedBases);
            editor.apply();
        }
        lockVotedButtons();
    }

    public void safeOldList(){

             base1 = Base64.encodeToString(parts[0].getBytes(), Base64.NO_WRAP);
             base2 = Base64.encodeToString(parts[1].getBytes(), Base64.NO_WRAP);
             base3 = Base64.encodeToString(parts[2].getBytes(), Base64.NO_WRAP);
             base4 = Base64.encodeToString(parts[3].getBytes(), Base64.NO_WRAP);
             base5 = Base64.encodeToString(parts[4].getBytes(), Base64.NO_WRAP);
             base6 = Base64.encodeToString(parts[5].getBytes(), Base64.NO_WRAP);
             base7 = Base64.encodeToString(parts[6].getBytes(), Base64.NO_WRAP);
             base8 = Base64.encodeToString(parts[7].getBytes(), Base64.NO_WRAP);
             base9 = Base64.encodeToString(parts[8].getBytes(), Base64.NO_WRAP);
             base10 = Base64.encodeToString(parts[9].getBytes(), Base64.NO_WRAP);


            mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base1);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        p1.setText("+"+dataSnapshot.child("p").getValue(Integer.class));
                        n1.setText("-"+dataSnapshot.child("n").getValue(Integer.class));
                        i1 = dataSnapshot.child("p").getValue(Integer.class);
                        o1 = dataSnapshot.child("n").getValue(Integer.class);
                        System.out.println("XXX"+i1);
                        stopAnimation();

                    }catch (Exception e){
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base1).child("p");
                        mDatabase.setValue(0);
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base1).child("n");
                        mDatabase.setValue(0);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base2);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p2.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n2.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i2 = dataSnapshot.child("p").getValue(Integer.class);
                    o2 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base2).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base2).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base3);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p3.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n3.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i3 = dataSnapshot.child("p").getValue(Integer.class);
                    o3 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base3).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base3).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base4);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p4.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n4.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i4 = dataSnapshot.child("p").getValue(Integer.class);
                    o4 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base4).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base4).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base5);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p5.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n5.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i5 = dataSnapshot.child("p").getValue(Integer.class);
                    o5 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base5).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base5).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base6);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p6.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n6.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i6 = dataSnapshot.child("p").getValue(Integer.class);
                    o6 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base6).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base6).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base7);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p7.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n7.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i7 = dataSnapshot.child("p").getValue(Integer.class);
                    o7 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base7).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base7).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base8);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p8.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n8.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i8 = dataSnapshot.child("p").getValue(Integer.class);
                    o8 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base8).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base8).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base9);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p9.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n9.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i9 = dataSnapshot.child("p").getValue(Integer.class);
                    o9 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base9).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base9).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base10);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    p10.setText("+"+dataSnapshot.child("p").getValue().toString());
                    n10.setText("-"+dataSnapshot.child("n").getValue().toString());
                    i10 = dataSnapshot.child("p").getValue(Integer.class);
                    o10 = dataSnapshot.child("n").getValue(Integer.class);
                }catch (Exception e){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base10).child("p");
                    mDatabase.setValue(0);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Transfers").child(base10).child("n");
                    mDatabase.setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }

    public void checkConnection(){{
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    isConnected = true;
                    downloadTransferNews();
                    stopAnimation();
                } else {
                    isConnected = false;
                    playAnimation();
                    try {
                        Snackbar.make(mMainView4.findViewById(android.R.id.content), "Keine Verbindung zum Server!", Snackbar.LENGTH_LONG)
                                .show();
                    }catch (Exception e){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}
    }

    private void playAnimation(){
        if (parts.length<3){
            lottieAnimationView.playAnimation();
            lottieAnimationView.setVisibility(View.VISIBLE);
        }
    }
    private void stopAnimation(){
        lottieAnimationView.pauseAnimation();
        lottieAnimationView.setVisibility(View.INVISIBLE);
    }

    private void downloadTransferNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.rb-fans.de")
                            .timeout(30000).ignoreContentType(true).get();
                    allText = doc.text();
                    allText = allText.substring(allText.indexOf("PERSONAL-GERÜCHTE")+17).trim();
                    allText = allText.substring(0, allText.indexOf("Archiv Personal-Gerüchte")).trim();
                    allText = allText.replace("Link Diskussion", "");

                    int x = 1;
                    String content;
                    while (x<=10) {

                        content = allText.substring( 0, allText.indexOf("Link Personalie")).trim();
                        allText = allText.replace(content, "");
                        allText = allText.replaceFirst("Link Personalie", "");
                        parts[x-1]= content;
                        System.out.println(parts[x-1]);
                        x++;

                    }

                    safeOldList();
                    checkButtonWasAlreadyClicked("x");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tv1.setText(parts[0]);
                            tv2.setText(parts[1]);
                            tv3.setText(parts[2]);
                            tv4.setText(parts[3]);
                            tv5.setText(parts[4]);
                            tv6.setText(parts[5]);
                            tv7.setText(parts[6]);
                            tv8.setText(parts[7]);
                            tv9.setText(parts[8]);
                            tv10.setText(parts[9]);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();

                    downloadTransferNews();
                }
            }
        }).start();
    }



}
