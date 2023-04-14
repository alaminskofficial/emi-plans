package com.alamin.emi.entities.test;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.alamin.emi.entities.EMITransactions;
import com.alamin.emi.utils.test.PojoTestUtils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class EMITransactionsTest {

	@Test
	public void testToString() throws Exception {
		assertFalse(new EMITransactions().toString().contains("@"));
		PojoTestUtils.validateAccessors(EMITransactions.class);
	}

	@Test
	public void equalsContract() {
		EMITransactions x = new EMITransactions();
		EMITransactions y = new EMITransactions();
		Assert.assertTrue(x.equals(y) && y.equals(x));
		Assert.assertTrue(x.hashCode() == y.hashCode());
		EqualsVerifier.simple().forClass(EMITransactions.class)  .withIgnoredAnnotations(Id.class, Embeddable.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
		PojoTestUtils.validateAccessors(EMITransactions.class);
	}

}
