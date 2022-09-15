package silverbeach.rbleipzigsupport.ui.posts;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Parcelable;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import silverbeach.rbleipzigsupport.R;


public class PostsFragment extends Fragment {

    private View mMainView2;
    private DatabaseReference mEventsDatabase;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mEventList;
    private FloatingActionButton addBtn;



    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView2 = inflater.inflate(R.layout.fragment_posts, container, false);

        addBtn = mMainView2.findViewById(R.id.addButton);

        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mEventsDatabase.keepSynced(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mEventList = (RecyclerView) mMainView2.findViewById(R.id.event_list);
        mEventList.setHasFixedSize(true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mEventList.setLayoutManager(mLayoutManager);
        mEventList.getRecycledViewPool().clear();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), WritePostActivity.class);
                getActivity().startActivity(myIntent);
            }
        });


        // Inflate the layout for this fragment
        return mMainView2;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Events, EventsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Events, EventsViewHolder>(

                Events.class,
                R.layout.event_single_layout,
                EventsViewHolder.class,
                mEventsDatabase.orderByChild("Timestamp")

        ) {
            @Override
            protected void populateViewHolder(final EventsViewHolder eventsViewHolder, final Events events, int i) {

                final String list_event_id = getRef(i).getKey();

                mEventsDatabase.child(list_event_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {

                            return;
                        }

                        String eventName = null;
                        String eventUser = null;
                        String eventId = null;
                        String eventText = null;
                        long eventComments = 0;
                        String eventCounter = null;
                        String uid = null;
                        try {

                            eventComments = dataSnapshot.child("comments").getChildrenCount();
                            eventName = dataSnapshot.child("Name").getValue().toString();
                            eventId = dataSnapshot.child("Id").getValue().toString();
                            eventText = dataSnapshot.child("Text").getValue().toString();
                            eventUser = dataSnapshot.child("User").getValue().toString();
                            eventCounter = dataSnapshot.child("Counter").getValue().toString();
                            try {
                                uid = dataSnapshot.child("uid").getValue().toString();
                            }catch (Exception x){
                                x.printStackTrace();
                                uid= "NO";
                            }

                        } catch (Exception e) {
                            // This will catch any exception, because they are all descended from Exception
                            System.out.println("Error " + e.getMessage());
                            //   Toast.makeText(getActivity(), "Event Anomalie!"+e,
                            //           Toast.LENGTH_LONG).show();
                            eventsViewHolder.setEventName("App bewerten");
                            eventsViewHolder.setEventText("Wenn sie dir gefällt, vergebe 5 Sterne im PlayStore");
                            eventsViewHolder.setEventComments("");
                            return;
                        }
                        eventsViewHolder.setEventName(eventName);
                        eventsViewHolder.setEventText(eventUser);
                        eventsViewHolder.setEventImage(eventCounter);
                        eventsViewHolder.setEventComments(eventComments + "");

                        final long finalEventComments = eventComments;
                        final String finalEventUser = eventUser;
                        final String finalEventName = eventName;
                        final String finalEventId = eventId;
                        final String finalEventText = eventText;
                        eventsViewHolder.mView2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                               Intent eventIntent = new Intent(getContext(), EventProfileActivity.class);
                               eventIntent.putExtra("Event_Name", finalEventName);
                               eventIntent.putExtra("Event_Id", finalEventId);
                               eventIntent.putExtra("Event_Text", finalEventText);
                               eventIntent.putExtra("Event_Comments", finalEventComments);
                               eventIntent.putExtra("Event_User", finalEventUser);
                               startActivity(eventIntent);
                                // getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

                            }
                        });
                        final String finalUid = uid;
                        eventsViewHolder.mView2.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {

                 //               Intent newIntentA = new Intent(getActivity(), ProfileActivity.class);
                 //               newIntentA.putExtra("user_id", finalUid);
                 //               startActivity(newIntentA);

                                return false;
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };


        firebaseRecyclerAdapter.notifyDataSetChanged();
        mEventList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        public View mView2;

        public EventsViewHolder(View itemView2) {
            super(itemView2);

            mView2 = itemView2;

        }

        public void setEventName(String name) {

            TextView eventName = (TextView) mView2.findViewById(R.id.comment_text);
            eventName.setText(name);
        }


        public void setEventText(String eventUser) {

            TextView eventTime = (TextView) mView2.findViewById(R.id.event_user);
            eventTime.setText(eventUser);

        }

        public void setEventComments(String eventComments) {

            TextView eventComment = (TextView) mView2.findViewById(R.id.event_comments);
            eventComment.setText("Antworten: "+eventComments);

        }


        public void setEventImage(String eventCounter) {
            int counter = Integer.parseInt(eventCounter);
            if (counter % 2 == 0) {
                // even
            //    ImageView imageView = (ImageView) mView2.findViewById(R.id.event_image);
               // imageView.setImageResource(R.drawable.logo_wappen_white);
            } else {
                // odd
           //     ImageView imageView = (ImageView) mView2.findViewById(R.id.event_image);
              //  imageView.setImageResource(R.drawable.oie_transparent);
            }


        }
    }


}
