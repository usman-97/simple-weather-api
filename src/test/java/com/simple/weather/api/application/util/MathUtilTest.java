package com.simple.weather.api.application.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class MathUtilTest 
{
	@Test
	public void testSafeRoundToNearestWhole_positiveValue() 
	{
		BigDecimal input = new BigDecimal("3.6");
		BigDecimal expected = new BigDecimal("4");
		assertEquals(expected, MathUtil.safeRoundToNearestWhole(input));
	}

	@Test
	public void testSafeRoundToNearestWhole_negativeValue() 
	{
		BigDecimal input = new BigDecimal("-2.5");
		BigDecimal expected = new BigDecimal("-3");
		assertEquals(expected, MathUtil.safeRoundToNearestWhole(input));
	}

	@Test
	public void testSafeRoundToNearestWhole_exactHalfUp() 
	{
		BigDecimal input = new BigDecimal("1.5");
		BigDecimal expected = new BigDecimal("2");
		assertEquals(expected, MathUtil.safeRoundToNearestWhole(input));
	}

	@Test
	public void testSafeRoundToNearestWhole_exactHalfDown() 
	{
		BigDecimal input = new BigDecimal("2.499999");
		BigDecimal expected = new BigDecimal("2");
		assertEquals(expected, MathUtil.safeRoundToNearestWhole(input));
	}

	@Test
	public void testSafeRoundToNearestWhole_zeroValue() 
	{
		BigDecimal input = BigDecimal.ZERO;
		assertEquals(BigDecimal.ZERO, MathUtil.safeRoundToNearestWhole(input));
	}

	@ParameterizedTest
	@NullSource
	public void testSafeRoundToNearestWhole_nullInput(BigDecimal nullInput) 
	{
		assertEquals(BigDecimal.ZERO, MathUtil.safeRoundToNearestWhole(nullInput));
	}

	@ParameterizedTest
	@ValueSource(strings = {"0.0001", "-0.0001", "999999999.999999"})
	public void testSafeRoundToNearestWhole_edgeCases(String value) 
	{
		BigDecimal input = new BigDecimal(value);
		BigDecimal result = MathUtil.safeRoundToNearestWhole(input);
		
		BigDecimal manuallyRounded = input.setScale(0, RoundingMode.HALF_UP);
		assertEquals(manuallyRounded, result);
	}
}