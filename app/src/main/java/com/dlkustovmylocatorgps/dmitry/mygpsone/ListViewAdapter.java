package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    Fragment activity;
    List<Upload> listUsers;
    LayoutInflater inflater;

    public static boolean checkState[];

    public ListViewAdapter(Fragment activity, List<Upload> listUsers) {
        this.activity = activity;
        this.listUsers = listUsers;
        checkState = new boolean[listUsers.size()];
    }

    @Override
    public int getCount() {
        return listUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return listUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /*public class ViewHolder {
        TextView tvItemName;
        CheckBox check;
    }*/

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) activity.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, null);

        TextView txtUser = (TextView) itemView.findViewById(R.id.txt_name);
        TextView txtEmail = (TextView) itemView.findViewById(R.id.txt_path_download);

        txtUser.setText(listUsers.get(position).getMyName());
        txtEmail.setText(listUsers.get(position).getMyUrlDownload());

        final CheckBox itemCheckbox = (CheckBox) itemView.findViewById(R.id.list_view_item_checkbox);



        itemCheckbox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               //Log.i(Constants.MY_TAG, "itemCheckbox.setOnClickListener(new View.OnClickListener()");

                       /* if(itemCheckbox.isChecked())
                        {
                            Log.i(Constants.MY_TAG, "itemCheckbox = true" );
                        }
                        else
                        {
                            Log.i(Constants.MY_TAG, "itemCheckbox = false" );
                        }*/
            /*    Log.i(Constants.MY_TAG, "ListViewAdapter size 555 = " + ListViewAdapter.checkState.length);
               for(int i = 0; i < checkState.length; i++)
                {
                    if(i == position)
                    {
                        //v.g
                        itemCheckbox.setChecked(true);
                        checkState[i]=true;
                    }
                    else
                    {
                        itemCheckbox.setChecked(false);
                        checkState[i]=false;
                    }
                    Log.i(Constants.MY_TAG, "checkState[" + i + "] = " + checkState[i]);
                }
                Log.i(Constants.MY_TAG, "notifyDataSetChanged();!!!!!!!!!!!!!!!!");
                notifyDataSetChanged();*/
            }
        });

        return  itemView;
    }
}
