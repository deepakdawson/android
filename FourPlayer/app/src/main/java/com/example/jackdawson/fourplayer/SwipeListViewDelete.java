package com.example.jackdawson.fourplayer;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by jack dawson on 4/22/2017.
 */

public class SwipeListViewDelete {

    public static interface SwipeListViewCallback {
        /**
         *
         * @return ListView
         */
        public abstract ListView getListView();

        /**
         *
         * @param isRight
         *            Swiping direction
         * @param position
         *            which item position is swiped
         */
        public abstract void onSwipeItem(boolean isRight, int position);

        /**
         * For single tap/Click
         *
         * @param adapter
         * @param position
         */
        public abstract void onItemClickListener(ListAdapter adapter,int position);

    }

    Context m_Context;
    ListView mListView;
    SwipeListViewCallback m_Callback;

    public SwipeListViewDelete(Context mContext, SwipeListViewCallback callback) {
        // TODO Auto-generated constructor stub
        if (callback == null) {
            //
            try {
                throw new Exception(
                        "Activity must be implement SwipeListViewCallback");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        init(mContext, callback);
    }

    public SwipeListViewDelete(Context mContext) throws Exception {
        // TODO Auto-generated constructor stub
        if (!(mContext instanceof SwipeListViewCallback)) {   // if it is true activity not implemented interface
            //
            throw new Exception(
                    "Activity must be implement SwipeListViewCallback");
        }
        init(mContext, (SwipeListViewCallback) mContext);
    }

    private ListView list;
    private int REL_SWIPE_MIN_DISTANCE;
    private int REL_SWIPE_MAX_OFF_PATH;
    private int REL_SWIPE_THRESHOLD_VELOCITY;

    protected void init(Context mContext, SwipeListViewCallback mCallback) {
        m_Context = mContext;
        m_Callback = mCallback;
    }

    public void exec() {
        //
        DisplayMetrics dm = m_Context.getResources().getDisplayMetrics();
        REL_SWIPE_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
        REL_SWIPE_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
        REL_SWIPE_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f + 0.5);
        //
        list = m_Callback.getListView();

        @SuppressWarnings("deprecation")
        final GestureDetector gestureDetector = new GestureDetector(
                new MyGestureDetector());

        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {                  //gestureDetector.onTouchEvent(event);
                return gestureDetector.onTouchEvent(event);                     // return true;
            }
        };
        list.setOnTouchListener(gestureListener);
    }

    private void myOnItemClick(int position) {
        if (position < 0)
            return;
        m_Callback.onItemClickListener(list.getAdapter(), position);

    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private int temp_position = -1;

        // Detect a single-click and call my own handler.
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            int pos = list.pointToPosition((int) e.getX(), (int) e.getY());
            myOnItemClick(pos);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {

            temp_position = list
                    .pointToPosition((int) e.getX(), (int) e.getY());
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH)
                return false;
            if (e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {

                int pos = list
                        .pointToPosition((int) e1.getX(), (int) e2.getY());

                if (pos >= 0 && temp_position == pos)
                    m_Callback.onSwipeItem(false, pos);
            } else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {

                int pos = list
                        .pointToPosition((int) e1.getX(), (int) e2.getY());
                if (pos >= 0 && temp_position == pos)
                    m_Callback.onSwipeItem(true, pos);

            }
            return false;
        }

    }



}
