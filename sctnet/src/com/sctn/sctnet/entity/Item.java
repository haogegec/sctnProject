package com.sctn.sctnet.entity;
/**
 * 
 * @author xueweiwei
 *
 */
public class Item {

	private String label;
	private String value;
	
	public Item(String label,String value){
		this.label = label;
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
