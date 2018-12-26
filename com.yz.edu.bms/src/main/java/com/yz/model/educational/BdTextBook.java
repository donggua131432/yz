package com.yz.model.educational;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BdTextBook {
	private String textbookId;

	private String textbookName;

	private String textbookType;

	private String isBook;

	private String publisher;

	private String isbn;

	private String price;

	private String count;

	private String store;

	private String alias;

	private String publisherTime;

	private String author;

	private String testSubject;

	private String remark;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private Date createTime;

	private String createUser;

	private String ext1;

	private String ext2;

	private String ext3;

	private List<Map<String, Object>> professional;

	public String getTestSubject() {
		return testSubject;
	}

	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}

	public List<Map<String, Object>> getProfessional() {
		return professional;
	}

	public void setProfessional(List<Map<String, Object>> professional) {
		this.professional = professional;
	}

	public String getTextbookId() {
		return textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId == null ? null : textbookId.trim();
	}

	public String getTextbookName() {
		return textbookName;
	}

	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName == null ? null : textbookName.trim();
	}

	public String getTextbookType() {
		return textbookType;
	}

	public void setTextbookType(String textbookType) {
		this.textbookType = textbookType == null ? null : textbookType.trim();
	}

	public String getIsBook() {
		return isBook;
	}

	public void setIsBook(String isBook) {
		this.isBook = isBook == null ? null : isBook.trim();
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher == null ? null : publisher.trim();
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn == null ? null : isbn.trim();
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price == null ? null : price.trim();
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count == null ? null : count.trim();
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store == null ? null : store.trim();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias == null ? null : alias.trim();
	}

	public String getPublisherTime() {
		return publisherTime;
	}

	public void setPublisherTime(String publisherTime) {
		this.publisherTime = publisherTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author == null ? null : author.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId == null ? null : updateUserId.trim();
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId == null ? null : createUserId.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1 == null ? null : ext1.trim();
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2 == null ? null : ext2.trim();
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3 == null ? null : ext3.trim();
	}
}