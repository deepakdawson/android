package com.example.jackdawson.fourplayer;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;


public class Audio extends AppCompatActivity implements ViewPager.PageTransformer{

   // MusicService musicService;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private SongAdapter songAdapter;
    private int tab_count=2;
    public boolean found=false;
    SongLoader songLoader;
    DataToFragments dataToFragments=new DataToFragments() {
        @Override
        public void sendlist(ArrayList<MediaFileInfo> list) {
          list=mediaList;
        }
    };
    public  ArrayList<MediaFileInfo> mediaList=null;
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaList=new ArrayList<MediaFileInfo>();
        songLoader=new SongLoader(this);
        songLoader.execute("audio");
        setContentView(R.layout.activity_audio);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayer1.isFloatingAction=true;
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),MusicPlayer1.class);
               // intent.putExtra("list of ",mediaList);
                //intent.putExtra("positi",0);
                startActivity(intent);


            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Music");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
         songAdapter=new SongAdapter(getSupportFragmentManager(),tab_count);
         songAdapter.arrayList=mediaList;
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setPageTransformer(true,this);
        mViewPager.setAdapter(songAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Log.d("size of array",String.valueOf(mediaList.size()));
        Log.d("value of true",String.valueOf(found));
       // Songs songs=(Songs) songAdapter.getItem(0);
        //songs.getList(mediaList);
        songAdapter.notifyDataSetChanged();
        /**
        send data to fragments
         */
       // dataToFragments.sendlist(mediaList);
       // Fragment fragment=Grid.newInstance(mediaList);
       // Bundle bundle=new Bundle();
       //// bundle.putParcelableArrayList("list",mediaList);
      //  fragment.setArguments(bundle);
      //  Grid grid=new Grid();
       //grid.setArguments(bundle);

    }


    @Override
    public void transformPage(View v ,float position){
         final float MIN_SCALE=0.85f;
        final float MIN_ALPHA=0.5f;

        int pageHeight=v.getHeight();
        int pageWidth=v.getWidth();

        if(position < -1){
            v.setAlpha(0);
        }else if(position <=1){

            float scaleFactor=Math.max(MIN_SCALE,1-Math.abs(position));
            float vertMargin=pageHeight*(1-scaleFactor)/2;
            float horzMargin=pageWidth*(1-scaleFactor)/2;
            if(position<0){
                v.setTranslationX(horzMargin-vertMargin/2);
            }else
            {
                v.setTranslationX(horzMargin+vertMargin/2);
            }

            //scale the page down
            v.setScaleX(scaleFactor);
            v.setScaleY(scaleFactor);

            //fade the page relative to its size
            v.setAlpha(MIN_ALPHA+(scaleFactor-MIN_SCALE)/(1-MIN_SCALE)*(1-MIN_ALPHA));
        }else {
            //this page is way off screen to the right
            v.setAlpha(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent();
            intent.setClass(this,About.class);
            intent.setPackage("com.example.jackdawson.fourplayer");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
       public interface for callback methods
     **/
    public interface DataToFragments{

        void sendlist(ArrayList<MediaFileInfo> list);
    }

      @Override
    public void onBackPressed(){
          this.finish();
      }
    @Override
    public void onDestroy(){
        this.finish();
        super.onDestroy();


    }

}
