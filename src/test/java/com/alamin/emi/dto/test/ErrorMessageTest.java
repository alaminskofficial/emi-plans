package com.alamin.emi.dto.test;

import static org.junit.Assert.assertFalse;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.alamin.emi.dto.ErrorMessage;
import com.alamin.emi.utils.test.PojoTestUtils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ErrorMessageTest {

	@Test
	public void testToString() throws Exception {
		assertFalse(new ErrorMessage(0, null, null, null).toString().contains("@"));
	}

	@Test
	public void equalsContract() {
		ErrorMessage x = new ErrorMessage(0, null, null, null);
		ErrorMessage y = new ErrorMessage(0, null, null, null);
		Assert.assertTrue(x.equals(y) && y.equals(x));
		Assert.assertTrue(x.hashCode() == y.hashCode());
		EqualsVerifier.simple().forClass(ErrorMessage.class)  .withIgnoredAnnotations(Id.class, Embeddable.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
		PojoTestUtils.validateAccessors(ErrorMessage.class);
	}

}
