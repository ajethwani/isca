package srm.isca.com.isca.Maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import srm.isca.com.isca.Maps.Card;
import srm.isca.com.isca.Maps.CardArrayAdapter;
import srm.isca.com.isca.R;

public class Events_list extends Activity {

    static class CardViewHolder {
        TextView line1;
        TextView line2;
    }

    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);

        for (int i = 0; i < 3; i++) {
            Card card = new Card("Event " + (i + 1),"Tech Park");
            cardArrayAdapter.add(card);
        }
        for (int i = 3; i < 6; i++) {
            Card card = new Card("Event " + (i + 1),"University Building");
            cardArrayAdapter.add(card);
        }
        for (int i = 6; i < 9; i++) {
            Card card = new Card("Event " + (i + 1),"Bio Tech");
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String building = cardArrayAdapter.getItem(position - 1).getLine2();
//                Toast.makeText(getBaseContext(),cardArrayAdapter.getItem(position-1).getLine2(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(), Maps.class);

                if (building.equals("Tech Park")) {

                    intent.putExtra("Lat", 12.824756);
                    intent.putExtra("Lng",80.045105);

                } else if (building.equals("University Building")) {

                    intent.putExtra("Lat", 12.823236);
                    intent.putExtra("Lng",80.042239);

                } else if (building.equals("Bio Tech")) {

                    intent.putExtra("Lat", 12.824658);
                    intent.putExtra("Lng",80.044046);

                }

                startActivity(intent);

            }
        });
    }
}