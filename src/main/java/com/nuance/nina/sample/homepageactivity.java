package com.nuance.nina.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class homepageactivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepageactivity);

        ListView lv;

        lv = (ListView) findViewById(R.id.listView);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> your_array_list = new ArrayList<>();
        your_array_list.add("Apple Pie");
        your_array_list.add("Carrot Cake");
        your_array_list.add("Baaaaaaacon");
        your_array_list.add("Because I'm a potato");
        your_array_list.add("Soylent");
        your_array_list.add("Yummy yum yums");
        your_array_list.add("Nom noms");
        your_array_list.add("Pizza");
        your_array_list.add("Chinese takeout swedish berries");
        your_array_list.add("No jimmy, that's not edible...");
        your_array_list.add("Dem gainz");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(getBaseContext(), "Recipe Selected", Toast.LENGTH_LONG).show();

            }
        });
    }
}
