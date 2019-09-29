package com.example.jackdawson.fourplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by jack dawson on 4/14/2017.
 */

public class SongAdapter extends FragmentStatePagerAdapter {

    private final int TAB_COUNT;

   public ArrayList<MediaFileInfo > arrayList;


    public SongAdapter(FragmentManager fragmentManager, int tab_count){
        // create object of fragment state pager adapter
        // so that data can be bound to adapter
        super(fragmentManager);
        // now make reference to TAB_COUNT
        this.TAB_COUNT=tab_count;
    }


    @Override
    public Fragment getItem(int position){
        //now decide which fragment to returned
        switch (position){
            //when audio tab is selected or touched or swiped
            case 0:
                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("list",arrayList);
                Songs songs=new Songs();
                songs.setArguments(bundle);
                return songs;
            //when user selects video tab or touch or slide
            case 1:
                Bundle bundle1=new Bundle();
                bundle1.putParcelableArrayList("list",arrayList);
                Grid grid=new Grid();
                grid.setArguments(bundle1);
                return grid;
            default:
                return  null;
        }
    }


    @Override
    public int getCount(){
        // return no of tabs
        return TAB_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Songs";
            case 1:
                return "Grid";

        }
        return null;
    }
}
