package com.alamin.emi.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "emi_tenures")
@Data
public class EMITenures {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private EMIProducts emiProducts;
	
	
	@Column(name = "minimum_transaction_amount")
	private Integer minimumTransactionAmount;
	
	@Column(name = "maximum_transaction_amount")
	private Integer maximumTransactionAmount;
	
	@Column(name = "tenure")
	private Integer tenure;
	
	@Column(name = "tenure_type")
	private String tenureType;
	
	@Column(name = "interest_rate")
	private Double interestRate;
	
	@Column(name = "processing_fee")
	private Double processinFee;
	
	@Column(name = "merchant_fee")
	private Double merchantFee;
	
	@Column(name = "merchant_fee_type")
	private String merchantFeeType;
	
	@Column(name = "additional_field")
	private String additionalField;
	
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
