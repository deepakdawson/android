package com.example.jackdawson.fourplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;



public class Songs extends Fragment {





  //  private OnFragmentInteractionListener mListener;

  private static ArrayList<MediaFileInfo> mediaFileInfos=null;

   //public  Audio audio;
    public ListView songsList;
   SongsListAdapter songsListAdapter=null;
    public  Songs() {
        //this.audio=audio1;
        // Required empty public constructor
    }
@Override
public void onAttach(Context context){
    super.onAttach(context);
   // mediaFileInfos=audio.mediaList;

}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param// param2 Parameter 2.
     * @return A new instance of fragment Songs.
     */
    // TODO: Rename and change types and number of parameters
  /* public static Songs newInstance( Audio audio1) {
        Songs fragment = new Songs();
      //  Bundle args = new Bundle();

      //  args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
       // fragment.setArguments(args);
        return fragment;
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaFileInfos=new ArrayList<MediaFileInfo>();
        //
        // get arguments from bundle
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mediaFileInfos = bundle.getParcelableArrayList("list");
            }
            if (songsListAdapter == null) {
                songsListAdapter = new SongsListAdapter(getActivity(), mediaFileInfos);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("size of list fragment", String.valueOf(mediaFileInfos.size()));
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_songs, container, false);
        songsList=(ListView)root.findViewById(R.id.list_song);
        songsList.setAdapter(new SongsListAdapter(getContext(),mediaFileInfos));
        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicPlayer1.isFloatingAction=false;
                Intent intent =new Intent();
                intent.setClass(getContext(),MusicPlayer1.class);
                intent.putExtra("position",position);
                intent.putExtra("list of song",  mediaFileInfos);
                intent.setPackage("com.example.jackdawson.fourplayer");
                if(intent != null){
                startActivity(intent);
                }else{
                    Log.d("Intnt is ","null");
                }
            }
        });
      //   new BackgroundWork().execute("load");

        //songsList.setAdapter(songsListAdapter);
       // songsListAdapter=null;
        return root;
    }


@Override
public void onDestroy(){
    super.onDestroy();
    mediaFileInfos=null;
    songsListAdapter=null;
}

    // TODO: Rename method, update argument and hook method into UI event
 /*   public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

  /*  public  class BackgroundWork extends AsyncTask<String,Void,Integer>{


      //  private Context context;
       // private List<MediaFileInfo> mlist;

        public BackgroundWork(){

        }

        @Override
        protected void onPreExecute(){

        }
           @Override
        protected Integer doInBackground(String... params){
               int result=0;
                if(params[0].equals("load")){
                    songsListAdapter=new SongsListAdapter(getActivity(),mediaFileInfos);
                    result=1;
                }
               return result;

           }

        @Override
        protected void onPostExecute(Integer result) {
          if(result==1){
              songsList.setAdapter(songsListAdapter);
          }
        }
    }

*/

}
