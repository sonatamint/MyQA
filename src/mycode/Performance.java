package mycode;

import java.util.Collections;
import java.util.ArrayList;
import java.io.*;

public class Performance implements Serializable{
		
	int titleNo;
	String title;
	ArrayList<String> nounGroups;
	ArrayList<String> pairs;
	ArrayList<Recommendation> recommendations;
	public static ArrayList<Performance> Perfos = new ArrayList<Performance>();
	
	public Performance(String titl,int tn){
		this.title=titl;
		this.titleNo=tn;
		this.pairs=new ArrayList<String>();
		this.nounGroups=new ArrayList<String>();
		this.recommendations=new ArrayList<Recommendation>();
		Perfos.add(this);
	}
	
	public void AddNounGrp(String nG){
		this.nounGroups.add(nG);
	}
	
	public void AddRecommendation(Recommendation rD){
		this.recommendations.add(rD);
	}
	
	public static void Print(int lowest, int Max, int Min){//尽量在异常发生处处理而不是由调用者处理
		try{
			FileOutputStream fout=new FileOutputStream("out.txt");
			PrintStream ps=new PrintStream(fout);
			for(SearchItem item:SearchItem.Items){
				ps.println("ACTIONS found for "+item.content.Singular+":");
				for(Mate mm:item.Mates){
					ps.print(mm.content+"-"+mm.times+" ");
				}
				ps.print("\r\n");
			}
			for(Performance pf:Perfos){
				ps.println("For the window title \""+pf.title+"\":");
				for(int i=0;i<pf.nounGroups.size();i++){
					String compound=pf.nounGroups.get(i);
					ps.println("  For the concept \""+compound+"\" we have found related concepts:");
					if(!pf.pairs.isEmpty()){
						ps.println("  "+pf.pairs.get(i));
					}
				}
				Collections.sort(pf.recommendations);
				int count=0;
				for(int j=0;j<pf.recommendations.size();j++){
					Recommendation rd=pf.recommendations.get(j);
					if(rd.priority<lowest&&count>=Min){
						break;
					}
					if(rd.mateForthis){
						ps.println(" @ "+rd.priority+"  "+rd.mateName+" "+rd.Name+"  "+rd.pairName);
					}
		    		else{
		    			ps.println(" @ "+rd.priority+"  "+rd.mateName+" "+rd.pairName+"  "+rd.Name);
		    		}
					for(String s:rd.knowledge.Sentences){
						ps.println("    "+s);
					}
					count+=1;
					if(count>=Max){
						break;
					}
				}
			}
			System.out.println("Output done! See out.txt in the same folder with MyQA_Vx.x.jar. Program exited.");
			ps.close();
			fout.close();
		}catch (Exception e){
			System.out.println(e);
		}
	}
}
