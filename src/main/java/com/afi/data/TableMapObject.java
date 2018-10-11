package com.afi.data;

public class TableMapObject {
	private String tableObjectName;
	private int length;
	private boolean nullable;
	private String type;
	
	public TableMapObject(String tableObjectName, int length, boolean nullable, String type) {
		this.tableObjectName = tableObjectName;
		this.length = length;
		this.nullable = nullable;
		this.type = type;
	}

	public String getTableObjectName() {
		return tableObjectName;
	}

	public int getLength() {
		return length;
	}

	public boolean isNullable() {
		return nullable;
	}

	public String getType() {
		return type;
	}
}
