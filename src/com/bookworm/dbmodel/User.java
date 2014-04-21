package com.bookworm.dbmodel;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer id;
	private Photo photo;
	private Department department;
	private String name;
	private String passwd;
	private Integer enrollYear;
	private Integer grade;
	private String email;
	private Set borrowingRecords = new HashSet(0);
	private Set readRecords = new HashSet(0);
	private Set readingHabits = new HashSet(0);
	private Set links = new HashSet(0);
	private Set wishs = new HashSet(0);
	private Set bookFiles = new HashSet(0);
	private Set wishResponses = new HashSet(0);
	private Set photos = new HashSet(0);
	private Set bookComments = new HashSet(0);

	// Constructors

	/** default constructor */
	public User() {
	}

	/** full constructor */
	public User(Photo photo, Department department, String name, String passwd,
			Integer enrollYear, Integer grade, String email,
			Set borrowingRecords, Set readRecords, Set readingHabits,
			Set links, Set wishs, Set bookFiles, Set wishResponses, Set photos,
			Set bookComments) {
		this.photo = photo;
		this.department = department;
		this.name = name;
		this.passwd = passwd;
		this.enrollYear = enrollYear;
		this.grade = grade;
		this.email = email;
		this.borrowingRecords = borrowingRecords;
		this.readRecords = readRecords;
		this.readingHabits = readingHabits;
		this.links = links;
		this.wishs = wishs;
		this.bookFiles = bookFiles;
		this.wishResponses = wishResponses;
		this.photos = photos;
		this.bookComments = bookComments;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Photo getPhoto() {
		return this.photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Integer getEnrollYear() {
		return this.enrollYear;
	}

	public void setEnrollYear(Integer enrollYear) {
		this.enrollYear = enrollYear;
	}

	public Integer getGrade() {
		return this.grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set getBorrowingRecords() {
		return this.borrowingRecords;
	}

	public void setBorrowingRecords(Set borrowingRecords) {
		this.borrowingRecords = borrowingRecords;
	}

	public Set getReadRecords() {
		return this.readRecords;
	}

	public void setReadRecords(Set readRecords) {
		this.readRecords = readRecords;
	}

	public Set getReadingHabits() {
		return this.readingHabits;
	}

	public void setReadingHabits(Set readingHabits) {
		this.readingHabits = readingHabits;
	}

	public Set getLinks() {
		return this.links;
	}

	public void setLinks(Set links) {
		this.links = links;
	}

	public Set getWishs() {
		return this.wishs;
	}

	public void setWishs(Set wishs) {
		this.wishs = wishs;
	}

	public Set getBookFiles() {
		return this.bookFiles;
	}

	public void setBookFiles(Set bookFiles) {
		this.bookFiles = bookFiles;
	}

	public Set getWishResponses() {
		return this.wishResponses;
	}

	public void setWishResponses(Set wishResponses) {
		this.wishResponses = wishResponses;
	}

	public Set getPhotos() {
		return this.photos;
	}

	public void setPhotos(Set photos) {
		this.photos = photos;
	}

	public Set getBookComments() {
		return this.bookComments;
	}

	public void setBookComments(Set bookComments) {
		this.bookComments = bookComments;
	}

}