package com.example.listdirectory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by omikr on 16/10/2016.
 */
public class ResultsAdapter extends BaseAdapter {

    Context context;
    Cursor data;
    private static LayoutInflater inflater = null;

    public ResultsAdapter(Context context, Cursor data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)

            vi = inflater.inflate(R.layout.result_row, null);

        for (int x = 0; x < data.getColumnCount(); x++) {

        }
            return vi;

    }

}
