package orvnge.wwnje.com.fucknews.bean;

import java.util.Date;

/**
 * Created by wwnje on 2016/11/24.
 */

public class TwentyData {

    private String title;
    private String desc;
    private String type;
    private Date date;
    private int hours;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
