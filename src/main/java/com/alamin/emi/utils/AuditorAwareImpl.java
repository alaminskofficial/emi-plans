package com.alamin.emi.utils;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.alamin.emi.constants.EMIConstants;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of(EMIConstants.MO_CONST);
	}

}
