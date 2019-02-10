package mycode;

import java.io.FileOutputStream;
import java.io.PrintStream;
import com.jacob.com.*;//jacob.dll放在system32下面

public class ResrcThread extends Thread{
	
	String current;
	Dispatch test;
	int time;
	
	public ResrcThread(int timeInSeconds){
		try{
			this.test = new Dispatch("GetTitle.Title");
			this.time=timeInSeconds;
		}catch (Exception e){
			System.out.println(e.toString());
			System.out.println("Problem in calling windows API functions, may need to manually create the title.txt file. Program exited.");
			System.exit(2);//2-WinAPI calling failed
		}
	}
	public void run(){
		Long strt = System.currentTimeMillis();
		try{
			FileOutputStream fout=new FileOutputStream("titles.txt");
			PrintStream ps=new PrintStream(fout);
			while((System.currentTimeMillis()-strt)<time*1000){
				String title = Dispatch.call(this.test, "GetTitle").toString();
				if(!title.equals(current)&&(title.length()>1)){
					current=title;
					ps.print(current+"/;");
				}
				//Thread.sleep(200);
			}
			ps.close();
			fout.close();
			System.out.println("Recording completed, restart the program to search the Internet, program exited.");
			System.exit(0);
		}catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Problem in calling windows API functions, may need to manually create the title.txt file. Program exited.");
			System.exit(2);//2-WinAPI calling failed
        }
		
		
	}
}
