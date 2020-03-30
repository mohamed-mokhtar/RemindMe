package com.remindme;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    private EditText rContent ;
    private  CheckBox rImportant ;
    private Dialog dialog;
    private ListView listView;
    private RemindersDbAdapter rAdapter;
    private RemindersSimpleCursorAdapter rCursor;

    //private  RemindersSimpleCursorAdapter Cursor = new RemindersSimpleCursorAdapter(this,R.layout.activity_main,);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view_id);
        rAdapter = new RemindersDbAdapter(this);
        rAdapter.open();
        Cursor cursor = rAdapter.fetchAllReminders();
        String[] from = new String[]{
                RemindersDbAdapter.COL_CONTENT};
        int[] to = new int[]{R.id.row_text};
        rCursor = new RemindersSimpleCursorAdapter(this, R.layout.reminder_item, cursor, from, to, 0);
        listView.setAdapter(rCursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rAdapter.close();
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
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_reminder_dialog);
        dialog.setTitle("NEW REMINDER .");
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
    public void AddReminder(View view)
    {
        rContent = (EditText) dialog.findViewById(R.id.text_reminder);
        rImportant  = (CheckBox) dialog.findViewById(R.id.check_important);
        listView = (ListView)  findViewById(R.id.list_view_id);
        String content = rContent.getText().toString();
        Boolean important = rImportant.isChecked();
        rAdapter.createReminder(content,important);
        Log.w(TAG, "Adding new reminder");
        Cursor c = rAdapter.fetchAllReminders();
        rCursor.changeCursor(c);
        dialog.dismiss();
    }
    public void ExitDialogReminder(View view)
    {
        dialog.dismiss();
        Log.w(TAG, "Cancel adding request");
    }

}
