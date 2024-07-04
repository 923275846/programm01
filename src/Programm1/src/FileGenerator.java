package Programm1.src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileGenerator {
	
	
	File newGeneratedFile;
	File fileFolder;
	
	//空构造方法  
	public FileGenerator() {
		  
	  }
	
	
	//构造方法
	public FileGenerator(File Folder) {
		fileFolder=Folder;
	}
	
	
	  //在文件夹folder里创建名为filename的空文件的方法，并设定newGeneratedFile为这个名字的文件，无论新建立还是已经存在
	   public void newF(String filename)
	   {
		   File file = new File(fileFolder, "File"+ filename + ".txt");
        try {
            // 创建新文件
            if (file.createNewFile()) {
                //System.out.println("File " + file.getName() + " Generated");//新建了名为filename的文件
                
                
            } else {
                //System.out.println("File " + file.getName() + " already exists");//名为filename的文件已经存在
               
            }
        } catch (IOException e) {
            System.out.println("Error occured: Generating File");
            e.printStackTrace();
        }
		    newGeneratedFile=file;
           
	   }
	   
	   //返回新生成的文件
	   public File getNewFile()
	   {
		   return newGeneratedFile;
	   }
	   
	   //重新设定文件夹
	   public void setFolder(File folder)
	   {
		   fileFolder=folder;
	   }
	   
	 
	   //随机生成并在文件file里写入固定v的数量和c的数量的pairs的方法
	   public void genPairs(int nVariables, int mClauses, File file) {
        Random random = new Random();//每次调用重新new了一个Random，所以不会重复使用同样的seed
        int range = 2*nVariables; 
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
           
            String stringN=Integer.toString(mClauses);
            writer.write(stringN);
            writer.newLine();

            
            for (int i = 0; i < mClauses; i++) {
                int first = 0;
                int second = 0;
                while (first == second || first == 0 || second == 0) {
                    first = random.nextInt(range + 1) - nVariables; 
                    second = random.nextInt(range + 1) - nVariables;
                }
                writer.write(first + " " + second);
                writer.newLine();
            }
            
            //System.out.println("File Generated：" + file);
            
            writer.close();
           
        } catch (IOException e) {
            System.err.println("Errors in File Generation：" + e.getMessage());
        }
    }
	   
	   
	   //在文件末尾写上是否可满足的方法
	   public void addResult(File f)
	 	{
	 		
	 		boolean b= Test(f);
	 		try (FileWriter fw = new FileWriter(f, true); // 打开文件追加模式
	 	             BufferedWriter bw = new BufferedWriter(fw)) {

	 			bw.newLine(); // 写入换行符    
	 			String lineToAdd = b ? "True" : "False"; // 根据布尔值确定要写入的内容
	 	            bw.write(lineToAdd); // 写入内容
	 	            bw.newLine(); // 写入换行符
	 	            //System.out.println("Successfully appended '" + lineToAdd + "' to the file.");
	 	            
	 	        } catch (IOException e) {
	 	            System.err.println("An error occurred while appending to the file.");
	 	            e.printStackTrace();
	 	        }
	 	}
	 	
	 	
	   //给一个文件, 为它创建cnf（读取等方法在cnf类里），返回是否能满足的布尔值
 	   public boolean Test(File f) {
 		   
 		 CNF c= new CNF(f);
 		 return c.getSAT();	   
 	   }
   
      
 	   //在不写入读取的情况下直接生成m从一定数量开始到一定数量结束的固定v数量的文件的方法
 	   //里面每行写m数和结果
 	   public void genList(int v, int startingC, int endingC, int interval )
 	   {
 		   
 		   newF("ListOfResults");
 		   try (BufferedWriter writer = new BufferedWriter(new FileWriter(newGeneratedFile, true)))
 		   {     
 			   System.out.println("Variables:"+v+", from "+startingC+" Clauses to "+endingC+" Clauses, interval: "+interval);
 			   writer.write("Variables:"+v+", from "+startingC+" Clauses to "+endingC+" Clauses, interval: "+interval);
 			   writer.newLine();//换行
 		  //省去写入和读取的过程，直接生成并计算一个cnf的可满足性
  		   int i=startingC;
  		   while(i<=endingC) 		   
  		   {
  			 writer.write(Integer.toString(i));//写句子数
  			 
  			 int p=0;//p是可满足的概率
  			 for(int j=0;j<100;j++)//尝试100次
  			 {
  				if( new CNF(v, i).getSAT())
  					p++;//当可满足时概率加1
  			 }
  			 
  			 { writer.write(", "+p);}
	          
	         writer.newLine();//换行
  			 i+=interval;//增加interval
  		   }
 		   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	   }
 	   
 	   public File returnList()
 	   {
 		   return newGeneratedFile;
 	   }
}
