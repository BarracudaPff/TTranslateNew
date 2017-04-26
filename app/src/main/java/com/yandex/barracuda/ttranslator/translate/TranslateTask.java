package com.yandex.barracuda.ttranslator.translate;

import android.os.AsyncTask;

import java.net.URL;
import java.util.Scanner;

/**
 * Created by Barracuda on 20.04.2017.
 */

public class TranslateTask extends AsyncTask<Void, Void, String> {
    private URL url;

    public String out;

    public TranslateTask(URL url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... urls) {
        try {
            out = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
            int start = out.indexOf("[\"") + 2;
            int end = out.indexOf("\"]");
            out = out.substring(start, end);
            System.out.println("OUT "+out);
        } catch (Exception e) {
            System.out.println("XML Parsing Excpetion = " + e);
            return null;
        }
        return out;
    }

    @Override
    protected void onPostExecute(String getResponse) {
        super.onPostExecute(out);
        System.out.println("Done Good: TranslateTask");
    }
}
