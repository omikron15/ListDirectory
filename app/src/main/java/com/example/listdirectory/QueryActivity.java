package com.example.listdirectory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.*;

import javax.xml.transform.Result;


public class QueryActivity extends AppCompatActivity {

    //variable put in for on click of custom adapter
    final Context context = this;

    //Declares String variable which will be used to store the location of the DB file of the current
    String DB_PATH = null;
    //
    String CERES_Directory;
    //
    String selected_Project;
    //
    int System_Type;

    //
    File db_File;

    //Declares SQLite db object
    SQLiteDatabase db;

    //Declares listview objects
    ListView InputListView;
    ListView OutputListView;
    ListView RelationshipListView;


    //Declares cursors used to hold DB table information
    Cursor Input_Cursor;
    Cursor Output_Cursor;
    Cursor Relationship_Cursor;
    Cursor Versions_Cursor;

    //Declares String Arrays to hold field names for each table

    //string inputs field commetned out for experiment with queryItem
    //String[] Input_Fields;
    QueryItem Input_Fields;


    QueryItem Output_Fields;
    QueryItem Relationship_Fields;

    String[] Versions_Fields;

    //Declares String arrays to hold query Strings
    //String[] Input_Query;

    //Declares array adapters used to populate list views with field names from relevant tables
    //Dont think a versions one is required but should revisit later?
    // ArrayAdapter Inputs_Field_adapter;

    customAdapter Inputs_Field_adapter;
    customAdapter Output_Field_adapter;
    customAdapter Relationship_Field_adapter;

    //adapter required for versions ?

    //Declare String object to hold input query string
    String Input_Query_String;
    String Output_Query_String;
    String Relationship_Query_String;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        //Retrieves data from MainActivity
        Intent intent_name = getIntent();
        CERES_Directory = intent_name.getStringExtra("Directory");
        selected_Project = intent_name.getStringExtra("Project");
        System_Type = intent_name.getIntExtra("System Type", 0);

        //Defines text views for referencing text displays
        TextView text2 = (TextView) findViewById(R.id.Text2);
        TextView text4 = (TextView) findViewById(R.id.Text4);



        //DB_Path is set to point at current projects DB file.
        //This is made up of the the CERES directory + the selected project + the DB file name
        //The DB file name should always be the name of the project with the extension .db
        DB_PATH = CERES_Directory + "/" + selected_Project + "/" + selected_Project + ".db";

        //Set display text to show Selected system and system type
        text2.setText(selected_Project);

