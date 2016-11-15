package vn.edu.poly.totalstreamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import vn.edu.poly.totalstreamer.Entities.User;

public class SignUpActivity extends AppCompatActivity {

    private static final String filename = "user_account";
    Button signUpButton;
    EditText email, password, fullname;
    Spinner age;
    ArrayAdapter ageAdapter;
    RadioButton male, female;
    ImageView avatar;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        avatar = (ImageView) findViewById(R.id.imageView);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        fullname = (EditText) findViewById(R.id.fullname);
        age = (Spinner) findViewById(R.id.spinner);
        male = (RadioButton) findViewById(R.id.radioButton);
        female = (RadioButton) findViewById(R.id.radioButton2);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        ageAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, generateAgeSpinner());
        age.setAdapter(ageAdapter);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_GET_CONTENT);
                a.setType("image/*");
                Intent b = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent chooser = Intent.createChooser(a, "Choose please: ");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{b});
                startActivityForResult(chooser, 999);
            }
        });
    }

    private void registerUser() {
        final String em = email.getText().toString();
        final String pw = password.getText().toString();

        if (!validateForm()) {
            return;
        }

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(em, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tri", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Failed! Something wrong...", Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        progressDialog.dismiss();
                        savingPreferences(em, pw);
                        signIn(em, pw);

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            databaseReference.child(uid).setValue(generateUserInformation());
                        }

                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(i);
                        // [END_EXCLUDE]
                    }
                });
    }

    private boolean validateForm() {
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

    private void savingPreferences(String em, String pw) {
        SharedPreferences sharepreference = getSharedPreferences(filename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepreference.edit();
        editor.putString("email", em);
        editor.putString("password", pw);
        editor.apply();
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
                            Toast.makeText(SignUpActivity.this, "Failed! Something wrong...",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        progressDialog.dismiss();
                        // [END_EXCLUDE]
                    }
                });
    }

    private User generateUserInformation() {
        String fn = fullname.getText().toString();

        int ages = Integer.parseInt(age.getSelectedItem().toString());

        int gen = -1;
        if (male.isChecked()) {
            gen = 1;
        } else if (female.isChecked()) {
            gen = 0;
        }

        int sub = -1;

        String ava = ImageView_To_Byte(avatar);

        return new User(fn, ages, gen, sub, ava);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 999 && resultCode == RESULT_OK) {

            if (data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                avatar.setImageBitmap(imageBitmap);
            } else {
                Uri uri = data.getData();
                avatar.setImageURI(uri);
            }

        }
    }

    public String ImageView_To_Byte(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return encodedImage;
    }

    // If you want to decode the image...
//    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
//    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//    myimageview.setImageBitmap(decodedByte);

    // Generate age value for spinner
    private List generateAgeSpinner() {
        List ageList = new ArrayList();
        for (int i = 15; i <= 100; i++) {
            ageList.add(i);
        }
        return ageList;
    }
}
