package com.yandex.barracuda.ttranslator.screens.extra;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yandex.barracuda.ttranslator.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barracuda on 21.04.2017.
 */

public class ListAdapter extends ArrayAdapter {
    private List list = new ArrayList();

    public ListAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler {
        ImageView star;
        TextView word;
        TextView translate;
        TextView fromto;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View row = convertView;
        final RowElement rowElement = (RowElement) this.getItem(position);
        final DataHandler handler;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.fav_layout_item, parent, false);

            handler = new DataHandler();
            handler.star = (ImageView) row.findViewById(R.id.imageView7);
            handler.word = (TextView) row.findViewById(R.id.itemWord);
            handler.translate = (TextView) row.findViewById(R.id.itemTranslate);
            handler.fromto = (TextView) row.findViewById(R.id.itemFromToLang);
            row.setTag(handler);

        } else {
            handler = (DataHandler) row.getTag();
        }

        handler.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper dbHelper = new DBHelper(getContext());
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor c = database.query("ttable", null, null, null, null, null, null);
                int a = 0;
                //region Find_A
                if (c.moveToFirst()) {
                    int idColIndex = c.getColumnIndex("id");
                    int favStateColIndex = c.getColumnIndex("fav_state");
                    do {
                        if (c.getInt(idColIndex) == rowElement.getId()) {
                            a = c.getInt(favStateColIndex);
                            break;
                        }
                    } while (c.moveToNext());
                }
                c.close();
//endregion
                if (a == 1) a = 0;
                else if (a == 0) a = 1;
                ContentValues values = new ContentValues();
                values.put("fav_state", a);
                database.update("ttable", values, "id = ?",
                        new String[]{Integer.toString(rowElement.getId())});
                if (a == 1)
                    handler.star.setImageResource(R.mipmap.star_yes);
                else if (a == 0)
                    handler.star.setImageResource(R.mipmap.star_no);
            }

        });

        row.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.dialog);
                builder.setNegativeButton("Удалить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DBHelper dbHelper = new DBHelper(getContext());
                                SQLiteDatabase database = dbHelper.getWritableDatabase();
                                database.delete("ttable", "id = " + rowElement.getId(), null);
                                handler.star.setImageResource(R.mipmap.deleted);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });

        if (rowElement.getFav_state() == 1)
            handler.star.setImageResource(R.mipmap.star_yes);
        else if (rowElement.getFav_state() == 0)
            handler.star.setImageResource(R.mipmap.star_no);

        handler.word.setText(rowElement.getWord());
        handler.translate.setText(rowElement.getTranslate());
        handler.fromto.setText(rowElement.getFrom_to());

        return row;
    }

}
