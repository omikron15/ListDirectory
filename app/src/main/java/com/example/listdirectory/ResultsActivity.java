package com.example.listdirectory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ResultsActivity extends AppCompatActivity {


    String Query_String;
    String DB_PATH;

    //Declares SQLite db object
    SQLiteDatabase db;

    Cursor Results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Intent intent_Results = getIntent();
        //Retrieve db file from query activity

        DB_PATH = intent_Results.getStringExtra("DB_PATH");
       // Query_String = intent_Results.getStringExtra("Query_String");
        Query_String = "SELECT * FROM INPUTS";
        db = SQLiteDatabase.openDatabase(DB_PATH, null, 1);

       Results = db.rawQuery(Query_String, null);
        Results.moveToFirst();

        //Now results in the cursor. we need to  ge this displayed in a grid view.


       // String test1 = Results.getString(Results.getColumnIndex("ID")) ;
       // Results.moveToNext();
       // String test2 = Results.getString(Results.getColumnIndex("ID")) ; ;
       // String test3 ;
       // String test4 ;


    }

    public void ShowInputs(final View view){


        String test1 = Results.getString(Results.getColumnIndex("ID"));


        Toast.makeText(getBaseContext(), test1, Toast.LENGTH_LONG).show();
        Results.moveToNext();
    }

    public void ShowOutputs(final View view){

        Toast.makeText(getBaseContext(), "Outputs", Toast.LENGTH_LONG).show();
    }

    public void ShowRelationships(final View view){

        Toast.makeText(getBaseContext(), "Relationships", Toast.LENGTH_LONG).show();
    }

    public void AddText(final View view){

        //code should alter the results row object to have any many textviews as there are collums in the query result.
        LinearLayout name3 = (LinearLayout) findViewById(R.id.ResultRowLayout);
        //ResultsText
        //TextView text = (TextView) findViewById(R.id.ResultsText);
        TableLayout table = (TableLayout) findViewById(R.id.TableLayout);



        TableRow tr_head = new TableRow(this);

        for (int x =0; x < Results.getColumnCount(); x++){

            TextView temp = new TextView(this);
            temp.setId(x);
            temp.setPadding(10, 10, 10, 10);
            temp.setTextSize(15);
            temp.setText(Results.getColumnName(x));
            tr_head.addView(temp);
        }

        table.addView(tr_head);

        Results.moveToFirst();



        //for (int x =0; x < Results.getCount(); x++) {
            for (int y =0; y < Results.getCount(); y++) {

                TableRow tr_data = new TableRow(this);

            //for (int z = 1; (z < Results.getColumnCount()-1); z++) {
            for (int z = 1; (z < Results.getColumnCount()); z++) {



                TextView temp2 = new TextView(this);
                temp2.setPadding(10, 10, 10, 10);
                temp2.setTextSize(15);
                temp2.setText(Results.getString(Results.getColumnIndex(Results.getColumnName(z))));
                tr_data.addView(temp2);


            }
                table.addView(tr_data);
                Results.moveToNext();
           //String test1 = Results.getString(Results.getColumnIndex("ID"));



        }







    }

    public void Close(final View view){

        super.finish();

    }



}//end of class
