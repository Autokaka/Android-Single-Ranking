package cn.dshitpie.dataanalyser;

public class CardInfo {
    protected String title;
    protected String code;
    protected String imgPath;
    protected static final String TITLE = "卡片标题";
    protected static final String CODE = "识别卡号";

    public CardInfo(String title, String code, String imgPath){
        this.title = title;
        this.code = code;
        this.imgPath = imgPath;
    }
}
