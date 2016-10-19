package com.example.listdirectory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
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

    String DB_PATH;

    File db_File;

    ArrayAdapter adapter;

    String[] DrawingNumber;
    String[] SheetNumber;
    String[] Combined;

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

                Cursor VersionQuery;
                String VersionQueryString;

                VersionQueryString = "SELECT DrawingNo, SheetNo, Revision, Details, Drawnby, Checkedby, RevDate FROM VERSIONS WHERE DrawingNo like '" + DrawingNumber[position].trim() + "' AND SheetNo like'" + SheetNumber[position].trim() + "'";


                VersionQuery = db.rawQuery(VersionQueryString, null);


            }//end of on item click method

        });//end of on click listener


    }//end of ShowDrawings method


}//end of activity
