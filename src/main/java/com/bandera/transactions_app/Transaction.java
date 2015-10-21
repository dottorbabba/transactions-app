package com.bandera.transactions_app;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Transaction {

	@JsonIgnore
	@Id
	private long id;

	@NotNull
	private String type;

	private double amount = 0.0;
	@JsonProperty("parent_id")
	private Long parentId;
	@JsonIgnore
	private String path;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

	public Long getParentId() {
		return parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
