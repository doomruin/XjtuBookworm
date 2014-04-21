package com.bookworm.searchbook;


/**
 * 
 * @author gaoyan
 *	获得每日推荐书籍使用的dbbean
 */
public class SimpleBookModel {
	
	private String bookName;
	private String bookSurfaceLink;//书的封面图片在服务器的地址
	private String isbn;
	private String author;
	
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookSurfaceLink() {
		return bookSurfaceLink;
	}
	public void setBookSurfaceLink(String bookSurfaceLink) {
		this.bookSurfaceLink = bookSurfaceLink;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
}
