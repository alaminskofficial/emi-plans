package com.alamin.emi.entities.test;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.alamin.emi.entities.EMITenures;
import com.alamin.emi.utils.test.PojoTestUtils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class EMITenuresTest {

	@Test
	public void testToString() throws Exception {
		assertFalse(new EMITenures().toString().contains("@"));
		PojoTestUtils.validateAccessors(EMITenures.class);
	}

	@Test
	public void equalsContract() {
		EMITenures x = new EMITenures();
		EMITenures y = new EMITenures();
		Assert.assertTrue(x.equals(y) && y.equals(x));
		Assert.assertTrue(x.hashCode() == y.hashCode());
		EqualsVerifier.simple().forClass(EMITenures.class)  .withIgnoredAnnotations(Id.class, Embeddable.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
		PojoTestUtils.validateAccessors(EMITenures.class);
	}

}
