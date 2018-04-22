package com.example.rajat.cryptocurrency;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import de.hdodenhof.circleimageview.CircleImageView;



public class ItemtwoFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener , View.OnClickListener {


    public static ItemtwoFragment newInstance(){
        ItemtwoFragment fragment= new ItemtwoFragment();
        return fragment;
    }


    private RelativeLayout Prof_Section;

    private Button SignOut,uploada,showa;
    private SignInButton SignIn;
    private TextView Name, Email;
    private CircleImageView Prof_Pic;
    private static final String TAG = "GoogleActivity";
    private static final int REQ_CODE = 9001;
    private FirebaseAuth mAuth;
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;

    public static GoogleApiClient googleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    final ItemtwoFragment ItemtwoFragment = this;

    private Dialog dialog;
    private TextView alert , alertmessage;
    private Button button , loginbutton;
    GoogleSignInOptions signInOptions;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.goolesignin,container,false);



        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogforshowbookmark);
        alertmessage = (TextView)dialog.findViewById(R.id.alertmessage);
        alert = (TextView)dialog.findViewById(R.id.message);
        button = (Button) dialog.findViewById(R.id.cancel_action);
        loginbutton = (Button) dialog.findViewById(R.id.login_now);




        //  uploada=(Button) rootView.findViewById(R.id.upload);
        showa=(Button)rootView.findViewById(R.id.show);
        Prof_Section = (RelativeLayout) rootView.findViewById(R.id.prof_section);
        SignOut = (Button) rootView.findViewById(R.id.bn_logout);
        SignIn = (SignInButton) rootView.findViewById(R.    id.bn_login);
        Name = (TextView) rootView.findViewById(R.id.name);
        Email = (TextView) rootView.findViewById(R.id.email);
        Prof_Pic = (CircleImageView) rootView.findViewById(R.id.prof_pic);
        SignIn.setOnClickListener(this);
        SignOut.setOnClickListener(this);
        Prof_Section.setVisibility(View.GONE);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.activity_main);
        progressDialog = new ProgressDialog(getActivity());

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString((R.string.default_web_client_id)))
                .requestEmail().build();

        mAuth = FirebaseAuth.getInstance();


        FirebaseUser currentuser = mAuth.getCurrentUser();
        updateUI(currentuser);


        showa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),show.class));

            }
        });





        return rootView;
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d("sign in","Stop");
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseUser currentuser = mAuth.getCurrentUser();
        updateUI(currentuser);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("sign in","Start");
        FirebaseUser currentuser = mAuth.getCurrentUser();
        updateUI(currentuser);
        if(googleApiClient == null || !googleApiClient.isConnected()){
            try {
                googleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity(), this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                        .build();
                Log.d("Api client","Api client loaded in onStart");

                googleApiClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("sign in","Destroy");
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage((FragmentActivity) getContext());
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_login:
                signIn();
                break;
            case R.id.bn_logout:

                alert.setText("Alert");
                alertmessage.setText("Confirm Logout ?");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });

                loginbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signOut();
                        dialog.dismiss();

                    }
                });
                dialog.show();
                break;
        }
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();

    }

    private void signIn() {
        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            progressDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }

    }




    private void signOut() {
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();
                if(googleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            updateUI(null);
                            Toast.makeText(getActivity(),"Logged out!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "Google API Client Connection Suspended");
            }
        });
    }


    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            try {
                final GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String name = account.getDisplayName();
                                    String email = account.getEmail();
                                    String img_url = "";
                                    try {
                                        img_url = account.getPhotoUrl().toString();
                                    } catch (Exception e) {
                                        img_url = "";
                                    }
                                    Name.setText(name);
                                    Email.setText(email);
                                    Glide.with(ItemtwoFragment.getContext()).load(img_url).placeholder(R.drawable.bluehead).into(Prof_Pic);                                   updateUI(user);
                                    Log.d(TAG,"PIC LOADED ONCE");
                                }
                                progressDialog.dismiss();
                            }
                        });
            }catch (Exception e){
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(),"No Internet Connection! Please Check your Network.",Toast.LENGTH_SHORT).show();

            updateUI(null);

        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            String a = user.getDisplayName();
            Name.setText(String.valueOf(a));
            Email.setText(String.valueOf(user.getEmail()));

            Glide.with(getActivity()).load(user.getPhotoUrl()).placeholder(R.drawable.bluehead).into(Prof_Pic);

            Log.d(TAG,"its getting loaded here");
            Prof_Section.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        } else {
            Prof_Section.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
        }
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bookmark,menu);

        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(getActivity(),show.class));}
        else {

            alert.setText("Message");
            alertmessage.setText("Please Login First!");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            loginbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment  fragment = new ItemtwoFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment);

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    ((MainActivity)getActivity()).fn_testing();
                    dialog.dismiss();

                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }*/

}