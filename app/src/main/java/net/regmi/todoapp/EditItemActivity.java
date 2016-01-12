package net.regmi.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    EditText etAddItemEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("EditItemActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etAddItemEditText = (EditText)findViewById(R.id.etAddItemEditText);
        String text_item = getIntent().getStringExtra(getString(R.string.INTENT_EXTRA_ITEM_TEXT));
        int position = getIntent().getIntExtra(getString(R.string.INTENT_EXTRA_ITEM_POSITION), 0);

        Log.v("EditItemActivity", "Text = " + text_item);
        etAddItemEditText.setText(text_item);
        etAddItemEditText.setSelection(text_item.length());

        //etAddItemEditText.requestFocus(); //Set in xml by default
    }
}
