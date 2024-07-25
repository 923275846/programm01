package Programm1.src;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	int num;
	boolean value;
	//List<Node> implicates;
	Node oppoNode;
	//List<Node> reachableNodes;
	
	
	public Node() {}
	
	public Node(int n) {
		//implicates=new ArrayList<>();
		num=n;
	}
	
	/*public void addIm(Node nod) {
		implicates.add(nod);
	}
	
	public List<Node> getIm(){
		return this.implicates;
	}*/
	
	public void setNum(int i) {
		this.num=i;
	}
	
	public int getNum() {
		return num;
	}
	
	public void setValue(boolean b)
	{
		this.value=b;
	}
	
	public boolean getValue() {
		return this.value;
	}
	
	public void setOppo(Node op)
	{
		oppoNode=op;
	}
	
	public Node getOppo()
	{
		return oppoNode;
	}
	
	/*public void setRN(List<Node> rn)
	{
		reachableNodes=rn;
	}*/
	
	 // 重写 equals 方法，用于比较节点对象是否相等
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // 如果是同一个对象，返回 true
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) { // 如果对象为 null 或者类型不匹配，返回 false
            return false;
        }
        Node node = (Node) obj; // 强制类型转换为 Node 类型
        return num == node.num; // 比较节点编号是否相等
    }

    // 重写 hashCode 方法，用于计算哈希值
    @Override
    public int hashCode() {
        return Integer.hashCode(num); // 返回节点编号的哈希值
    }

}
