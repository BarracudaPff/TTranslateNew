package com.yandex.barracuda.ttranslator.translate;

import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * Created by Barracuda on 21.04.2017.
 */

public class SpannableText {

    private String resultSpanS;
    private SpannableStringBuilder span;

    public SpannableText() {
        span = new SpannableStringBuilder();
    }

    public void addSpanTitle(String node, StyleSpan style, String str) {
        int l = span.length();
        span.append(node);
        span.setSpan(style, l, l + node.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.append(str);
    }

    public void addSpanTitle(String node, StyleSpan style, ForegroundColorSpan color, String str) {
        int l = span.length();
        span.append(node);
        span.setSpan(style, l, l + node.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(color, l, l + node.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.append(str);
    }

    public void addSpanTitle(String node, ForegroundColorSpan color, String str) {
        int l = span.length();
        span.append(node);
        span.setSpan(color, l, l + node.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.append(str);
    }

    public SpannableStringBuilder getSpan() {
        return span;
    }
}
