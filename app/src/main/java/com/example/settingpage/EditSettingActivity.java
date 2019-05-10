package com.example.settingpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileReader;

import static android.app.ProgressDialog.show;

public class EditSettingActivity extends AppCompatActivity {

    ImageButton imageButton1, imageButton2, imageButton3, imageButton4;
    ImageView close, image_profile1,image_profile2, image_profile3, image_profile4;
    TextView save;
    MaterialEditText nickname,password;
    private RadioButton male, female;

    FirebaseAuth firebaseAuth;

//    private Uri mImageUri;
//    private StorageTask uploadTask;
//    StorageReference storageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_setting);


        close = findViewById(R.id.close);

        imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);



        save = (Button) findViewById(R.id.save);
        nickname = findViewById(R.id.nickname);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        password = findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditSettingActivity.this, "It work", Toast.LENGTH_SHORT).show();
            }
        });









    }
}
