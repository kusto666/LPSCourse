package com.dlkustovmylocatorgps.dmitry.mygpsone;

/*public class PagerTaskAdapter {
}*/
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PagerTaskAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    TabTaskCurr tab1 = null;
    TabTaskEnd tab2 = null;
    TabTaskReports tab3 = null;
    public PagerTaskAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (tab1 == null)
                tab1 = new TabTaskCurr();
                //Log.d(MainActivity.m_TAG, "TabTask tab = " + tab1.toString() );
                return tab1;
            case 1:
                if (tab2 == null)
                tab2 = new TabTaskEnd();
                //Log.d(MainActivity.m_TAG, "TabTask tab = " + tab2.toString() );
                return tab2;
            case 2:
                if (tab3 == null)
                tab3 = new TabTaskReports();
               // Log.d(MainActivity.m_TAG, "TabTask tab = " + tab3.toString() );
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
