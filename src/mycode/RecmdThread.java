package mycode;

import java.util.Collections;
import java.util.Scanner;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.util.regex.*;
import java.io.*;

public class RecmdThread extends Thread {
	
	String[] titles;
	int startTitle;//上次退出前处理完毕的title编号+1
	int currentID;//概念出现的位次
	String context;
	PlrSgl converter;
	MaxentTagger mytagger;
	int neighborNo;
	int mateNo;
	int historyLength;
	
	public RecmdThread(String[] Titles){//可以读入前次没运行到底的例子
		this.titles=Titles;
		this.currentID=0;
		this.startTitle=0;
		this.context="ansys";
		this.neighborNo=4;
		this.mateNo=4;
		this.historyLength=8;
		converter= PlrSgl.getInstance();
		try{//Load the tagger
			this.mytagger = new MaxentTagger("left3words-wsj-0-18.tagger");
		}catch(Exception e){
			System.out.println(e.toString()+"-- Error in loading the Stanford POS tagger. Program exited.");
            System.exit(3);//3-POS tagger error
		}
		ObjectInputStream objr = null;
		try{//Load previous unfinished running
			objr = new ObjectInputStream(new FileInputStream("objects.sav"));
			this.startTitle=objr.readInt();
			this.historyLength=objr.readInt();
			this.neighborNo=objr.readInt();
			this.mateNo=objr.readInt();
			this.context=objr.readUTF();
			while(true){
				Object o = objr.readObject();
				if(o instanceof SearchItem){
					SearchItem added = (SearchItem)o;
					SearchItem.Items.add(added);
				}
				if(o instanceof Performance){
					Performance added = (Performance)o;
					Performance.Perfos.add(added);
				}
				if(o instanceof Mate){
					break;
				}
			}
			objr.close();
			this.currentID=SearchItem.Items.get(SearchItem.Items.size()-1).ID+1;
		}
		catch (FileNotFoundException e){
			System.out.println("No history data, a brand new start. To change the configuration, enter \"c\" from keyboard, to use the default setting, just press \"enter\"...");
			Scanner scanner=new Scanner(System.in);
			String change = scanner.nextLine();
			if(change.trim().equalsIgnoreCase("c")){
				System.out.println("Input a word describing the knowledge domain(if no domain restriction, just press \"enter\") of the task, spell carefully since this cannot be retried):");
				String contextS = scanner.nextLine();
				if(contextS.trim().length()==0){
					this.context="";
				}
				else{
					this.context=contextS.trim().toLowerCase();
				}
				boolean correctInput=false;
				while(!correctInput){
					System.out.println("Input the number of concepts looking back when finding neighbors:");
					String historyS = scanner.nextLine();
					try{
						this.historyLength=Integer.parseInt(historyS);
						correctInput=true;
					}catch (NumberFormatException ex){
						System.out.println(" ! Input an Integer !");
					}
				}
				correctInput=false;
				while(!correctInput){
					System.out.println("Input the number of adjacent neighbors used in searching knowledge:");
					String neighS = scanner.nextLine();
					try{
						this.neighborNo=Integer.parseInt(neighS);
						correctInput=true;
					}catch (NumberFormatException ex){
						System.out.println(" ! Input an Integer !");
					}
				}
				correctInput=false;
				while(!correctInput){
					System.out.println("Input the number of verbs used for each concept when searching knowledge:");
					String verbS = scanner.nextLine();
					try{
						this.mateNo=Integer.parseInt(verbS);
						correctInput=true;
					}catch (NumberFormatException ex){
						System.out.println(" ! Input an Integer !");
					}
				}
			}
			else{
				System.out.println("Using the default settings:");
				System.out.println("Topic: "+this.context);
				System.out.println("Looking back: "+this.historyLength);
				System.out.println("Neighbor number: "+this.neighborNo);
				System.out.println("Verbs of each noun: "+this.mateNo);
			}
		}
		catch (IOException e) {
            System.out.println("Data in objects.sav are broken, delete it and retry. Now exiting...");
            System.exit(4);//4-sav file error
        }
		catch (ClassNotFoundException e) {
            System.out.println("Data in objects.sav are broken, delete it and retry. Now exiting...");
            System.exit(4);
        }
		catch (IndexOutOfBoundsException e) {
            System.out.println("Data in objects.sav are broken, delete it and retry. Now exiting...");
            System.exit(4);
        }
	}
	
