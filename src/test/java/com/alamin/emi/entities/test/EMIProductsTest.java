package com.alamin.emi.entities.test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.alamin.emi.entities.EMIProducts;
import com.alamin.emi.utils.test.PojoTestUtils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class EMIProductsTest {

	@Test
	public void testToString() throws Exception {
		assertFalse(new EMIProducts().toString().contains("@"));
		PojoTestUtils.validateAccessors(EMIProducts.class);
	}

	@Test
	public void equalsContract() {
		EMIProducts x = new EMIProducts();
		EMIProducts y = new EMIProducts();
		Assert.assertTrue(x.equals(y) && y.equals(x));
		Assert.assertTrue(x.hashCode() == y.hashCode());
		EqualsVerifier.simple().forClass(EMIProducts.class)  .withIgnoredAnnotations(Id.class, Embeddable.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
		PojoTestUtils.validateAccessors(EMIProducts.class);
	}

}
