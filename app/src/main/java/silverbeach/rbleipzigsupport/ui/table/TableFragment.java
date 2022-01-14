package silverbeach.rbleipzigsupport.ui.table;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import silverbeach.rbleipzigsupport.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment {


        String allText;
        private ListView list;
        private View mMainView;
        private TextView debugTV;
        ArrayList listItems;
        ArrayAdapter<String> adapter;

        public TableFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mMainView = inflater.inflate(R.layout.fragment_table, container, false);
            list =  mMainView.findViewById(R.id.list_transfer);

            listItems = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(mMainView.getContext(), R.layout.table, R.id.commentText, listItems);
            list.setAdapter(adapter);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Document doc = Jsoup.connect("https://www.kicker.de/1-bundesliga/spieltag")
                                .timeout(20000).ignoreContentType(true).get();
                        allText = doc.text();
                        allText = allText.substring(allText.indexOf("Pl. Verein Sp. Diff. Pkt.")+26).trim();
                        allText = allText.substring(0, allText.indexOf("Tabelle")).trim();
                        allText = allText.replace("(M)", "");
                        allText = allText.replace("(P)", "");
                        allText = allText.replace("P)", "");
                        allText = allText.replace("(N)", "");
                        allText = allText.replace("(M,P)", "");
                        allText = allText.replace("(M,", "");
                        allText = allText.replace("   ", " ");
                        allText = allText.replace("  ", " ");

                        String [] parts = allText.split(" ");


                        int i = 1;
                        int a = 0;


                        try {

                            listItems.add("Platz  Sp. Verein     Diff.  Pkt.");
                            while (i <= 18) {
                                String position = parts[a].trim();
                                if (position.length() < 2) position = " " + position;
                                String name = parts[a + 1];
                                name = name.trim();
                                name = StringUtils.rightPad(name, 12, " ");
                                String matches = parts[a + 3].trim();
                                String diff = parts[a + 4].trim();
                                if (diff.length() < 3) diff = " " + diff;
                                if (diff.length() == 2) diff = " " + diff;
                                String points = parts[a + 5].trim();
                                a = a + 6;
                                String newItem = position + ".    " + matches + "  " + name + diff + "    " + points;
                                listItems.add(newItem);

                                System.out.println(newItem);
                                i++;
                            }
                        }catch (Exception e){
                            listItems.clear();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    adapter.notifyDataSetChanged();

                                }
                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // here you check the value of getActivity() and break up if needed
                    if(getActivity() == null) return;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            adapter.notifyDataSetChanged();

                        }
                    });
                }
            }).start();

            // Inflate the layout for this fragment
            return mMainView;

        }

        @Override
        public void onStart() {
            super.onStart();

        }
    }