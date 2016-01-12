package net.regmi.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> todoAdapter;
    ListView listViewItems;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // populate the ArrayList
        populateArrayItems();

        // get the List reference of View from Layout Resources
        listViewItems = (ListView) findViewById(R.id.lvItems);

        // Set the list view to the ArrayList adapter
        listViewItems.setAdapter(todoAdapter);

        // Get reference of Edit Text from Layout Resources
        editText = (EditText) findViewById(R.id.etEditText);

        //Add Long Action Listener for a list view item.
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                todoAdapter.notifyDataSetChanged();
                writeItemsToFile();
                return false;
            }
        });

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
        todoItems.add(editText.getText().toString());

        // Notify the adapter about addition of new data
        // so that the list view gets updated.
        todoAdapter.notifyDataSetChanged();

        // Clear the Text in the Textbox so that user can enter new item.
        editText.setText("");

        // Write to the file todo.txt in the current directory
        writeItemsToFile();
    }
}
