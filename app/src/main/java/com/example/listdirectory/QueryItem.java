package com.example.listdirectory;

/**
 * Created by omikr on 04/10/2016.
 */
public class QueryItem {

    public String[] Field_Names;
    public String[] Query_String;
    public int changeNumber;

    public QueryItem(String[] fields, String[] query ){

        this.Field_Names = fields;
        this.Query_String = query;
        changeNumber = 0;

    }

    public int getCount() {

        return Field_Names.length;
    }

    public boolean hasQuery(){

        if(changeNumber == 0){
            return false;
        }else{
            return true;
        }
    }
}
