package silverbeach.rbleipzigsupport.ui.survey;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import silverbeach.rbleipzigsupport.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyFragment extends Fragment {

    private DatabaseReference mDatabase;
    private String frage,s1,s2,s3,s4,s5,s6;
    private String r1,r2,r3,r4,r5,r6;
    private int i1,i2,i3,i4,i5,i6;
    private TextView questionTv;
    private Button b1,b2,b3,b4,b5,b6, bresult;

    private View mMainView;


    public SurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_survey, container, false);

        // Inflate the layout for this fragment


        questionTv = (TextView) mMainView.findViewById(R.id.textView14);
        b1 = (Button) mMainView.findViewById(R.id.survey1);
        b2 = (Button) mMainView.findViewById(R.id.survey2);
        b3 = (Button) mMainView.findViewById(R.id.survey3);
        b4 = (Button) mMainView.findViewById(R.id.survey4);
        b5 = (Button) mMainView.findViewById(R.id.survey5);
        b6 = (Button) mMainView.findViewById(R.id.survey6);
        bresult = (Button) mMainView.findViewById(R.id.bresult);
        bresult.setVisibility(View.INVISIBLE);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //informationen aus server laden und anzeigen
        // mDatabase.keepSynced(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Survey");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                frage = dataSnapshot.child("Question").getValue().toString();
                String name = preferences.getString("SURVEY", "d");
                if (name.contains(frage)){
                    blockButtons();
                }
                s1 = dataSnapshot.child("Solution1").child("Solution1").getValue().toString();
                s2 = dataSnapshot.child("Solution2").child("Solution2").getValue().toString();
                s3 = dataSnapshot.child("Solution3").child("Solution3").getValue().toString();
                s4 = dataSnapshot.child("Solution4").child("Solution4").getValue().toString();
                s5 = dataSnapshot.child("Solution5").child("Solution5").getValue().toString();
                s6 = dataSnapshot.child("Solution6").child("Solution6").getValue().toString();

                r1 = dataSnapshot.child("Solution1").child("Result").getValue().toString();
                r2 = dataSnapshot.child("Solution2").child("Result").getValue().toString();
                r3 = dataSnapshot.child("Solution3").child("Result").getValue().toString();
                r4 = dataSnapshot.child("Solution4").child("Result").getValue().toString();
                r5 = dataSnapshot.child("Solution5").child("Result").getValue().toString();
                r6 = dataSnapshot.child("Solution6").child("Result").getValue().toString();

                questionTv.setText(frage);
                b1.setText(s1+"");
                b2.setText(s2+"");
                b3.setText(s3+"");
                b4.setText(s4+"");
                b5.setText(s5+"");
                b6.setText(s6+"");

                if (b1.getText().toString()==""){
                    b1.setVisibility(View.INVISIBLE);
                }
                if (b2.getText().toString()==""){
                    b2.setVisibility(View.INVISIBLE);
                }
                if (b3.getText().toString()==""){
                    b3.setVisibility(View.INVISIBLE);
                }
                if (b4.getText().toString()==""){
                    b4.setVisibility(View.INVISIBLE);
                }
                if (b5.getText().toString()==""){
                    b5.setVisibility(View.INVISIBLE);
                }
                if (b6.getText().toString()==""){
                    b6.setVisibility(View.INVISIBLE);
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Kein Internet",
                        Toast.LENGTH_LONG).show();
            }
        });



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i1 = Integer.parseInt(r1.toString());
                i1++;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Survey").child("Solution1").child("Result");
                mDatabase.setValue(i1);
                saveVote();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i2 = Integer.parseInt(r2.toString());
                i2++;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Survey").child("Solution2").child("Result");
                mDatabase.setValue(i2);
                saveVote();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i3 = Integer.parseInt(r3.toString());
                i3++;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Survey").child("Solution3").child("Result");
                mDatabase.setValue(i3);
                saveVote();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i4 = Integer.parseInt(r4.toString());
                i4++;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Survey").child("Solution4").child("Result");
                mDatabase.setValue(i4);
                saveVote();
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i5 = Integer.parseInt(r5.toString());
                i5++;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Survey").child("Solution5").child("Result");
                mDatabase.setValue(i5);
                saveVote();
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i6 = Integer.parseInt(r6.toString());
                i6++;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Survey").child("Solution6").child("Result");
                mDatabase.setValue(i6);
                saveVote();
            }
        });
        bresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent4 = new Intent(getActivity(), SurveyResultActivity.class);
                startActivity(newIntent4);
            }
        });

        return mMainView;
    }

    private void saveVote() {

        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("SURVEY", frage).apply();
        Intent newIntent4 = new Intent(getActivity(), SurveyResultActivity.class);
        startActivity(newIntent4);
    }

    private void blockButtons(){

          b1.setClickable(false);
          b2.setClickable(false);
          b3.setClickable(false);
          b4.setClickable(false);
          b5.setClickable(false);
          b6.setClickable(false);

        b1.setAlpha(0.35f);
        b2.setAlpha(0.35f);
        b3.setAlpha(0.35f);
        b4.setAlpha(0.35f);
        b5.setAlpha(0.35f);
        b6.setAlpha(0.35f);
        bresult.setVisibility(View.VISIBLE);
    }

}
