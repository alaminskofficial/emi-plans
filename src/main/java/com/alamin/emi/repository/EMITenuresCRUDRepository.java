package com.alamin.emi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alamin.emi.entities.EMITenures;

@Repository
public interface EMITenuresCRUDRepository extends JpaRepository <EMITenures ,Integer>{
	
	@Query("SELECT r FROM EMITenures r WHERE r.emiProducts.id =:id ")
	EMITenures getTenureByProductId(@Param("id") int id);
	

}
