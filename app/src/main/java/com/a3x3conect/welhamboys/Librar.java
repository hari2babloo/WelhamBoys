package com.a3x3conect.welhamboys;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import a3x3conect.com.mycampus365.R;

public class Librar extends AppCompatActivity {
    EditText search;
    String testval;
    JSONArray jArray;
    JSONObject json_data;
    ProgressDialog pd;
    List<DataFish> filterdata=new ArrayList<>();
    private RecyclerView mRVFishPrice;
    private AdapterFish mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.librar);
        search = (EditText) findViewById( R.id.search);
        getSupportActionBar().setTitle("Library");
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.drawable.book);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        pd = new ProgressDialog(Librar.this);
        pd.setMessage("Getting Data from Server...");
        pd.show();
        new JsonAsync().execute("http://mycampus3655151.cloudapp.net/welham/webservices/library.php");

    }

    private void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                query = query.toString().toLowerCase();
                mAdapter.notifyDataSetChanged();

                filterdata.clear();

                for (int i = 0; i < jArray.length(); i++) {

                    try {
                        json_data = jArray.getJSONObject(i);
                        String s = json_data.getString("name").toLowerCase();
                        String d = json_data.getString("producer").toLowerCase();
                        DataFish fishData = new DataFish();
                        if (s.contains(query) || d.contains(query)) {
                            fishData.name = json_data.getString("name");
                            fishData.producer = json_data.getString("producer");
                            fishData.status = json_data.getString("status");
                            fishData.pic = json_data.getString("pic");
                            filterdata.add(fishData);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    // fishData.Id=json_data.getString("Id");

                }
                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(Librar.this, filterdata);

                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(Librar.this));
                mAdapter.notifyDataSetChanged();

            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class JsonAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line= " ";
                while ((line=reader.readLine())!=null){
                    buffer.append(line);
                }

                return  buffer.toString();
                //

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection!=null){
                    connection.disconnect();
                }

                try {
                    if (reader!=null){
                        reader.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            Log.e("result",result);
            pd.dismiss();
            pd.cancel();
            //  pdLoading.dismiss();
            List<DataFish> data=new ArrayList<>();

            //  pdLoading.dismiss();
            try {

                jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    //   JSONArray jsonObject = sys.getJSONArray(i);
                    json_data = jArray.getJSONObject(i);
                    Log.e("json",json_data.toString());
                    DataFish fishData = new DataFish();
                    fishData.name = json_data.getString("name");
                  //  Log.e("prefname",fishData.preferredName);
                    fishData.pic=json_data.getString("pic");
                    // fishData.Id=json_data.getString("Id");
                    fishData.producer=json_data.getString("producer");
                    fishData.status= json_data.getString("status");
                    data.add(fishData);

                }

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(Librar.this, data);
               // Log.e("data",data.toString());
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(Librar.this));
                addTextListener();

            } catch (JSONException e) {
                Toast.makeText(Librar.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    public class DataFish {

        public String name;
        public String pic;
      //  public String rollGroup;
        public String producer;
        public String status;
    }

    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<DataFish> data= Collections.emptyList();
        DataFish current;
        int currentPos=0;
        private Context context;
        private LayoutInflater inflater;

        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<DataFish> data){
            this.context=context;
            inflater= LayoutInflater.from(context);
            this.data=data;
        }

        // Inflate the layout when viewholder created


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.container_fish, parent,false);
            MyHolder holder=new MyHolder(view);
            return holder;
        }

        // Bind data
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            MyHolder myHolder= (MyHolder) holder;
            DataFish current=data.get(position);

            myHolder.one.setText("Name: " +current.name );
            myHolder.two.setText("Producer: " +current.producer);
           // myHolder.two.setVisibility(View.GONE);
            myHolder.three.setText("Status: " +current.status);
           //myHolder.four.setText("Image Link: " +current.pic);
            //  myHolder.textPrice.setText("Rs. " + current.Title + "\\Kg");

            // load image into imageview using glide
            myHolder.img.setVisibility(View.VISIBLE);
            //Glide.with(Librar.this).load(current.pic).into(myHolder.img);
Glide.with(Librar.this).load(current.pic).into(myHolder.img);


        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder{

            TextView one;
            TextView two;
            TextView three;
            TextView four;
            TextView five;
            TextView six;
            TextView seven;
            TextView eight;
            TextView nine;
            TextView ten;
            ImageView img;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                one= (TextView) itemView.findViewById(R.id.one);
                img = (ImageView)findViewById(R.id.img);
                two = (TextView) itemView.findViewById(R.id.two);
                three = (TextView) itemView.findViewById(R.id.three);
                four = (TextView) itemView.findViewById(R.id.four);
                four.setVisibility(View.GONE);
                five= (TextView) itemView.findViewById(R.id.five);
                img =(ImageView)itemView.findViewById(R.id.img);
                six = (TextView) itemView.findViewById(R.id.six);
                seven = (TextView) itemView.findViewById(R.id.seven);
                eight = (TextView) itemView.findViewById(R.id.eight);
                nine = (TextView)itemView.findViewById(R.id.nine);
                ten = (TextView)itemView.findViewById(R.id.ten);
                five.setVisibility(View.GONE);
                six.setVisibility(View.GONE);
                seven.setVisibility(View.GONE);
                eight.setVisibility(View.GONE);
                nine.setVisibility(View.GONE);
                ten.setVisibility(View.GONE);
            }

        }

    }
}
