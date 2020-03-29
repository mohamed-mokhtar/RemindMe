package com.remindme;

import android.os.Bundle;
import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    //RemindersDbAdapter RDBA = new RemindersDbAdapter(this);
    //TextView reminderContent  = (TextView) findViewById(R.id.text_reminder);
    //TextView reminderImportant  = (CheckBox) findViewById(R.id.check_important);
    // ListView listView = (ListView)  findViewById(R.id.list_view);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_reminder_dialog);
        dialog.setTitle("NEW REMINDER .");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            dialog.show();
            return true;
        }
        else if (id == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void AddReminder()
    {
        //String content = reminderContent.getText().toString();
        //Boolean important = reminderImportant.isEnabled();
        //RDBA.createReminder(content,important);
        // Log.w(TAG, "HIIIII fROM rEM");
    }
    public void CancelDialog()
    {
        // dialog.cancel();
    }
}
