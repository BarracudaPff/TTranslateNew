package com.yandex.barracuda.ttranslator.screens.extra;

/**
 * Created by Barracuda on 21.04.2017.
 */

public class RowElement {
    private int fav_state;
    private String word;
    private String translate;
    private String from_to;
    private int id;

    public RowElement(int fav_state, String word, String translate, String from_to, int id) {
        this.fav_state = fav_state;
        this.word = word;
        this.translate = translate;
        this.from_to = from_to;
        this.id = id;
    }

    public int getFav_state() {
        return fav_state;
    }

    public void setFav_state(int fav_state) {
        this.fav_state = fav_state;
    }

    public String getWord() {
        return word;
    }

    public String getTranslate() {
        return translate;
    }

    public String getFrom_to() {
        return from_to;
    }

    public int getId() {
        return id;
    }
}
