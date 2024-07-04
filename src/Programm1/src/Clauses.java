package Programm1.src;

public class Clauses {
	
	public Variables a;
	public Variables b;
	public Boolean bol1;
	public Boolean bol2;
	public int Clausenum;
	
	public Clauses()
	{
		
	}
	
	public void setClauses(Variables v1, Variables v2, Boolean b1, Boolean b2)
	{
		this.a=v1;
		this.b=v2;
		this.bol1=b1;
		this.bol2=b2;
	}
	
	public void setClauseNum(int i)
	{
		Clausenum=i;
	}
	
	public int getClauseNum()
	{
		return Clausenum;
	}
	
	public Variables getVar1()
	{
		return a;
	}
	
	public Variables getVar2()
	{
		return b;
	}

	public boolean getB1()
	{
		return bol1;
	}
	
	public boolean getB2()
	{
		return bol2;
	}
}
