package com.example.listdirectory;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.widget.AdapterView.*;

public class MainActivity extends AppCompatActivity {

    //Following lines have been commented out because i dont know what they are for. Can be deleted if testing runs fine.
    //private ArrayList<File> fileList = new ArrayList<File>();
    //private LinearLayout LinearView;

    //Declares a string object which stores the hard coded location of the CERES directory on the SD card
    final String CERES_Directory = "/storage/7612-68F3/CERES";

    //Declares a file object called CERES_Files which points at the CERES folder on the SD card.
    File CERES_File;

    //Declares a string array list which will be used to store a list of all the projects in the CERES folder
    String[] Project_List;

    //Declares an ArrayAdapter which is used to populate listview with all the projects in the CERES folder
    ArrayAdapter adapter;

    //Declares string object which will hold the project selected by the user
    String selected_Project;

    //Declares string object CE_Path which will contain the string  path to the C&E directory of the selected project
    String CE_Path;

    //Declare String array CE_list to store list of C&E files
    String[] CE_List;

    //Declare File object CE_File. This is the C&E directory as a file object
    File CE_File;

    //Declares an ArrayAdapter which is used to populate listview2 with all the PDFs in the C&E directory of the selected project
    ArrayAdapter adapter2;

    //Declares String variable to hold value of selected PDF file
    String selected_CE;

    //Declares a integer variable to determine the type of system
    //0 = ESD, 1=F&G
    Integer System_Type;

    //Declare a boolean variable to indicate a system type error
    //usually this will indicate that the system type cannot be resoled to ESD or F&G
    //false = no error, true = error
    boolean Systems_Type_Error = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //Button will show all the projects  in the CERES Directory on the SD CARD
    public void ShowFolder(final View view) {

        //Locates all relevant design objects
        ListView ListView1 = (ListView) findViewById(R.id.ListView1);
        final ListView ListView2 = (ListView) findViewById(R.id.ListView2);
        final Button button2 = (Button) findViewById(R.id.Button2);
        final Button button3 = (Button) findViewById(R.id.Button3);
        final Button button4 = (Button) findViewById(R.id.Button4);
        final Button VersionButton = (Button) findViewById(R.id.VersionButton);

        //Creates file object using CERES location.
        //Will be used to verify that the pathway exists (i.e. that the CERES folder exists)
        //Also used for returning list of project names.
        CERES_File = new File(CERES_Directory);

        //TODO - should have built in error handling for if CERES folder is not located
        //Next step may crash app if no folders exist.

        //Populate Project_List array with list of folders
        Project_List = CERES_File.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return new File(dir, filename).isDirectory();
            }
        });

        //code to sort projects list alphabetically
        Arrays.sort(Project_List);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Project_List);
        ListView1.setAdapter(adapter);

        //Code dictates what happens when the user selects an item from the project list
        ListView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected_Project = ((TextView)view).getText().toString();

                //Set ESD/F&G flag
                //if project cannot be resoled to ESD or F&G then set error flag and reset system type to null
                if(selected_Project.contains("ESD")){
                    System_Type = 0; //Project type is set to ESD
                }else if (selected_Project.contains("FAG") || selected_Project.contains("FIRE")) {
                    System_Type = 1; //Project type is set to F&G
                }else{ // System type cannot be determined
                    System_Type = null; //Project type set to null
                    Systems_Type_Error = true; //System error flag set to true
                }

                Toast.makeText(getBaseContext(), selected_Project, Toast.LENGTH_LONG).show();

                //sets location of C&E directory for selected project
                CE_Path = CERES_Directory + "/" + selected_Project + "/Cause and Effects";

                //Set CE_File object to directory path selected by user
                CE_File = new File(CE_Path);
                //Populate CE_List with list of files in CE_File directory
                //improved code now only returns PDF files
                CE_List = CE_File.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                                               return filename.endsWith(".pdf");
                    }
                });

                //button 2 is only set visible once a project has been selected from list view 1
                //Buttons 3 and 4 are made invisible. This removes chances of errors if the user has already loaded a project.
                button2.setVisibility(view.VISIBLE);
                button3.setVisibility(view.INVISIBLE);
                VersionButton.setVisibility(view.INVISIBLE);
                ListView2.setVisibility(view.INVISIBLE);

                //check to ensure that system type has been defined by evaluating Systems_Type_Error
                //Query button should not be available if the error is present
                if(Systems_Type_Error){
                    button4.setVisibility(view.INVISIBLE);
                }else{
                    button4.setVisibility(view.VISIBLE);
                    VersionButton.setVisibility(view.VISIBLE);

                }


            }
        });//end of on click method


    }//end of showFolder method


    public void ShowFiles(final View view) {

        //Locates all relevant design objects
        ListView ListView2 = (ListView) findViewById(R.id.ListView2);
        final Button button3 = (Button) findViewById(R.id.Button3);
        final Button button4 = (Button) findViewById(R.id.Button4);

        //adapter set to contain all C&E files
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CE_List);

        //List view is populated by adapter
        ListView2.setAdapter(adapter2);
        //List view is made visible
        ListView2.setVisibility(view.VISIBLE);

        //Code for when items in list view are clicked
        ListView2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //sets variable to the C&E that has been selected
                selected_CE = ((TextView)view).getText().toString();

                //open selected C&E button becomes visible
                button3.setVisibility(view.VISIBLE);

                //Selected C&E name is shown to use in toast
                Toast.makeText(getBaseContext(), selected_CE, Toast.LENGTH_LONG).show();

            }
        });

        }//End of show files method

    public void OpenPDF(final View view) {

        Intent target = new Intent(Intent.ACTION_VIEW);

        //Sets variable to full pathway of selected C&E
        File CE_File = new File(CE_Path + "/" + selected_CE);

        //Sets target to the selected C&E and specifies the type as PDF
        target.setDataAndType(Uri.fromFile(CE_File), "application/pdf");

        //Unsure on purpose
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        //Creates intent for displaying PDF application options
        Intent intent = Intent.createChooser(target, "Open File");

        // runs new intent
        startActivity(intent);

    }//end of Open PDF method

    public void QueryMenu(final View view) {

        //New intent created to open the query activity
        Intent intent_Query = new Intent(this, QueryActivity.class);

        //CERES directory variable is set up to be passed to the Query activity
        intent_Query.putExtra("Directory", CERES_Directory);

        //The selected project variable is set up to be passed to the Query activity
        intent_Query.putExtra("Project", selected_Project);

        //The system type of the selected project is set up to be passed to the Query activity
        intent_Query.putExtra("System Type", System_Type);

        //The Query Activity is started/called
        startActivity(intent_Query);


    }//End of query Menu method

    public void VersionMenu(final View view) {

        //New intent created to open the query activity
        Intent intent_Version = new Intent(this, VersionActivity.class);

        //CERES directory variable is set up to be passed to the Query activity
        intent_Version.putExtra("Directory", CERES_Directory);

        //The selected project variable is set up to be passed to the Query activity
        intent_Version.putExtra("Project", selected_Project);


        //The Query Activity is started/called
        startActivity(intent_Version);


    }//End of query Menu method



    }//End of Class





