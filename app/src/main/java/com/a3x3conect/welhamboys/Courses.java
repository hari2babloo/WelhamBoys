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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

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

public class Courses extends AppCompatActivity {
    EditText search;
    JSONArray jsonArray;
    ProgressDialog pd;
    List<DataFish> filterdata=new ArrayList<>();
    private RecyclerView mRVFishPrice;
    private AdapterFish mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Courses");
            getSupportActionBar().setIcon(R.drawable.courssmal);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        search = (EditText) findViewById( R.id.search);
        addTextListener();
        pd = new ProgressDialog(Courses.this);
        pd.setMessage("Getting Data from Server...");
        pd.show();
        new JsonAsync().execute("http://mycampus3655151.cloudapp.net/welham/webservices/course.php");

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

                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONArray ss = jsonArray.getJSONArray(i);

                        DataFish fishData = new DataFish();
                        String s = ss.get(0).toString().toLowerCase();

                        String d = ss.get(1).toString().toLowerCase();

                        if (s.contains(query) || d.contains(query)) {
                            fishData.classno = (String) ss.get(0);
                            fishData.subjectname = (String) ss.get(1);
                            filterdata.add(fishData);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    // fishData.Id=json_data.getString("Id");

                }
                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(Courses.this, filterdata);

                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(Courses.this));
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
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = " ";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();
                //

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
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
            pd.dismiss();
            pd.cancel();
            //this method will be running on UI thread

            Log.e("result", result);
            List<DataFish> data=new ArrayList<>();
            try {

               // DataFish dataFish = new DataFish();
            jsonArray= new JSONArray(result);

                for (int i=0;i<jsonArray.length();i++){
                    JSONArray ss = jsonArray.getJSONArray(i);
                    DataFish fishData = new DataFish();
                    fishData.classno= ss.get(0).toString();
                    fishData.subjectname =ss.get(1).toString();
                    data.add(fishData);
                    Log.e("ss",data.toString());
                }
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(Courses.this, data);

                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(Courses.this));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private class DataFish {

        public String classno;
        public String subjectname;
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

            myHolder.one.setText("Class : " +current.classno);
            myHolder.two.setText("Subject : " +current.subjectname);

            //  myHolder.textPrice.setText("Rs. " + current.Title + "\\Kg");


            // load image into imageview using glide
//            Glide.with(context).load("http://192.168.1.7/test/images/" + current.fishImage)
//                    .placeholder(R.drawable.ic_img_error)
//                    .error(R.drawable.ic_img_error)
//                    .into(myHolder.ivFish);

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

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                one= (TextView) itemView.findViewById(R.id.one);

                two = (TextView) itemView.findViewById(R.id.two);
                three = (TextView) itemView.findViewById(R.id.three);
                four = (TextView) itemView.findViewById(R.id.four);
                five= (TextView) itemView.findViewById(R.id.five);

                six = (TextView) itemView.findViewById(R.id.six);
                seven = (TextView) itemView.findViewById(R.id.seven);
                eight = (TextView) itemView.findViewById(R.id.eight);
                nine = (TextView)itemView.findViewById(R.id.nine);
                ten = (TextView)itemView.findViewById(R.id.ten);
               three.setVisibility(View.GONE);
                four.setVisibility(View.GONE);
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
