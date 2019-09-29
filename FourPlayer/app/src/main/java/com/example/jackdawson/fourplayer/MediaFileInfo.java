package com.example.jackdawson.fourplayer;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by jack dawson on 4/14/2017.
 */

public class MediaFileInfo implements Parcelable{

    private String fileName,filePath,fileType,artist,duration;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeString(artist);
        dest.writeString(duration);

    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

         public MediaFileInfo  createFromParcel(Parcel in){
             return new MediaFileInfo(in);
         }
        public MediaFileInfo[] newArray(int size) {
            return new MediaFileInfo[size];
        }

    };

    public MediaFileInfo(Parcel in ){
        fileName=in.readString();
        filePath=in.readString();
        artist=in.readString();
        duration=in.readString();
    }




    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public void setDuration(String duration){
        this.duration=duration;
    }
    public String getMediaDuration(){
        return duration;
    }
    public void setArtist(String artist){
        this.artist=artist;
    }
    public String getArtist(){
        return artist;
    }
}
