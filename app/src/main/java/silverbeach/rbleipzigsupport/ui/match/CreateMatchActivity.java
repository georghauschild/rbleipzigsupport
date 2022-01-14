package silverbeach.rbleipzigsupport.ui.match;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import silverbeach.rbleipzigsupport.R;

public class CreateMatchActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button GoBtn , DeleteBtn;
    private EditText HomeEditText, AwayEditText, InfoEditText, Info2EditText, Info3EditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        HomeEditText = (EditText) findViewById(R.id.editTextHome);
        AwayEditText = (EditText) findViewById(R.id.editTextAway);
        InfoEditText = (EditText) findViewById(R.id.editTextInfo);
        Info2EditText = (EditText) findViewById(R.id.editTextInfo2);
        Info3EditText = (EditText) findViewById(R.id.editTextInfo3);
        GoBtn = (Button) findViewById(R.id.buttonGo);
        DeleteBtn = (Button) findViewById(R.id.button_delete);

        String homeTeamString, awayTeamString;





        GoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String homeTeamString, awayTeamString, infoString,info2String, info3String;
                homeTeamString = HomeEditText.getText().toString();
                awayTeamString = AwayEditText.getText().toString();
                infoString = InfoEditText.getText().toString();
                info2String = Info2EditText.getText().toString();
                info3String = Info3EditText.getText().toString();



                mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("Home");
                mDatabase.setValue(homeTeamString);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("Away");
                mDatabase.setValue(awayTeamString);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("Info");
                mDatabase.setValue(infoString);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("Info");
                mDatabase.setValue(infoString);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("Info2");
                mDatabase.setValue(info2String);
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("Info3");
                mDatabase.setValue(info3String);
            }
        });

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("comments");
                mDatabase.setValue("deleted");
            }
        });


    }
}

