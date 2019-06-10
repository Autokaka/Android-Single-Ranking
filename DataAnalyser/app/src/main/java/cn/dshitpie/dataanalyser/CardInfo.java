package cn.dshitpie.dataanalyser;

public class CardInfo {
    protected String title;
    protected String code;
    protected String imgPath;
    protected static final String TITLE = "点击修改标题";
    protected static final String CODE = "卡号";

    public CardInfo(String title, String code, String imgPath){
        this.title = title;
        this.code = code;
        this.imgPath = imgPath;
    }
    public CardInfo(String imgPath){
        this.title = "";
        this.code = "正在获取识别结果...";
        this.imgPath = imgPath;
    }
}
