package com.candykick.huhs2ndmentoring.map;

/**
 * Created by candykick on 2019. 8. 28..
 */

public class MapData {
    public String place_name, address_name, place_url, x, y;

    public MapData() {}

    public MapData(String place_name, String address_name, String place_url, String x, String y) {
        this.place_name = place_name;
        this.address_name = address_name;
        this.place_url = place_url;
        this.x = x;
        this.y = y;
    }
}