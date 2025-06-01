package com.simple.weather.api.application.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtil
{
	/**
	 * Safely round given decimal value to nearest whole number
	 * 
	 * @param value
	 * 			The value which needs to be rounded
	 * @return	Rounded value to nearest whole number
	 */
	public static BigDecimal safeRoundToNearestWhole(BigDecimal value)
	{
		BigDecimal roundedValue = BigDecimal.ZERO;
		
		if (value != null)
		{
			roundedValue = value.setScale(0, RoundingMode.HALF_UP);
		}
		return roundedValue;
	}
}
