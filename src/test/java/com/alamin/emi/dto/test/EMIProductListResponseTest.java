package com.alamin.emi.dto.test;

import static org.junit.Assert.assertFalse;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.alamin.emi.dto.EMIProductListResponse;
import com.alamin.emi.utils.test.PojoTestUtils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class EMIProductListResponseTest {

	@Test
	public void testToString() throws Exception {
		assertFalse(new EMIProductListResponse().toString().contains("@"));
	}

	@Test
	public void equalsContract() {
		EMIProductListResponse x = new EMIProductListResponse();
		EMIProductListResponse y = new EMIProductListResponse();
		Assert.assertTrue(x.equals(y) && y.equals(x));
		Assert.assertTrue(x.hashCode() == y.hashCode());
		EqualsVerifier.simple().forClass(EMIProductListResponse.class)  .withIgnoredAnnotations(Id.class, Embeddable.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
		PojoTestUtils.validateAccessors(EMIProductListResponse.class);
	}

}
