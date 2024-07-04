package Programm1.src;

import java.util.ArrayList;
import java.util.List;

public class Variables {
	
	List<Clauses> clausesWithThisVar;
	boolean value;
	int number;
	
	
	public Variables()
	{
		clausesWithThisVar=new ArrayList<>();
	}
	
	public void addClauses(Clauses c)
	{
		clausesWithThisVar.add(c);
	}
	
	public void setValue(boolean b) {
		value=b;
	}
	public boolean getValue() {
		return value;
	} 
	
	public void setNum(int i) {
		number=i;
	}
	
	public int getNum() {
		return number;
	}
	
	public List<Clauses> getClauses(){
		return clausesWithThisVar;
	}
	

}
