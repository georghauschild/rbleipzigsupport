package silverbeach.rbleipzigsupport.ui.match;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

import silverbeach.rbleipzigsupport.MainActivity;
import silverbeach.rbleipzigsupport.ProfileActivity;
import silverbeach.rbleipzigsupport.R;
import silverbeach.rbleipzigsupport.StartActivity;


public class MatchFragment extends Fragment {

    private View mMainView3;
    private ImageView ImageHome, ImageAway;
    private ListView listView;
    private TextInputLayout input;
    private TextInputEditText input_text;
    private ImageButton sendBtn;
    private TextView infoTV, info2TV, info3TV;
    private Long diff, oldLong, NewLong;
    private MyCount counter;
    private DatabaseReference mDatabase, mUserDatabase;
    private Comment comment;
    private ArrayList<String> list, uid_list;
    private ArrayAdapter<String> adapter;
    private FirebaseUser mCurrentUser;
    private String current_uid, userName, newComment, rank;
    private ProgressDialog mProgress;
    private Boolean isConnected;




    public MatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView3 = inflater.inflate(R.layout.fragment_match, container, false);
        // Inflate the layout for this fragment
        ImageHome = (ImageView) mMainView3.findViewById(R.id.imageHomeClub);
        ImageAway = (ImageView) mMainView3.findViewById(R.id.imageAwayClub);
        listView = (ListView) mMainView3.findViewById(R.id.listView);
        input = (TextInputLayout) mMainView3.findViewById(R.id.input);
        input_text = (TextInputEditText) mMainView3.findViewById(R.id.input_text);
        sendBtn = (ImageButton) mMainView3.findViewById(R.id.sendBtn);
        infoTV = (TextView) mMainView3.findViewById(R.id.infoTv);
        info2TV = (TextView) mMainView3.findViewById(R.id.info2Tv);
        info3TV = (TextView) mMainView3.findViewById(R.id.info3Tv);

        infoTV.setSingleLine(false);
        infoTV.setText("L  O  A  D");

