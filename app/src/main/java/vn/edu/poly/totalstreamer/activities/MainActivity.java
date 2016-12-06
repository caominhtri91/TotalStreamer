package vn.edu.poly.totalstreamer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.edu.poly.totalstreamer.R;

public class MainActivity extends AppCompatActivity {

    private static final String filename = "user_account";
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    ProgressDialog progressDialog;

    ImageView twitchImage, youtubeImage;

    CallbackManager mCallbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        twitchImage = (ImageView) findViewById(R.id.imageView);
        youtubeImage = (ImageView) findViewById(R.id.imageView2);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        // [START auth_state_listener]
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Tri", "onAuthStateChanged:signed_in:" + user.getUid());
                    updateUI();
                } else {
                    // User is signed out
                    Log.d("Tri", "onAuthStateChanged:signed_out");
                    restoringPreferences();
                }
            }
        };

        twitchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TwitchGamesActivity.class);
                startActivity(i);
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.loginButton:
                showLoginDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showLoginDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_login, null);
        dialogBuilder.setView(dialogView);

        final EditText email = (EditText) dialogView.findViewById(R.id.email);
        final EditText password = (EditText) dialogView.findViewById(R.id.password);

        loginButton = (LoginButton) dialogView.findViewById(R.id.login_button);


        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Tri", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Tri", "facebook:onError", error);
            }
        });


        dialogBuilder.setTitle("Login");
        dialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (validateForm(email, password)) {
                    signIn(email.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "You must enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Sign Up", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        restoringPreferences();
    }

    private void restoringPreferences() {
        SharedPreferences pref = getSharedPreferences(filename, MODE_PRIVATE);
        String em = pref.getString("username", "");
        String pw = pref.getString("password", "");
        if (!em.equals("") && !pw.equals("")) {
            signIn(em, pw);
        }
    }

    private void signIn(final String email, String password) {
        Log.d("Tri", "signIn:" + email);

        progressDialog.setMessage("Login in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tri", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Tri", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Failed! Something wrong...",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        progressDialog.dismiss();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI() {
        ActionMenuItemView loginButton = (ActionMenuItemView) findViewById(R.id.loginButton);
        loginButton.setVisibility(View.GONE);
    }

    private boolean validateForm(EditText email, EditText password) {
        boolean valid = true;

        String emText = email.getText().toString();
        if (TextUtils.isEmpty(emText)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String pwText = password.getText().toString();
        if (TextUtils.isEmpty(pwText)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Tri", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tri", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Tri", "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