        if(System_Type == 0 ){
            text4.setText("ESD");
        }else {
            text4.setText("F&G");
        }

    }

    public void DBConnection(final View view) {

        //Listview variables matched to GUI listview names
        InputListView = (ListView) findViewById(R.id.InputListView);
        OutputListView = (ListView) findViewById(R.id.OutputListView);
        RelationshipListView = (ListView) findViewById(R.id.RelationshipListView);

        //used to verify if DB file exists, if yes then continue with query code, if no then throw error message
        db_File = new File(DB_PATH);
        if(db_File.exists()){

            //final parameter is set to 1 which indicates read only. When set to 0 (read/write) the code will not run
            //possibly due to insufficient permissions in manifest
            //read only access is all that this app requires
            db = SQLiteDatabase.openDatabase(DB_PATH, null, 1);
            Toast.makeText(getBaseContext(), "Connected!", Toast.LENGTH_LONG).show();

            //Functions here to split depending on if system ESD or F&G
            //Flag parameter to be obtained from main activity.

            if(System_Type == 0 || System_Type == 1  )  {


                //Loads cursors with all data from ESD Tables
                Input_Cursor = db.rawQuery("SELECT * FROM INPUTS", null);
                Output_Cursor = db.rawQuery("SELECT * FROM OUTPUTS", null);
                Relationship_Cursor = db.rawQuery("SELECT * FROM RELATIONSHIP", null);
                Versions_Cursor = db.rawQuery("SELECT * FROM Versions", null);

                //Input_Fields = Input_Cursor.getColumnNames();

                //attempt 1
                //Input_Fields.Field_Names = Input_Cursor.getColumnNames();

                //for (int i = 0; i < (Input_Cursor.getCount() -1); i++ ){
                //     Input_Query[i] = " ";
                //}
                String[] Input_Query = new String[Input_Cursor.getCount()];
                String[] Output_Query = new String[Output_Cursor.getCount()];
                String[] Relationship_Query = new String[Relationship_Cursor.getCount()];

                Input_Fields = new QueryItem(Input_Cursor.getColumnNames(),Input_Query);
                Output_Fields = new QueryItem(Output_Cursor.getColumnNames(),Output_Query);
                Relationship_Fields = new QueryItem(Relationship_Cursor.getColumnNames(),Relationship_Query);

                //
                Versions_Fields = Versions_Cursor.getColumnNames();

                //Working display code
                //Inputs_Field_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Input_Fields);
                //InputListView.setAdapter(Inputs_Field_adapter);

                Inputs_Field_adapter = new customAdapter(this, Input_Fields);
                InputListView.setAdapter(Inputs_Field_adapter);


                Output_Field_adapter = new customAdapter(this, Output_Fields);
                OutputListView.setAdapter(Output_Field_adapter);

                Relationship_Field_adapter = new customAdapter(this, Relationship_Fields);
                RelationshipListView.setAdapter(Relationship_Field_adapter);

                Toast.makeText(getBaseContext(), "ESD Fields", Toast.LENGTH_LONG).show();



            }else if(System_Type == 1){
                //F&G function is possibly no longer required?
                //now that fire area and fire zones are not going to be included as tables to be queried
                //FAG_Load();



               //Inputs_Field_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Input_Fields);
               //InputListView.setAdapter(Inputs_Field_adapter);

               // Output_Field_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Output_Fields);
                //OutputListView.setAdapter(Output_Field_adapter);

                //Relationship_Field_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Relationship_Fields);
               // RelationshipListView.setAdapter(Relationship_Field_adapter);


               // Toast.makeText(getBaseContext(), "F&G Fields", Toast.LENGTH_LONG).show();

            }

        }else{

            Toast.makeText(getBaseContext(), "ERROR! System Type not defined", Toast.LENGTH_LONG).show();

        } // end of if statement for db file exists verification

        //code for opening user input prompt
        InputListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptView = li.inflate(R.layout.user_input, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                final EditText userinput = (EditText) promptView.findViewById(R.id.User_Input_Thing);
                final TextView Prompt_Title = (TextView) promptView.findViewById(R.id.Prompt_Title);


                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //example shows the ok button being pressed and the text being displayed in a box.
                        //code to change adapter value for field 2 should go in here?

                        Input_Fields.Query_String[position] = (userinput.getText()).toString();
                        Input_Fields.changeNumber ++;
                        // Inputs_Field_adapter.notifyDataSetChanged();


                        Toast.makeText(getBaseContext(), userinput.getText(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

                //Creates dialog box, sets the title of the box to the name of the field that was clicked then shows the dialog box
                AlertDialog alertDialog = alertDialogBuilder.create();
                Prompt_Title.setText(Input_Fields.Field_Names[position]);
                alertDialog.show();

            }


        });


        OutputListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptView = li.inflate(R.layout.user_input, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                final EditText userinput = (EditText) promptView.findViewById(R.id.User_Input_Thing);
                final TextView Prompt_Title = (TextView) promptView.findViewById(R.id.Prompt_Title);


                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //example shows the ok button being pressed and the text being displayed in a box.
                        //code to change adapter value for field 2 should go in here?

                        Output_Fields.Query_String[position] = (userinput.getText()).toString();
                        Output_Fields.changeNumber ++;
                        // Inputs_Field_adapter.notifyDataSetChanged();


                        Toast.makeText(getBaseContext(), userinput.getText(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

                //Creates dialog box, sets the title of the box to the name of the field that was clicked then shows the dialog box
                AlertDialog alertDialog = alertDialogBuilder.create();
                Prompt_Title.setText(Output_Fields.Field_Names[position]);
                alertDialog.show();

            }


        });


        RelationshipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptView = li.inflate(R.layout.user_input, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                final EditText userinput = (EditText) promptView.findViewById(R.id.User_Input_Thing);
                final TextView Prompt_Title = (TextView) promptView.findViewById(R.id.Prompt_Title);


                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //example shows the ok button being pressed and the text being displayed in a box.
                        //code to change adapter value for field 2 should go in here?

                        Relationship_Fields.Query_String[position] = (userinput.getText()).toString();
                        Relationship_Fields.changeNumber ++;

                        // Inputs_Field_adapter.notifyDataSetChanged();


                        Toast.makeText(getBaseContext(), userinput.getText(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

                //Creates dialog box, sets the title of the box to the name of the field that was clicked then shows the dialog box
                AlertDialog alertDialog = alertDialogBuilder.create();
                Prompt_Title.setText(Relationship_Fields.Field_Names[position]);
                alertDialog.show();

            }


        });

    } //end of DB connect function

    //return to main activity
    public void CloseQuery(final View view) {
        super.finish();
    }


    //Code to build query string and execute
    //
    public void RunQuery(final View view){


        Input_Query_String = "SELECT * FROM INPUTS";
            if(Input_Fields.hasQuery()) {
                Input_Query_String = CreateQuery(Input_Query_String, Input_Fields);
            }

        Output_Query_String = "SELECT * FROM OUTPUTS";
            if(Output_Fields.hasQuery()) {
                Output_Query_String = CreateQuery(Output_Query_String, Output_Fields);
            }

        Relationship_Query_String = "SELECT * FROM RELATIONSHIP";
            if(Relationship_Fields.hasQuery()) {
                Relationship_Query_String = CreateQuery(Relationship_Query_String, Relationship_Fields);
            }

        //code to open Results activity

        //New intent created to open the query activity
        Intent intent_Results = new Intent(this, ResultsActivity.class);

        //db_File variable is set up to be passed to the Results activity
        intent_Results.putExtra("DB_PATH", DB_PATH);
        intent_Results.putExtra("Input_Query_String", Input_Query_String);
        intent_Results.putExtra("Output_Query_String", Output_Query_String);
        intent_Results.putExtra("Relationship_Query_String", Relationship_Query_String);

        //The Query Activity is started/called
        startActivity(intent_Results);
    }

    public String CreateQuery(String Qstring, QueryItem Qitem ){

        String dataString = Qstring;
        QueryItem item = Qitem;
        boolean isfirst = true;

        for(int i=0; i<item.getCount(); i++){

            if (item.Query_String[i] != null ){

                if (isfirst != true){
                        dataString = dataString + "AND ";
                        dataString = dataString + item.Field_Names[i] + " ='" + item.Query_String[i].trim() + "' ";
                    }else {
                        dataString = dataString + " WHERE " + item.Field_Names[i] + " ='" + item.Query_String[i].trim() + "' ";
                        isfirst = false;
                    }
            }

        }

        return dataString;

    }//end of createQuery method

} //end of class
