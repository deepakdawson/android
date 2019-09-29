package com.example.jackdawson.fourplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import java.util.ArrayList;





/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link// Grid.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link// Grid#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Grid extends Fragment {



  //  private OnFragmentInteractionListener mListener;

    public static ArrayList<MediaFileInfo> gridList=new ArrayList<MediaFileInfo>();
    private GridAdapter gridAdapter=null;

    public Grid() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Grid newInstance(ArrayList<MediaFileInfo> list1) {
        //gridList=list1;
      //  Bundle bundle=new Bundle();
      //  bundle.putParcelableArrayList("list",list1);
        Grid fragment = new Grid();
       // fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                gridList = bundle.getParcelableArrayList("list");
            }

            if (gridAdapter == null) {
                gridAdapter = new GridAdapter(getActivity(), gridList);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("size of grid list",String.valueOf(gridList.size()));
        Log.d("value of save",String.valueOf(savedInstanceState));
        View root =inflater.inflate(R.layout.fragment_grid, container, false);
        GridView gridView=(GridView) root.findViewById(R.id.icon);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 MusicPlayer1.isFloatingAction=false;
                Intent intent=new Intent();
                intent.setClass(getContext(),MusicPlayer1.class);
                intent.setPackage("com.example.jackdawson.fourplayer");
                intent.putExtra("position",position);
                intent.putExtra("list of song",gridList);
                startActivity(intent);
            }
        });
        gridAdapter=null;
        return root;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        gridList=null;
    }
    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
   /* @Override
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
*/
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
    }*/




}
