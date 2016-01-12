package net.regmi.todoapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    /*
      Also take a look at http://developer.android.com/guide/topics/ui/layout/listview.html
     */
    private ArrayList<String> todoItems;
    private ArrayAdapter<String> todoAdapter;
    private ListView listViewItems;
    private EditText etAddItemEditText;
    //public static final String INTENT_EXTRA_ITEM_TEXT = "";
    //public static final String INTENT_EXTRA_ITEM_TEXT_ON_SAVE = "";
    public static final int INTENT_ITEM_REQUEST_CODE = 0x01;
    public static final int INTENT_ITEM_RESULT_CODE = 0x02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate() - savedInstanceState " + savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // populate the ArrayList
        populateArrayItems();

        // get the List reference of View from Layout Resources
        listViewItems = (ListView) findViewById(R.id.lvItems);

        // Set the list view to the ArrayList adapter
        listViewItems.setAdapter(todoAdapter);

        // Get reference of Edit Text from Layout Resources
        etAddItemEditText = (EditText) findViewById(R.id.etAddItemEditText);

        //Add Long Action Listener for a list view item to delete an item.
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                todoAdapter.notifyDataSetChanged();
                writeItemsToFile();
                return false;
            }
        });

        // Add Click Listener to open an activity to edit selected item
        listViewItems.setOnItemClickListener(new ListViewOnItemClickListener());
    }


    private class ListViewOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = parent.getItemAtPosition(position).toString();
            Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
            intent.putExtra(getString(R.string.INTENT_EXTRA_ITEM_TEXT), selectedItem);
            intent.putExtra(getString(R.string.INTENT_EXTRA_ITEM_POSITION), position);
            Log.v(LOG_TAG, "Before Starting Activity Text = " + selectedItem);
            try {
                //startActivity(i);
                startActivityForResult(intent, INTENT_ITEM_REQUEST_CODE);
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INTENT_ITEM_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_CANCELED) {
                Log.v(LOG_TAG, "Error in sending the result from EditItemActivity");
                return;
            }

            if(resultCode == Activity.RESULT_OK) {
                String editedText =
                        data.getExtras()
                            .getString(getString(R.string.INTENT_EXTRA_ITEM_EDITED_TEXT));
                int editedTextPosInListView = data.getExtras()
                                                  .getInt(getString(R.string.INTENT_EXTRA_ITEM_EDITED_TEXT_POSITION));
                Log.v(LOG_TAG, "Edited Text =" + editedText + " position = " + editedTextPosInListView);
                todoItems.set(editedTextPosInListView, editedText);
                todoAdapter.notifyDataSetChanged();
                writeItemsToFile();
            }
        }
    }

    public void populateArrayItems() {

        todoItems = new ArrayList<String>();
        /*
        todoItems.add("Items 1");
        todoItems.add("Items 2");
        todoItems.add("Items 3");
        */
        readItemsFromFile();
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItemsFromFile() {
        File filesDir = getFilesDir();
        File file = new File( filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<String >(FileUtils.readLines(file));
        } catch (IOException ex)  {
            ex.printStackTrace();
        }
    }

    private void writeItemsToFile() {
        File filesDir = getFilesDir();
        File file = new File( filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file,todoItems);
        } catch (IOException ex)  {
            ex.printStackTrace();
        }
    }

    public void onAddItem(View view) {

        // Add new text item to our ArrayList
        todoItems.add(etAddItemEditText.getText().toString());

        // Notify the adapter about addition of new data
        // so that the list view gets updated.
        todoAdapter.notifyDataSetChanged();

        // Clear the Text in the Textbox so that user can enter new item.
        etAddItemEditText.setText("");

        // Write to the file todo.txt in the current directory
        writeItemsToFile();
    }
}
