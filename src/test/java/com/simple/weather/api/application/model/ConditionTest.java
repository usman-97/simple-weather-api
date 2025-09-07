package com.simple.weather.api.application.model;

import org.junit.jupiter.api.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

class ConditionTest
{
	@Test
	void testLocation()
	{
		final Validator validator = ValidatorBuilder.create()
				.with(new GetterTester())
				.with(new SetterTester())
				.build();
		
		final PojoClass ConditionPojo = PojoClassFactory.getPojoClass(Condition.class);
		
		validator.validate(ConditionPojo);
	}
}
