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

    private ArrayList<File> fileList = new ArrayList<File>();
    private LinearLayout LinearView;

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

        ListView ListView1 = (ListView) findViewById(R.id.ListView1);
        final ListView ListView2 = (ListView) findViewById(R.id.ListView2);
        final Button button2 = (Button) findViewById(R.id.Button2);
        final Button button3 = (Button) findViewById(R.id.Button3);
        final Button button4 = (Button) findViewById(R.id.Button4);


        //Directory 1 = getRootDirectory;
       // File root;
        //root = Environment.getRootDirectory(); //returns" /system/"
       // String[] rootList = root.list();
        //File[] rootListFiles = root.listFiles();

        //String[] File_List;
        //File_List = root.list();

        //Directory 2 = getExternalStorageDirectory()
       // File root; //returns "/storage/emulated/0"
      //  root = Environment.getExternalStorageDirectory();
      //  String[] rootList = root.list();
      //  File[] rootListFiles = root.listFiles();

      //  String[] File_List;
       // File_List = root.list();

       // Directory 3 = getDataDirectory()
       // File root;
       // root = Environment.getDataDirectory(); //returns "/data"
       // String[] rootList = root.list();
       // File[] rootListFiles = root.listFiles();

       // String[] File_List;
       // File_List = root.list();

        //Directory 4 - manual input of SD card directory

        CERES_File = new File(CERES_Directory);

        //Function listFiles() will return file objects of full path. (For both files and folders)
        //File[] rootListFiles;
        //rootListFiles = CERES_File.listFiles();

        //Function list() will return string objects of file name only. (For both files and folders)
        //In this case the string array will contain a list of the files and folder in the main CERES directory
        //Project_List = CERES_File.list();

        //improved code will populate Project_List with only folders. Files are no longer included.
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

        ListView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected_Project = ((TextView)view).getText().toString();

                //Set ESD/F&G flag
                //if project cannot be resoled to ESD or F&G then set error flag and reset system type to null
                if(selected_Project.contains("ESD")){
                    System_Type = 0;
                }else if (selected_Project.contains("FAG") || selected_Project.contains("FIRE")) {
                    System_Type = 1;
                }else{
                    System_Type = null;
                    Systems_Type_Error = true;
                }



                Toast.makeText(getBaseContext(), selected_Project, Toast.LENGTH_LONG).show();

                CE_Path = CERES_Directory + "/" + selected_Project + "/Cause and Effects";

                //Toast.makeText(getBaseContext(), CE_Path, Toast.LENGTH_LONG).show();

                //Set CE_File object to directory path selected by user
                CE_File = new File(CE_Path);
                //Populate CE_List with list of files in CE_File directory
                //improved code now only returns PDF files
                CE_List = CE_File.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                       // return new File(dir, filename).
                        return filename.endsWith(".pdf");
                    }
                });

                //button 2 is only set visible once a project has been selected from list view 1
                //Buttons 3 and 4 are made invisible. This removes chances of errors if the user has already loaded a project.
                button2.setVisibility(view.VISIBLE);
                button3.setVisibility(view.INVISIBLE);
                ListView2.setVisibility(view.INVISIBLE);

                //check to ensure that system type has been defined by evaluating Systems_Type_Error
                //Query button should not be available if the error is present
                if(Systems_Type_Error){
                    button4.setVisibility(view.INVISIBLE);
                }else{
                    button4.setVisibility(view.VISIBLE);
                }


            }
        });


    }


    public void ShowFiles(final View view) {

        ListView ListView2 = (ListView) findViewById(R.id.ListView2);
        final Button button3 = (Button) findViewById(R.id.Button3);
        final Button button4 = (Button) findViewById(R.id.Button4);

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CE_List);

        ListView2.setAdapter(adapter2);
        ListView2.setVisibility(view.VISIBLE);

        //ListView1.setOnItemClickListener(new OnItemClickListener() {

        ListView2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                selected_CE = ((TextView)view).getText().toString();


                button3.setVisibility(view.VISIBLE);


                Toast.makeText(getBaseContext(), selected_CE, Toast.LENGTH_LONG).show();




            }
        });

        }

    public void OpenPDF(final View view) {

        Intent target = new Intent(Intent.ACTION_VIEW);

        File test = new File(CE_Path + "/" + selected_CE);

        target.setDataAndType(Uri.fromFile(test), "application/pdf");

        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");

        startActivity(intent);

    }

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


    }


    //End of Class
    }





