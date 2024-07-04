package Programm1.src;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	static String filename="OutputProgramm1";
	static File newfile;
	static List<Clauses> cList= new ArrayList<>();
	static List<Variables> vList=new ArrayList<>();
	
	
	static int numc;//number of clauses for to be generated CNF
	static int numv;//number of variables for to be generated CNF
	static int numf;//number of files to be generated
	public static File folder;
	
	static int numVar;
	static int numStartingC;
	static int numEndingC;
	static int interval;
	
	
	static Path filePath;
	static File generatedFile;
    
	public static void main(String args[]) {
		
	try (Scanner scanner = new Scanner(System.in)) {
			
			System.out.println("input 1 to generate a number of Files with same numc and numv;");
			System.out.println("input 2 to generate a number of Files with same numv and increasing numc;");
			
			if (scanner.hasNextInt()) {
			    int firstInput = scanner.nextInt();
			    
			    
			    // 输入为1时，生成一定量的同样c和v的文件
			    if (firstInput == 1) {
			    	getFCV(scanner);
			        GenIFile(numf);
			        //测试每个文件的结果并写在末尾
			        System.out.println("Finished!");
			        
			    }
			    
			    //输入为2时，接受一个v，一个起始c，一个数量
			    else if(firstInput==2) {
			    	
			    	getRecordArgs(scanner);
			    	TestRecord(numVar, numStartingC,numEndingC,interval);
			    	
			    	System.out.println("Finished!");
			    }
			    	
			    
			    else
			    {System.out.print("The number must be larger than 0");}
			    
			    
			  }
		}
	
    }
	
	
    

	//接受输入的fcv，设置numf， numv和numc。
	public static void getFCV(Scanner scanner)
	{
		// 接受文件数量
        System.out.print("please Input the number of Files you want to generate: ");
        if (scanner.hasNextInt()) {
            numf = scanner.nextInt();
        } else {
            System.out.println("Please make sure your input is an int");
            scanner.next(); // 清除无效输入
        }

        // 接受变量数量
        System.out.print("please input the number of variables: ");
        if (scanner.hasNextInt()) {
            numv = scanner.nextInt();
        } else {
            System.out.println("Please make sure your input is an int");
            scanner.next(); // 清除无效输入
        }

        // 接受句子数量
        System.out.print("please input the number of clauses: ");
        if (scanner.hasNextInt()) {
            numc = scanner.nextInt();
        } else {
            System.out.println("Please make sure your input is an int");
            scanner.next(); // 清除无效输入
        }   
		
	}
	
	//生成i个文件，命名为FileI，并调用GenPairs方法在里面写入pairs， 在结尾写上是否满足
	public static void GenIFile(int i)
	{
		// 获取桌面路径
        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop";

        // 定义生成文件夹路径
        folder = new File(desktopPath + File.separator + "GeneratedFiles");

        // 如果文件夹不存在则创建
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        FileGenerator fg=new FileGenerator(folder);//创建文件编辑器，设定好路径文件夹
        
        // 在文件夹中调用newF生成i个文件，文件名分别为File1到i， 并且调用genPairs写好内容， 调用addResult写好对错
        for (int j = 1; j <= i; j++) { 
        	fg.newF("File"+j);//生成名为j的新空文件
        	File f=fg.getNewFile();//获取新生成的文件
        	fg.genPairs(numv,numc,f);//为文件生成numv，numc的随机变量和句子
        	fg.addResult(f);//为它加上是否能满足在末尾
        }
	}
	

     
     
	   public static void getRecordArgs(Scanner scanner) {
		   
		// 接受变量数量 v
	        System.out.print("please Input the number of Variables: ");
	        if (scanner.hasNextInt()) {
	            numVar = scanner.nextInt();
	        } else {
	            System.out.println("Please make sure your input is an int");
	            scanner.next(); // 清除无效输入
	        }

	        // 接受句子起始数量 starting c
	        System.out.print("please input the starting Clause number: ");
	        if (scanner.hasNextInt()) {
	            numStartingC = scanner.nextInt();
	        } else {
	            System.out.println("Please make sure your input is an int");
	            scanner.next(); // 清除无效输入
	        }

	       // 接受句子结束数量 ending c
	        System.out.print("please Input the ending Clause number: ");
	        if (scanner.hasNextInt()) {
	            numEndingC = scanner.nextInt();
	        } else {
	            System.out.println("Please make sure your input is an int");
	            scanner.next(); // 清除无效输入
	        }

	        // 接受间隔大小
	        System.out.print("please input interval: ");
	        if (scanner.hasNextInt()) {
	            interval = scanner.nextInt();
	        } else {
	            System.out.println("Please make sure your input is an int");
	            scanner.next(); // 清除无效输入
	        }
	        
		   
	   }

  	   //给变量数，从开始的句子数开始，测试一定数量的可满足性，并记录
  	   public static void TestRecord(int v, int startingC, int endingC, int interval )
  	   {
  		   

  			// 获取桌面路径
  	        String userHome = System.getProperty("user.home");
  	        String desktopPath = userHome + File.separator + "Desktop";

  	        // 定义生成文件夹路径
  	        folder = new File(desktopPath + File.separator + "GeneratedFiles");

  	        // 如果文件夹不存在则创建
  	        if (!folder.exists()) {
  	            folder.mkdir();
  	        }
  	        
  	        FileGenerator fg=new FileGenerator(folder);//创建文件编辑器，设定好路径文件夹
  	        
  	        //调用文件编辑器的方法来写高铝列表
  	        fg.genList(v, startingC, endingC, interval );
  	        
  	        ChartGenerator cg=new ChartGenerator(fg.returnList());
  	        cg.GenChart();
  	        System.out.println("Finished");
  		  
  		   
  	   }
  	   

     
}
