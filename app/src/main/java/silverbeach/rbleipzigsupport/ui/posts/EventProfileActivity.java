package silverbeach.rbleipzigsupport.ui.posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import silverbeach.rbleipzigsupport.ProfileActivity;
import silverbeach.rbleipzigsupport.R;

public class EventProfileActivity extends AppCompatActivity {

    private String event_name_from_extra, event_id_from_extra, userName;
    private TextView texttv, clickstv;
    private ListView listView;
    private DatabaseReference mDatabase, mUserDatabase;
    private Comment comment;
    private ArrayList<String> list, uid_list;
    private ArrayAdapter<String> adapter;
    private String clicksString, newComment,  current_uid;;
    private int clicksInt;
    private long events_comments;
    private TextInputLayout input;
    private TextInputEditText input_text;
    private Boolean isConnected;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;
    private ImageButton sendBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_profile);

        event_name_from_extra = getIntent().getStringExtra("Event_Name");
        event_id_from_extra = getIntent().getStringExtra("Event_Id");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(event_name_from_extra);


        clickstv = (TextView) findViewById(R.id.clicktv);
        listView = (ListView) findViewById(R.id.listView);
        texttv = (TextView) findViewById(R.id.event_user);
        input = (TextInputLayout) findViewById(R.id.input);
        input = (TextInputLayout) findViewById(R.id.input);
        input_text = (TextInputEditText) findViewById(R.id.input_text);
        sendBtn = findViewById(R.id.sendBtn);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id_from_extra).child("comments");
        mDatabase.keepSynced(true);
        comment = new Comment();
        list = new ArrayList<>();
        uid_list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.comment2, R.id.commentText, list);


        checkConnection();
        getUserName();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    comment = ds.getValue(Comment.class);
                    list.add(comment.getCommentText().toString()+" -"+comment.getUser().toString());
                    uid_list.add(comment.getUid());
                }

                listView.setAdapter(adapter);
                //scroll to bottom
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String uid = "null";
                try {
                    uid = uid_list.get(i);
                    Intent newIntentA = new Intent(getApplicationContext(), ProfileActivity.class);
                    newIntentA.putExtra("user_id", uid);
                    startActivity(newIntentA);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Ausgewählter Benutzer hat veraltete App Version!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        getClicks();

        event_name_from_extra = getIntent().getStringExtra("Event_Name");
        final String event_text_from_extra = getIntent().getStringExtra("Event_Text");
        events_comments = getIntent().getLongExtra("Event_Comments", 0);
        texttv.setText(event_text_from_extra);

        Linkify.addLinks(texttv, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
        Linkify.addLinks(texttv, Linkify.ALL );

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadComment();
                hideKeyboard(EventProfileActivity.this);
            }
        });

    }

    public void getClicks(){
        // klicks downloaden
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id_from_extra).child("Clicks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clicksString = dataSnapshot.getValue().toString();
                clicksInt = Integer.parseInt(clicksString);
                clickstv.setText("Aufrufe: "+clicksInt);

                String seen = PreferenceManager.getDefaultSharedPreferences(EventProfileActivity.this).getString("event"+event_name_from_extra, "unseen");
                if (seen.equals("unseen")){
                    int newClicks = clicksInt++;
                    mDatabase.setValue(clicksInt++);
                    PreferenceManager.getDefaultSharedPreferences(EventProfileActivity.this).edit().putString("event"+event_name_from_extra, "seen").apply();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadComment(){
        //Progress
        mProgress = new ProgressDialog(getApplicationContext());
        mProgress.setTitle("Kommentar wird hochgeladen");
        mProgress.setMessage("Bitte warte einen Augenblick");
//        mProgress.show();


        newComment = input.getEditText().getText().toString().trim();
        if (isConnected) {
            if (!TextUtils.isEmpty(newComment)) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference index = database.getReference("Events").child(event_id_from_extra).child("comments");
                index.push().setValue(new silverbeach.rbleipzigsupport.ui.posts.Comment(newComment, userName, current_uid)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        try {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                input_text.setText("");
                                input.clearFocus();
                                //set new timestamp
                                Map timestamp = ServerValue.TIMESTAMP;
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id_from_extra).child("Timestamp");
                                mDatabase.setValue(timestamp);



                            } else {
                                mProgress.dismiss();
                                Toast.makeText(getApplicationContext(), "Kommentar konnten nicht gespeichert werden", Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e) {
                            System.out.println("Error " + e.getMessage());
                            return;
                        }
                    }
                });
            } else {
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), "Du musst erst ein Kommentar schreiben", Toast.LENGTH_LONG).show();
            }
        }else {
            //Wenn kein internet
            Snackbar.make(getCurrentFocus(), "Keine Verbindung zum Server!", Snackbar.LENGTH_LONG)
                    .show();

            hideKeyboard(EventProfileActivity.this);
            mProgress.dismiss();
        }
    }

    public void getUserName(){
        //Name herausfinden für comments
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        current_uid = mCurrentUser.getUid();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void checkConnection(){{
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    isConnected = true;
                } else {
                    isConnected = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
