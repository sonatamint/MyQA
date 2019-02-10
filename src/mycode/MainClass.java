package mycode;

import java.io.*;
import java.util.Scanner;

public class MainClass {
	
	public static void main(String[] args) throws Exception{
		//Create branch Q1
		try{//Load task description
			FileInputStream fin=new FileInputStream("titles.txt");
			BufferedReader bufferreader = new BufferedReader(new InputStreamReader(fin));
			String[] records =bufferreader.readLine().split("/;");
			bufferreader.close();
			fin.close();
			RecmdThread rct = new RecmdThread(records);
			rct.run();
		}catch (FileNotFoundException e) {//Dup exception?
            System.out.println("The file named titles.txt does not exist in the same folder with MyQA_Vx.x.jar. " +
            		"To start recording window titles, input \"r\" from keyboard and press \"enter\", or just press \"enter\" to quit...");
            Scanner scanner=new Scanner(System.in);
			String record = scanner.nextLine();
			if(record.trim().equalsIgnoreCase("r")){
				int time=120;
				boolean correctInput=false;
				while(!correctInput){
					System.out.println("Input the time in seconds for window title capturing: (120 suggested)");
					String timeS = scanner.nextLine();
					try{
						time=Integer.parseInt(timeS);
						correctInput=true;
					}catch (NumberFormatException ex){
						System.out.println(" ! Input an Integer !");
					}
				}
				ResrcThread rst = new ResrcThread(time);
				rst.run();
			}
			else{
				System.out.println("Program exited.");
	            System.exit(1);//1-no title file
			}
        }
	}
}
