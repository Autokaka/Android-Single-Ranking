package cn.dshitpie.habitsgalaxy.DataBase;

public class Planet {

    public Planet() {

    }
    public Planet(String PLANET_ID, String TITLE, String TREE_NUM, String START_DATE, String END_DATE) {
        this.PLANET_ID = PLANET_ID;
        this.TITLE = TITLE;
        this.TREE_NUM = TREE_NUM;
        this.START_DATE = START_DATE;
        this.END_DATE = END_DATE;
    }

    //表名
    public static final String TABLE_NAME = "GLOBE";

    //字段名
    public static final String KEY_PLANET_ID = "PLANET_ID";
    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_TREE_NUM = "TREE_NUM";
    public static final String KEY_START_DATE = "START_DATE";
    public static final String KEY_END_DATE = "END_DATE";

    //变量名
    public String PLANET_ID;
    public String TITLE;
    public String TREE_NUM = "0";
    public String START_DATE;
    public String END_DATE;

}
