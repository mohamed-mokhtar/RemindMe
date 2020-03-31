package com.remindme;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    private EditText rContent ;
    private  CheckBox rImportant ;
    private Dialog dialog;
    private ListView listView;
    private RemindersDbAdapter rAdapter;
    private RemindersSimpleCursorAdapter rCursor;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View v,  int position, long id) {
                final int pos = parent.getPositionForView(v);
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.edit_reminder, popup.getMenu());
                popup.show();
                final View current=v;
                final int rId=(int) rCursor.getItemId(position);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Reminder z=rAdapter.fetchReminderById(rId);
                        switch(item.getItemId()){
                            case R.id.action_edit:
                                dialog = new Dialog(MainActivity.this);
                                dialog.setContentView(R.layout.add_reminder_dialog);
                                TextView title=dialog.findViewById(R.id.text_title);
                                title.setText("EDIT REMINDER");
                                TextView reminderText=dialog.findViewById(R.id.text_reminder);
                                TextView t=current.findViewById(R.id.row_text);
                                reminderText.setText(t.getText() );
                                Button commitEdit=dialog.findViewById(R.id.btn_commit);
                                CheckBox imp=dialog.findViewById(R.id.check_important);
                                if(z.getImportant()==1)
                                    imp.setChecked(true);
                                commitEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        updateReminder(rId);
                                    }
                                });
                                Button cancelEdit=dialog.findViewById(R.id.btn_cancel);
                                cancelEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                return true;
                            case R.id.action_delete:
                                //dialog warning
                                dialog = new Dialog(MainActivity.this);
                                dialog.setContentView(R.layout.delete_warning);
                                dialog.show();
                                Button no=dialog.findViewById(R.id.buttonno);
                                Button yes=dialog.findViewById(R.id.buttonyes);
                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DeleteReminder(rId);
                                    }
                                });
                                no.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    //On click function
                                    public void onClick(View view) {
                                        dialog.dismiss();

                                    }
                                });}
                                return true;

                    }


        });
    }});
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
        dialog.setTitle("NEW REMINDER");
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
    public void updateReminder(int rId)
    {
        rContent = (EditText) dialog.findViewById(R.id.text_reminder);
        rImportant  = (CheckBox) dialog.findViewById(R.id.check_important);
        listView = (ListView)  findViewById(R.id.list_view_id);
        String content = rContent.getText().toString();
        Boolean important = rImportant.isChecked();
        Reminder z=rAdapter.fetchReminderById(rId);
        z.setContent(content);
        int important01;
        if(important==true)
            important01=1;
        else important01=0;
        z.setImportant(important01);
        rAdapter.updateReminder(z);
        Log.w(TAG, "updating  reminder");
        Cursor c = rAdapter.fetchAllReminders();
        rCursor.changeCursor(c);
        dialog.dismiss();
    }
    public void DeleteReminder(int rId)
    {
        rContent = (EditText) dialog.findViewById(R.id.text_reminder);
        rImportant  = (CheckBox) dialog.findViewById(R.id.check_important);
        listView = (ListView)  findViewById(R.id.list_view_id);
        rAdapter.deleteReminderById(rId);
        Log.w(TAG, "deleting  reminder");
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
