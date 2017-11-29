package com.a3x3conect.welhamboys;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class Sample extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterFish mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        new JsonAsync().execute("http://seller.storesnoffers.com/api/home/redeemed-offers/0");

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

          //  pdLoading.dismiss();
            List<DataFish> data=new ArrayList<>();

          //  pdLoading.dismiss();
            try {

                JSONObject reader = new JSONObject(result);
                Log.e("Data",reader.toString());

                JSONArray sys  =reader.getJSONArray("Data");
                Log.e("obj",sys.toString());

//JSONArray ssss = sys.getJSONArray()                //          JSONArray jArray = new JSONArray(result);
//                JSONObject Jobj1 = new JSONObject(result);
//                JSONObject joob2 = Jobj1.getJSONObject("Data");
//                Log.e("Data",joob2.toString());

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<sys.length();i++){
                 //   JSONArray jsonObject = sys.getJSONArray(i);
                    JSONObject jsonObject = sys.getJSONObject(i);

                   // JSONObject json_data = jArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
                   // fishData.BillAmount= jsonObject.getInt("BillAmount");
                   // Log.e("BillAmt",fishData.BillAmount);
                    fishData.RedeemedBy= jsonObject.optString("RedeemedBy").toString();

                    fishData.RedeemedTime= jsonObject.getString("RedeemedTime");
                    fishData.Title= jsonObject.getString("Title");
                    fishData.Id= jsonObject.getInt("Id");
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(Sample.this, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(Sample.this));

            } catch (JSONException e) {
                Toast.makeText(Sample.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
    public class DataFish {

        public String BillAmount;
        public String RedeemedBy;
        public String RedeemedTime;
        public String Title;
        public int Id;
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
            myHolder.textFishName.setText(current.Title);
            myHolder.textSize.setText("Size: " + current.RedeemedBy);
            myHolder.textType.setText("Category: " + current.RedeemedTime);
            myHolder.textPrice.setText("Rs. " + current.Id + "\\Kg");
            myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

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

            TextView textFishName;
            ImageView ivFish;
            TextView textSize;
            TextView textType;
            TextView textPrice;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                textFishName= (TextView) itemView.findViewById(R.id.one);
                //ivFish= (ImageView) itemView.findViewById(R.id.two);
                textSize = (TextView) itemView.findViewById(R.id.two);
                textType = (TextView) itemView.findViewById(R.id.three);
                textPrice = (TextView) itemView.findViewById(R.id.four);
            }

        }

    }
}