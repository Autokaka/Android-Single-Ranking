package cn.dshitpie.testrecyclerview;

public class ContactInfo {
    protected String name = "小明";
    protected String surname = "西门";
    protected String email = "fever@icloud.com";
    protected static final String NAME_PREFIX = "Name_";
    protected static final String SURNAME_PREFIX = "Surname_";
    protected static final String EMAIL_PREFIX = "email_";

    public ContactInfo(String name, String surname, String email){
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}
