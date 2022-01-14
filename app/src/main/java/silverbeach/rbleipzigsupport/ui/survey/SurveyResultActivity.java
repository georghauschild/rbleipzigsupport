package silverbeach.rbleipzigsupport.ui.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import silverbeach.rbleipzigsupport.R;

public class SurveyResultActivity extends AppCompatActivity {

    private TextView rtv1,rtv2,rtv3,rtv4,rtv5,rtv6, questiontv, alltv;
    private String s1,s2,s3,s4,s5,s6, question;
    private String r1,r2,r3,r4,r5,r6;
    private int i1,i2,i3,i4,i5,i6, ix;
    private ProgressBar p1,p2,p3,p4,p5,p6;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);

        rtv1 = (TextView) findViewById(R.id.rbtv);
        rtv2 = (TextView) findViewById(R.id.rbtv1);
        rtv3 = (TextView) findViewById(R.id.rbtv3);
        rtv4 = (TextView) findViewById(R.id.rbtv4);
        rtv5 = (TextView) findViewById(R.id.rbtv5);
        rtv6 = (TextView) findViewById(R.id.rbtv6);
        questiontv = (TextView) findViewById(R.id.textView16);
        alltv = (TextView) findViewById(R.id.textView17);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Umfrage");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Survey");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                question = dataSnapshot.child("Question").getValue().toString();
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

                rtv1.setText(s1+"");
                rtv2.setText(s2+"");
                rtv3.setText(s3+"");
                rtv4.setText(s4+"");
                rtv5.setText(s5+"");
                rtv6.setText(s6+"");

                p1 = (ProgressBar) findViewById(R.id.progressBar);
                p2 = (ProgressBar) findViewById(R.id.progressBar1);
                p3 = (ProgressBar) findViewById(R.id.progressBar3);
                p4 = (ProgressBar) findViewById(R.id.progressBar4);
                p5 = (ProgressBar) findViewById(R.id.progressBar5);
                p6 = (ProgressBar) findViewById(R.id.progressBar6);

                if (rtv1.getText().toString()==""){
                    p1.setVisibility(View.INVISIBLE);
                    rtv1.setVisibility(View.INVISIBLE);
                }
                if (rtv2.getText().toString()==""){
                    p2.setVisibility(View.INVISIBLE);
                    rtv2.setVisibility(View.INVISIBLE);
                }
                if (rtv3.getText().toString()==""){
                    p3.setVisibility(View.INVISIBLE);
                    rtv3.setVisibility(View.INVISIBLE);
                }
                if (rtv4.getText().toString()==""){
                    p4.setVisibility(View.INVISIBLE);
                    rtv4.setVisibility(View.INVISIBLE);
                }
                if (rtv5.getText().toString()==""){
                    p5.setVisibility(View.INVISIBLE);
                    rtv5.setVisibility(View.INVISIBLE);
                }
                if (rtv6.getText().toString()==""){
                    p6.setVisibility(View.INVISIBLE);
                    rtv6.setVisibility(View.INVISIBLE);
                }

                questiontv.setText(question);
                rtv1.setText(s1+" ("+r1+")");
                rtv2.setText(s2+" ("+r2+")");
                rtv3.setText(s3+" ("+r3+")");
                rtv4.setText(s4+" ("+r4+")");
                rtv5.setText(s5+" ("+r5+")");
                rtv6.setText(s6+" ("+r6+")");

                if (!r1.equals(""))i1 = Integer.parseInt(r1);
                if (!r2.equals("")) i2 = Integer.parseInt(r2);
                if (!r3.equals(""))i3 = Integer.parseInt(r3);
                if (!r4.equals(""))i4 = Integer.parseInt(r4);
                if (!r5.equals(""))i5 = Integer.parseInt(r5);
                if (!r6 .equals(""))i6 = Integer.parseInt(r6);
                ix = i1+i2+i3+i4+i5+i6;
                alltv.setText("Teilnehmer: "+ix);

                float f1 = (float) i1/ix;
                float q1 = f1*100;
                int o1 = (int)q1;

                float f2 = (float) i2/ix;
                float q2 = f2*100;
                int o2 = (int)q2;

                float f3 = (float) i3/ix;
                float q3 = f3*100;
                int o3 = (int)q3;

                float f4 = (float) i4/ix;
                float q4 = f4*100;
                int o4 = (int)q4;

                float f5 = (float) i5/ix;
                float q5 = f5*100;
                int o5 = (int)q5;

                float f6 = (float) i6/ix;
                float q6 = f6*100;
                int o6 = (int)q6;


                List<Integer> list = Arrays.asList(o1,o2,o3,o4,o5,o6);
                int max = Collections.max(list);

                //  Toast.makeText(SurveyResultActivity.this, ""+max,
                //          Toast.LENGTH_LONG).show();



                p1.setProgress(o1);
                p1.setMax(max);
                p2.setProgress(o2);
                p2.setMax(max);
                p3.setProgress(o3);
                p3.setMax(max);
                p4.setProgress(o4);
                p4.setMax(max);
                p5.setProgress(o5);
                p5.setMax(max);
                p6.setProgress(o6);
                p6.setMax(max);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
