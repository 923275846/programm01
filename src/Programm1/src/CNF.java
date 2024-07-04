package Programm1.src;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CNF {
	
	
	List<Clauses> cList= new ArrayList<>();
    List<Variables> vList=new ArrayList<>();
	
	
	public int numc;//number of clauses
	public int numv;//number of variables (actual used, the size of vList)
	public int maxv;//the largest variable number
	
	public File file;
	
	public Graph nodeGraph;
	public boolean Sat;
	
	//不用文件，直接生成的构造方法
	public CNF(int nVariables, int mClauses) {
		
		//新建两个list
        this.cList=new ArrayList<>();
        this.vList=new ArrayList<>();
        this.numc=mClauses;
        this.maxv=nVariables;
        
        Random random = new Random();//每次调用重新new了一个Random，所以不会重复使用同样的seed
        int range = 2*nVariables; 
        for (int i = 0; i < mClauses; i++) {
            int num1 = 0;
            int num2 = 0;
            while (num1 == num2 || num1 == 0 || num2 == 0) {
                num1 = random.nextInt(range + 1) - nVariables; 
                num2 = random.nextInt(range + 1) - nVariables;
            }
            
        	//查找List里有没有num的为int【】里的数字的绝对值的var，如果没有，新建var对象并加入list，如果有，就是它
          	 int absv1=Math.abs(num1);
          	 int absv2=Math.abs(num2);
          	 
          	 Variables vari1=findOrCreateV(absv1);
          	 Variables vari2=findOrCreateV(absv2);
          	 
          	 //新建一个Clause，并且加入clist,设定num为Clausenumber
          	 Clauses c=new Clauses();
          	 cList.add(c);
          	 c.setClauseNum(i+1);
          	 
          	 //设定c的各项,注意使用有正负的num，而不是abs，保证能正确设定正负
          	 buildC(num1, num2, vari1, vari2, c);
          	 //设定两个var,把新建的c加入它们的语句list
          	 buildV(vari1,c);
          	 buildV(vari2,c);         	 
          	 
        }
        
        setSAT();
		
	}
	
	//给文件，读取文件构造cnf的方法
	public CNF(File f) {
		
		readFile(f);
		setSAT();
		
	}
      
	
	
	   //阅读一个新的文档f， 并新建一个CNF，调用build和FindOrCreate方法，设定cList和vList，以及numc和numv。执行结束后，两个list里应该有正确的c和v对象。每次读取重新设定。
       public void readFile(File f)
    {
   	 
   	 try {
	        if (!f.exists()) {
	            System.err.println("File does not exist " );
	            return;
	        }
	        
	        //设定file
	        file=f;
	        //新建两个list
	        this.cList=new ArrayList<>();
	        this.vList=new ArrayList<>();
   	 
   	 try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
   		   int clauseNum=0;
   		 // 读取文件的第一行
            String firstLine = reader.readLine();
            // 尝试将第一行解析为整数
            try {
                clauseNum = Integer.parseInt(firstLine);
                //System.out.println("the Number of CLauses recorded in the first line is ：" + clauseNum);
                
               
            } catch (NumberFormatException e) {
                // 如果第一行不是数字，抛出异常
                System.err.println("the first line is not a number：" + firstLine);
            }
            

            String line;
            int Clausenumber=0;
            //读取接下来的每一行
            //当一行不为空时，为它查询list，创建c，查找有没有v，如果没有也创建v，并把c和v写入列表
            while ((line = reader.readLine()) != null) {
               
                Clausenumber ++;
                int num1=0;
                int num2=0;
              //把内容转换为int【】
           	 try {
           		// 拆分字符串并转换为整数
                    String[] parts = line.split("\\s+"); // 使用空格分隔字符串
                    if (parts.length == 2) {
                        num1 = Integer.parseInt(parts[0]);
                        num2 = Integer.parseInt(parts[1]);
                       // System.out.println("Number 1: " + num1 + ", Number 2: " + num2);
                    } else {
                        System.err.println("Invalid line format: " + line);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Line " + Clausenumber + " Error: " + e.getMessage());
                }
           	 
           	//查找List里有没有num的为int【】里的数字的绝对值的var，如果没有，新建var对象，如果有，就是它
           	 int absv1=Math.abs(num1);
           	 int absv2=Math.abs(num2);
           	 
           	 Variables vari1=findOrCreateV(absv1);
           	 Variables vari2=findOrCreateV(absv2);
           	 
           	 //新建一个Clause，并且加入clist,设定num为Clausenumber
           	 Clauses c=new Clauses();
           	 cList.add(c);
           	 c.setClauseNum(Clausenumber);
           	 
           	 //设定c的各项,注意使用有正负的num，而不是abs，保证能正确设定正负
           	 buildC(num1, num2, vari1, vari2, c);
           	 //设定两个var,把新建的c加入它们的语句list
           	 buildV(vari1,c);
           	 buildV(vari2,c);         	 
           	 
            }

            // 文档结束
            //System.out.println("End of file reached. Total Clauses read: " + Clausenumber);
            numc=cList.size();//设定读取的文件的numc
            numv=vList.size();//设定读取的文件的numv
            
            
            //设定最大的var数字
            maxv= 0;
   		    for (Variables var:vList)
   		    {
   			  if(var.getNum() >maxv)
   			  {
   				  maxv=var.getNum();
   			  }
   		    }
   		    
            
        } catch (IOException e) {
            // 文件读取错误
            System.err.println("Error by reading file：" + e.getMessage());
        }
   	 
   	 } catch (Exception e) {
	        System.err.println("Error in getting the file：" + e.getMessage());
	    }

       }
       
       
     

       //创建clause：给一个新建了的clause设定两个var和正负
       public void buildC(int v1, int v2, Variables var1, Variables var2, Clauses c)
    {
   	 boolean b1=true;
   	 boolean b2=true;
   	 if(v1<0)
   	 {
   		b1=false;
   	 }
   	
   	 if(v2<0)
   	 {
   		 b2=false;
   	 }
   	 c.setClauses(var1,var2,b1,b2);
    }
    
       //为var的clause list里加入这个clause
       public void buildV(Variables v, Clauses c)
    {
   	 v.addClauses(c); 
    }
    
       //给出绝对值，查找list里有没有对应num的var，如果有返回它，如果没有，新建它并加入list并返回它
       public Variables findOrCreateV(int num)
    {
   	 for (Variables item : vList) {
            if (item.getNum()==num) {
                return item;
            }
        }
   	 Variables newv= new Variables();
   	 newv.setNum(num);
   	 vList.add(newv);
   	 return newv;
    }

     //实验并设置是否能被满足
     public void setSAT()
       {
    	   nodeGraph = new Graph(numc, maxv);//不能使用vlist的size，因为有的var没有被使用到，vlistsize<maxv
           nodeGraph.iniGraph(cList, vList);
           if (nodeGraph.trysat()) {
               //System.out.println("this is satisfiable");
               Sat= true;
           } else {
               //System.out.println("this is unsatisfiable");
               Sat= false;
           }
       }
       
     //打印数值
     public void printValues()
       {
    	    System.out.println( "largest var"+maxv);
  		    System.out.println("numc"+numc+", numv"+numv);
		    System.out.println("clist"+cList.size()+", vlist"+vList.size());
       
       }
    

     public boolean getSAT()
     {
    	 return Sat;
     }


   //用不到了的方法：
	     
	 //写出vList的内容   
	 public void printvList() {
		 
		 
		 
		 Path currentPath = FileSystems.getDefault().getPath("").toAbsolutePath();
		    System.out.println("Current Path by Generation: " + currentPath);
		    String pathAndName=currentPath.toString();
		    pathAndName=pathAndName+"\\src\\output\\ListOfVariables";
		    System.out.println("Filename and Path: " + pathAndName);
	     try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathAndName))) {
	        
	         int i=0;
	    	 for (; i<vList.size();i++) {
	    		 
	    		 writer.write("variable ");
	    		 int vnum=vList.get(i).getNum();
	    		 writer.write(Integer.toString(vnum));
	    		 writer.write(": ");
	    		 List<Clauses> lc= vList.get(i).getClauses();
	    		 int j=0;
	    		 for(;j<lc.size();j++) {
	    			 Clauses cj=lc.get(j);
	    			 int cnum=cj.getClauseNum();
	    			 writer.write("(clause");
	    			 writer.write(Integer.toString(cnum));
	    			 writer.write("),");
	    		 }
	    		 writer.newLine();
	    	 }
	         System.out.println("File Generated：" + pathAndName);
	         
	 
	      // 打开文件
	         File file = new File(pathAndName);
	         Desktop desktop = Desktop.getDesktop();
	         if (file.exists()) {
	             desktop.open(file);
	         } else {
	             System.err.println("File does not exist.");
	         }
	         
	     } catch (IOException e) {
	         System.err.println("Errors in File Generation：" + e.getMessage());
	     }
	 
	    	 
	     }
	     
	 //写出clist的内容
	 public void printcList()
	     {
	    	
	    	 
	    	 Path currentPath = FileSystems.getDefault().getPath("").toAbsolutePath();
	    	    System.out.println("Current Path by Generation: " + currentPath);
	    	    String pathAndName=currentPath.toString();
	    	    pathAndName=pathAndName+"\\src\\output\\ListOfClauses";
	    	    System.out.println("Filename and Path: " + pathAndName);
	         try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathAndName))) {
	    	 
	    	 
	    	 
	    	 
	    	 int i=0;
	    	 for(; i<cList.size();i++){
	    		 Clauses c=cList.get(i);
	    		 int cnum=c.getClauseNum();
	    		 writer.write("Clause ");
	    		 writer.write(Integer.toString(cnum));
	    		 writer.write(": var");
	    		 writer.write(Integer.toString(c.getVar1().getNum()));
	    		 if(c.getB1())
	    		 {
	    			 writer.write(" True;");
	    		 }
	    		 else
	    		 {
	    			 writer.write(" False;");
	    		 }
	    		 writer.write(" var");
	    		 writer.write(Integer.toString(c.getVar2().getNum()));
	    		 if(c.getB2())
	    		 {
	    			 writer.write(" True;");
	    		 }
	    		 else
	    		 {
	    			 writer.write(" False;");
	    		 }
	    		 writer.newLine();
	    	 }
	    	 
	    	 
	    	 // 打开文件
	         File file = new File(pathAndName);
	         Desktop desktop = Desktop.getDesktop();
	         if (file.exists()) {
	             desktop.open(file);
	         } else {
	             System.err.println("File does not exist.");
	         }
	         
	     } catch (IOException e) {
	         System.err.println("Errors in File Generation：" + e.getMessage());
	     }
	 
	     }
	     
	 //清空文件
	 public void clearFileContent() {       
	         if (!file.exists()) {
	             System.out.println("file does not exist：" + file);
	             return;
	         }

	         try (FileWriter fileWriter = new FileWriter(file, false)) {
	             // 使用FileWriter覆盖写入空字符串，清空文件内容
	             fileWriter.write("");
	             System.out.println("file cleared：" + file);
	         } catch (IOException e) {
	             System.out.println("Failed to clear file：" + file);
	             e.printStackTrace();
	         }
	     }
	     
	     






}
