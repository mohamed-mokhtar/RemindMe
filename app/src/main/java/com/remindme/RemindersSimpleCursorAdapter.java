package com.remindme;

import android.content.Context;
import android.database.Cursor;
import androidx.core.content.ContextCompat;
import android.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by engMa_000 on 2017-04-03.
 */

public class RemindersSimpleCursorAdapter extends SimpleCursorAdapter {

    public RemindersSimpleCursorAdapter(Context context, int layout, Cursor c, String[]
            from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
    //to use a viewholder, you must override the following two methods and define a ViewHolder class
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.colImp = cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_IMPORTANT);
            holder.listTab = view.findViewById(R.id.list_view);
            view.setTag(holder);
        }
        if (cursor.getInt(holder.colImp) > 0) {
            holder.listTab.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
        } else {
            holder.listTab.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
        }
    }
    static class ViewHolder {
        //store the column index
        int colImp;
        //store the view
        View listTab;
    }

}