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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class VersionActivity extends AppCompatActivity {

    //Declares String variable which specifies CERES project diretory on SD card. This value is obtained from the main activity.
    String CERES_Directory;

    //Declares String variable which specifies the selected project. This value is obtained from the main activity.
    String Selected_Project;

    //Declares SQLite db object
    SQLiteDatabase db;

    //Declares version cursor for DB results.
    Cursor Versions_Cursor;

    TableLayout table;
    ListView listview;
    TableRow tr_head;
    Button RevisionButton;

    String DB_PATH;

    File db_File;

    ArrayAdapter adapter;

    String[] DrawingNumber;
    String[] SheetNumber;
    String[] Combined;



    Cursor VersionQuery;
    String VersionQueryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        Intent intent_Version = getIntent();

        CERES_Directory = intent_Version.getStringExtra("Directory");
        Selected_Project = intent_Version.getStringExtra("Project");



    }//end of on create

    public void ShowDrawings(final View view) {

        listview = (ListView) findViewById(R.id.DrawingView);
        table = (TableLayout) findViewById(R.id.VersionTable);
        RevisionButton = (Button) findViewById(R.id.ShowRevisionsButton);

        //ListView ListView1 = (ListView) findViewById(R.id.ListView1);
        DB_PATH = CERES_Directory + "/" + Selected_Project + "/" + Selected_Project + ".db";

        db_File = new File(DB_PATH);
        if(db_File.exists()){
            db = SQLiteDatabase.openDatabase(DB_PATH, null, 1);

            Versions_Cursor = db.rawQuery("SELECT * FROM VERSIONS", null);
            Versions_Cursor.moveToFirst();

            DrawingNumber = new String[Versions_Cursor.getCount()];
            SheetNumber = new String[Versions_Cursor.getCount()];
            Combined = new String[Versions_Cursor.getCount()];

            for (int x=0; x < Versions_Cursor.getCount(); x++ ){
                DrawingNumber[x] = Versions_Cursor.getString(Versions_Cursor.getColumnIndex("DrawingNo"));
                SheetNumber[x] = Versions_Cursor.getString(Versions_Cursor.getColumnIndex("SheetNo"));
                Combined[x] = DrawingNumber[x] + " Sheet No. " + SheetNumber[x];
                Versions_Cursor.moveToNext();

                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Combined);
                listview.setAdapter(adapter);




            }

        }else{

            //Error handling for if DB file cant be opened

        }//end of if

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                VersionQueryString = "SELECT DrawingNo, SheetNo, Revision, Details, Drawnby, Checkedby, RevDate FROM VERSIONS WHERE DrawingNo like '" + DrawingNumber[position].trim() + "' AND SheetNo like'" + SheetNumber[position].trim() + "'";


                VersionQuery = db.rawQuery(VersionQueryString, null);

                Toast.makeText(getBaseContext(), Combined[position], Toast.LENGTH_LONG).show();

                RevisionButton.setVisibility(View.VISIBLE);


            }//end of on item click method

        });//end of on click listener


    }//end of ShowDrawings method

    public void ShowRevisions(final View view) {

        if (table != null){
            table.removeAllViews();
        }

        tr_head = new TableRow(this);

        for (int x =0; x < VersionQuery.getColumnCount(); x++){

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

            temp.setText(VersionQuery.getColumnName(x));
            tr_head.addView(temp);
        }

        table.addView(tr_head);

        VersionQuery.moveToFirst();

        for (int y =0; y < VersionQuery.getCount(); y++) {

            TableRow tr_data = new TableRow(this);

            for (int z = 0; (z < VersionQuery.getColumnCount()); z++) {

                TextView temp2 = new TextView(this);
                temp2.setPadding(10, 10, 10, 10);
                temp2.setTextSize(15);

                GradientDrawable gd2 = new GradientDrawable();
                gd2.setCornerRadius(0);
                gd2.setStroke(1, 0xFF000000);
                temp2.setBackground(gd2);

                temp2.setText(VersionQuery.getString(VersionQuery.getColumnIndex(VersionQuery.getColumnName(z))));
                tr_data.addView(temp2);


            }
            table.addView(tr_data);
            VersionQuery.moveToNext();

        }

    }//end of showRevisions method

    public void CloseVersions(final View view) {

        super.finish();

    }

}//end of activity
