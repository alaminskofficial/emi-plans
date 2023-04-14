package com.alamin.emi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alamin.emi.entities.EMIProducts;

@Repository
public interface EMIProductCRUDRepository extends JpaRepository <EMIProducts ,Integer> {
	
	@Query("SELECT r FROM EMIProducts r WHERE r.id =:id ")
	public EMIProducts getProductById(@Param("id") int id);

}
