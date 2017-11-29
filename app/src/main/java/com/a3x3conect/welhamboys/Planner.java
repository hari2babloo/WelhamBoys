package com.a3x3conect.welhamboys;


    import android.content.Context;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.text.Editable;
    import android.text.Html;
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

    public class Planner extends AppCompatActivity {
        EditText search;
        String testval;
        JSONArray jsonArray;
        JSONObject json_data;
        List<DataFish> filterdata=new ArrayList<>();
        private RecyclerView mRVFishPrice;
        private AdapterFish mAdapter;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.planner);
            getSupportActionBar().setTitle("Planner");
            search = (EditText) findViewById( R.id.search);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setIcon(R.drawable.plansmal);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            new JsonAsync().execute("http://mycampus3655151.cloudapp.net/welham/webservices/planner.php");
           // addTextListener();
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
                Log.e("result",result);
                List<DataFish> data=new ArrayList<>();

                try {
                    jsonArray = new JSONArray(result);
                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jsonArray.length();i++){
                        //   JSONArray jsonObject = sys.getJSONArray(i);
                        json_data = jsonArray.getJSONObject(i);
                        Log.e("json",json_data.toString());
                        DataFish fishData = new DataFish();
                        fishData.course = json_data.getString("course");
                        Log.e("prefname",fishData.course);
                        fishData.class1=json_data.getString("class");
                        // fishData.Id=json_data.getString("Id");
                        fishData.date=json_data.getString("date");
                        fishData.timestart= json_data.getString("timestart");
                        fishData.timeend = json_data.getString("timeend");
                       // Log.e("prefname",fishData.course);
                        fishData.name1=json_data.getString("name");
                        // fishData.Id=json_data.getString("Id");
                        fishData.Summary=json_data.getString("summary");
                        fishData.teachersNotes= json_data.getString("teachersNotes");
                        fishData.DueDate = json_data.getString("DueDate");
                        fishData.homework= json_data.getString("homework");
                        fishData.description = json_data.getString("description");
                        data.add(fishData);
                    }

                    // Setup and Handover data to recyclerview
                    mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                    mAdapter = new AdapterFish(Planner.this, data);
                    Log.e("data",data.toString());
                    mRVFishPrice.setAdapter(mAdapter);
                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(Planner.this));
                   addTextListener();

                } catch (JSONException e) {
                    Toast.makeText(Planner.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

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
                    try {

                        // Extract data from json and store into ArrayList as class objects
                        for(int i=0;i<jsonArray.length();i++){
                            //   JSONArray jsonObject = sys.getJSONArray(i);
                            json_data = jsonArray.getJSONObject(i);
                            Log.e("json",json_data.toString());

                            String s = json_data.getString("course").toLowerCase();
                            String d = json_data.getString("class").toLowerCase();

                            if (s.contains(query) || d.contains(query)) {
                                DataFish fishData = new DataFish();

                                fishData.course = json_data.getString("course");
                                Log.e("prefname",fishData.course);
                                fishData.class1=json_data.getString("class");
                                // fishData.Id=json_data.getString("Id");
                                fishData.date=json_data.getString("date");
                                fishData.timestart= json_data.getString("timestart");
                                fishData.timeend = json_data.getString("timeend");
                                // Log.e("prefname",fishData.course);
                                fishData.name1=json_data.getString("name");
                                // fishData.Id=json_data.getString("Id");
                                fishData.Summary=json_data.getString("summary");
                                fishData.teachersNotes= json_data.getString("teachersNotes");
                                fishData.DueDate = json_data.getString("DueDate");
                                fishData.homework= json_data.getString("homework");
                                fishData.description = json_data.getString("description");
                                filterdata.add(fishData);


                            }
                        }

                        // Setup and Handover data to recyclerview
                        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                        mAdapter = new AdapterFish(Planner.this, filterdata);
                        Log.e("filterdata",filterdata.toString());
                        mRVFishPrice.setAdapter(mAdapter);
                        mRVFishPrice.setLayoutManager(new LinearLayoutManager(Planner.this));
                        mAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Toast.makeText(Planner.this, e.toString(), Toast.LENGTH_LONG).show();
                    }


                    // fishData.Id=json_data.getString("Id")
                }


                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }

        public class DataFish {

            public String class1;
            public String course;
            public String date;
            public String description;
            public String DueDate;
            public String homework;
            public String name1;
            public String Summary;
            public String teachersNotes;
            public String timeend;
            public String timestart;
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

                myHolder.one.setText("Course : " +current.course);
                myHolder.two.setText("Class : " +current.class1);
               // myHolder.two.setVisibility(View.GONE);
                myHolder.three.setText("Date: " +current.date);
                myHolder.four.setText("Time Start : " +current.timestart +"    Time End : "+current.timeend);
                myHolder.five.setText("Name  :   "+current.name1);
                myHolder.six.setText("Summary :  "+current.Summary);
                myHolder.seven.setText( "Description: "+ Html.fromHtml(current.description));

                myHolder.eight.setText("Teacher Notes: "+ Html.fromHtml(current.teachersNotes));
                myHolder.nine.setText("Home Work :" + current.homework);
                myHolder.ten.setText("Due Date : "+current.DueDate);

                myHolder.one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Planner.this, "Hello World", Toast.LENGTH_SHORT).show();
                    }
                });

                //  myHolder.textPrice.setText("Rs. " + current.Title + "\\Kg");
//                myHolder.four.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

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
                }

            }

        }

    }
