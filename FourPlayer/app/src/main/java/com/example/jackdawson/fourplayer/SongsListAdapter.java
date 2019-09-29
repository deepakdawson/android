package com.example.jackdawson.fourplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jack dawson on 4/14/2017.
 */

public class SongsListAdapter extends BaseAdapter {

   private  Context audio;
   private ArrayList<MediaFileInfo> list=null;
    private static LayoutInflater inflater=null;

   public SongsListAdapter(@NonNull Context audio, ArrayList<MediaFileInfo> list){
        this.audio=audio;
       this.list=list;
       inflater=(LayoutInflater)audio.getSystemService(audio.LAYOUT_INFLATER_SERVICE);
   }
    @Override
    public int getCount() {
// TODO Auto‐generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
      // TODO Auto‐generated method stub
        return position;
    }

    private static class ViewHolder{
        TextView artist;
        TextView title;
        TextView duration;
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        MediaFileInfo mediaFileInfo=list.get(position);
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.songs_list, null);
            viewHolder=new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.textView_title);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView_songs_list);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.artist.setText(mediaFileInfo.getArtist());
        viewHolder.title.setText(mediaFileInfo.getFileName());
        viewHolder.duration.setText(setSongTime(Integer.parseInt(mediaFileInfo.getMediaDuration())));
        try{
            mediaMetadataRetriever.setDataSource(mediaFileInfo.getFilePath());
            if(mediaMetadataRetriever != null) {
                byte[] thumbnail=mediaMetadataRetriever.getEmbeddedPicture();

                if (thumbnail!= null) {
                    Bitmap bmp= BitmapFactory.decodeByteArray(thumbnail,0,thumbnail.length);
                  Bitmap bmp1= ThumbnailUtils.extractThumbnail(bmp,80,50);
                    viewHolder.imageView.setImageBitmap(bmp1);
                }
                else {
                    viewHolder.imageView.setImageResource(R.drawable.default_thumbnail);
                }
            }
            mediaMetadataRetriever.release();
        }
        catch (Exception e){
            e.getCause();
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
// TODO Auto‐generated method stub
        return position;
    }

    /*
    get song time in minuts from milliseconds
     */

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

}
