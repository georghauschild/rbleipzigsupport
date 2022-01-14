package silverbeach.rbleipzigsupport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mRootRef;
    private String user_id, display_name1, time;
    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfilTime;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileName = (TextView) findViewById(R.id.profile_displayName);
        mProfilTime = (TextView) findViewById(R.id.profile_timetv);
        mProfileStatus = (TextView) findViewById(R.id.profile_status);

        user_id = getIntent().getStringExtra("user_id");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Profil wird geladen");
        mProgressDialog.setMessage("Bitte warten Sie einen Augenblick");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profil");

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String display_name = dataSnapshot.child("name").getValue().toString();
                    display_name1 = display_name;
                    String status = dataSnapshot.child("status").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    time = dataSnapshot.child("online").getValue().toString();

                    long time_long = Long.parseLong(time);
                    try {
                        String last_seen = GetTimeAgo.getTimeAgo(time_long, ProfileActivity.this);
                        mProfilTime.setText("zuletzt online: " + last_seen);
                    }catch (Exception e){
                        mProfilTime.setText("zuletzt online: jetzt gerade");

                    }

                    mProfileName.setText(display_name);
                    mProfileStatus.setText("\"" + status + "\"");
                    mProgressDialog.dismiss();

                    Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mProfileImage);
                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    System.out.println("Error " + e.getMessage());
                    Toast.makeText(ProfileActivity.this, "Profil konnte nicht geladen werden!",
                            Toast.LENGTH_LONG).show();
                    Intent newIntent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(newIntent);
                    return;
                }
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
