package com.example.jackdawson.fourplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton audio,video,audio_rec,videoRec;

    private  final static int EXTERNAL_STORAGE_PERMISSION_RESULT_CODE =0;

     Animation rotate,alpha;
    View relativeLayout;
    TextView fourInOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkExternalStoragePermission();
        relativeLayout=findViewById(R.id.activity_main);
        audio=(ImageButton) findViewById(R.id.AudioButton);
        video=(ImageButton) findViewById(R.id.VideoButton);
        audio_rec=(ImageButton) findViewById(R.id.Audio_rec_button);
        videoRec=(ImageButton) findViewById(R.id.video_record);
        fourInOne=(TextView)findViewById(R.id.four_in_one);
        rotate= AnimationUtils.loadAnimation(this,R.anim.rotate);
        alpha=AnimationUtils.loadAnimation(this,R.anim.alpha);
      //  relativeLayout=AnimationUtils.loadLayoutAnimation(this,R.anim.rotate);
        audio.setOnClickListener(this);
        video.setOnClickListener(this);
        audio_rec.setOnClickListener(this);
        videoRec.setOnClickListener(this);
        fourInOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public  boolean onTouch(View v, MotionEvent event) {
                 v.startAnimation(alpha);
                return true;
            }
        });

    }

    @Override
    public void onClick(View view){
        int id=view.getId();
        switch(id){

            case R.id.AudioButton:
               audio.startAnimation(rotate);
                Intent intent=new Intent();
                intent.setClass(this,Audio.class);
               startActivity(intent);
                break;
            case R.id.VideoButton:
               video.startAnimation(rotate);
                Intent intent1=new Intent();
                intent1.setClass(this,Video.class);
                startActivity(intent1);
                break;
            case R.id.Audio_rec_button:
                 audio_rec.startAnimation(rotate);
                Intent intent2=new Intent();
                intent2.setClass(getApplicationContext(),AudioRecorder.class);
                intent2.setPackage("com.example.jackdawson.fourplayer");
                startActivity(intent2);
                break;
            case R.id.video_record:
                videoRec.startAnimation(rotate);
                Intent intent3=new Intent();
                intent3.setPackage("com.example.jackdawson.fourplayer");
                intent3.setClass(this,VideoRocorder.class);
                startActivity(intent3);

        }
    }

    //
    // check permission for EXTERNAL_STORAGE_PERMISSION
    // FOR ANDROID 6.0
    private void checkExternalStoragePermission(){
        //
        //now chcek permission from M and above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    == PackageManager.PERMISSION_GRANTED){
            }
            else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(getApplicationContext(),"App needs permission for Storage",Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_PERMISSION_RESULT_CODE );
            }


        }
    }

    //
    // requst result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String [] permission, @NonNull int [] grantResults){
        switch(requestCode){
            case EXTERNAL_STORAGE_PERMISSION_RESULT_CODE:
                if( grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"permission granted",Toast.LENGTH_SHORT).show();
                }
               /* if(grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    Log.d("camera permission ","granted");
                    Toast.makeText(getApplicationContext(),"Camera Permission granted",Toast.LENGTH_LONG).show();
                }
*/
                break;

            default:
                super.onRequestPermissionsResult(requestCode,permission,grantResults);
        }
    }






}
