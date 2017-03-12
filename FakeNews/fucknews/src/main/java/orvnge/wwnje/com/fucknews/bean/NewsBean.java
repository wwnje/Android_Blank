package orvnge.wwnje.com.fucknews.bean;

public class NewsBean {

	private String title;
	private String desc;
	private String time;
	private String content_url;
	private String pic_url;
	private String type;
	private String finder;
	private Boolean isBook;//是否加入了书签
	private Boolean isLike;//是否是喜欢
	private int news_id;

	public int getNews_id() {
		return news_id;
	}

	public void setNews_id(int news_id) {
		this.news_id = news_id;
	}

	public Boolean getBook() {
		return isBook;
	}

	public void setBook(Boolean book) {
		isBook = book;
	}

	public Boolean getLike() {
		return isLike;
	}

	public void setLike(Boolean like) {
		isLike = like;
	}

	public String getFinder() {
		return finder;
	}

	public void setFinder(String finder) {
		this.finder = finder;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent_url() {
		return content_url;
	}
	public void setContent_url(String content_url) {
		this.content_url = content_url;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
