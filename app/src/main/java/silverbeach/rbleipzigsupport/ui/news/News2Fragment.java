package silverbeach.rbleipzigsupport.ui.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import silverbeach.rbleipzigsupport.R;

public class News2Fragment extends Fragment {

    private String allText;
    private TextView tv1;
    private TextView ptv1;
    private View mMainView4;
    private int i;
    private List<String> titleList = new ArrayList<String>();
    private List<String> publisherList = new ArrayList<String>();
    private List<String> linkList = new ArrayList<String>();
    private ImageButton imageButton1;




    public News2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView4 = inflater.inflate(R.layout.fragment_news1, container, false);

        tv1 = (TextView) mMainView4.findViewById(R.id.newsTextview1);
        ptv1 = (TextView) mMainView4.findViewById(R.id.publisherTextView1);
        imageButton1 = (ImageButton) mMainView4.findViewById(R.id.imageButton1);


        i = 0;
        downloadNews();


        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse(linkList.get(1)); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getContext(),"Artikel kann nicht angezeigt werden.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mMainView4;
    }

    @Override
    public void onDestroyView() {
        i=0;
        super.onDestroyView();
    }

    private void downloadNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.rb-fans.de")
                            .timeout(30000).ignoreContentType(true).get();

                    while (i != 5){
                        Element element = doc.getElementsByClass("meldung").get(i).child(1);
                        String bsp = element.text();
                        titleList.add(bsp);

                        Element element2 = doc.getElementsByClass("meldung").get(i).child(0);
                        String bsp2 = element2.attr("title").toString();
                        publisherList.add(bsp2);

                        Element element3 = doc.getElementsByClass("meldung").get(i).child(1);
                        String bsp3 = element3.attr("href").toString();
                        linkList.add(bsp3);

                        i=++i;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //set to textviews
                            tv1.setText(titleList.get(1)+"...");
                            ptv1.setText(publisherList.get(1));

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //set to textviews
                            tv1.setText("Pressenews können aktuell nicht angezeigt werden.");
                        }
                    });
                }
            }
        }).start();
    }

}