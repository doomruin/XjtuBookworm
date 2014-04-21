package com.bookworm.dbmodel;

/**
 * ReadingNote entity. @author MyEclipse Persistence Tools
 */

public class ReadingNote implements java.io.Serializable {

	// Fields

	private Integer id;
	private ReadRecord readRecord;
	private String note;
	private Integer pageNumber;

	// Constructors

	/** default constructor */
	public ReadingNote() {
	}

	/** full constructor */
	public ReadingNote(ReadRecord readRecord, String note, Integer pageNumber) {
		this.readRecord = readRecord;
		this.note = note;
		this.pageNumber = pageNumber;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ReadRecord getReadRecord() {
		return this.readRecord;
	}

	public void setReadRecord(ReadRecord readRecord) {
		this.readRecord = readRecord;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getPageNumber() {
		return this.pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

}