package com.bookworm.dbmodel;

import java.util.Date;

/**
 * WishResponse entity. @author MyEclipse Persistence Tools
 */

public class WishResponse implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Wish wish;
	private Date staleDate;
	private String responseInfo;

	// Constructors

	/** default constructor */
	public WishResponse() {
	}

	/** minimal constructor */
	public WishResponse(User user, Wish wish) {
		this.user = user;
		this.wish = wish;
	}

	/** full constructor */
	public WishResponse(User user, Wish wish, Date staleDate,
			String responseInfo) {
		this.user = user;
		this.wish = wish;
		this.staleDate = staleDate;
		this.responseInfo = responseInfo;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Wish getWish() {
		return this.wish;
	}

	public void setWish(Wish wish) {
		this.wish = wish;
	}

	public Date getStaleDate() {
		return this.staleDate;
	}

	public void setStaleDate(Date staleDate) {
		this.staleDate = staleDate;
	}

	public String getResponseInfo() {
		return this.responseInfo;
	}

	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo;
	}

}