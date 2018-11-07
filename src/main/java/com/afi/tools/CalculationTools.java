package com.afi.tools;

public class CalculationTools {
	public static double calculatePrefColumnWidth(int columnLength, int tableMapLength) {
		return Math.max(columnLength, tableMapLength) * 10.0;
	}
}
