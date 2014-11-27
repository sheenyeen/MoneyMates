package com.example.pfm;

import java.util.ArrayList;

public class Bill {
	public String billId = "";
	public String billName = "";
	public String billAmount = "";
	public String billDate = "";
	public String billCategoryId = "";
	public String billRemark = "";
	public boolean isPaid = false;
	
	public Bill(String a, String b, String c, String d, String e, String f, String g){
		if(a.equals(""))
			a = "-";
		if(b.equals(""))
			b = "-";
		if(c.equals(""))
			c = "-";
		if(d.equals(""))
			d = "-";
		if(e.equals(""))
			e = "-";
		if(f.equals(""))
			f = "-";
		if(g.equals(""))
			g = "-";
		
		billId = a; 
		billName = b;
		billAmount = c;
		billDate = d;
		billCategoryId = e;
		billRemark = f;
		isPaid = g.equals("true");
	}
	
	public String toString(){
		return billId+","+billName+","+billAmount+","+billDate+","+billCategoryId+","+billRemark+","+isPaid;
	}
	
	public static Bill createFromString(String a){
		String [] x = a.split(",");
		ArrayList<String> c = new ArrayList<String>();
		
		for(String b : x){
			c.add(b);
		}
		while(c.size()<7)
			c.add("-");
		
		return new Bill(c.get(0),c.get(1),c.get(2),c.get(3),c.get(4),c.get(5),c.get(6));
	}
}
