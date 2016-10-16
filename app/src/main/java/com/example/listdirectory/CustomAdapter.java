package com.example.listdirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by CR on 03/10/2016.
 */
class customAdapter extends BaseAdapter {

    Context context;
    QueryItem data;
    private static LayoutInflater inflater = null;

    public customAdapter(Context context, QueryItem data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return data.length;
        return data.Field_Names.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return data[position];
        return data.Field_Names[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        TextView field = (TextView) vi.findViewById(R.id.field);
        field.setText(data.Field_Names[position]);

        //experiment code for second entry in custom list view

        TextView InText = (TextView) vi.findViewById(R.id.text);

        if (data.Field_Names[position] == null){
            InText.setText("BLANK");
        }else {
            InText.setText(data.Query_String[position]);
        }

        return vi;
    }




}
