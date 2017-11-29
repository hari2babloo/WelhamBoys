package com.a3x3conect.welhamboys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import a3x3conect.com.mycampus365.R;

public class multiselecttest extends AppCompatActivity {
    ListView myList;

    Button getChoice;

    String[] listContent = {

            "January",

            "February",

            "March",

            "April",

            "May",

            "June",

            "July",

            "August",

            "September",

            "October",

            "November",

            "December"

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiselecttest);

        myList = (ListView)findViewById(R.id.listView);

        getChoice = (Button)findViewById(R.id.getlist);
        ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(this,

                android.R.layout.simple_list_item_multiple_choice,

                listContent);
        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        myList.setAdapter(adapter);

        getChoice.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View v) {

                // TODO Auto-generated method stub



                String selected = "";



                int cntChoice = myList.getCount();

                SparseBooleanArray sparseBooleanArray = myList.getCheckedItemPositions();

                for(int i = 0; i < cntChoice; i++){

                    if(sparseBooleanArray.get(i)) {

                        selected += myList.getItemAtPosition(i).toString() + "\n";



                    }

                }



                Toast.makeText(multiselecttest.this,

                        selected,

                        Toast.LENGTH_LONG).show();

            }});




    }
}
