package com.alamin.emi.dto.test;

import static org.junit.Assert.assertFalse;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.alamin.emi.dto.PosResponseParams;
import com.alamin.emi.utils.test.PojoTestUtils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class PosResponseParamsTest {

	@Test
	public void testToString() throws Exception {
		assertFalse(new PosResponseParams().toString().contains("@"));
	}

	@Test
	public void equalsContract() {
		PosResponseParams x = new PosResponseParams();
		PosResponseParams y = new PosResponseParams();
		Assert.assertTrue(x.equals(y) && y.equals(x));
		Assert.assertTrue(x.hashCode() == y.hashCode());
		EqualsVerifier.simple().forClass(PosResponseParams.class)  .withIgnoredAnnotations(Id.class, Embeddable.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
		PojoTestUtils.validateAccessors(PosResponseParams.class);
	}

}
