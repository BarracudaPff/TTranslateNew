package com.yandex.barracuda.ttranslator.screens.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yandex.barracuda.ttranslator.R;
import com.yandex.barracuda.ttranslator.screens.extra.DBHelper;
import com.yandex.barracuda.ttranslator.screens.extra.ListAdapter;
import com.yandex.barracuda.ttranslator.screens.extra.RowElement;

/**
 * Created by Barracuda on 17.04.2017.
 */

public class ThirdFragment extends Fragment {

    ListAdapter list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        final View myView = inflater.inflate(R.layout.favorites, container, false);
        ListView view = (ListView) myView.findViewById(R.id.list_fav);
        list = new ListAdapter(getContext(), R.layout.fav_layout_item);

        view.setAdapter(list);
//region cursor
        Cursor c = database.query("ttable", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int favStateColIndex = c.getColumnIndex("fav_state");
            int wordColIndex = c.getColumnIndex("word");
            int translateColIndex = c.getColumnIndex("translate");
            int from_toColIndex = c.getColumnIndex("from_to");

            do {
                System.out.println(c.getInt(favStateColIndex));
                RowElement rowElement = new RowElement(c.getInt(favStateColIndex), c.getString(wordColIndex),
                        c.getString(translateColIndex), c.getString(from_toColIndex), c.getInt(idColIndex));
                list.add(rowElement);

            } while (c.moveToNext());
        }
        c.close();
//endregion cursor
        return myView;
    }
}
