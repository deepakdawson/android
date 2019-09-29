package com.example.jackdawson.fourplayer;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class Video extends AppCompatActivity implements SwipeListViewDelete.SwipeListViewCallback{

    private ListView gridView;
   private static ArrayList<String> video_name;
    // private VideoAdapter videoAdapter;
   private   boolean isFillVideoListucessfully=false;
    int counter=0;
    //
    // list of color
    Integer [] color={ 0xe74c3c,0xc0392b,0xd35400,0x2c3e50,0x2ecc71,0x9b59b6,0xe67e22};
     Random random;
  //  Animation alpha;
    private MyAdapter m_Adapter;

   private static ArrayList<VideoInfo > videoInfoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        videoInfoArrayList=new ArrayList<VideoInfo>();
        new VideoLoader().execute("video");
        super.onCreate(savedInstanceState);
       // alpha= AnimationUtils.loadAnimation(this,R.anim.alpha);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Video");
        setContentView(R.layout.activity_video);
         random=new Random();
        gridView = (ListView) findViewById(R.id.video_list);
      //  videoAdapter=new VideoAdapter(videoInfoArrayList,this);
        //gridView.setAdapter(videoAdapter);
        fillVideoList();
        SwipeListViewDelete swipeListViewDelete=new SwipeListViewDelete(this,this);
        swipeListViewDelete.exec();
        m_Adapter=new MyAdapter(video_name);
        gridView.setAdapter(m_Adapter);

    }

    @Override
    protected void onStart(){
           // gridView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.card_view, R.id.video_grid_title, video_name));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   counter++;
               // if(counter==3){
               //     counter=0;
                //    gridView.setBackgroundColor(color[random.nextInt(color.length-1)]);
               // }
              //  view.startAnimation(alpha);
             //   Intent intent=new Intent();
              //  intent.setClass(getApplicationContext(),VideoPlayer.class);
             //   intent.setPackage("com.example.jackdawson.fourplayer");
             //   intent.putExtra("path",videoInfoArrayList.get(position).getFilePath());
               // startActivity(intent);
            }
        });
      super.onStart();
    }

    private  void fillVideoList(){
        try {
            video_name = new ArrayList<String>();
            for (int i = 0; i < videoInfoArrayList.size(); i++) {
                video_name.add(videoInfoArrayList.get(i).getFileName());
            }
        }catch (IndexOutOfBoundsException e){
            e.getMessage();
        }
        //set variable to let know when to populate view
         isFillVideoListucessfully=true;
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        this.finish();
        videoInfoArrayList=null;
    }

    /**
     *  interface implementation methods
     */




    @Override
    public ListView getListView() {
        // TODO Auto-generated method stub
        return gridView;
    }

    @Override
    public void onSwipeItem(boolean isRight, int position) {
        // TODO Auto-generated method stub
        m_Adapter.onSwipeItem(isRight, position);
    }

    @Override
    public void onItemClickListener(ListAdapter adapter, int position) {
       // view.startAnimation(alpha);
        Intent intent=new Intent();
        intent.setClass(getApplicationContext(),VideoPlayer.class);
        intent.setPackage("com.example.jackdawson.fourplayer");
        intent.putExtra("path",videoInfoArrayList.get(position).getFilePath());
        startActivity(intent);

    }


    /**
     *  MyAdapter Class implementation
     */
    public class MyAdapter extends BaseAdapter {

        private final int INVALID = -1;
        protected int DELETE_POS = -1;
        private ArrayList<String> arrayList=new ArrayList<String>();

        //
        //   public constructor
        public MyAdapter( ArrayList<String> arrayList) {
           this.arrayList=arrayList;

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
                convertView = LayoutInflater.from(Video.this).inflate(
                        R.layout.vido_delete_and_txt_view, null);
            }
            TextView title=(TextView) convertView.findViewById(R.id.video_delete_title);
            ImageButton delete=(ImageButton)convertView.findViewById(R.id.delete1);

            if (DELETE_POS == position) {
                delete.setVisibility(View.VISIBLE);
            } else
                delete.setVisibility(View.GONE);
            delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    deleteItem(position);
                    try {
                        File file = new File(videoInfoArrayList.get(position).getFilePath());
                        file.delete();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            title.setText(getItem(position));

            return convertView;

        }


    }




    /**
     * video loader that loads videos from storage
      */


    public class VideoLoader extends AsyncTask< String,ArrayList<MediaFileInfo>,Void> {

        public VideoLoader(){

        }
        @Override
        protected void onPreExecute() {
            //setProgressBarIndeterminateVisibility(true);

        }

        @Override
        protected Void doInBackground(String... params) {

            if(params[0].equals("video")){
              parseAllVideo();

            }

            return null;
        }
        private void parseAllVideo() {
            try {
                String name = null;
                String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                        MediaStore.Video.Thumbnails.VIDEO_ID};

                int video_column_index;
                String[] proj = {MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.SIZE};
                Cursor videocursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        proj, null, null, null);
                int count = videocursor.getCount();
                Log.d("No of video", "" + count);
                for (int i = 0; i < count; i++) {
                  VideoInfo videoInfo=new VideoInfo();
                    video_column_index = videocursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                    videocursor.moveToPosition(i);
                    name = videocursor.getString(video_column_index);

                    videoInfo.setFileName(name);

                    int column_index = videocursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    videocursor.moveToPosition(i);
                    String filepath = videocursor.getString(column_index);

                    videoInfo.setFilePath(filepath);

                    videoInfoArrayList.add(videoInfo);
                    // id += " Size(KB):" +
                    // videocursor.getString(video_column_index);


                }
                videocursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void onPostExecute(Void a) {

        }


    }



   /* public static class VideoAdapter extends BaseAdapter{

        private static ArrayList<VideoInfo> dataSet;
        private static   Bitmap bmThumbnail;
        LayoutInflater inflater;
        Context context;
        public VideoAdapter(ArrayList<VideoInfo> videoInfos, Context context){
            this.dataSet=videoInfos;
            this.context=context;
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataSet.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        private static class ViewHolder{
            TextView title;
            //ImageView thumbnail;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView==null) {
                holder=new ViewHolder();
                convertView = inflater.inflate(R.layout.card_view, null);
               // holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail_video);
                holder.title = (TextView) convertView.findViewById(R.id.video_grid_title);
                convertView.setTag(holder);

            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            VideoInfo videoInfo=dataSet.get(position);
            holder.title.setText(videoInfo.getFileName());
             bmThumbnail = ThumbnailUtils.
                    extractThumbnail(ThumbnailUtils.createVideoThumbnail(videoInfo.getFilePath(),
                            MediaStore.Video.Thumbnails.MINI_KIND), 64, 64);
            if(bmThumbnail!=null){
               // holder.thumbnail.setImageBitmap(bmThumbnail);
            }
            return convertView;
        }


    }
*/


}
