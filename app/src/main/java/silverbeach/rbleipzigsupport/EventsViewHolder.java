package silverbeach.rbleipzigsupport;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class EventsViewHolder extends RecyclerView.ViewHolder {

    View mView2;

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
        eventComment.setText("Antworten: " + eventComments);

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
