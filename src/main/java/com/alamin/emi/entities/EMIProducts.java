package com.alamin.emi.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "emi_products")
@Data
public class EMIProducts {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	@Column(name = "product_type")
	private String productType;
	
	@Column(name = "product_sub_type")
	private String productSubType;
	
	@Column(name = "emi_provider")
	private String emiProvider;
	
	@Column(name = "api")
	private String api;
	
	@Column(name = "credentials")
	private String credentials;
	
	@Column(name = "callback_url")
	private String callbackUrl;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@Column(name="updated_by")
	private String updatedBy;

}
