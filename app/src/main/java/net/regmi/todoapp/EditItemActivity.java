package net.regmi.todoapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private final String LOG_TAG = EditItemActivity.class.getSimpleName();

    private EditText etAddItemEditText;
    private Button btnEditSave;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("EditItemActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        displaySelectedItem();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get a reference to Save Button and add handler for save button
        ((Button)findViewById(R.id.btnEditSave)).setOnClickListener(new SaveButtonOnClickListener());
    }

    private void displaySelectedItem() {
        etAddItemEditText = (EditText)findViewById(R.id.etAddItemEditText);
        String text_item = getIntent().getStringExtra(getString(R.string.INTENT_EXTRA_ITEM_TEXT));
        itemPosition = getIntent().getIntExtra(getString(R.string.INTENT_EXTRA_ITEM_POSITION), 0);
        Log.v(LOG_TAG, "Text in the Intent: " + text_item);
        Log.v(LOG_TAG, "Position in the Intent: " + itemPosition);

        etAddItemEditText.setText(text_item);

        // Put the cursor at the end of the EditText
        etAddItemEditText.setSelection(text_item.length());

        //etAddItemEditText.requestFocus(); //Set in xml by default
    }

    private class SaveButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent data = new Intent();
            String editedText = etAddItemEditText.getText().toString();
            data.putExtra(getString(R.string.INTENT_EXTRA_ITEM_EDITED_TEXT), editedText);
            data.putExtra(getString(R.string.INTENT_EXTRA_ITEM_EDITED_TEXT_POSITION), itemPosition);
            Log.v(LOG_TAG, "Sending intent back to parent activity - intent Extra param: " + editedText + " position:" + itemPosition);
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }
}
