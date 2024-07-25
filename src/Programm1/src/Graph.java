package Programm1.src;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Graph {
	
	static Node[] nodes;
	static List<Node>[] GraphChart;
	private int index;
    private Stack<Node> stack;
    private List<List<Node>> sccs;
    //private Map<Node, Integer> nodeToIndex;
    //private Map<Node, Integer> nodeToLowlink;
    //private Set<Node> onStack;
    static Set<Node>[] GraphChartSet;
    
    
    private int[] scc;
    private int sccCount;
	
	public Graph(int cnum, int vnum)
	{
		nodes=new Node[2*vnum];
		/*GraphChart = (List<Node>[]) new List[2 * vnum];
        for (int i = 0; i < 2 * vnum; i++) {
            GraphChart[i] = new ArrayList<>();
            GraphChart[i].add(null); // 初始化需要占位符
        }*/
		
	        GraphChartSet = createGraphChart(2 * vnum);
		 for (int i = 0; i < 2 * vnum; i++) {
	            GraphChartSet[i] = new HashSet<Node>();  // 明确指定类型参数
	        }
		
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	private Set<Node>[] createGraphChart(int size) {
        return (Set<Node>[]) new HashSet[size];
    }

	
	
	public void iniGraph(List<Clauses> cList, List<Variables> vList)
	{
		for(Variables v: vList)
		{
			addVar(v);//fill nodes
		}

		for(int j=0; j<nodes.length; j++)
		{
			//GraphChart[j].add(nodes[j]);//fill the first line of chartgraph把表格的每个列表开头设为对应的node，和nodes里面的序号一致
			GraphChartSet[j].add(nodes[j]); // 填充 GraphChart 的第一行
		}

		for(Clauses c: cList)
		{
			addClause(c);//fill the other lines
		}

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
		/*if(!searchNode(GraphChart[ivMinus1], nodes[iv2]));
		{
			GraphChart[ivMinus1].add(nodes[iv2]);
			//nodes[ivMinus1].addIm(nodes[iv2]); 不要再使用node的这个im了，只使用这个类里的array of list
		}
		
		if(!searchNode(GraphChart[ivMinus2], nodes[iv1]));
		{
			GraphChart[ivMinus2].add(nodes[iv1]);
			//nodes[ivMinus2].addIm(nodes[iv1]);
		}*/
		
		
		
		// 使用 HashSet 进行检查和添加
        GraphChartSet[ivMinus1].add(nodes[iv2]);
        GraphChartSet[ivMinus2].add(nodes[iv1]);
		
	}
	
	//问题：每加一个相反，都遍历一次
	//极端情况：m的平方？
	public boolean searchNodeOld(List<Node> list, Node node)
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
		
	public boolean searchNode(Set<Node> set, Node node) {
        return set != null && node != null && set.contains(node);
    }
	
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
      
    	 findSCCs(); // 确保强连通分量已计算并初始化 scc
    	 
    	for (int i=0; i<nodes.length; i++)
        {
        	if (i%2==0)
        	{
        		if((nodes[i]!=null && nodes[i+1]!=null)&&(scc[i]==scc[i+1]))//在同一个团块里
        			      {return false;}			
        	}
        }
        return true; 
    }
    
    /*
  //找出强关联部分
    public List<List<Node>> findSCCs1() {
        index = 0;
        stack = new Stack<>();
        sccs = new ArrayList<>();
        nodeToIndex = new HashMap<>();
        nodeToLowlink = new HashMap<>();
        onStack = new HashSet<>();

        for (Node node : nodes) {
            if (node != null && !nodeToIndex.containsKey(node)) {
                tarjan(node);
            }
        }

        return sccs;
    }
用tarjan算法
    private void tarjan(Node node) {
        nodeToIndex.put(node, index);
        nodeToLowlink.put(node, index);
        index++;
        stack.push(node);
        onStack.add(node);

        for (Node neighbor : GraphChart[caculateN(node.getNum())]) {
            if (neighbor == null) continue;

            if (!nodeToIndex.containsKey(neighbor)) {
                tarjan(neighbor);
                nodeToLowlink.put(node, Math.min(nodeToLowlink.get(node), nodeToLowlink.get(neighbor)));
            } else if (onStack.contains(neighbor)) {
                nodeToLowlink.put(node, Math.min(nodeToLowlink.get(node), nodeToIndex.get(neighbor)));
            }
        }

        if (nodeToLowlink.get(node).equals(nodeToIndex.get(node))) {
            List<Node> scc = new ArrayList<>();
            Node w;
            do {
                w = stack.pop();
                onStack.remove(w);
                scc.add(w);
            } while (!w.equals(node));
            sccs.add(scc);
        }
    }
    */
    
    
    private void dfs(Node node, boolean[] visited, List<Node>[] graph, List<Node> scc) {
        Stack<Node> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            int num = caculateN(current.getNum());
            if (!visited[num]) {
                visited[num] = true;
                scc.add(current);
                for (Node neighbor : graph[num]) {
                    if (!visited[caculateN(neighbor.getNum())]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
    }
    
    
    public List<List<Node>> findSCCs() {
    	
    	 // 先初始化 scc 数组
        scc = new int[nodes.length];
        Arrays.fill(scc, -1); // 初始化为-1

        boolean[] visited = new boolean[nodes.length];
        Stack<Node> stack = new Stack<>();

        //1: 填充栈
        for (Node node : nodes) {
            if (node != null && !visited[caculateN(node.getNum())]) {
                fillOrder(node, visited, stack);
            }
        }

        //2: 获取反向图
        List<Node>[] reversedGraph = getReversedGraph();
        Arrays.fill(visited, false);
        sccs = new ArrayList<>();
        sccCount = 0;

        //3: 处理栈中的节点
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if (!visited[caculateN(node.getNum())]) {
                List<Node> sccList = new ArrayList<>();
                dfs(node, visited, reversedGraph, sccList);

                // 分配强连通分量编号
                for (Node n : sccList) {
                    scc[caculateN(n.getNum())] = sccCount;
                }
                sccs.add(sccList);
                sccCount++;
            }
        }

        return sccs;
    }

    private void fillOrder(Node node, boolean[] visited, Stack<Node> stack) {
        Stack<Node> tempStack = new Stack<>();
        tempStack.push(node);

        while (!tempStack.isEmpty()) {
            Node currentNode = tempStack.peek();
            int num = caculateN(currentNode.getNum());

            if (!visited[num]) {
                visited[num] = true;
                boolean pushedAnyNeighbor = false;

                for (Node neighbor : GraphChartSet[num]) {
                    if (neighbor != null && !visited[caculateN(neighbor.getNum())]) {
                        tempStack.push(neighbor);
                        pushedAnyNeighbor = true;
                    }
                }

                if (!pushedAnyNeighbor) {
                    stack.push(tempStack.pop());
                }
            } else {
                stack.push(tempStack.pop());
            }
        }
    }


    private List<Node>[] getReversedGraph() {
        List<Node>[] reversedGraph = new List[GraphChartSet.length];
        for (int i = 0; i < GraphChartSet.length; i++) {
            reversedGraph[i] = new ArrayList<>();
        }

        for (int i = 0; i < GraphChartSet.length; i++) {
            for (Node node : GraphChartSet[i]) {
                if (node != null) {
                    reversedGraph[caculateN(node.getNum())].add(nodes[i]);
                }
            }
        }

        return reversedGraph;
    }

    /*拓扑排序
    public List<Node> topologicalSortSCCs() {
        List<List<Node>> sccs = findSCCs();
        Map<Node, List<Node>> dag = new HashMap<>();
        Map<Node, Integer> inDegree = new HashMap<>();

        for (List<Node> scc : sccs) {
            for (Node node : scc) {
                dag.putIfAbsent(node, new ArrayList<>());
                inDegree.putIfAbsent(node, 0);
                for (Node neighbor : node.getIm()) {
                    if (!scc.contains(neighbor)) {
                        dag.get(node).add(neighbor);
                        inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
                    }
                }
            }
        }

        Queue<Node> queue = new LinkedList<>();
        for (Node node : dag.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.add(node);
            }
        }

        List<Node> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            topoOrder.add(node);

            for (Node neighbor : dag.get(node)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return topoOrder;
    }
    
    //是否可以满足
    public boolean is2SATSatisfiable() {
        List<List<Node>> sccs = findSCCs();
        Map<Node, Integer> nodeToScc = new HashMap<>();

        for (int i = 0; i < sccs.size(); i++) {
            for (Node node : sccs.get(i)) {
                nodeToScc.put(node, i);
            }
        }

        for (int i = 0; i < nodes.length; i += 2) {
            Node varNode = nodes[i];
            Node notVarNode = nodes[i + 1];

         // 确保节点在 nodeToScc 中有值
            if (nodeToScc.containsKey(varNode) && nodeToScc.containsKey(notVarNode)) {
                if (nodeToScc.get(varNode).equals(nodeToScc.get(notVarNode))) {
                    return false;
                }
            }        }
      
        //求值
        /*
        List<Node> topoOrder = topologicalSortSCCs();
        Map<Node, Boolean> assignment = new HashMap<>();

        for (Node node : topoOrder) {
            if (!assignment.containsKey(node) && !assignment.containsKey(node.getOppo())) {
                assignment.put(node, true);
                assignment.put(node.getOppo(), false);
            }
        }

        for (Node node : nodes) {
            if (node != null) {
                node.setValue(assignment.get(node));
            }
        }
       

        return true;
    }
    */
    
    
    
    
    public List<Node> topologicalSortSCCs() {
        List<Node> topoOrder = new ArrayList<>();
        int[] inDegree = new int[sccCount];
        List<Integer>[] dag = new List[sccCount];
        
        for (int i = 0; i < sccCount; i++) {
            dag[i] = new ArrayList<>();
        }

        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (node != null) {
                int sccNum = scc[caculateN(node.getNum())];
                for (Node neighbor : GraphChart[caculateN(node.getNum())]) {
                    if (neighbor != null) {
                        int neighborSccNum = scc[caculateN(neighbor.getNum())];
                        if (sccNum != neighborSccNum && !dag[sccNum].contains(neighborSccNum)) {
                            dag[sccNum].add(neighborSccNum);
                            inDegree[neighborSccNum]++;
                        }
                    }
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < sccCount; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int sccNum = queue.poll();
            for (Node node : sccs.get(sccNum)) {
                topoOrder.add(node);
            }
            for (int neighborSccNum : dag[sccNum]) {
                inDegree[neighborSccNum]--;
                if (inDegree[neighborSccNum] == 0) {
                    queue.add(neighborSccNum);
                }
            }
        }

        return topoOrder;
    }

   
    
    
    

}
