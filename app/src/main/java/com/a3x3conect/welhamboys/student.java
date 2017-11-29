package com.a3x3conect.welhamboys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
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
import android.widget.Toast;

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

public class student extends AppCompatActivity {
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
        setContentView(R.layout.student);
        search = (EditText) findViewById( R.id.search);
        getSupportActionBar().setTitle("Student");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.drawable.atndncsmal);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //  getActionBar().setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(student.this);
        pd.setMessage("Getting Data from Server...");
        pd.show();
        new JsonAsync().execute("http://mycampus3655151.cloudapp.net/welham/webservices/student.php");

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
                        String s = json_data.getString("rollGroup").toLowerCase();
                        String d = json_data.getString("surname").toLowerCase();
                        String m = json_data.getString("preferredName").toLowerCase();
                        DataFish fishData = new DataFish();
                        if (s.contains(query) || d.contains(query) || m.contains(query)) {
                            fishData.preferredName = json_data.getString("preferredName");
                            fishData.rollGroup = json_data.getString("rollGroup");
                            fishData.surname = json_data.getString("surname");
                            filterdata.add(fishData);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    // fishData.Id=json_data.getString("Id");

                }
                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(student.this, filterdata);

                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(student.this));
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
            pd.dismiss();
            pd.cancel();
            Log.e("result",result);

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
                    fishData.preferredName = json_data.getString("preferredName");
                    Log.e("prefname",fishData.preferredName);
                    fishData.surname=json_data.getString("surname");
                    // fishData.Id=json_data.getString("Id");
                    fishData.rollGroup=json_data.getString("rollGroup");
                    fishData.email= json_data.getString("email");
                    fishData.gender= json_data.getString("gender");
                    fishData.dob= json_data.getString("dob");

                    data.add(fishData);

                }

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(student.this, data);
                Log.e("data",data.toString());
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(student.this));
                addTextListener();

            } catch (JSONException e) {
                Toast.makeText(student.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    public class DataFish {

        public String preferredName;
        public String email;
        public String rollGroup;
        public String surname;

        public String gender;
        public String dob;
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
            final DataFish current=data.get(position);

            myHolder.one.setText("Name: " +current.preferredName+"  "+current.surname);
            myHolder.two.setText("Roll Group: " +current.rollGroup);
            myHolder.three.setText("Email: " +current.email);
            myHolder.three.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            myHolder.three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",current.email, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
            myHolder.four.setText("DOB: " +current.dob);
            myHolder.five.setText("Gender: " +current.gender);
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
                six.setVisibility(View.GONE);
                seven = (TextView) itemView.findViewById(R.id.seven);
                seven.setVisibility(View.GONE);
                eight = (TextView) itemView.findViewById(R.id.eight);
                eight.setVisibility(View.GONE);
                nine = (TextView)itemView.findViewById(R.id.nine);
                nine.setVisibility(View.GONE);
                ten = (TextView)itemView.findViewById(R.id.ten);
                ten.setVisibility(View.GONE);
            }

        }




    }

}
