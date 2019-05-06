package cn.dshitpie.magicalconch.controller;

public class Note {
    //表名
    public static final String TABLE = "todo";
    // 列名
    public static final String KEY_title="title";
    public static final String KEY_context="context";
    public static final String KEY_location="location";
    public static final String KEY_longitude = "longitude";
    public static final String KEY_latitude = "latitude";
    public String title;
    public String location;
    public String context;
    public double latitude;
    public double longitude;
}
