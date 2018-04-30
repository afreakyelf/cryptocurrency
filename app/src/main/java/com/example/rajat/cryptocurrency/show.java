package com.example.rajat.cryptocurrency;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.model.Circle;




public class show extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth firebaseauth;
    public static FirebaseRecyclerAdapter<show_items, ShowDataViewHolder> mFirebaseAdapter;
    private ProgressDialog progressDialog;
    ImageView nointernet = null;
    TextView nobookmarks;
    ProgressBar progressBar;
    public static String bookmark_id;

    public show() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rvforbookmarks);
        Firebase.setAndroidContext(this);

        nointernet = (ImageView) findViewById(R.id.nointernet);
        nobookmarks = (TextView) findViewById(R.id.nobookmarks);
        nobookmarks.setVisibility(View.GONE);




        progressBar =  findViewById(R.id.progress);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);

     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
*/


        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseauth = FirebaseAuth.getInstance();
        final String userid =firebaseauth.getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference("News_User_Details").child(userid);
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager ll  = new LinearLayoutManager(show.this);
        ll.setReverseLayout(true);
        recyclerView.setLayoutManager(ll);





    }

    public FirebaseRecyclerAdapter<show_items, ShowDataViewHolder> getmFirebaseAdapter() {
        return mFirebaseAdapter;
    }

    public void setmFirebaseAdapter(FirebaseRecyclerAdapter<show_items, ShowDataViewHolder> mFirebaseAdapter) {
        this.mFirebaseAdapter = mFirebaseAdapter;
    }


    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView image_title  ;
        private final ImageView image_url;
        private TextView imge_des;
        private ImageView btn_delete;
        private TextView time;




        public ShowDataViewHolder(final View itemView)
        {
            super(itemView);
            image_url = (ImageView) itemView.findViewById(R.id.fetch_image);
            image_title = (TextView) itemView.findViewById(R.id.fetch_image_title);
            imge_des = (TextView) itemView.findViewById(R.id.fetch_description);
            btn_delete = (ImageView) itemView.findViewById(R.id.btn_delete);


        }

        private void Image_Title(String title)
        {
            image_title.setText(title);
        }



        private void Image_URL(String title)
        {
            Glide.with(itemView.getContext())
                    .load(title)
                    .crossFade()
                    .thumbnail(Glide.with(itemView.getContext()).load(R.drawable.loading_circle))
                    .into(image_url);

        }


        public void Image_Des(String title) {
            imge_des.setText(title);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        setmFirebaseAdapter(new FirebaseRecyclerAdapter<show_items, ShowDataViewHolder>(
                show_items.class, R.layout.showbookamrks, ShowDataViewHolder.class, myRef.orderByValue()) {
            public void populateViewHolder(final ShowDataViewHolder viewHolder, show_items model, final int position) {


                bookmark_id = getRef(position).getKey();
                send(bookmark_id);
                Log.e("BookMark",bookmark_id);
                viewHolder.Image_URL(model.getImage_URL());
                viewHolder.Image_Title(model.getImage_Title());
                viewHolder.Image_Des(model.getImage_des());



                //OnClick Item
                viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {

                        final Dialog dialog = new Dialog(show.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.alertdialogforshowbookmark);

                        final TextView alertmessage = (TextView)dialog.findViewById(R.id.alertmessage);
                        final TextView alert = (TextView)dialog.findViewById(R.id.message);
                        final Button cancel = (Button) dialog.findViewById(R.id.cancel_action);
                        final Button ok = (Button) dialog.findViewById(R.id.login_now);

                        alert.setText("Alert");
                        alertmessage.setText("Confirm Delete ?");
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });



                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int selectedItems = position;

                                if(getmFirebaseAdapter().getItemCount() >2) {
                                    getmFirebaseAdapter().getRef(selectedItems).removeValue();
                                    getmFirebaseAdapter().notifyItemRemoved(selectedItems);
                                    recyclerView.invalidate();
                                    notifyItemRangeChanged(position, getItemCount());
                                }
                                else {
                                    getmFirebaseAdapter().getRef(selectedItems).removeValue();
                                    getmFirebaseAdapter().notifyItemRemoved(selectedItems);
                                    recyclerView.invalidate();
                                    onStart();
                                }
                                dialog.dismiss();

                            }
                        });

                        dialog.show();


                    }

                });



                progressBar.setVisibility(View.GONE);
            }

        });





        recyclerView.setAdapter(getmFirebaseAdapter());


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    // Toast.makeText(show.this,"No Bookmarks added yet!",Toast.LENGTH_SHORT).show();
                    nobookmarks.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private static String send(String bookmark_id) {

        return bookmark_id;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
