package Programm1.src;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Graph {
	
	static Node[] nodes;
	static List<Node>[] GraphChart;
	
	@SuppressWarnings("unchecked")
	public Graph(int cnum, int vnum)
	{
		nodes=new Node[2*vnum];
		GraphChart = (List<Node>[]) new List[2 * vnum];
        for (int i = 0; i < 2 * vnum; i++) {
            GraphChart[i] = new ArrayList<>();
            GraphChart[i].add(null); // 初始化需要占位符
        }
		
	}
	
	
	public void iniGraph(List<Clauses> cList, List<Variables> vList)
	{
		//System.out.println("adding vars");
		for(Variables v: vList)
		{
			addVar(v);//fill nodes
		}
		
		//System.out.println("adding starting nodes");
		for(int j=0; j<nodes.length; j++)
		{
			GraphChart[j].add(nodes[j]);//fill the first line of chartgraph把表格的每个列表开头设为对应的node，和nodes里面的序号一致
		}
		
		//System.out.println("adding clauses");
		for(Clauses c: cList)
		{
			addClause(c);//fill the other lines
		}
		
		//System.out.println("finished generating graph");
	}
	
	
	
	// use a for schleife for all the variables to add all the vars to nodes 填充nodes array
	public void addVar(Variables var)
	{
		int i=var.getNum();//获取这个变量的i，也就是它是第几个变量，如var1， var2
		Node ni=new Node(i);//为 是 这个变量创建一个node
		//System.out.println("ini Node"+i );
		Node nMinusi=new Node(-i);	//为 非 这个变量创建一个node
		//System.out.println("ini Node"+(-i) );
		ni.setOppo(nMinusi);//互相设置它们的相反Node
		nMinusi.setOppo(ni);
		
		nodes[2*i-2]=ni;//是i变量 的node 编码为2i-2
		nodes[2*i-1]=nMinusi;	//非i变量 的node 编码为2i-1
	}
	
	
	
	public void addClause(Clauses c)
	{
		//获取一个句子的前后两个变量并且获取它们的编码
		Variables var1=c.getVar1();
		Variables var2=c.getVar2();
		int i1=var1.getNum();
		int i2=var2.getNum();
		
		//获取前后两个的符号，是 非
		boolean b1=c.getB1();
		boolean b2=c.getB2();
		
		int iv1;//the array number of the first node 第一个变量的node的数字
		int iv2;//the array number of the second node 第二个变量的node 的数字
		int ivMinus1;//the array number of the contract of the first node 第一个变量的相反的node数字
		int ivMinus2;//the array number of the contract of the second node 第二个变量的相反的node数字
		
		if(b1)//if the first bool is true 当第一个变量符号为是时
		{
			iv1=i1*2-2; //它的node（是）的序号，也就是在nodes里面的位置号，node【2i-2】在此是node【iv1】就是num为iv1的node
			ivMinus1=i1*2-1; //它的相反node（非），node【2i-1】在此是node【ivMinus1】就是num为-iv1的node
		}
		else//当第一个变量符号为非时
		{
			iv1=i1*2-1;//他的node（非）
			ivMinus1=i1*2-2;//他的相反node（是）
		}
		
		if(b2)//当第二个变量符号为是时，以下同上
		{
		    iv2=i2*2-2;
		    ivMinus2=i2*2-1;
		}
		else
		{
			iv2=i2*2-1;
			ivMinus2=i2*2-2;
		}
		
		//now need to write down ivminus1->iv2 and ivMinus2->iv1 下面写，第一个node的相反推出第二个node，第二个node的相反推出第一个node
		//in both the chart and the nodelist of Node 写进chart里，也写进每个node里
		//for both List<nodes>[ivminus1] and nodesList<nodes>[ivminus2]先查找里面是否已经有对应node了，如果没有，加进去
		if(!searchNode(GraphChart[ivMinus1], nodes[iv2]));
		{
			GraphChart[ivMinus1].add(nodes[iv2]);
			nodes[ivMinus1].addIm(nodes[iv2]);
		}
		
		if(!searchNode(GraphChart[ivMinus2], nodes[iv1]));
		{
			GraphChart[ivMinus2].add(nodes[iv1]);
			nodes[ivMinus2].addIm(nodes[iv1]);
		}
		
		
	}
	
	public boolean searchNode(List<Node> list, Node node)
	{
		 if (list == null || node == null) {//列表为空或者node为null时，返回无
	            return false;
	        }

	        for (Node n : list) {
	            if (n != null && n.getNum()==node.getNum()) {//如果node的num相等，返回true
	                return true;
	            }
	        }
	        return false;
	}
		
		
		
		
		/*不再需要查找空位，可以直接插入
		int next=findNextLeer(GraphChart, ivMinus1);
		
        if(next>=0) {
			GraphChart[ivMinus1][next]=nodes[iv2];
			System.out.println("graph line"+ivMinus1+"added at"+next +"var "+iv2 );
		}
        else
        {
        	System.err.println("full");
        }
		next=findNextLeer(GraphChart, ivMinus2);
        if(next>=0) {
			GraphChart[ivMinus2][next]=nodes[iv1];
			System.out.println("Graph line"+ivMinus2+"added at"+next+"var "+iv1 );
		}
		else
		{System.err.println("full");}
		
		//for both Node ivminus1 and Node ivminus2 use addIm()
		
		nodes[ivMinus1].addIm(nodes[iv2]);
		nodes[ivMinus2].addIm(nodes[iv1]);
	public int findNextLeer(Node[][] chart, int i)
	{
		int l=chart[i].length;
		for(int j=1; j<l; j++)//start from 1, since the 0 should be the node itself
		{
			if(chart[i][j] ==null) {
				System.out.println("next leer place is "+j);
				return j;
			}
			
		}
		return -1;//no leer place
	}
	*/
	
	
	
	public void printChart()
	{
		//print the chart here
		//in main, first call addVar, then addClause, finally printChart
		try {
            writeGraphChartToFile();
            //System.out.println("GraphChart: " + fileName);

            // 打开生成的文件
            openFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	static String fileName = "GraphChartOutput.txt";
	public static void writeGraphChartToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            	for (List<Node> row1 : GraphChart) {
                    for (Node node : row1) {
                        if (node != null) {
                            writer.write(String.valueOf(node.getNum()+" "));
                            
                        } else {
                            
                        }
                        
                    }
                    
                    writer.newLine();
                }
        }
    }
    public static void openFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("can't open：" + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    /*
    
    public void goThroughChart()
    {
    	for (int i=0; i<nodes.length;i++)
    	{
    		if(nodes[i]!=null)
    		{searchI(i);}
    	}
    }
    
    //找哪个来着？？？？？？？？？？？？
    
    public void searchI(int i) {
        List<Node> reachableNodes = dfsFromFirstNode(GraphChart, i);

        GraphChart[i].get(0).setRN(reachableNodes);
    }
    
    
    public static List<Node> dfsFromFirstNode(List<Node>[] chart, int i) {
        List<Node> reachableNodes = new ArrayList<>();
        boolean[] visited = new boolean[chart.length];
        dfs(chart, i, reachableNodes, visited);
        return reachableNodes;
    }

    
    private static void dfs(List<Node>[] chart, int i, List<Node> reachableNodes, boolean[] visited) {
        if (i < 0 || i >= chart.length || visited[i]) {
            return;
        }
        visited[i] = true;

        Node startNode = chart[i].get(0);
        reachableNodes.add(startNode);

        for (int j = 1; j < chart[i].size(); j++) {
            Node nextNode = chart[i].get(j);

            if (nextNode != null) {
                int nextIndex = findRowIndex(chart, nextNode);
                if (nextIndex != -1 && !visited[nextIndex]) {
                    dfs(chart, nextIndex, reachableNodes, visited);
                } else {
                    System.err.println("Node not found or already visited: " + nextNode.getNum());
                }
            }
        }
    }
    
    private static int findRowIndex(List<Node>[] chart, Node node) {
        for (int i = 0; i < chart.length; i++) {
            if (chart[i].get(0) != null && chart[i].get(0).getNum() == node.getNum()) {
                return i;
            }
        }
        return -1;
    }
    */
    
    
    
    
    
    public int caculateN(int i)//用num计算它在grapghchart的哪个位置
    {
    	if(i>0) 
    	{return 2*i-2;}
    	else
    	{
    		return -2*i-1;//为什么是-2*i-1?那个负号是干什么的？ 是i变量和非i变量设置时，i都是正的，现在读取的i是负时就是非变量，所以直接把负数i再负过来
    	}
    }
    //从start能否到达target
    public boolean canReach(int start, int target) {
        boolean[] visited = new boolean[nodes.length];//用一个布尔数组，数组i位置上代表GraphChart[i]开始的node是否已经到达
        return dfs2(start, target, visited);
    }
    
   
    
 // dfs，使用显式栈
    public boolean dfs2(int current, int target, boolean[] visited) {
        Stack<Integer> stack = new Stack<>();
        stack.push(current);

        while (!stack.isEmpty()) {
            int currentnum = stack.pop();
            //System.out.println(numberofRe);
            int currentNumValue = caculateN(currentnum);
            int targetNumValue = caculateN(target);

            if (currentNumValue == targetNumValue) {
                return true;
            }

            if (!visited[currentNumValue]) {
                visited[currentNumValue] = true;

                // 遍历所有邻接节点
                for (Node next : GraphChart[currentNumValue]) {
                    if (next != null && !visited[caculateN(next.getNum())]) {
                        stack.push(next.getNum());
                    }
                }
            }
        }

        return false;
    }
    
    
    
    public boolean trysat()
    {
        for (int i=0; i<nodes.length; i++)
        {
        	if (i%2==0)
        	{
        		if((nodes[i]!=null && nodes[i+1]!=null)&&canReach(nodes[i].getNum(), nodes[i+1].getNum()))//from i to not i
        				{
        			      if(canReach(nodes[i+1].getNum(), nodes[i].getNum()));//from not i to i, bicycle
        			      {return false;}
        			
        				}
        	}
        	/*else
        	{
        		if(nodes[i]!=null && nodes[i-1]!=null &&canReach(nodes[i].getNum(), nodes[i-1].getNum() ))
        		{
        			if(canReach(nodes[i-1].getNum(),nodes[i].getNum()))//bicycle
        			{
        				return false;
        			}
        		}
        	}*/
        }
        return true;
    }
    
    
    
    
    
    
    
    
    
    
    

}
