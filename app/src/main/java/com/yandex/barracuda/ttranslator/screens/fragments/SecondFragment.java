package com.yandex.barracuda.ttranslator.screens.fragments;

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

public class SecondFragment extends Fragment {

    ListAdapter list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.favorites, container, false);

        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ListView view = (ListView) myView.findViewById(R.id.list_fav);
        list = new ListAdapter(getContext(), R.layout.fav_layout_item);

        view.setAdapter(list);

        Cursor c = database.query("ttable", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int favStateColIndex = c.getColumnIndex("fav_state");
            int wordColIndex = c.getColumnIndex("word");
            int translateColIndex = c.getColumnIndex("translate");
            int from_toColIndex = c.getColumnIndex("from_to");

            do {
                RowElement rowElement = new RowElement(c.getInt(favStateColIndex), c.getString(wordColIndex),
                        c.getString(translateColIndex), c.getString(from_toColIndex), c.getInt(idColIndex));
                if (c.getInt(favStateColIndex) == 0)
                    list.add(rowElement);

            } while (c.moveToNext());
        }
        c.close();

        return myView;
    }
}
