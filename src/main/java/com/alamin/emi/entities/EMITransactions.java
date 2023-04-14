package com.alamin.emi.entities;

import java.io.Serializable;
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
@Table(name = "emi_transactions")
@Data
public class EMITransactions implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "terminal_id")
	private String terminalId;
	
	@Column(name = "customer_id")
	private Integer customerId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "product_type")
	private String productType;
	
	@Column(name = "product_sub_type")
	private String productSubType;
	
	@Column(name = "emi_amount")
	private Double emiAmount;
	
	@Column(name = "loan_amount")
	private Double loanAmount;
	
	@Column(name = "interest_rate")
	private Double interestRate;
	
	@Column(name = "processing_fee")
	private Double processinFee;
	
	@Column(name = "tenure")
	private Integer tenure;
	
	@Column(name = "tenure_type")
	private String tenureType;
	
	@Column(name = "invoice_id")
	private Integer invoiceId;
	
	@Column(name = "journey_type")
	private String journeyType;
	
	@Column(name = "additional_data")
	private String additionalData;
	
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
	
	@Column(name="token")
	private String token;
	
	@Column(name="bank_ref_no")
	private String bankRefNo;

}
