package com.alamin.emi.dto.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.alamin.emi.dto.EMIProductCRUDRequest;
import com.alamin.emi.utils.test.PojoTestUtils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class EMIProductCRUDRequestTest {

	@Test
	public void testToString() throws Exception {
		assertFalse(new EMIProductCRUDRequest().toString().contains("@"));
	}

	@Test
	public void equalsContract() {
		EMIProductCRUDRequest x = new EMIProductCRUDRequest();
		EMIProductCRUDRequest y = new EMIProductCRUDRequest();
		Assert.assertTrue(x.equals(y) && y.equals(x));
		Assert.assertTrue(x.hashCode() == y.hashCode());
		EqualsVerifier.simple().forClass(EMIProductCRUDRequest.class)  .withIgnoredAnnotations(Id.class, Embeddable.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
		PojoTestUtils.validateAccessors(EMIProductCRUDRequest.class);
	}

}
