package silverbeach.rbleipzigsupport.ui.profile;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import silverbeach.rbleipzigsupport.MainActivity;
import silverbeach.rbleipzigsupport.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private View mMainView;

    private DatabaseReference mUserDatabase, mStatusDatabase;
    private FirebaseUser mCurrentUser;
    public static String name;
    private ImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus, mScore;
    private Button mStatusBtn;
    private Button mImageBtn;
    private StorageReference mImageStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_PICK = 1;
    private String m_Text = "";
    private ProgressDialog mProgress;



    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inflate the layout for this fragment
        mDisplayImage =  mMainView.findViewById(R.id.settings_image);
        mName = (TextView) mMainView.findViewById(R.id.settings_name);
        mStatus = (TextView) mMainView.findViewById(R.id.settings_status);
        mStatusBtn = (Button) mMainView.findViewById(R.id.settings_status_btn);
        mImageBtn = (Button) mMainView.findViewById(R.id.settings_image_btn);
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);

                if(!image.equals("default")) {

                    Picasso.with(getActivity()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(getContext()).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Statustext eingeben");

                 // Set up the input
                final EditText input = new EditText(getContext());
                 // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mProgress = new ProgressDialog(getActivity());
                        mProgress.setTitle("Änderungen werden gespeichert");
                        mProgress.setMessage("Bitte warte einen Augenblick");
                        mProgress.show();
                        m_Text = input.getText().toString();
                        if (!m_Text.equals("")) {
                            mStatusDatabase.child("status").setValue(m_Text).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        mProgress.dismiss();

                                    } else {

                                        Toast.makeText(getContext(), "Änderungen konnten nicht gespeichert werden", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }else {
                            Toast.makeText(getContext(), "Statusbeschreibung eingeben!", Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();



            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainIntent = new Intent(getActivity(), ImageProfileActivity.class);
                startActivity(mainIntent);

                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
                        */

            }
        });


        return mMainView;
    }



}
