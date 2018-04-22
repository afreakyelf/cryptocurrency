package com.example.rajat.cryptocurrency;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by chivu on 23/3/18.
 */

public class marketAdapter extends RecyclerView.Adapter<marketAdapter.ViewHolder> {

    private ArrayList<HashMap<String, Object>> al_details;
    market fragment;
    Context mContext;
    FirebaseAuth mAuth;
    String sprice,shigh,slow,names;
    int hour,min,sec,ms,dd,m,y;
    private ArrayList al_details2;
    static String a,b,ca,d,e,f,g;

    marketAdapter(ArrayList<HashMap<String, Object>> al_details, market news, Context context) {
        this.al_details = al_details;
        this.fragment = news;
        this.mContext  = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.market, viewGroup, false);
        return new ViewHolder(view);
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,price,high,low;
        private ImageView add;


        ViewHolder(View view) {
            super(view);

            name =view.findViewById(R.id.name);
            price =view.findViewById(R.id.price);
            high = view.findViewById(R.id.high);
            low = view.findViewById(R.id.low);
            add  = view.findViewById(R.id.add);
        }



    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.name.setText(al_details.get(i).get("FROMSYMBOL").toString());
        viewHolder.price.setText(al_details.get(i).get("PRICE").toString());
        viewHolder.high.setText(al_details.get(i).get("HIGH").toString());
        viewHolder.low.setText(al_details.get(i).get("LOW").toString());

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        sec = c.get(Calendar.SECOND);
        ms = c.get(Calendar.MILLISECOND);
        dd = c.get(Calendar.DATE);
        m = c.get(Calendar.MONTH);
        y = c.get(Calendar.YEAR);

        a = String.valueOf(hour);
        b = String.valueOf(min);
        ca = String.valueOf(sec);
        d = String.valueOf(ms);
        e= String.valueOf(dd);
        f = String.valueOf(m);
        g = String.valueOf(y);



        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Saving...", Toast.LENGTH_SHORT).show();
            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();

            if (user!=null){
                sprice = al_details.get(i).get("PRICE").toString();
                slow = al_details.get(i).get("LOW").toString();
                shigh = al_details.get(i).get("HIGH").toString();
                names = al_details.get(i).get("NAME").toString();


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                final String userid = mAuth.getCurrentUser().getUid();

                final Firebase mref = new Firebase("https://rajat-b182e.firebaseio.com/").child("Crypto_User_Details").child(userid).child(a+b+ca+d);
                databaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                         Firebase child_name = mref.child("PRICE");
                        child_name.setValue(sprice);
                        child_name = mref.child("HIGHDAY");
                        child_name.setValue(shigh);
                        child_name = mref.child("LOWDAY");
                        child_name.setValue(slow);
                        child_name = mref.child("TIME");
                        child_name.setValue(a+':'+b+':'+ca+ " Hr.");
                        child_name = mref.child("NAME");
                        child_name.setValue(names);
                        child_name = mref.child("DATE");
                        child_name.setValue(e+'/'+f+'/'+g);

                        Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            }
        });

    }

    @Override
    public int getItemCount() {
        return al_details.size();
    }


}