        setCountdown();
        setImagesAndInfos();
        setComments();
        getUserName();
        checkConnection();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadComment();
                hideKeyboard(getActivity());
            }
        });
        sendBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (rank.equals("admin")){
                    Intent newIntentA = new Intent(getActivity(), CreateMatchActivity.class);
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
                    Intent newIntentA = new Intent(getActivity(), ProfileActivity.class);
                    newIntentA.putExtra("user_id", uid);
                    startActivity(newIntentA);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Ausgewählter Benutzer hat veraltete App Version!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


        return mMainView3;
    }




    private void setCountdown(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Match");
        mDatabase.keepSynced(true);


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Info = null;
                Info = dataSnapshot.child("Info").getValue().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
                String oldTime = formatter.format(new Date());
                String NewTime = dataSnapshot.child("Info").getValue().toString();
                Date oldDate, newDate;
                try {
                    oldDate = formatter.parse(oldTime);
                    newDate = formatter.parse(NewTime);
                    oldLong = oldDate.getTime();
                    NewLong = newDate.getTime();
                    diff = NewLong - oldLong;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                counter = new MyCount(diff, 1000);
                counter.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setImagesAndInfos(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            String HomeTeam = null;
            String AwayTeam = null;
            String Info2 = null;
            String Info3 = null;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HomeTeam = dataSnapshot.child("Home").getValue().toString();
                AwayTeam = dataSnapshot.child("Away").getValue().toString();
                Info2 = dataSnapshot.child("Info2").getValue().toString();
                Info3 = dataSnapshot.child("Info3").getValue().toString();
                info2TV.setText("" + Info2);
                info3TV.setText("" + Info3);

                switch (HomeTeam) {
                    case "0":
                        ImageHome.setImageResource(R.drawable.wno);
                        break;
                    case "1":
                        ImageHome.setImageResource(R.drawable.wleipzignew);
                        break;
                    case "2":
                        ImageHome.setImageResource(R.drawable.wberlin);
                        break;
                    case "3":
                        ImageHome.setImageResource(R.drawable.wmainz);
                        break;
                    case "4":
                        ImageHome.setImageResource(R.drawable.wdortmund);
                        break;
                    case "5":
                        ImageHome.setImageResource(R.drawable.wnurnberg);
                        break;
                    case "6":
                        ImageHome.setImageResource(R.drawable.whoffenheim);
                        break;
                    case "7":
                        ImageHome.setImageResource(R.drawable.wleverkusen);
                        break;
                    case "8":
                        ImageHome.setImageResource(R.drawable.wfreiburg);
                        break;
                    case "9":
                        ImageHome.setImageResource(R.drawable.wfrankfurt);
                        break;
                    case "10":
                        ImageHome.setImageResource(R.drawable.wwolfsburg);
                        break;
                    case "11":
                        ImageHome.setImageResource(R.drawable.waugsburg);
                        break;
                    case "12":
                        ImageHome.setImageResource(R.drawable.wdusseldorf);
                        break;
                    case "13":
                        ImageHome.setImageResource(R.drawable.wstuttgart);
                        break;
                    case "14":
                        ImageHome.setImageResource(R.drawable.whannover);
                        break;
                    case "15":
                        ImageHome.setImageResource(R.drawable.wbremen);
                        break;
                    case "16":
                        ImageHome.setImageResource(R.drawable.wgelsenkirchen);
                        break;
                    case "17":
                        ImageHome.setImageResource(R.drawable.wmunchen);
                        break;
                    case "18":
                        ImageHome.setImageResource(R.drawable.wgladbach);
                        break;
                    case "19":
                        ImageHome.setImageResource(R.drawable.wsalzburg);
                        break;
                    case "20":
                        ImageHome.setImageResource(R.drawable.wunion);
                        break;
                    case "21":
                        ImageHome.setImageResource(R.drawable.wpaderborn);
                        break;
                    case "22":
                        ImageHome.setImageResource(R.drawable.wkoln);
                        break;
                    case "23":
                        ImageHome.setImageResource(R.drawable.whsv);
                        break;
                    case "24":
                        ImageHome.setImageResource(R.drawable.wpauli);
                        break;
                    case "25":
                        ImageHome.setImageResource(R.drawable.wbielefeld);
                        break;
                    case "26":
                        ImageHome.setImageResource(R.drawable.waue);
                        break;
                    case "27":
                        ImageHome.setImageResource(R.drawable.wdresden);
                        break;
                    case "28":
                        ImageHome.setImageResource(R.drawable.wparis);
                        break;
                    case "29":
                        ImageHome.setImageResource(R.drawable.wrealmadrid);
                        break;
                    case "30":
                        ImageHome.setImageResource(R.drawable.watleticomadrid);
                        break;
                    case "31":
                        ImageHome.setImageResource(R.drawable.wbarcelona);
                        break;
                    case "32":
                        ImageHome.setImageResource(R.drawable.wtottenham);
                        break;
                    case "33":
                        ImageHome.setImageResource(R.drawable.wmancity);
                        break;
                    case "34":
                        ImageHome.setImageResource(R.drawable.wliverpool);
                        break;
                    case "35":
                        ImageHome.setImageResource(R.drawable.wchelsea);
                        break;
                    case "36":
                        ImageHome.setImageResource(R.drawable.wturin);
                        break;
                    case "37":
                        ImageHome.setImageResource(R.drawable.wneapel);
                        break;
                    case "38":
                        ImageHome.setImageResource(R.drawable.wintermailand);
                        break;
                    case "39":
                        ImageHome.setImageResource(R.drawable.wajax);
                        break;
                    case "40":
                        ImageHome.setImageResource(R.drawable.wdonezk);
                        break;


                    default:
                        ImageHome.setImageResource(R.drawable.wno);
                        break;
                }

                switch (AwayTeam) {
                    case "0":
                        ImageAway.setImageResource(R.drawable.wno);
                        break;
                    case "1":
                        ImageAway.setImageResource(R.drawable.wleipzignew);
                        break;
                    case "2":
                        ImageAway.setImageResource(R.drawable.wberlin);
                        break;
                    case "3":
                        ImageAway.setImageResource(R.drawable.wmainz);
                        break;
                    case "4":
                        ImageAway.setImageResource(R.drawable.wdortmund);
                        break;
                    case "5":
                        ImageAway.setImageResource(R.drawable.wnurnberg);
                        break;
                    case "6":
                        ImageAway.setImageResource(R.drawable.whoffenheim);
                        break;
                    case "7":
                        ImageAway.setImageResource(R.drawable.wleverkusen);
                        break;
                    case "8":
                        ImageAway.setImageResource(R.drawable.wfreiburg);
                        break;
                    case "9":
                        ImageAway.setImageResource(R.drawable.wfrankfurt);
                        break;
                    case "10":
                        ImageAway.setImageResource(R.drawable.wwolfsburg);
                        break;
                    case "11":
                        ImageAway.setImageResource(R.drawable.waugsburg);
                        break;
                    case "12":
                        ImageAway.setImageResource(R.drawable.wdusseldorf);
                        break;
                    case "13":
                        ImageAway.setImageResource(R.drawable.wstuttgart);
                        break;
                    case "14":
                        ImageAway.setImageResource(R.drawable.whannover);
                        break;
                    case "15":
                        ImageAway.setImageResource(R.drawable.wbremen);
                        break;
                    case "16":
                        ImageAway.setImageResource(R.drawable.wgelsenkirchen);
                        break;
                    case "17":
                        ImageAway.setImageResource(R.drawable.wmunchen);
                        break;
                    case "18":
                        ImageAway.setImageResource(R.drawable.wgladbach);
                        break;
                    case "19":
                        ImageAway.setImageResource(R.drawable.wsalzburg);
                        break;
                    case "20":
                        ImageAway.setImageResource(R.drawable.wunion);
                        break;
                    case "21":
                        ImageAway.setImageResource(R.drawable.wpaderborn);
                        break;
                    case "22":
                        ImageAway.setImageResource(R.drawable.wkoln);
                        break;
                    case "23":
                        ImageAway.setImageResource(R.drawable.whsv);
                        break;
                    case "24":
                        ImageAway.setImageResource(R.drawable.wpauli);
                        break;
                    case "25":
                        ImageAway.setImageResource(R.drawable.wbielefeld);
                        break;
                    case "26":
                        ImageAway.setImageResource(R.drawable.waue);
                        break;
                    case "27":
                        ImageAway.setImageResource(R.drawable.wdresden);
                        break;
                    case "28":
                        ImageAway.setImageResource(R.drawable.wparis);
                        break;
                    case "29":
                        ImageAway.setImageResource(R.drawable.wrealmadrid);
                        break;
                    case "30":
                        ImageAway.setImageResource(R.drawable.watleticomadrid);
                        break;
                    case "31":
                        ImageAway.setImageResource(R.drawable.wbarcelona);
                        break;
                    case "32":
                        ImageAway.setImageResource(R.drawable.wtottenham);
                        break;
                    case "33":
                        ImageAway.setImageResource(R.drawable.wmancity);
                        break;
                    case "34":
                        ImageAway.setImageResource(R.drawable.wliverpool);
                        break;
                    case "35":
                        ImageAway.setImageResource(R.drawable.wchelsea);
                        break;
                    case "36":
                        ImageAway.setImageResource(R.drawable.wturin);
                        break;
                    case "37":
                        ImageAway.setImageResource(R.drawable.wneapel);
                        break;
                    case "38":
                        ImageAway.setImageResource(R.drawable.wintermailand);
                        break;
                    case "39":
                        ImageAway.setImageResource(R.drawable.wajax);
                        break;
                    case "40":
                        ImageAway.setImageResource(R.drawable.wdonezk);
                        break;


                    default:
                        ImageAway.setImageResource(R.drawable.wrbl);
                        break;
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setComments(){
        //comments
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Match").child("comments");
        mDatabase.keepSynced(true);
        comment = new Comment();
        list = new ArrayList<>();
        uid_list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), R.layout.comment, R.id.commentText, list);

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
            Intent startIntent = new Intent(getContext(), StartActivity.class);
            startActivity(startIntent);
            getActivity().finish();
        }
    }

    public void uploadComment() {

        //Progress
        mProgress = new ProgressDialog(getContext());
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
                                Toast.makeText(getContext(), "Kommentar konnten nicht gespeichert werden", Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e) {
                            System.out.println("Error " + e.getMessage());
                            return;
                        }
                    }
                });
            } else {
                mProgress.dismiss();
                Toast.makeText(getContext(), "Du musst erst ein Kommentar schreiben", Toast.LENGTH_LONG).show();
            }
        }else {
            //Wenn kein internet
            Snackbar.make(getView(), "Keine Verbindung zum Server!", Snackbar.LENGTH_LONG)
                    .show();
            hideKeyboard(getActivity());
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

    // countdowntimer is an abstract class, so extend it and fill in methods
    public class MyCount extends CountDownTimer {
        MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            infoTV.setText("L  I  V  E");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;


            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis));
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
            long sec = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));

            if (days==0 && hours!=0 && minutes!=0){

                infoTV.setText(hours+"h "+minutes+"m "+sec+"s");
            }else if(days==0 && hours==0 && minutes!=0){
                infoTV.setText(minutes+"m "+sec+"s");
            }else if(days==0 && hours==0 &&minutes==0){
                infoTV.setText(sec+"s");
            }else {
                infoTV.setText(days+"d "+hours+"h "+minutes+"m "+sec+"s");
            }

        }


    }

}

