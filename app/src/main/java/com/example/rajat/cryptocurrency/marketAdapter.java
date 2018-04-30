package com.example.rajat.cryptocurrency;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class marketAdapter extends RecyclerView.Adapter<marketAdapter.ViewHolder>{

    ArrayList<HashMap<String, Object>> al_details;
    market fragment;
    Context mContext;

    public static boolean boolean_id;
    boolean setnews = false;

    ImageView iv_image;
    TextView tv_title;

    String URLTOIMAGE="urlToImage";
    String DESCRIPTION="description";
    String AUTHOR="author";
    String TITLE="title";
    String URL_WEB="url";
    String PUBLISHEDAT="publishedAt";

    Snackbar snackbar;

    DatabaseReference mDatabaseReference;
    StorageReference mStorageReference;
    ProgressDialog mProgressDialog;
    private Firebase mRoofRef;
    public Uri mImgUri= null;
    FirebaseAuth mAuth;

    public String  atv, des;
    public static String time;
    FirebaseAuth firebaseauth;
    String userid , timea;




    public marketAdapter(ArrayList<HashMap<String,Object>> al_details, market fragment, Context context) {

        this.al_details = al_details;
        this.fragment=fragment;
        this.mContext=context;
    }


    @Override
    public marketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view);

        return viewHolder1;
    }



    public void callback(HashMap<String, Object> tmpMap, String dataType, int mode) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image ;
        public ImageView share ,bookmark;
        TextView tv_title;
        RelativeLayout rl_click;
        Button button;



        public ViewHolder(View v) {

            super(v);

            iv_image = v.findViewById(R.id.imageview);
            tv_title = v.findViewById(R.id.tv1);
            rl_click = v.findViewById(R.id.relmarket);
            share = v.findViewById(R.id.share);
            bookmark = v.findViewById(R.id.bookmark);

            // readmore = (TextView) v.findViewById(R.id.readmore);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareintent = new Intent(Intent.ACTION_SEND);
                    shareintent.setType("text/plain");
                    shareintent.putExtra(Intent.EXTRA_TEXT,al_details.get(getLayoutPosition()).get("url").toString());
                    mContext.startActivity(Intent.createChooser(shareintent,"Share Via"));
                }
            });

        }


        public void fn_savedstate(final String timea) {
            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();

            if(user!=null) {
                userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("News_User_Details").child(userid).hasChild(timea+userid)){
                            bookmark.setImageResource(R.drawable.bookmarkedstar);
                        }else {
                            bookmark.setImageResource(R.drawable.bookmarkstar);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else {


            }



        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder vholder, final int position) {



        try {
            Glide.with(mContext).load(al_details.get(position).get(URLTOIMAGE))
                    .thumbnail( Glide.with(mContext).load(R.drawable.loading_icon))
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .listener(new RequestListener<Object, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.d("headline glide issue ","issue on position"+al_details.get(position).get("title"));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("headline glide success","Success on position"+al_details.get(position).get("title"));
                            vholder.iv_image.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(vholder.iv_image);

            Log.d("headline link" + position, (String) al_details.get(position).get(URLTOIMAGE));

        } catch (Exception e) {
            e.printStackTrace();

        }

        vholder.tv_title.setText(al_details.get(position).get("title").toString());



        //    boolean_id = false;
        timea= al_details.get(position).get("publishedAt").toString();
        timea = timea.replace(".", ":");
        Log.d("time",timea);
        vholder.fn_savedstate(timea);




        vholder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                setnews = true;

                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();



                if (user != null) {

                    atv = al_details.get(position).get("title").toString();
                    des = al_details.get(position).get("description").toString();
                    time = al_details.get(position).get("publishedAt").toString();
                    time = time.replace(".", ":");

                    mImgUri = Uri.parse(al_details.get(position).get("urlToImage").toString());




                    mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
                    final String userid = firebaseauth.getCurrentUser().getUid();


                    mRoofRef = new Firebase("https://rajat-608ab.firebaseio.com/").child("News_User_Details").child(userid).child(time + userid);
                //    mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://newsly-e074e.appspot.com");

                    mDatabaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (setnews) {
                                if (dataSnapshot.child("News_User_Details").child(userid).hasChild(time + userid)) {
                                    mDatabaseReference.child("News_User_Details").child(userid).child(time + userid).removeValue();
                                    vholder.bookmark.setImageResource(R.drawable.bookmarkstar);
                                    Snackbar.make(vholder.rl_click,"Bookmark removed",Snackbar.LENGTH_SHORT).setActionTextColor(Color.YELLOW).show();
                                    setnews = false;

                                }
                                else {
                                    vholder.bookmark.setImageResource(R.drawable.bookmarkedstar);
                                    final String id = mRoofRef.getKey();
                                    Log.e("Bookmark ID", id);
                                    final Firebase childRef_name = mRoofRef.child("Image_Title");
                                    childRef_name.setValue(atv);
                                    Firebase childdes = mRoofRef.child("Image_des");
                                    childdes.setValue(des);
                                    mRoofRef.child("Image_URL").setValue(mImgUri.toString());
                                    Snackbar.make(vholder.rl_click,"Bookmarked",Snackbar.LENGTH_SHORT).show();

                                }
                            }




                        }

                        //end of datachange

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                }



                else {
                    Toast.makeText(mContext, "Please login First", Toast.LENGTH_SHORT).show();

                }

            }


        });




    }

    private void abc(final String time) {

    }


    @Override
    public int getItemCount() {

        return al_details.size();
    }



}

