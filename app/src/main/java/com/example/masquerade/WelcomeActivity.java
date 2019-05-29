package com.example.masquerade;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    ConstraintLayout logo_layout;
    ConstraintLayout welcome;
    ConstraintLayout signin;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth  mAuth;
    private DatabaseReference mDatabase;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("Created", "ON CREATE");
        super.onCreate(savedInstanceState);
        getApplication().setTheme(R.style.AppTheme);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_welcome);

        logo_layout = findViewById(R.id.logo_layout);
        welcome = findViewById(R.id.welcome);
        signin = findViewById((R.id.signin));
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        // sign in start
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1030427491069-3hvkhfksob78t4pjpua9pqhjekk1clef.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;

        final ImageButton start = findViewById(R.id.start);
        start.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse));

        final Animation out_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        final Animation in_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        out_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                signin.startAnimation(in_anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        in_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                signin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(welcome, "translationY", height);
                animation.setDuration(1000);
                animation.start();
                logo_layout.startAnimation(out_anim);
                start.clearAnimation();
                logo_layout.removeView(start);
            }
        });

    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Recurring signIn, direct to home page
        if (currentUser != null) {
            Log.d("ALRDY SIGNED IN", "user is signed in");
            startActivity(new Intent(WelcomeActivity.this, SettingActivity.class));
            finish();
        }
    }
    // [End on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(WelcomeActivity.this, "Login Failed", Toast.LENGTH_SHORT);
            }
        }

    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUserMetadata metadata = user.getMetadata();

                            // Check if new user
                            boolean is_new = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (is_new) {
                                // The user is new, put in database.
                                writeUser(user.getUid(), user.getEmail());
                                Log.d(TAG,"DB");
                                // show setting & profile
                                startActivity(new Intent(WelcomeActivity.this, SettingActivity.class));
                                finish();

                            } else {

                                // This is an existing user, show home page.
                                startActivity(new Intent(WelcomeActivity.this, SettingActivity.class));
                                finish();
                            }

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    // [START signin]
    private void signIn() {
        Log.d("SignIn", "calling sign in");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    // [END signin]

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Log.d("Listener","Clicked");
        if (i == R.id.sign_in_button) {
            Log.d("Listener","Sign in Button Clicked");
            signIn();
        }
    }
        // todo : sign out, disconnect button
        /*
        else if (i == R.id.signOutButton) {

            signOut();
        } else if (i == R.id.disconnectButton) {
            revokeAccess();
        }
        */
    private void writeUser(String user_Id, String email){
        User user = User.getInstance();
        user.setEmail(email);
        user.setUser_id(user_Id);
        mDatabase.child("Users").child(user_Id).setValue(user);
    }
}
