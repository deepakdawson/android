package com.example.jackdawson.fourplayer;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcel;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jack dawson on 4/14/2017.
 */

public class SongLoader extends AsyncTask< String,ArrayList<MediaFileInfo>,Void>{


    private Audio mActivity;
    public SongLoader(Audio audio){
        this.mActivity=audio;
    }
    @Override
    protected void onPreExecute() {
        //setProgressBarIndeterminateVisibility(true);

    }

    @Override
    protected Void doInBackground(String... params) {

        if(params[0].equals("audio")){
            parseAllAudio();
            mActivity.found=true;
        }

       return null;
    }

    @Override
    protected void onPostExecute(Void a) {

    }




        private void parseAllAudio() {
        try {
            String TAG = "Audio";
            Cursor cur = mActivity.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    null);

            if (cur == null) {
                // Query failed...
                Log.e(TAG, "Failed to retrieve music: cursor is null :-(");

            }
            else if (!cur.moveToFirst()) {
                // Nothing to query. There is no music on the device. How boring.
                Log.e(TAG, "Failed to move cursor to first row (no query results).");

            }else {
                Log.i(TAG, "Listing...");
                // retrieve the indices of the columns where the ID, title, etc. of the song are

                // add each song to mItems
                do {
                    int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                    int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
                    int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
                    int filePathIndex = cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    Log.i(TAG, "Title column index: " + String.valueOf(titleColumn));
                    Log.i(TAG, "ID column index: " + String.valueOf(titleColumn));

                    Log.i("Final ", "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn) + "Path: " + cur.getString(filePathIndex));
                    MediaFileInfo audio = new MediaFileInfo(Parcel.obtain());
                    audio.setFileName(cur.getString(titleColumn));
                    audio.setFilePath(cur.getString(filePathIndex));
                    audio.setArtist(cur.getString(artistColumn));
                    audio.setDuration(cur.getString(durationColumn));
                   //audio.setFileType(type);
                   mActivity.mediaList.add(audio);


                } while (cur.moveToNext());
                cur.close();
                Log.d("size of list ",String.valueOf(mActivity.mediaList.size()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
