package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    Fragment activity;
    List<Upload> listUsers;
    LayoutInflater inflater;

    public ListViewAdapter(Fragment activity, List<Upload> listUsers) {
        this.activity = activity;
        this.listUsers = listUsers;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) activity
                .getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, null);

        TextView txtUser = (TextView) itemView.findViewById(R.id.txt_name);
        TextView txtEmail = (TextView) itemView.findViewById(R.id.txt_path_download);

        txtUser.setText(listUsers.get(i).getMyName());
        txtEmail.setText(listUsers.get(i).getMyUrlDownload());

        return  itemView;
    }
}
