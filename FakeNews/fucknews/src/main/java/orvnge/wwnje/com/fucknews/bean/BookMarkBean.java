package orvnge.wwnje.com.fucknews.bean;

/**
 * Created by wwnje on 2017/2/22.
 * 书签和喜欢的共用
 */

public class BookMarkBean {

    /**
     * bookmark_id : 1
     * news_title : 10种“小情绪”
     * news_desc : 他们声称找到了愤怒、厌恶、嫉妒、恐惧、开心、欲望、骄傲、悲伤以及羞耻这些情绪的神经相关物。
     * news_content_url : http://mp.weixin.qq.com/s?__biz=MTg1MjI3MzY2MQ==&mid=2651681280&idx=1&sn=08f01327e837c56bc639c3b4e5bfbbd5&scene=0#wechat_redirect
     * news_pic_url : http://mmbiz.qpic.cn/mmbiz_jpg/icZklJrRfHgDVjxYbMS8emBic2TJtC81kgvEqNjkSHbk8A79viaWfV2yBFIKhsvU9zntyNfWtpITcMunpHDUic5wvw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1
     * type : Life
     * FINDER_ID : 1
     * finder_name : 1
     * book_version : 0
     */

    private String bookmark_id;
    private String news_title;
    private String news_desc;
    private String news_content_url;
    private String news_pic_url;
    private String type;
    private String finder_id;
    private String finder_name;
    private int book_version;

    public int getNew_id() {
        return new_id;
    }

    public void setNew_id(int new_id) {
        this.new_id = new_id;
    }

    private int new_id;

    public String getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(String bookmark_id) {
        this.bookmark_id = bookmark_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_desc() {
        return news_desc;
    }

    public void setNews_desc(String news_desc) {
        this.news_desc = news_desc;
    }

    public String getNews_content_url() {
        return news_content_url;
    }

    public void setNews_content_url(String news_content_url) {
        this.news_content_url = news_content_url;
    }

    public String getNews_pic_url() {
        return news_pic_url;
    }

    public void setNews_pic_url(String news_pic_url) {
        this.news_pic_url = news_pic_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFinder_id() {
        return finder_id;
    }

    public void setFinder_id(String finder_id) {
        this.finder_id = finder_id;
    }

    public String getFinder_name() {
        return finder_name;
    }

    public void setFinder_name(String finder_name) {
        this.finder_name = finder_name;
    }

    public int getBook_version() {
        return book_version;
    }

    public void setBook_version(int book_version) {
        this.book_version = book_version;
    }
}
