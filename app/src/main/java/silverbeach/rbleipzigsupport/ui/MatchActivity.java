package silverbeach.rbleipzigsupport.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import silverbeach.rbleipzigsupport.Comment2;
import silverbeach.rbleipzigsupport.R;import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import silverbeach.rbleipzigsupport.MainScreen;
import silverbeach.rbleipzigsupport.ProfileActivity;
import silverbeach.rbleipzigsupport.R;
import silverbeach.rbleipzigsupport.StartActivity;
import silverbeach.rbleipzigsupport.ui.match.Comment;
import silverbeach.rbleipzigsupport.ui.match.MatchFragment;

public class MatchActivity extends AppCompatActivity {

    private View mMainView3;
    private ImageView ImageHome, ImageAway;
    private ListView listView;
    private TextInputLayout input;
    private TextInputEditText input_text;
    private ImageButton sendBtn;
    private TextView infoTV, info2TV, info3TV;
    private Long diff, oldLong, NewLong;
    private MatchFragment.MyCount counter;
    private DatabaseReference mDatabase, mUserDatabase;
    private Comment2 comment2;
    private Comment comment;
    private ArrayList<String> list, uid_list;
    private ArrayAdapter<String> adapter;
    private FirebaseUser mCurrentUser;
    private String current_uid, userName, newComment, rank;
    private ProgressDialog mProgress;
    private Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        listView = (ListView) findViewById(R.id.listView);
        input = (TextInputLayout) findViewById(R.id.input);
        input_text = (TextInputEditText) findViewById(R.id.input_text);
        sendBtn = (ImageButton) findViewById(R.id.sendBtn);

        setComments();
        getUserName();
        checkConnection();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Spieltagschat");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadComment();
                hideKeyboard(MatchActivity.this);

            }
        });
        sendBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (rank.equals("admin")){
                    Intent newIntentA = new Intent(MatchActivity.this, MainScreen.class);
                    startActivity(newIntentA);


                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String uid = "null";
                try {
                    uid = uid_list.get(i);
                    Intent newIntentA = new Intent(MatchActivity.this, ProfileActivity.class);
                    newIntentA.putExtra("user_id", uid);
                    startActivity(newIntentA);
                }catch (Exception e){
                    Toast.makeText(MatchActivity.this, "Ausgewählter Benutzer hat veraltete App Version!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void setComments(){
        //comments
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("comments");
        mDatabase.keepSynced(true);
        comment2 = new Comment2();
        list = new ArrayList<>();
        uid_list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(MatchActivity.this, R.layout.comment2, R.id.commentText, list);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    comment = ds.getValue(Comment.class);
                    list.add(comment.getCommentText().toString() + " -" + comment.getUser().toString());
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
    }

    private void getUserName(){
        //Name herausfinden für comments
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
            });        }catch (Exception e){
            Intent startIntent = new Intent(MatchActivity.this, StartActivity.class);
            startActivity(startIntent);
            MatchActivity.this.finish();
        }
    }

    public void uploadComment() {

        //Progress
        mProgress = new ProgressDialog(MatchActivity.this);
        mProgress.setTitle("Kommentar wird hochgeladen");
        mProgress.setMessage("Bitte warte einen Augenblick");
        mProgress.show();

        newComment = input.getEditText().getText().toString().trim();

        if (isConnected) {
            if (!TextUtils.isEmpty(newComment)) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference index = database.getReference("Match").child("comments");
                index.push().setValue(new Comment(newComment, userName, current_uid)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        try {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                input_text.setText("");
                                input.clearFocus();

                            } else {
                                mProgress.dismiss();
                                Toast.makeText(MatchActivity.this, "Kommentar konnten nicht gespeichert werden", Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e) {
                            System.out.println("Error " + e.getMessage());
                            return;
                        }
                    }
                });
            } else {
                mProgress.dismiss();
                Toast.makeText(MatchActivity.this, "Du musst erst ein Kommentar schreiben", Toast.LENGTH_LONG).show();
            }
        }else {
            //Wenn kein internet
            Snackbar.make(getCurrentFocus(), "Keine Verbindung zum Server!", Snackbar.LENGTH_LONG)
                    .show();
            hideKeyboard(MatchActivity.this);
            mProgress.dismiss();
        }

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
