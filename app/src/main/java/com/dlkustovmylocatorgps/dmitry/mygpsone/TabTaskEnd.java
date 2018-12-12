package com.dlkustovmylocatorgps.dmitry.mygpsone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabTaskEnd extends Fragment {


    public TabTaskEnd() {
        Log.d(MainActivity.m_TAG, "new TabTask TabTaskEnd CONSTRUCTOR!!!" );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(MainActivity.m_TAG, "new TabTask TabTaskEnd" );
        return inflater.inflate(R.layout.fragment_tab_task_end, container, false);
    }

}
