package com.example.listdirectory;

/**
 * Created by omikr on 04/10/2016.
 */
public class QueryItem {

    public String[] Field_Names;
    public String[] Query_String;

    public QueryItem(String[] fields, String[] query ){

        this.Field_Names = fields;
        this.Query_String = query;

    }

    public int getCount() {

        return Field_Names.length;
    }


}
