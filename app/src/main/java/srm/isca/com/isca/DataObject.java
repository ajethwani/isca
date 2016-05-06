package srm.isca.com.isca;

public class DataObject {
    private String mText1;
    private String mText2;

    DataObject (String text1){
        mText1 = text1;

    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}