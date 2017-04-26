package com.yandex.barracuda.ttranslator.translate;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Barracuda on 20.04.2017.
 */

public class DictionaryTask extends AsyncTask<Void, Void, SpannableStringBuilder> {
    private URL url;

    private static String STR_ERROR_INTERNET = "Error connection\n";
    private static String STR_ERROR_RUNTIME = "Error connection\n";
    private static String STR_ERROR_EXPLAIN = "\nCheck your internet connection and try again";

    private SpannableText n = new SpannableText();

    public DictionaryTask(URL url) {
        this.url = url;
    }

    @Override
    protected SpannableStringBuilder doInBackground(Void... urls) {

        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(connection.getInputStream());
            doc.getDocumentElement().normalize();

            Node rootN = doc.getDocumentElement().getFirstChild().getNextSibling();

            for (Node child = rootN; child != null; child = child.getNextSibling()) {
                int i = 1;
                getDef(child);
                if (child == rootN)
                    n.getSpan().delete(0, 2);
                for (Node childF = child.getFirstChild().getNextSibling(); childF != null; childF = childF.getNextSibling()) {
                    n.addSpanTitle("\n" + Integer.toString(i) + " ", new StyleSpan(Typeface.BOLD_ITALIC), "");
                    getTrAll(childF);
                    i++;
                }
            }
        } catch (UnknownHostException e) {
            n.getSpan().clear();
            n.getSpan().append(STR_ERROR_INTERNET).append(STR_ERROR_EXPLAIN);
            n.getSpan().setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),1,3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return n.getSpan();
        } catch (ConnectException e) {
            n.getSpan().clear();
            n.getSpan().append(STR_ERROR_RUNTIME).append(STR_ERROR_EXPLAIN);
            n.getSpan().setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),1,3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return n.getSpan();
        } catch (RuntimeException e) {
            n.getSpan().clear();
            n.getSpan().append(STR_ERROR_RUNTIME).append(STR_ERROR_EXPLAIN);
            n.getSpan().setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),1,3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return n.getSpan();
        } catch (Exception e) {
            n.getSpan().clear();
            return n.getSpan();
        }
        return n.getSpan();
    }

    @Override
    protected void onPostExecute(SpannableStringBuilder f) {
        super.onPostExecute(n.getSpan());
        System.out.println("Done Good: DictionaryTask");
    }

    private void getDef(Node start) {
        n.getSpan().append("\n\n");
        n.addSpanTitle(start.getFirstChild().getFirstChild().getNodeValue(), new StyleSpan(Typeface.ITALIC), " ");
        NamedNodeMap startAttr = start.getAttributes();
        for (int i = 0; i < startAttr.getLength(); i++) {
            if (startAttr.item(i).getNodeName().equals("ts")) {
                String a = "[" + start.getAttributes().item(i).getNodeValue() + "]";
                n.addSpanTitle(a, new StyleSpan(Typeface.ITALIC), new ForegroundColorSpan(Color.rgb(0, 0, 158)), " ");
                n.getSpan().append("\n");
                break;
            }
        }
        for (int i = 0; i < startAttr.getLength(); i++) {
            if (startAttr.item(i).getNodeName().equals("pos")) {
                n.addSpanTitle(start.getAttributes().item(i).getNodeValue(), new StyleSpan(Typeface.ITALIC), new ForegroundColorSpan(Color.rgb(200, 158, 158)), " ");
                break;
            }
        }
        n.getSpan().append("\n");
    }

    private void getTrAll(Node start) {
        boolean flag = true;
        boolean flag2 = false;
        boolean flag3 = false;
        n.addSpanTitle(start.getFirstChild().getFirstChild().getNodeValue(), new StyleSpan(Typeface.NORMAL), " ");
        getAtt(start);

        for (Node child = start.getFirstChild().getNextSibling(); child != null; child = child.getNextSibling()) {
            switch (child.getNodeName()) {
                case "syn":
                    //region syn
                    n.getSpan().append(", ");
                    n.addSpanTitle(child.getFirstChild().getFirstChild().getNodeValue(), new StyleSpan(Typeface.NORMAL), " ");
                    getAtt(child);
                    break;
                //endregion
                case "mean":
                    //region mean
                    if (flag)
                        n.getSpan().append("\n(");
                    flag = false;
                    n.addSpanTitle(child.getFirstChild().getFirstChild().getNodeValue(), new ForegroundColorSpan(Color.rgb(0, 0, 158)), ", ");
                    break;
                //endregion
                case "ex":
                    //region ex
                    flag3 = true;
                    if (!flag) {
                        n.getSpan().delete(n.getSpan().length() - 2, n.getSpan().length());
                        n.getSpan().append(")");
                        flag = true;
                    }
                    if (!flag2) {
                        n.getSpan().append("\n");
                        flag2 = true;
                    }
                    n.addSpanTitle(child.getFirstChild().getFirstChild().getNodeValue(), new ForegroundColorSpan(Color.rgb(0, 158, 158)), "");
                    n.addSpanTitle(" — ", new ForegroundColorSpan(Color.rgb(0, 158, 158)), "");
                    n.addSpanTitle(child.getFirstChild().getNextSibling().getFirstChild().getFirstChild().getNodeValue(), new ForegroundColorSpan(Color.rgb(0, 158, 158)), "\n");
                    break;
                //endregion
                default:
                    break;
            }

        }
        if (flag3)
            n.getSpan().delete(n.getSpan().length() - 2, n.getSpan().length());
        if (!flag) {
            n.getSpan().delete(n.getSpan().length() - 2, n.getSpan().length());
            n.getSpan().append(")");
        }


    }

    private void getAtt(Node start) {
        NamedNodeMap startAttr = start.getAttributes();
        for (int i = 0; i < startAttr.getLength(); i++) {
            if (!startAttr.item(i).getNodeName().equals("pos"))
                n.addSpanTitle(startAttr.item(i).getNodeValue(), new ForegroundColorSpan(Color.rgb(158, 158, 158)), "");
        }
        if (startAttr.getLength() == 1)
            n.getSpan().delete(n.getSpan().length() - 1, n.getSpan().length());
    }
}


