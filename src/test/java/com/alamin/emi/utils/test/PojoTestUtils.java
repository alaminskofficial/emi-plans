package com.alamin.emi.utils.test;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

public class PojoTestUtils {
	 
    private static final Validator ACCESSOR_VALIDATOR = ValidatorBuilder.create()
                                                    .with(new GetterTester())
                                                    .with(new SetterTester())
                                                    .build();
 
    public static void validateAccessors(final Class<?> clazz) {
        ACCESSOR_VALIDATOR.validate(PojoClassFactory.getPojoClass(clazz));
    }
}