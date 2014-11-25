package com.example.pfm;

import java.util.ArrayList;

public class Bill {
	public String billId = "";
	public String billName = "";
	public String billAmount = "";
	public String billDate = "";
	public String billCategoryId = "";
	public String billRemark = "";
	
	public Bill(String a, String b, String c, String d, String e, String f){
		billId = a; 
		billName = b;
		billAmount = c;
		billDate = d;
		billCategoryId = e;
		billRemark = f;
	}
	
	public String toString(){
		return billId+","+billName+","+billAmount+","+billDate+","+billCategoryId+","+billRemark;
	}
	
	public static Bill createFromString(String a){
		String [] x = a.split(",");
		ArrayList<String> c = new ArrayList<String>();
		
		for(String b : x){
			c.add(b);
		}
		while(c.size()<6)
			c.add("");
		return new Bill(c.get(0),c.get(1),c.get(2),c.get(3),c.get(4),c.get(5));
	}
}
