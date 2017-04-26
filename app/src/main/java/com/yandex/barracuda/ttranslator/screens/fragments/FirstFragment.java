package com.yandex.barracuda.ttranslator.screens.fragments;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yandex.barracuda.ttranslator.R;
import com.yandex.barracuda.ttranslator.translate.DictionaryTask;
import com.yandex.barracuda.ttranslator.translate.TranslateTask;
import com.yandex.barracuda.ttranslator.screens.extra.DBHelper;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Barracuda on 17.04.2017.
 */

public class FirstFragment extends Fragment implements View.OnClickListener {
    private DictionaryTask task;
    private TranslateTask mTask;
    private String t = "";
    private EditText editText;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;

    private Timer timer;
    private final long DELAY = 1000;

    private String spinnerIdFrom;
    private String spinnerIdTo;

    public DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myView = inflater.inflate(R.layout.translate, container, false);
        ImageView imag = (ImageView) myView.findViewById(R.id.doTranslate);
        imag.setOnClickListener(this);
        editText = (EditText) myView.findViewById(R.id.ed_text);
        spinnerFrom = (Spinner) myView.findViewById(R.id.spinner_from);
        spinnerTo = (Spinner) myView.findViewById(R.id.spinner_to);

        dbHelper = new DBHelper(getContext());

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerIdFrom = getResources().getStringArray(R.array.languages_keys)[position];
                if (editText.getText().toString().replace(" ", "").replace("\n", "").length() != 0)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Translate(myView);
                        }
                    });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerIdTo = getResources().getStringArray(R.array.languages_keys)[position];
                if (editText.getText().toString().replace(" ", "").replace("\n", "").length() != 0)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Translate(myView);
                        }
                    });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                System.out.println("PLS");
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Translate(myView);
                            }
                        });
                    }
                }, DELAY);
            }
        });
        return myView;
    }

    @Override
    public void onClick(View v) {
        int a = spinnerFrom.getSelectedItemPosition();
        System.out.println(spinnerFrom.getSelectedItemPosition());
        int b = spinnerTo.getSelectedItemPosition();
        String flag = spinnerIdFrom;
        spinnerIdFrom = spinnerIdTo;
        spinnerIdTo = flag;
        spinnerFrom.setSelection(b);
        spinnerTo.setSelection(a);
    }

    private void Translate(View v) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        t = editText.getText().toString();

        try {
            mTask = new TranslateTask(new URL(getURLTranlate(t)));
            mTask.execute();

            task = new DictionaryTask(new URL(getURLDict(t)));
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            TextView textView2 = (TextView) v.findViewById(R.id.tranlsate_top);
            textView2.setText(mTask.get());
            TextView textView = (TextView) v.findViewById(R.id.text_translate);
            textView.setText(task.get());
            cv.put("fav_state", 1);
            cv.put("word", t);
            cv.put("translate", textView2.getText().toString());
            cv.put("from_to", (spinnerIdFrom + "-" + spinnerIdTo).toUpperCase());
            db.insert("ttable", null, cv);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getURLDict(String text) {
        text = text.replace(" ", "%20").replace("\n", "%20");
        Resources res = getContext().getResources();
        String url = res.getString(R.string.urlTranslateDict);
        url += (res.getString(R.string.keyDict));
        url += ("&lang=");
        url += (spinnerIdFrom + "-" + spinnerIdTo);
        url += ("&text=");
        url += (text);
        url += ("&ui=ru");
        System.out.println("\n" + url + "\n");
        return url;
    }

    private String getURLTranlate(String text) {
        text = text.replace(" ", "%20").replace("\n", "%20");
        Resources res = getContext().getResources();
        String url = res.getString(R.string.urlTranslate);
        url += (res.getString(R.string.keyTrans));
        url += ("&text=");
        url += (text);
        url += ("&lang=");
        url += (spinnerIdFrom + "-" + spinnerIdTo);
        return url;
    }

}