	public void writeObj(int ongoing){
		ObjectOutputStream objw = null;
		try{
			objw = new ObjectOutputStream(new FileOutputStream("objects.sav"));
			objw.writeInt(ongoing);
			objw.writeInt(this.historyLength);
			objw.writeInt(this.neighborNo);
			objw.writeInt(this.mateNo);
			objw.writeUTF(this.context);
			for(SearchItem si:SearchItem.Items){
				objw.writeObject(si);
			}
			for(Performance pf:Performance.Perfos){
				objw.writeObject(pf);
			}
			objw.writeObject(new Mate("End"));//结束标记
			objw.close();
		}catch (Exception e) {//不容易出现问题
            System.out.println(e.toString());
        }
	}
	
	public void run(){
		if(this.startTitle>=titles.length){//all titles have been processed
			int LowThreshold=2;
			int max=5;
			int min=2;
			Scanner scanner=new Scanner(System.in);
			System.out.println("The searching phase seems done, to check the performance--");
			boolean correctInput=false;
			while(!correctInput){
				System.out.println("Input the lower bound of the score of knowledge pieces recommended: (For example, 2)");
				String LowThresholdS = scanner.nextLine();
				try{
					LowThreshold=Integer.parseInt(LowThresholdS);
					correctInput=true;
				}catch (NumberFormatException e){
					System.out.println(" ! Input an Integer !");
				}
			}
			correctInput=false;
			while(!correctInput){
				System.out.println("Input the upper bound of the number of knowledge pieces recommended at a time: (For example, 5)");
				String maxS = scanner.nextLine();
				try{
					max=Integer.parseInt(maxS);
					correctInput=true;
				}catch (NumberFormatException e){
					System.out.println(" ! Input an Integer !");
				}
			}
			correctInput=false;
			while(!correctInput){
				System.out.println("Input the lower bound of the number of knowledge pieces recommended at a time: (For example, 2)");
				String minS = scanner.nextLine();
				try{
					min=Integer.parseInt(minS);
					correctInput=true;
				}catch (NumberFormatException e){
					System.out.println(" ! Input an Integer !");
				}
			}
			Performance.Print(LowThreshold, max, min);
			return;
		}
		for(SearchItem captured:SearchItem.Items){
			System.out.println(captured.content.Singular);
		}
		System.out.println("Above are captured concepts, now starting with the "+(this.startTitle+1)+"th title:");
		for (int i=this.startTitle;i<titles.length;i++){//if some titles have not been processed
			Performance thistime = new Performance(titles[i],i);
			String lowCase="";
			if(this.context.length()>0){//不再重复搜索主题词
				lowCase=titles[i].toLowerCase().replaceAll("\\b"+this.context+"\\b", "");
			}
			else{
				lowCase=titles[i].toLowerCase();
			}
			String tagged=this.mytagger.tagString(lowCase);
			Pattern NNNNS = Pattern.compile("(\\b\\w+/NN ?)+");
			Matcher finding = NNNNS.matcher(tagged);
			while(finding.find()){//一个复合名词词组
				String storage="";
    			String nns = finding.group();
    			Pattern NN = Pattern.compile("\\b\\w+(?=/NN)");
    			Matcher noun = NN.matcher(nns);
    			while(noun.find()){
        			storage+=noun.group()+",";
        		}
    			String[] asItems=storage.split(",");
    			String words="";
    			for(int j=0;j<asItems.length;j++){//复合名词数组,最后一个可能是复数
    				words+=this.converter.singularize(asItems[j])+" ";//对以s结尾的专有名词会产生错误
    				boolean isDup=false;
        			for(int k=0;k<SearchItem.Items.size();k++){//发现以往存在的概念，将原概念的id置为最新
        				SearchItem checkDup=SearchItem.Items.get(k);
        				if(checkDup.content.Singular.equals(words.trim())){//不改变SearchItem.Items的元素个数
        					checkDup.ID=currentID;
        					currentID+=1;
        					isDup=true;
        					break;
        				}
        			}
        			if(!isDup){
        				SearchItem item=new SearchItem(currentID,new Content(words.trim(),this.converter),this.context,words.trim().contains(" "));
        				currentID+=1;
        				if(!item.SearchGoogle()){//当调用搜索引擎出现错误,保存当前对象并退出
        					System.out.println("Seems Google has banned this IP for too many queries, retry in about 6 hours!");
        					System.out.println("Program exited.");
        		            System.exit(5);//5-Google error
        				}
        				item.Findmate(this.mytagger);
        			}
    			}
    			Collections.sort(SearchItem.Items);//保证最新获取的概念在最底部
    			int innerScore=-1;//比较一个复合名词分解出各概念的mate多少作为推荐依据
    			int maxIndex=SearchItem.Items.size()-1;
    			for(int j=0;j<asItems.length;j++){
    				int scr= SearchItem.Items.get(SearchItem.Items.size()-1-j).Mates.size();
    				if(scr>innerScore){
    					innerScore=scr;
    					maxIndex=SearchItem.Items.size()-1-j;
    				}
    			}
    			if(maxIndex!=SearchItem.Items.size()-1){
    				SearchItem toRec = SearchItem.Items.get(maxIndex);
        			thistime.nounGroups.add(toRec.content.Singular);
        			if(!toRec.Gdistance(this.historyLength)){
        				System.out.println("Seems Google has banned this IP for too many queries, retry in about 6 hours!");
        				System.out.println("Program exited.");
    		            System.exit(5);//5-Google error
        			}
        			String pairstore="";
        			for(int p=0;p<toRec.Pairs.size();p++){
        				Pair pair=toRec.Pairs.get(p);
                		pairstore+=pair.pairItem.content.Singular+"-"+pair.cooccurrence+" ";
            		}
        			thistime.pairs.add(pairstore);
        			if(!toRec.Recommend(i,this.neighborNo,this.mateNo,this.historyLength)){
        				System.out.println("Seems Google has banned this IP for too many queries, retry in about 6 hours!");
        				System.out.println("Program exited.");
    		            System.exit(5);//5-Google error
        			}
        			for(Recommendation rcm:toRec.Recs){//添加属于本次搜索产生的推荐
        				if(rcm.sectionNo==i){
        					thistime.recommendations.add(rcm);
        				}
    				}
    			}//复合名词子概念中mate最多的以及复合名词全称被搜索邻居并推荐
    			SearchItem toRec = SearchItem.Items.get(SearchItem.Items.size()-1);
    			thistime.nounGroups.add(toRec.content.Singular);
    			if(!toRec.Gdistance(this.historyLength)){
    				System.out.println("Seems Google has banned this IP for too many queries, retry in about 6 hours!");
    				System.out.println("Program exited.");
		            System.exit(5);//5-Google error
    			}
    			String pairstore="";
    			for(int p=0;p<toRec.Pairs.size();p++){
    				Pair pair=toRec.Pairs.get(p);
            		pairstore+=pair.pairItem.content.Singular+"-"+pair.cooccurrence+" ";
        		}
    			thistime.pairs.add(pairstore);
    			if(!toRec.Recommend(i,this.neighborNo,this.mateNo,this.historyLength)){
    				System.out.println("Seems Google has banned this IP for too many queries, retry in about 6 hours!");
    				System.out.println("Program exited.");
		            System.exit(5);//5-Google error
    			}
    			for(Recommendation rcm:toRec.Recs){//添加属于本次搜索产生的推荐
    				if(rcm.sectionNo==i){
    					thistime.recommendations.add(rcm);
    				}
				}
    		}
			System.out.println("saving current...");
			writeObj(i+1);
			System.out.println("savings done!");
		}
		System.out.println("Searching phase done. Program exited. To check the performance, start the program again.");
	}
}
