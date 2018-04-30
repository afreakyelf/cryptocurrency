package com.example.rajat.cryptocurrency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;



public class market extends Fragment implements MyListener , SwipeRefreshLayout.OnRefreshListener{


    private RecyclerView recyclerView;
    private marketAdapter adapter;
    MyListener ml;
    market news;
    final int home= 0;
    String RESULT = "result";
    ArrayList<HashMap<String, Object>> al_details;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    final long period = 5000;
    String price,high,low,name;
    int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.layout,container,false);

        recyclerView =  rootview.findViewById(R.id.rv);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);



        progressBar = rootview.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout = rootview.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadJSON(0);
            }
        });


        loadJSON(0);

        listener();



        return rootview;
    }

    private void listener() {
        setOnEventListener(this);
    }

    public void setOnEventListener(MyListener ml) {
        this.ml = ml;
    }

    public void loadJSON(final int mode){
            progressBar.setVisibility(View.VISIBLE);


        final ApiInterfaceformarket apiInterface = Clientformarket.getClient().create(ApiInterfaceformarket.class);
        Call<ResponseBody> call = apiInterface.getJSON();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                progressBar.setVisibility(View.GONE);
                ArrayList<HashMap<String, String>> dataList = new ArrayList<>();



                if(response.isSuccessful()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    try {
                        HashMap<String, Object> dataMap = null;
                        String str_testing = response.body().string();
                        dataMap = new HashMap<String, Object>();
                        JSONObject jsonObject = new JSONObject(str_testing);
                        JSONArray data= jsonObject.getJSONArray("articles");
                        for(int i=0;i<data.length();i++){
                            JSONObject d = data.getJSONObject(i);
                            //  String description = d.getString("description");
                            String title = d.getString("title");
                            String Image = d.getString("urlToImage");
                            String url = d.getString("url");
                            String des = d.getString("description");
                            String time = d.getString("publishedAt");
                            //tmp hashmap for single contact
                            HashMap<String,String> samachar = new HashMap<>();


                            samachar.put("title",title);
                            samachar.put("urlToImage",Image);
                            samachar.put("url",url);
                            samachar.put("description",des);
                            samachar.put("publishedAt",time);

                            dataList.add(samachar);

                        }




                    Log.e("json"+"162", "jsonObject1: " + dataList);
                        dataMap.put(RESULT, dataList);

                        switch (mode)
                        {
                            case home:
                                ml.callback(dataMap, "live", mode);
                                Log.e("Utils123", "response : " + str_testing.toString());
                                break;
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getActivity(),"Something wrong", LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Call",t.toString());
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),"Check your Internet Connection",Toast.LENGTH_SHORT);
            }
        });


    }



    @Override
    public void callback(HashMap<String, Object> tmpMap, String dataType, int mode) {
        if(mode == home){
            al_details = (ArrayList<HashMap<String, Object>>) tmpMap.get(RESULT);
            if (al_details.size()!=0){
                adapter = new marketAdapter(al_details,news,getContext());
                recyclerView.setAdapter(adapter);

            }
        }
    }

    @Override
    public void onRefresh() {
        loadJSON(0);
        swipeRefreshLayout.setRefreshing(true);
        progressBar.setVisibility(View.GONE);
    }
}



