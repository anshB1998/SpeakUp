package snowleopard.speakup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddStoryActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mDesc;

    private TextView mLat;
    private TextView mLang;

    private Button mSubmit;

    private ImageButton mImg;

    private Uri mImgU = null;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private StorageReference mStorage;

//    private TextView latTV;
//    private TextView lngTV;


    public static final int GALLERY_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        mTitle = (EditText)findViewById(R.id.etTitle);
        mDesc = (EditText)findViewById(R.id.etDesc);
        mLang = (TextView)findViewById(R.id.tv_lng);
        mLat = (TextView)findViewById(R.id.tv_lat);


        mSubmit= (Button) findViewById(R.id.btSub);

        mImg = (ImageButton) findViewById(R.id.btImg);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Story");
        mStorage  = FirebaseStorage.getInstance().getReference().child("Photos");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        mProgress = new ProgressDialog(this);

//        String lat = getIntent().getStringExtra("lat");
//        TextView latTv = (TextView) findViewById(R.id.tv_lat);
//        latTv.setText(lat);
//        String lng = getIntent().getStringExtra("lng");
//        TextView lngTv = (TextView) findViewById(R.id.tv_lng);
//        lngTv.setText(lng);

        LinearLayout mLocation = (LinearLayout )findViewById(R.id.location);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStoryActivity.this,MapActivity.class);
                startActivityForResult(intent, 0 );
            }
        });


        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    public void startPosting() {
        mProgress.setMessage("Is Uploading");
        mProgress.show();
        final String title = mTitle.getText().toString().trim();
        final String desc = mDesc.getText().toString().trim();
        final String latitude = mLat.getText().toString().trim();
        final String longitude = mLang.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude))
        {
            StorageReference filepath = mStorage.child(mImgU.getLastPathSegment());
            filepath.putFile(mImgU).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri DownloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newpost = mDatabase.push();
                    newpost.child("Title").setValue(title);
                    newpost.child("Description").setValue(desc);
                    newpost.child("Latitude").setValue(latitude);
                    newpost.child("Longitude").setValue(longitude);
                    newpost.child("ImageUrl").setValue(DownloadUrl.toString().trim());
                    newpost.child("Owner").setValue(mCurrentUser.getUid());

                    mProgress.dismiss();
                    Intent intent = new Intent(AddStoryActivity.this,ListViewActivity.class);
                    startActivity(intent );
                    finish();

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            mImgU = data.getData();
            mImg.setImageURI(mImgU);
        }

        if(requestCode == 0 && resultCode == RESULT_OK)
        {
            String lat = data.getStringExtra("lat");
            TextView latTv = (TextView) findViewById(R.id.tv_lat);
            latTv.setText(lat);
            String lng = data.getStringExtra("lng");
            TextView lngTv = (TextView) findViewById(R.id.tv_lng);
            lngTv.setText(lng);
        }

    }
}