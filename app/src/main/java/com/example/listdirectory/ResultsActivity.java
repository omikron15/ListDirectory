package com.example.listdirectory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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


    String Input_Query_String;
    String Output_Query_String;
    String Relationship_Query_String;

    //Declares String object for databse path
    String DB_PATH;

    //Declares SQLite db object
    SQLiteDatabase db;

    Cursor Input_Results;
    Cursor Output_Results;
    Cursor Relationship_Results;

    LinearLayout name3;
    TableLayout table;

    TableRow tr_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        table = (TableLayout) findViewById(R.id.TableLayout);

        Intent intent_Results = getIntent();
        //Retrieve db file from query activity

        DB_PATH = intent_Results.getStringExtra("DB_PATH");
        Input_Query_String = intent_Results.getStringExtra("Input_Query_String");
        Output_Query_String = intent_Results.getStringExtra("Output_Query_String");
        Relationship_Query_String = intent_Results.getStringExtra("Relationship_Query_String");

        //Query_String = "SELECT * FROM INPUTS";
        db = SQLiteDatabase.openDatabase(DB_PATH, null, 1);

        Input_Results = db.rawQuery(Input_Query_String, null);
        Input_Results.moveToFirst();

        Output_Results = db.rawQuery(Output_Query_String, null);
        Output_Results.moveToFirst();

        Relationship_Results = db.rawQuery(Relationship_Query_String, null);
        Relationship_Results.moveToFirst();






    }

    public void ShowInputs(final View view){


        if (table != null){
            table.removeAllViews();
        }
        ShowData(view, Input_Results);

    }

    public void ShowOutputs(final View view){


        if (table != null){
            table.removeAllViews();
        }
        ShowData(view, Output_Results);


    }

    public void ShowRelationships(final View view){

        if (table != null){
            table.removeAllViews();
        }
        ShowData(view, Relationship_Results);

    }

    public void ShowData(View view, Cursor Incursor){

        Cursor cursor = Incursor;

        //TableLayout table = (TableLayout) findViewById(R.id.TableLayout);

        tr_head = new TableRow(this);

        for (int x =0; x < cursor.getColumnCount(); x++){

            TextView temp = new TextView(this);
            temp.setId(x);
            temp.setPadding(10, 10, 10, 10);
            temp.setTextSize(15);
            temp.setBackgroundColor(Color.GRAY);
            temp.setTypeface(null, Typeface.BOLD);

            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(0);
            gd.setStroke(1, 0xFF000000);
            temp.setBackground(gd);

            temp.setText(cursor.getColumnName(x));
            tr_head.addView(temp);
        }

        table.addView(tr_head);

        cursor.moveToFirst();

        for (int y =0; y < cursor.getCount(); y++) {

            TableRow tr_data = new TableRow(this);

            for (int z = 0; (z < cursor.getColumnCount()); z++) {

                TextView temp2 = new TextView(this);
                temp2.setPadding(10, 10, 10, 10);
                temp2.setTextSize(15);

                GradientDrawable gd2 = new GradientDrawable();
                gd2.setCornerRadius(0);
                gd2.setStroke(1, 0xFF000000);
                temp2.setBackground(gd2);

                temp2.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(z))));
                tr_data.addView(temp2);


            }
            table.addView(tr_data);
            cursor.moveToNext();

        }

    }//end of showdata method


    public void Close(final View view){

        super.finish();

    }//end of close method



}//end of class
