package vn.edu.poly.totalstreamer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import vn.edu.poly.totalstreamer.R;
import vn.edu.poly.totalstreamer.entities.User;
import vn.edu.poly.totalstreamer.myLog.L;

public class AccountActivity extends AppCompatActivity {

    TextView txtEmail;
    EditText fullname;
    Spinner age;
    SpinnerAdapter ageAdapter;
    RadioButton radioMale, radioFemale;
    Button updateButton;
    ImageView avatar;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtEmail = (TextView) findViewById(R.id.txtEmail);
        fullname = (EditText) findViewById(R.id.fullname);
        age = (Spinner) findViewById(R.id.spinner);
        radioMale = (RadioButton) findViewById(R.id.radioButton);
        radioFemale = (RadioButton) findViewById(R.id.radioButton2);
        updateButton = (Button) findViewById(R.id.updateButton);
        avatar = (ImageView) findViewById(R.id.imageView);

        ageAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, generateAgeSpinner());
        age.setAdapter(ageAdapter);

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();

        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txtEmail.setText(firebaseUser.getEmail());
                fullname.setText(user.getFullname());
                age.setSelection(((ArrayAdapter) age.getAdapter()).getPosition(user.getAge()));
                if (user.getGender() == 1) {
                    radioFemale.setChecked(false);
                    radioMale.setChecked(true);
                } else {
                    radioMale.setChecked(false);
                    radioFemale.setChecked(true);
                }

                byte[] decodedString = Base64.decode(user.getAvatar(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                avatar.setImageBitmap(decodedByte);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                L.m(databaseError.toString());
            }
        });
    }

    public void updateAccount(View v) {
        if (!TextUtils.isEmpty(fullname.getText().toString())) {
            String fn = fullname.getText().toString();
            databaseReference.child(firebaseUser.getUid()).child("fullname").setValue(fn);

            int ages = Integer.parseInt(age.getSelectedItem().toString());
            databaseReference.child(firebaseUser.getUid()).child("age").setValue(ages);

            int gen = -1;
            if (radioMale.isChecked()) {
                gen = 1;
            } else if (radioFemale.isChecked()) {
                gen = 0;
            }
            databaseReference.child(firebaseUser.getUid()).child("gender").setValue(gen);

            String ava = ImageView_To_Byte(avatar);
            databaseReference.child(firebaseUser.getUid()).child("avatar").setValue(ava);
        }
    }

    // Generate age value for spinner
    private List generateAgeSpinner() {
        List ageList = new ArrayList();
        for (int i = 15; i <= 100; i++) {
            ageList.add(i);
        }
        return ageList;
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

    public void resetPassword(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = firebaseUser.getEmail();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            L.t("An email has been sent");
                        }
                    }
                });
    }
}
