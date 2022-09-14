package silverbeach.rbleipzigsupport.ui.transfers;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import silverbeach.rbleipzigsupport.R;

public class TransfersFragment extends Fragment {

    private View mMainView4;
    private String allText;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
    private TextView nameTv1, nameTv2, nameTv3, nameTv4, nameTv5, nameTv6, nameTv7, nameTv8, nameTv9, nameTv10;

    private int i;
    private List<String> contentList = new ArrayList<String>();


    public TransfersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView4 = inflater.inflate(R.layout.fragment_transfers, container, false);

        i = 0;

        tv1 = mMainView4.findViewById(R.id.transferTV1);
        tv2 = mMainView4.findViewById(R.id.transferTV2);
        tv3 = mMainView4.findViewById(R.id.transferTV3);
        tv4 = mMainView4.findViewById(R.id.transferTV4);
        tv5 = mMainView4.findViewById(R.id.transferTV5);
        tv6 = mMainView4.findViewById(R.id.transferTV6);
        tv7 = mMainView4.findViewById(R.id.transferTV7);
        tv8 = mMainView4.findViewById(R.id.transferTV8);
        tv9 = mMainView4.findViewById(R.id.transferTV9);
        tv10 = mMainView4.findViewById(R.id.transferTV10);

        nameTv1 = mMainView4.findViewById(R.id.name1);
        nameTv2 = mMainView4.findViewById(R.id.name2);
        nameTv3 = mMainView4.findViewById(R.id.name3);
        nameTv4 = mMainView4.findViewById(R.id.name4);
        nameTv5 = mMainView4.findViewById(R.id.name5);
        nameTv6 = mMainView4.findViewById(R.id.name6);
        nameTv7 = mMainView4.findViewById(R.id.name7);
        nameTv8 = mMainView4.findViewById(R.id.name8);
        nameTv9 = mMainView4.findViewById(R.id.name9);
        nameTv10 = mMainView4.findViewById(R.id.name10);


        downloadTransferNews();

        // Inflate the layout for this fragment
        return mMainView4;
    }

    private void downloadTransferNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.rb-fans.de")
                            .timeout(30000).ignoreContentType(true).get();

                        Element element1 = doc.getElementById("personal1").child(2);
                        Element element2 = doc.getElementById("personal2").child(2);
                        Element element3 = doc.getElementById("personal3").child(2);
                        Element element4 = doc.getElementById("personal4").child(2);
                        Element element5 = doc.getElementById("personal5").child(2);
                        Element element6 = doc.getElementById("personal6").child(2);
                        Element element7 = doc.getElementById("personal7").child(2);
                        Element element8 = doc.getElementById("personal8").child(2);
                        Element element9 = doc.getElementById("personal9").child(2);
                        Element element10 = doc.getElementById("personal10").child(2);

                        Element date1 = doc.getElementById("personal1").child(1);
                        Element date2 = doc.getElementById("personal2").child(1);
                        Element date3 = doc.getElementById("personal3").child(1);
                        Element date4 = doc.getElementById("personal4").child(1);
                        Element date5 = doc.getElementById("personal5").child(1);
                        Element date6 = doc.getElementById("personal6").child(1);
                        Element date7 = doc.getElementById("personal7").child(1);
                        Element date8 = doc.getElementById("personal8").child(1);
                        Element date9 = doc.getElementById("personal9").child(1);
                        Element date10 = doc.getElementById("personal10").child(1);



                        Element name1 = doc.getElementsByClass("artikel_bold_red").get(0);
                        Element name2 = doc.getElementsByClass("artikel_bold_red").get(1);
                        Element name3 = doc.getElementsByClass("artikel_bold_red").get(2);
                        Element name4 = doc.getElementsByClass("artikel_bold_red").get(3);
                        Element name5 = doc.getElementsByClass("artikel_bold_red").get(4);
                        Element name6 = doc.getElementsByClass("artikel_bold_red").get(5);
                        Element name7 = doc.getElementsByClass("artikel_bold_red").get(6);
                        Element name8 = doc.getElementsByClass("artikel_bold_red").get(7);
                        Element name9 = doc.getElementsByClass("artikel_bold_red").get(8);
                        Element name10 = doc.getElementsByClass("artikel_bold_red").get(9);

                    Log.d("transfer content list",name1.text());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tv1.setText(element1.text());
                            tv2.setText(element2.text());
                            tv3.setText(element3.text());
                            tv4.setText(element4.text());
                            tv5.setText(element5.text());
                            tv6.setText(element6.text());
                            tv7.setText(element7.text());
                            tv8.setText(element8.text());
                            tv9.setText(element9.text());
                            tv10.setText(element10.text());

                            nameTv1.setText(name1.text()+",   "+date1.text());
                            nameTv2.setText(name2.text()+",   "+date2.text());
                            nameTv3.setText(name3.text()+",   "+date3.text());
                            nameTv4.setText(name4.text()+",   "+date4.text());
                            nameTv5.setText(name5.text()+",   "+date5.text());
                            nameTv6.setText(name6.text()+",   "+date6.text());
                            nameTv7.setText(name7.text()+",   "+date7.text());
                            nameTv8.setText(name8.text()+",   "+date8.text());;
                            nameTv9.setText(name9.text()+",   "+date9.text());
                            nameTv10.setText(name10.text()+",   "+date10.text());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();

                    // downloadTransferNews();
                }
            }
        }).start();
    }
}
