package silverbeach.rbleipzigsupport.ui.posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import silverbeach.rbleipzigsupport.MainActivity;
import silverbeach.rbleipzigsupport.R;

public class WritePostActivity extends AppCompatActivity {

    private TextInputLayout event_name;
    private TextInputLayout event_text;
    private Button event_create;
    private DatabaseReference mDatabase;
    private ProgressDialog mLoginProgress;
    private long counter;
    private long reverseCounter;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        event_name = (TextInputLayout) findViewById(R.id.comment_text);
        event_text = (TextInputLayout) findViewById(R.id.event_user);
        event_create = (Button) findViewById(R.id.event_finish_btn);
        mLoginProgress = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Beitrag erstellen");

        //ORDNUNG DER EVENTS DURCH HERAUSFINDEN DER MENGE AN EVENTS UND DANN TIMESTAMP +1
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = dataSnapshot.getChildrenCount();
                reverseCounter = -counter;
                //   Toast.makeText(Create_EventActivity.this, "createcounter: "+counter,
                //          Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Name herausfinden für comments
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        event_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(WritePostActivity.this);
                builder.setTitle("Achtung");
                builder.setMessage("Entspricht der Post den Regeln und beschreibt die Überschrift gut das Thema?");
                builder.setPositiveButton("Ja, alles korrekt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = event_name.getEditText().getText().toString();
                        String text = event_text.getEditText().getText().toString();
                        if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(text) & !name.contains(".")& !name.contains("#")& !name.contains("$")& !name.contains("[")& !name.contains("]")& !name.contains("/"))
                        {
                            mLoginProgress.setTitle("Post wird verarbeitet");
                            mLoginProgress.setMessage("Einen Moment Geduld bitte");
                            mLoginProgress.setCanceledOnTouchOutside(false);
                            mLoginProgress.show();

                            uploadEvent(name, text);

                        }
                        else {
                            Toast.makeText(WritePostActivity.this, "Unzulässige Zeichen: / . # ! $ [ ] /", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton("Überarbeiten", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();


            }
        });


    }



    private void uploadEvent(String name, String text) {
        int zahl =(int)((Math.random()) * 1000 + 1);
        String event_id = name+zahl;
        Map timestamp = ServerValue.TIMESTAMP;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id).child("Timestamp");
        mDatabase.setValue(timestamp);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id).child("Name");
        mDatabase.setValue(name);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id).child("uid");
        mDatabase.setValue(mCurrentUser.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id).child("Id");
        mDatabase.setValue(event_id);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id).child("User");
        mDatabase.setValue(userName);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id).child("Clicks");
        mDatabase.setValue(0);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id).child("Counter");
        mDatabase.setValue(reverseCounter--);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id).child("Text");
        mDatabase.setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mLoginProgress.hide();
                finish();

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
