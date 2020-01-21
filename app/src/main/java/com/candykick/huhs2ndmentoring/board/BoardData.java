package com.candykick.huhs2ndmentoring.board;

/**
 * Created by candykick on 2019. 9. 4..
 */

public class BoardData {
    public String title, contents, username;

    public BoardData() {}

    public BoardData(String title, String contents, String username) {
        this.title = title;
        this.contents = contents;
        this.username = username;
    }
}
