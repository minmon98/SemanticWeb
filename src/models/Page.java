package models;

public class Page {
	private String title;
	private String url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Page(String title, String url) {
		super();
		this.title = title;
		this.url = url;
	}
	public Page() {
		super();
	}
	
	public String convertUrlToPath() {
		int newStringIndex = 8;
		return "data/" + this.getUrl().substring(newStringIndex);
	}
	
	@Override
	public String toString() {
		return "page [title=" + title + ", url=" + url + "]";
	}
}
