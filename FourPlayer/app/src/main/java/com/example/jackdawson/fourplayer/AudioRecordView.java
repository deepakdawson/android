package com.example.jackdawson.fourplayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class AudioRecordView extends AppCompatActivity implements SwipeListViewDelete.SwipeListViewCallback{

    private ArrayList<File> recordlist;
    private ArrayList<String> name;
    private  ListView listView;
    MediaPlayer mediaPlayer;
    private ImageButton play;
    Thread seekbar;
    private SeekBar seekBar;
    Handler handler;
    Dialog dialog;
    private TextView textView;


    private MyAdapter m_Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_record_view);
        handler=new Handler(getMainLooper());
         try {
             File file = new File(Environment.getExternalStorageDirectory(), "/Four Player Audio Recorder");
             recordlist = findRecording(file);
             if (recordlist != null) {
               name = new ArrayList<String>(recordlist.size());

                 for (int i = 0; i < recordlist.size(); i++) {
                     name.add( recordlist.get(i).getName().toString().replace(".3gpp", ""));
                 }
                 Log.d("size of name ",String.valueOf(name.size()));
                 Log.d("record",name.get(name.size()-1));
             }

             listView = (ListView) findViewById(R.id.record_audio_list);
             SwipeListViewDelete swipeListViewDelete=new SwipeListViewDelete(this,this);
             swipeListViewDelete.exec();
             m_Adapter=new MyAdapter(name,this);
             listView.setAdapter(m_Adapter);
           //  listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.recored_audio_list_view,
                 //    R.id.record_audio_list_text_color, name));
         }catch(NullPointerException e){
             e.printStackTrace();
         }catch (Exception e){e.printStackTrace();}

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
              /*  Uri uri=Uri.fromFile(recordlist.get(position));
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                dialog=new Dialog(AudioRecordView.this);
                dialog.setContentView(R.layout.audio_record_play);
                textView=(TextView)dialog.findViewById(R.id.record_audio_title);
                play=(ImageButton)dialog.findViewById(R.id.audio_record_play);
                seekBar=(SeekBar)dialog.findViewById(R.id.record_audio_seekbar);
                seekbar=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int totalDuration=0;
                        int currentProgress = 0;
                        totalDuration=mediaPlayer.getDuration();
                        seekBar.setProgress(0);
                        seekBar.setMax(totalDuration);
                        while (currentProgress< totalDuration){
                            try{
                                Thread.sleep(500);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(mediaPlayer!=null) {
                                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                        }
                                    }
                                });

                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }catch (IllegalStateException e){
                                e.printStackTrace();
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(mediaPlayer!=null && fromUser){
                            mediaPlayer.seekTo(seekBar.getProgress());
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                           if(mediaPlayer!=null){
                               mediaPlayer.seekTo(seekBar.getProgress());
                           }
                    }
                });

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id=v.getId();
                        switch (id){
                            case R.id.audio_record_play:
                                if(mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                    play.setImageResource(R.drawable.ic_pause_white_36dp);
                                }else{
                                    mediaPlayer.start();
                                    play.setImageResource(R.drawable.ic_play_white_36dp);
                                }
                                break;
                        }
                    }
                });
                textView.setText(name.get(position));
                dialog.show();
                mediaPlayer.start();
                seekbar.start();

*/
            }

        });


    }

    public void deleteFromStorae(int position){
        if(position < 0){
            new IndexOutOfBoundsException("index is negative ");
            return;
        }else if(position >= 0 && position < recordlist.size()) {
            recordlist.get(position).delete();
            recordlist.remove(position);
        }
    }


    @Override
    public ListView getListView() {
        // TODO Auto-generated method stub
        return listView;
    }


    @Override
    public void onSwipeItem(boolean isRight, int position) {
        // TODO Auto-generated method stub
        m_Adapter.onSwipeItem(isRight, position);
    }

    @Override
    public void onItemClickListener(ListAdapter adapter, int position) {



        Uri uri=Uri.fromFile(recordlist.get(position));
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        dialog=new Dialog(AudioRecordView.this);
        dialog.setContentView(R.layout.audio_record_play);
        textView=(TextView)dialog.findViewById(R.id.record_audio_title);
        play=(ImageButton)dialog.findViewById(R.id.audio_record_play);
        seekBar=(SeekBar)dialog.findViewById(R.id.record_audio_seekbar);
        seekbar=new Thread(new Runnable() {
            @Override
            public void run() {
                int totalDuration=0;
                int currentProgress = 0;
                totalDuration=mediaPlayer.getDuration();
                seekBar.setProgress(0);
                seekBar.setMax(totalDuration);
                while (currentProgress< totalDuration){
                    try{
                        Thread.sleep(500);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(mediaPlayer!=null) {
                                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                }
                            }
                        });

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }catch (IllegalStateException e){
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer!=null){
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                switch (id){
                    case R.id.audio_record_play:
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                            play.setImageResource(R.drawable.ic_pause_white_36dp);
                        }else{
                            mediaPlayer.start();
                            play.setImageResource(R.drawable.ic_play_white_36dp);
                        }
                        break;
                }
            }
        });
        textView.setText(name.get(position));
        dialog.show();
        mediaPlayer.start();
        seekbar.start();
       // dialog.onBackPressed();
       dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
           @Override
           public void onDismiss(DialogInterface dialog) {

               try {
                   if (mediaPlayer != null) {
                       if(mediaPlayer.isPlaying()){
                           seekbar.interrupt();
                           mediaPlayer.pause();
                           mediaPlayer.stop();
                           mediaPlayer.release();
                           mediaPlayer=null;
                       }else
                       {
                           seekbar.interrupt();
                           mediaPlayer.release();
                           mediaPlayer=null;
                       }
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       });
    }

    /**
     *    Nested inner class
     */
    public class MyAdapter extends BaseAdapter {

        private final int INVALID = -1;
        protected int DELETE_POS = -1;
        private ArrayList<String> arrayList=  new ArrayList<String>();

        private AudioRecordView audioRecordView;

        public MyAdapter(ArrayList<String> arrayList,AudioRecordView audioRecordView) {
            // TODO Auto-generated constructor stub
             this.audioRecordView=audioRecordView;
            this.arrayList = arrayList;
            Log.d("size od arraylist",String.valueOf(arrayList.size()));
            Log.d("record",arrayList.get(arrayList.size()-1));
        }

        public void onSwipeItem(boolean isRight, int position) {
            // TODO Auto-generated method stub
            if (isRight == false) {
                DELETE_POS = position;
            } else if (DELETE_POS == position) {
                DELETE_POS = INVALID;
            }
            //
            notifyDataSetChanged();
        }

        //
        //  delete method

        public void deleteItem(int pos) {
            //
            arrayList.remove(pos);
            DELETE_POS = INVALID;
            notifyDataSetChanged();

        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList.size();
        }

        @Override
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(AudioRecordView.this).inflate(
                        R.layout.recored_audio_list_view, null);
            }

            TextView recorName=(TextView)convertView.findViewById(R.id.record_audio_list_text_color);
            ImageButton delete=(ImageButton)convertView.findViewById(R.id.delete);
            if (DELETE_POS == position) {
                delete.setVisibility(View.VISIBLE);
            } else
                delete.setVisibility(View.GONE);
            delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    deleteItem(position);
                    deleteFromStorae(position);
                }
            });

            recorName.setText(getItem(position));

            return convertView;

        }

    }


    //this method finds recording in a directory called "four player audio recording"

    public ArrayList<File> findRecording(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] songFromRoot = root.listFiles();
        try {
            for (File singleFile : songFromRoot) {
                if (singleFile.getName().endsWith(".3gpp") || singleFile.getName().endsWith(".wav")) {
                    al.add(singleFile);
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (al.isEmpty()) {
            //check if al is empty; then return null
            return null;
        }
        //return array list containing files
        return al;
    }

    //
    //  Activity destroy method
    @Override
    public void onDestroy(){
        if(dialog==null){
            super.onDestroy();
            return;
        }

            try {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        seekbar.interrupt();
                        mediaPlayer.pause();
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        dialog.dismiss();
                        mediaPlayer = null;
                        super.onDestroy();
                        this.finish();
                    } else {
                        seekbar.interrupt();
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        dialog.dismiss();
                        super.onDestroy();
                        this.finish();
                    }
                } else {
                    super.onDestroy();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


           // dialog.cancel();
           // super.onDestroy();

       // super.onDestroy();
    }

    // Activity back pressed method

    @Override
    public void onBackPressed(){
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    ///seekbar.interrupt();
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    dialog.dismiss();
                    mediaPlayer = null;
                    super.onBackPressed();
                } else {
                   // seekbar.interrupt();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    dialog.dismiss();
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
