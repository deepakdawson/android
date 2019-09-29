package com.example.jackdawson.fourplayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MusicPlayer1 extends AppCompatActivity implements View.OnClickListener ,MediaPlayer.OnCompletionListener
,MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener{


    private ImageButton playButton, nextButton, previousButton,looping;
    private SeekBar mSeekBar;
    int position=0;
    private TextView songTitle, songtime,elepse;
    // it is thread runs in background
    Thread updateSeekBar;

  private static  ArrayList<MediaFileInfo> list=new ArrayList<MediaFileInfo>();
   private static MediaPlayer mp;
    private Handler uihandler;
    boolean  stop=true;
    private ImageView MusicTheme;
    Uri selectedImageUri;
    boolean isImageSelected =false;
    public String path;
  public byte [] b;
    byte [] true1=new byte[4];
    public static final int SELECT_PICTURE=100;
    boolean stopGetDuration=true;
    static boolean isFloatingAction=false;

    StringBuilder stringBuilderPath=new StringBuilder();

    ////////////////////////////////////////////
    GestureDetectorCompat gestureDetectorCompat;
    VelocityTracker velocityTracker;
    private static final String DEBUG_TAG = "Gestures";
    AudioManager audioManager;

    int totalDuration=0;
     boolean isUpdateSekBarThreadStarted=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player1);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBAr = getSupportActionBar();
        mActionBAr.setDisplayHomeAsUpEnabled(true);
        uihandler=new Handler();
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        gestureDetectorCompat=new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                velocityTracker= VelocityTracker.obtain();
                Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
                velocityTracker.addMovement(e2);
                velocityTracker.computeCurrentVelocity(1000);
                float currntVelocity=velocityTracker.getYVelocity(e2.getPointerId(e2.getActionIndex()));
                if(currntVelocity > 0 && currntVelocity < 400.00000) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                }
                else if(currntVelocity < 0 && currntVelocity < (-400.00000)){
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        playButton = (ImageButton) findViewById(R.id.playSongButton);
        previousButton = (ImageButton) findViewById(R.id.previousSongButton);
        nextButton = (ImageButton) findViewById(R.id.nextSongButton);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setProgress(0);
        songTitle = (TextView) findViewById(R.id.title_of_song);
        songtime = (TextView) findViewById(R.id.songTime);
        MusicTheme=(ImageView)findViewById(R.id.imageView);

        MusicTheme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetectorCompat.onTouchEvent(event);
                return true;
            }
        });
        looping=(ImageButton)findViewById(R.id.loop);
        elepse=(TextView)findViewById(R.id.time);

       try {
            ArrayList<String> returnArrayList = getImagePathFromStorage(Environment.getExternalStorageDirectory());
            Log.d("true",returnArrayList.get(1));
           if(returnArrayList!=null) {
               if (returnArrayList.get(1).equals("true")) {
                   Uri uri = Uri.parse(returnArrayList.get(0));
                   MusicTheme.setImageURI(uri);
               }else {
                   MusicTheme.setImageResource(R.drawable.musictheme);
               }
           }else{
               MusicTheme.setImageResource(R.drawable.musictheme);
           }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        looping.setOnClickListener(this);




        // update seekbar thread
        updateSeekBar = new Thread(new Runnable() {
            @Override
            public void run() {
                //total duration

                int currentProgress = 0;
                if(mp !=null ) {
                    totalDuration = mp.getDuration();
                    Log.d("thread duration ",String.valueOf(mp.getDuration()));
                }

                mSeekBar.setProgress(0);
                while (currentProgress < totalDuration && stop==true) {
                    try{
                        if(mp !=null && mp.isPlaying()){
                            mSeekBar.setMax(mp.getDuration());
                            Log.d("while duration ",String.valueOf(mp.getDuration()));
                        }


                        Thread.sleep(500);
                        uihandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if(mp!= null) {
                                    mSeekBar.setProgress(mp.getCurrentPosition());
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (IllegalStateException e){
                        e.getMessage();
                    }
                }
            }
        });

        Intent intentMusicPlayer=getIntent();
        Bundle infoFromAudio=intentMusicPlayer.getExtras();
        /**
         * Floating Action Button Handling
         */
        if(isFloatingAction==false) {
            if(mp != null){
                mp.stop();
                mp.release();
            }
            list = infoFromAudio.getParcelableArrayList("list of song");
            position = infoFromAudio.getInt("position");
            Log.d("position",String.valueOf(position));
            MediaFileInfo mediaFileInfo = list.get(position);
            mp = new MediaPlayer();
            try {
                mp.setDataSource(mediaFileInfo.getFilePath());
                mp.prepareAsync();
            } catch (Exception e) {
                e.getMessage();
            }

            songtime.setText(setSongTime(Integer.parseInt(mediaFileInfo.getMediaDuration())));
            Log.d("song time duration ", mediaFileInfo.getMediaDuration());
            songTitle.setText(mediaFileInfo.getFileName());

            mp.setOnCompletionListener(this);
            mp.setOnPreparedListener(this);
            //updateSeekBar.start();
           // Log.d("seeekbar ", updateSeekBar.toString());
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              if(mp != null) {
                  elepse.setText(setSongTime(mp.getCurrentPosition()));
              }
                if(fromUser){
                    if(mp != null) {
                        mp.seekTo(seekBar.getProgress());
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mp!=null) {
                    mp.seekTo(seekBar.getProgress());
                }
            }
        });




    }


@Override
public void onResume(){



    super.onResume();
}



    // button click listener
    @Override
    public void onClick(View view){
        //
        //get id of view
        int id=view.getId();
        //
        // apply switch
        switch (id){
            //
            // case play button
            case R.id.playSongButton:
                if (mp == null) {
                    return;
                }else
                if(mp.isPlaying()){
                    mp.pause();
                    playButton.setImageResource(R.drawable.ic_pause_white_36dp);
                }
                else {
                    mp.start();
                    playButton.setImageResource(R.drawable.ic_play_white_36dp);
                }
                break;
            //
            // case for nest song
            case R.id.nextSongButton:
                     /*
                     media player is now stopd and can move to only two states
                     1-  preparing
                     2- prepared
                     these states come after initialized states means setting data sources

                    so we should try to call reset so that media player can be reached to idle state
                    from where we can reinitialize it with setting data source
                      */
                if (mp == null) {
                    return;
                }else {
                    stopGetDuration = false;
                    mp.stop();
                    mp.reset();
                    position = (position + 1) % list.size();
                    MediaFileInfo mediaFileInfo = list.get(position);
                    try {
                        mp.setDataSource(mediaFileInfo.getFilePath());
                        mp.prepareAsync();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    stopGetDuration = true;
                    songTitle.setText(mediaFileInfo.getFileName());
                    mSeekBar.setProgress(0);
                    mSeekBar.setMax(Integer.parseInt(mediaFileInfo.getMediaDuration()));
                    songtime.setText(setSongTime(Integer.parseInt(mediaFileInfo.getMediaDuration())));

                }
                break;
            //
            // case for previous button
            case R.id.previousSongButton:
                if(mp == null){
                    return;
                }else {
                    stopGetDuration = false;
                    mp.stop();
                    mp.reset();
                    position = (position - 1 < 0) ? list.size() - 1 : position - 1;
                    MediaFileInfo mediaFileInfo1 = list.get(position);
                    try {
                        mp.setDataSource(mediaFileInfo1.getFilePath());
                        mp.prepareAsync();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    stopGetDuration = true;
                    songTitle.setText(mediaFileInfo1.getFileName());
                    mSeekBar.setProgress(0);
                    mSeekBar.setMax(Integer.parseInt(mediaFileInfo1.getMediaDuration()));
                    songtime.setText(setSongTime(Integer.parseInt(mediaFileInfo1.getMediaDuration())));
                    if(mp!=null) {
                        Log.d("pre duration ", String.valueOf(mp.getDuration()));
                    }
                }
                break;
            case R.id.loop:
                if(mp == null){
                    return;
                }
                if(mp.isLooping()==false){
                    looping.setImageResource(R.drawable.ic_mp_repeat_once_btn);
                    mp.setLooping(true);
                }
                else{
                    looping.setImageResource(R.drawable.ic_mp_repeat_all_btn);
                    mp.setLooping(false);
                }
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer){


        if (mp == null) {
            return;
        }else {
            stopGetDuration = false;
           // mp.stop();
            mp.reset();
            position = (position + 1) % list.size();
            MediaFileInfo mediaFileInfo = list.get(position);
            try {
                mp.setDataSource(mediaFileInfo.getFilePath());
                mp.prepareAsync();
            } catch (Exception e) {
                e.getMessage();
            }
            stopGetDuration = true;
            songTitle.setText(mediaFileInfo.getFileName());
            mSeekBar.setProgress(0);
            mSeekBar.setMax(Integer.parseInt(mediaFileInfo.getMediaDuration()));
            songtime.setText(setSongTime(Integer.parseInt(mediaFileInfo.getMediaDuration())));

        }


    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int arg1,int arg2){

        Log.d("inetrnal error ","ocurred");
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer){

         //   totalDuration = mp.getDuration();
            mp.start();
        try {
            if (isUpdateSekBarThreadStarted && updateSeekBar.isAlive() == false) {
                isUpdateSekBarThreadStarted = false;
                updateSeekBar.start();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

            }

    //  create option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //
        // now inflate menu from rsource
        getMenuInflater().inflate(R.menu.menu_main2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //
        // now get menu item id
        int id=item.getItemId();
        if(id == R.id.action_Theme){
            Intent intent=new Intent();
            intent.setType("image/");
            intent.setAction(intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"select Picture"),SELECT_PICTURE);
            return true;
        }
        //
        // if no id match occur then call super method
        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    isImageSelected=true;
                    path = getPathFromUri(selectedImageUri);
                    Log.d("path of selected image",path);
                    MusicTheme.setImageURI(selectedImageUri);
                }
            }
        }
    }
    public String getPathFromUri(Uri uri){
        String res=null;
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor=getContentResolver().query(uri,proj,null,null,null);
        if(cursor.moveToFirst()){
            int colums_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res=cursor.getString(colums_index);

        }
        cursor.close();
        return res;
    }


    //
    //set song time
    public String setSongTime(int millis){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        StringBuilder sb=new StringBuilder();
        sb.append(minutes);
        sb.append(":");
        sb.append(seconds);
        return sb.toString();
    }

    public void saveToStorage(File rootDir){

        String  b1="true";
        boolean isCreatedDir=false;
        File mFile;
        File mFile1;
        File file=new File(rootDir,"four Player");
        if(file.exists()){
            Log.d("directory =="+file.getAbsolutePath(),"exist");
            Log.d("checking ","files");
            mFile=new File(file,"path.txt");
            mFile1=new File(file,"true.txt");
            if(mFile.exists() && mFile1.exists()){
                Log.d("file=="+mFile.getAbsolutePath(),"exist");
                Log.d("files=="+mFile1.getAbsolutePath(),"exist");
                mFile.delete();
                mFile1.delete();
                Log.d("files "+mFile.getAbsolutePath()+"\n"+mFile1.getAbsolutePath(),"deleted");
                                    /*
                                    now create new files again
                                     */
                try {
                    boolean fileCreatedPath = mFile.createNewFile();
                    boolean fileCreateTrue = mFile1.createNewFile();
                    if(fileCreatedPath && fileCreateTrue) {
                        try {
                            if(mFile.exists() && mFile1.exists()){
                                if(mFile.canWrite() && mFile1.canWrite()) {
                                    FileOutputStream mFileOutputStream = new FileOutputStream(mFile.getAbsoluteFile());
                                    FileOutputStream mFileOutputStream1 = new FileOutputStream(mFile1.getAbsoluteFile());
                                    mFileOutputStream.write(path.getBytes());
                                    mFileOutputStream.close();
                                    mFileOutputStream1.write(b1.getBytes());
                                    mFileOutputStream1.close();
                                                    /*
                                                    write done
                                                     */
                                    Log.d("file write", "done");
                                }
                            }
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }else{
                ///
                // directory exist but file not exists
                // create new files
                try {
                    boolean fileCreatedPath = mFile.createNewFile();
                    boolean fileCreateTrue = mFile1.createNewFile();
                    if(fileCreatedPath && fileCreateTrue) {
                        try {
                            if(mFile.exists() && mFile1.exists()){
                                if(mFile.canWrite() && mFile1.canWrite()) {
                                    FileOutputStream mFileOutputStream = new FileOutputStream(mFile.getAbsoluteFile());
                                    FileOutputStream mFileOutputStream1 = new FileOutputStream(mFile1.getAbsoluteFile());
                                    mFileOutputStream.write(path.getBytes());
                                    mFileOutputStream.close();
                                    mFileOutputStream1.write(b1.getBytes());
                                    mFileOutputStream1.close();
                                                    /*
                                                    write done
                                                     */
                                    Log.d("file write", "done");
                                }
                            }
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
        else {
            Log.d("path", file.getAbsolutePath());
                                /*
                                two player directory not exist
                                create Two Player diretory
                               */
            isCreatedDir = file.mkdir();
        }
        if(isCreatedDir){
            try {
                mFile=new File(file,"/path.txt");
                mFile1=new File(file,"/true.txt");
                boolean isFileCreated= mFile.createNewFile();
                boolean isTrueCreated=mFile1.createNewFile();
                if(isFileCreated && isTrueCreated){
                    try {
                        if (mFile.exists() && mFile1.exists()) {
                            if (mFile.canWrite() && mFile1.canWrite()) {
                                FileOutputStream fileOutputStream = new FileOutputStream(mFile.getAbsoluteFile());
                                fileOutputStream.write(path.getBytes());
                                fileOutputStream.close();
                                FileOutputStream fileOutputStream1=new FileOutputStream(mFile1.getAbsoluteFile());
                                fileOutputStream1.write(b1.getBytes());
                                fileOutputStream1.close();
                            }
                        }
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                        Log.d("file not found ",e.toString());
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
                Log.d(" io error",e.toString());
            }

        }
    }



    public ArrayList<String> getImagePathFromStorage(File path){

        try {
            File file = new File(path, "/four Player/path.txt");
            File file1=new File(path,"/four Player/true.txt");
            if(file.exists() && file1.exists()){
                if(file.canRead() && file1.canRead()){
                    Log.d("path",file.getAbsolutePath());
                    Long l=new Long(file.length());
                    b=new byte[l.intValue()];
                    FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
                    FileInputStream fileInputStream1=new FileInputStream(file1.getAbsoluteFile());

                    Log.d("reading ","files");
                    fileInputStream.read(b);
                    for(byte b1: b){
                        char a=(char)b1;
                        stringBuilderPath.append(a);
                    }
                    fileInputStream1.read(true1);
                    Log.d("file read ","done");
                    fileInputStream.close();
                    fileInputStream1.close();

                }

            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
       // StringBuilder stringBuilderPath=new StringBuilder();
         StringBuilder stringBuilderTrue=new StringBuilder();


        for(byte b2:true1){
            char d=(char)b2;
            stringBuilderTrue.append(d);
        }
        String returnPath=stringBuilderPath.toString();
        String returnTrue=stringBuilderTrue.toString();
        Log.d(" strng",stringBuilderPath.toString());
        Log.d("true'",stringBuilderTrue.toString());
        ArrayList<String> arrayList=new ArrayList<String>();
        arrayList.add(returnPath);          // position 0
        arrayList.add(returnTrue);         // position 1
        Log.d("returning array list","containing files");
        if(arrayList.isEmpty()){
            return null;
        }
        return arrayList;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isImageSelected){
                    saveToStorage(Environment.getExternalStorageDirectory());

                }
            }
        }).start();

    }

  @Override
    public void onBackPressed(){
      //
      //check whether mp is null or not
      if(mp==null){
          super.onBackPressed();
      }
      else
          if(mp != null) {
              if (mp.isPlaying() == false) {
                  stop = false;

                  mp.stop();
                  mp.release();
                  updateSeekBar.interrupt();
                  mp = null;
                  super.onBackPressed();
              }
              if(isFloatingAction){
                  isFloatingAction=false;
              }
              super.onBackPressed();
          }

  }

}
