package com.bookworm.scanbook;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 李珂嘉 xml处理解析数据评论 复用5个方法
 * 
 * @author Administrator
 * 
 */
public class ReviewContentHandler extends DefaultHandler {
	private List<BookReview> lists = null;
	private BookReview currentBookReview;

	private String tagName = null;// 当前解析的元素标签

	public List<BookReview> getBookReview() {

		return lists;

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		// super.characters(ch, start, length);
		if (tagName != null && currentBookReview!=null) {

			String data = new String(ch, start, length);

			if (tagName.equals("title")) {

				this.currentBookReview.setmReviewTitle(data);

			} else if (tagName.equals("summary")) {

				this.currentBookReview.setmContent(data);

			} else if (tagName.equals("name")) {
				this.currentBookReview.setmUser(data);
			} else if (tagName.equals("updated")) {
				this.currentBookReview.setmTime(data);
			}

		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		// super.endElement(uri, localName, qName);
		if (localName.equals("entry")) {

			lists.add(currentBookReview);

			currentBookReview = null;

		}

		this.tagName = null;

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		// super.startDocument();
		lists = new ArrayList<BookReview>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		// super.startElement(uri, localName, qName, attributes);

		if (localName.equals("entry")) {
			currentBookReview = new BookReview();
		} else if (localName.equals("gd:rating")) {
			currentBookReview.setmGrade(Integer.parseInt(attributes
					.getValue("value")));
		}

		this.tagName = localName;

	}

}
