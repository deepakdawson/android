package com.example.jackdawson.fourplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack dawson on 4/15/2017.
 */

public class GridAdapter extends BaseAdapter {

    private Context mContext;
   private ArrayList<MediaFileInfo> mediaGrid;
    LayoutInflater inflater;

    public GridAdapter(Context context, ArrayList<MediaFileInfo> list){
        this.mContext=context;
        this.mediaGrid=list;
         inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mediaGrid.size();
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
           TextView artist;
           ImageView thumbnail;
       }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
       if(convertView==null) {
           holder=new ViewHolder();
            convertView = inflater.inflate(R.layout.songs_grid, null);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail_grid);
            holder.title = (TextView) convertView.findViewById(R.id.title_grid);
           holder.artist = (TextView) convertView.findViewById(R.id.artist_grid);
           convertView.setTag(holder);
       }else{
           holder=(ViewHolder)convertView.getTag();
       }

        MediaFileInfo mediaFileInfo=mediaGrid.get(position);
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        holder.title.setText(mediaFileInfo.getFileName());
        holder.artist.setText(mediaFileInfo.getArtist());
        try{
            mediaMetadataRetriever.setDataSource(mediaFileInfo.getFilePath());
            if(mediaMetadataRetriever != null){
                byte[] thumb=mediaMetadataRetriever.getEmbeddedPicture();
                if(thumb != null){
                    Bitmap bmp= BitmapFactory.decodeByteArray(thumb,0,thumb.length);
                    Bitmap bmp1= ThumbnailUtils.extractThumbnail(bmp,128,128);
                    holder.thumbnail.setImageBitmap(bmp1);
                }
                else {
                     holder.thumbnail.setImageResource(R.drawable.default_thumbnail_128);
                }
            }
        }
        catch (Exception e)
        {
          e.getCause();
        }
        mediaMetadataRetriever.release();

        return convertView;
    }

}
