package mycode;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class SearchItem implements Comparable<SearchItem>,Serializable{
	
	public int ID;//�˸�����ֵ�λ��
	public boolean isCompound;//�Ƿ񸴺�����
    public ArrayList<Mate> Mates;
    public ArrayList<Recommendation> Recs;
    public ArrayList<Pair> Pairs;
    public static ArrayList<SearchItem> Items = new ArrayList<SearchItem>();
    public Content content;
    public String context;
    private transient QuryTmplt template;
    
    public SearchItem(int id ,Content content ,String context, boolean compound){
        this.ID=id;
        this.content=content;
        this.context=context;
        this.isCompound=compound;
        this.template = new QuryTmplt(this.content, this.context);
        this.Mates = new ArrayList<Mate>();
        this.Recs = new ArrayList<Recommendation>();
        this.Pairs = new ArrayList<Pair>();
        SearchItem.Items.add(this);
    }
    
    public boolean SearchGoogle(){
    	try {
        	InetAddress addr = InetAddress.getByName("WWW.GOOGLE.COM.TW");
        	for (int i=0; i<this.template.Queries.length; i++){
        		System.out.println(this.template.Queries[i]);//!!!!!!!!!!!
        		//System.out.println(this.template.RegexPatt[i]);//!!!!!!!!!!!
        		Socket socket = new Socket(addr, 80);//����Socket�������������������ͨ��
        		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
        		wr.write("GET " + this.template.Queries[i] + " HTTP/1.0\n");
                wr.write("Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/msword, application/vnd.ms-powerpoint, */*\n");
                wr.write("Accept-Language: en-us\n");
                wr.write("Accept-Encoding: gzip, deflate\n");
                wr.write("User-Agent: Mozilla/4.0\n");
                wr.write("Host: WWW.GOOGLE.COM.TW\n");
                wr.write("\n");
                wr.flush();
                //���շ��������صĲ�ѯ���
                BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line="";
                while (true) {
                    line = rd.readLine();
                    //System.out.println(line);//!!!!!!!!!!!
                    if(line.length()>4000){//
                    	//if(line.contains("class=\"g\"")){//��׼ȷ����ȡ���������
                    		//System.out.println(line);//!!!!!!!!!!!
                    		Pattern pattern = Pattern.compile(this.template.RegexPatt[i],Pattern.CASE_INSENSITIVE);
                    		Matcher matcher = pattern.matcher(line);
                    		while(matcher.find()){
                    			this.template.Results[i]+=matcher.group().replace("</b>", "").replace("<br>  ", "").replace("<b>", "").toLowerCase()+"\n";
                    		}
                    		break;
                    	//}//
                    }
                }
                wr.close();
                rd.close();
            }
            return true;
        }catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    public void Findmate(MaxentTagger tagger){
    	for (int i=0; i<this.template.Queries.length; i++){
    		String[] s = this.template.Results[i].split("\n");
    		//String[] t = s.clone();
    		this.template.Results[i]=""+s.length;//�洢���ʾ�ģ�������������
    		Pattern synpattern = Pattern.compile(this.template.SyntcPatt[i]);//,Pattern.CASE_INSENSITIVE);
    		//System.out.println("POS of the results returned by query "+i+" :");//!!!!!!!!!!
    		for (int j=0; j<s.length; j++){
    			boolean dup=false;
        		//for (int k=0;k<j;k++){//�ų�һģһ�����ʾ�
        			//if(t[k].trim().equals(s[j].trim())){//
        			//	dup=true;//
        			//	break;//
        			//}//
        		//}//
        		if (dup==false){
        			s[j]=tagger.tagString(s[j]);//���Ա�ע�Դ�Сд����
            		//System.out.println(s[j]);//!!!!!!!!!!
            		Matcher synmatcher = synpattern.matcher(s[j]);
            		if(synmatcher.find()){
            			int cc=0;
            			for(Mate m:Mates){//ͳ�Ƹ��ʳ��ִ���
            				if(m.content.equalsIgnoreCase(synmatcher.group())){//����IgnoreCaseҲ��
            					m.times+=1;
            					cc=1;
            					break;
            				}
            			}
            			if(cc==0)
            			this.Mates.add(new Mate(synmatcher.group()));
            		}
        		}
    		}
    	}
    	Collections.sort(this.Mates);
    }
    
    public boolean Gdistance(int scale){//��ǰ������֮ǰ������������,����ҳ��ѯ���������ͬ��һ��Ĵ�����ʾ
    	try {
        	//System.out.println("SENtences where current item("+this.Content+") and previous items cooccur: ");//!!!!!!!!!
    		int thisindex=SearchItem.Items.indexOf(this);
    		InetAddress addr = InetAddress.getByName("WWW.GOOGLE.COM.TW");
        	for (int i=1;i<=thisindex&&i<scale+1;i++){//����֮ǰscale������Ƚ�,�±겻��Խ��
        		SearchItem item=SearchItem.Items.get(thisindex-i);
        		boolean contains=false;
        		if(this.isCompound||item.isCompound){//��������Ǹ�������,��pair�������Ӵʴ�
        			if(this.content.Singular.contains(item.content.Singular)||item.content.Singular.contains(this.content.Singular)){
        				contains=true;
        			}
        		}
        		boolean alreadyPair=false;
        		for(Pair pa:this.Pairs){
        			if(pa.pairItem.content==item.content){
        				alreadyPair=true;
        				break;
        			}
        		}
        		if(!alreadyPair&&!contains){
        			System.out.println("   /search?q="+this.content.Singular.replace(" ", "+")+"+"+item.content.Singular.replace(" ", "+")+"+"+this.context+"&hl=en&num=50");//!!!!!!!!!!!
        			Socket socket = new Socket(addr, 80);
            		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            		wr.write("GET " + "/search?q="+this.content.Singular.replace(" ", "+")+"+"+item.content.Singular.replace(" ", "+")+"+"+this.context+"&hl=en&num=50" + " HTTP/1.0\n");//�����ʵ�����ʽ��������
                    wr.write("Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/msword, application/vnd.ms-powerpoint, */*\n");
                    wr.write("Accept-Language: en-us\n");
                    wr.write("Accept-Encoding: gzip, deflate\n");
                    wr.write("User-Agent: Mozilla/4.0\n");
                    wr.write("Host: WWW.GOOGLE.COM.TW\n");
                    wr.write("\n");
                    wr.flush();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    while (true) {
                        line = rd.readLine();
                        if(line.length()>4000){
                        	//if(line.contains("class=\"g\"")){
                        		//System.out.println(line);//!!!!!!!!!!!
                        		Pattern pattern = Pattern.compile("("+this.content.Singular+"[ <>/\\w]*"+item.content.Singular+")|("+item.content.Singular+"[ <>/\\w]*"+this.content.Singular+
                    					")|("+this.content.Singular+"[ <>/\\w]*"+item.content.Plural+")|("+item.content.Singular+"[ <>/\\w]*"+this.content.Plural+
                    					")|("+this.content.Plural+"[ <>/\\w]*"+item.content.Singular+")|("+item.content.Plural+"[ <>/\\w]*"+this.content.Singular+
                    					")|("+this.content.Plural+"[ <>/\\w]*"+item.content.Plural+")|("+item.content.Plural+"[ <>/\\w]*"+this.content.Plural+")",Pattern.CASE_INSENSITIVE);
                    			Matcher matcher = pattern.matcher(line);
                        		int cooccur=0;
                        		while(matcher.find()){//ԭʼ��ѯ��ϳ�����3��
                        			cooccur+=1;
                        			//System.out.println(matcher.group());//!!!!!!!!!!
                        		}
                        		this.Pairs.add(new Pair(item,cooccur));
                        		item.Pairs.add(new Pair(this,cooccur));//����Pair
                        		break;
                        	//}
                        }
                    }
                    wr.close();
                    rd.close();
        		}
            }
        	Collections.sort(this.Pairs);//�����¾�item��Ҫ��Recommend()֮ǰִ����һ����
        	return true;
        }catch (Exception e) {
            System.out.println(e.toString());
            return false;
        } 
    }
    
    public boolean Recommend(int tn,int np,int nm,int hl){
    	try {
        	InetAddress addr = InetAddress.getByName("WWW.GOOGLE.COM.TW");
        	for (int i=0;(i<np)&&(i<this.Pairs.size());i++){//ѡǰnp������������pairItem��������
        		Pair pair = this.Pairs.get(i);
        		SearchItem item = pair.pairItem;
        		if(!pair.searched&&(item.ID>=this.ID-hl)){//�ɸ���un-searched���ھӿ��ܷǳ���****
            		int li=nm;//����鵽use��mate��������³�ȡ
            		for(int j=0;(j<li)&&(j<this.Mates.size());j++){//ѡ��ǰItemǰnm������������mate��������
            			Mate mate= this.Mates.get(j);
            			int c=mate.times;
            			if(mate.content.equals("use")||mate.content.equals("utilize")||mate.content.equals("make")||mate.content.equals("have")){//Stop word list
            				li+=1;
            			}
            			KnowledgePiece kp = new KnowledgePiece();
            			//System.out.println("   For PAIR "+item.content.Original+" and Mate "+mate.content+":");//!!!!!!!!!!!!
            			System.out.println("      /search?q="+mate.content+"+"+this.content.Singular.replace(" ", "+")+"+"+item.content.Singular.replace(" ", "+")+"+"+this.context+"&hl=en&num=30");//!!!!!!!!!!!!
            			Socket socket = new Socket(addr, 80);
                		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
                		wr.write("GET " + "/search?q="+mate.content+"+"+this.content.Singular.replace(" ", "+")+"+"+item.content.Singular.replace(" ", "+")+"+"+this.context+"&hl=en&num=30" + " HTTP/1.0\n");
                        wr.write("Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/msword, application/vnd.ms-powerpoint, */*\n");
                        wr.write("Accept-Language: en-us\n");
                        wr.write("Accept-Encoding: gzip, deflate\n");
                        wr.write("User-Agent: Mozilla/4.0\n");
                        wr.write("Host: WWW.GOOGLE.COM.TW\n");
                        wr.write("\n");
                        wr.flush();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line="";
                        while (true) {
                            line = rd.readLine();
                            if(line.length()>4000){
                            	//if(line.contains("class=\"g\"")){
                            		Pattern pattern = Pattern.compile("href=[^>]*>((?!</a>)[^\\.\\?!])*("+this.content.Plural+"|"+this.content.Singular+")((?!</a>)[^\\.\\?!])*|(?<=class=\"j\"><font size=\"-1\">)[^\\.\\?!]*("+this.content.Plural+"|"+this.content.Singular+")[^\\.\\?!]*|(?<=\\. |\\.</b> )((?!</a>)[^\\.\\?!])*("+this.content.Plural+"|"+this.content.Singular+")((?!</a>)[^\\.\\?!])*",Pattern.CASE_INSENSITIVE);
                            		Matcher matcher = pattern.matcher(line);
                            		while(matcher.find()){
                            			String rr = matcher.group();
                            			rr=rr.replaceAll("href=[^>]*>", "");
                            			String r=rr.toLowerCase();
                            			if((r.contains(item.content.Plural)||r.contains(item.content.Singular))&& r.contains(mate.content)&& r.contains(" ")&& !r.contains("<i>")){//ͳһ��Сд
                            				String s=rr.replace("<b>", "").replace("</b>", "").replace("<br>  ", "");
                            				//sentence parsing****
                            				kp.Sentences.add(s);
                            				kp.Urls.add("");
                            			}
                            		}
                            		break;
                            	//}
                            }
                        }
                        wr.close();
                        rd.close();
                        if(!kp.Sentences.isEmpty()){
            				this.Recs.add(new Recommendation(tn,c*kp.Sentences.size(),this.content.Singular,item.content.Singular,mate.content,true,kp));
            				item.Recs.add(new Recommendation(tn,c*kp.Sentences.size(),item.content.Singular,this.content.Singular,mate.content,false,kp));
                        }
            		}
            		li=nm;
            		for(int j=0;(j<li)&&(j<item.Mates.size());j++){//Ϊÿ��pairItemѡǰ�ĸ�����������mate��������~~�¸�����ھӶ��ǽ��µ�
            			Mate mate= item.Mates.get(j);
            			int c=mate.times;
            			if(mate.content.equals("use")||mate.content.equals("get")||mate.content.equals("utilize")||mate.content.equals("make")||mate.content.equals("have")){//Stop word list
            				li+=1;
            			}
            			KnowledgePiece kp = new KnowledgePiece();
            			//System.out.println("   For PAIR "+item.content.Original+" and Mate "+mate.content+":");//!!!!!!!!!!!!
            			System.out.println("      /search?q="+this.content.Singular.replace(" ", "+")+"+"+mate.content+"+"+item.content.Singular.replace(" ", "+")+"+"+this.context+"&hl=en&num=30");//!!!!!!!!!!!!
            			Socket socket = new Socket(addr, 80);
                		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
                		wr.write("GET " + "/search?q="+this.content.Singular.replace(" ", "+")+"+"+mate.content+"+"+item.content.Singular.replace(" ", "+")+"+"+this.context+"&hl=en&num=30" + " HTTP/1.0\n");
                        wr.write("Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/msword, application/vnd.ms-powerpoint, */*\n");
                        wr.write("Accept-Language: en-us\n");
                        wr.write("Accept-Encoding: gzip, deflate\n");
                        wr.write("User-Agent: Mozilla/4.0\n");
                        wr.write("Host: WWW.GOOGLE.COM.TW\n");
                        wr.write("\n");
                        wr.flush();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line="";
                        while (true) {
                            line = rd.readLine();
                            if(line.length()>4000){
                            	//if(line.contains("class=\"g\"")){
                            		Pattern pattern = Pattern.compile("href=[^>]*>((?!</a>)[^\\.\\?!])*("+this.content.Plural+"|"+this.content.Singular+")((?!</a>)[^\\.\\?!])*|(?<=class=\"j\"><font size=\"-1\">)[^\\.\\?!]*("+this.content.Plural+"|"+this.content.Singular+")[^\\.\\?!]*|(?<=\\. |\\.</b> )((?!</a>)[^\\.\\?!])*("+this.content.Plural+"|"+this.content.Singular+")((?!</a>)[^\\.\\?!])*",Pattern.CASE_INSENSITIVE);
                            		Matcher matcher = pattern.matcher(line);
                            		while(matcher.find()){
                            			String rr = matcher.group();
                            			rr=rr.replaceAll("href=[^>]*>", "");
                            			String r=rr.toLowerCase();
                            			if((r.contains(item.content.Plural)||r.contains(item.content.Singular))&& r.contains(mate.content)&& r.contains(" ")&& !r.contains("<i>")){
                            				String s=rr.replace("<b>", "").replace("</b>", "").replace("<br>  ", "");  
                            				kp.Sentences.add(s);
                            				kp.Urls.add("");
                            			}
                            		}
                            		break;
                            	//}
                            }
                        }
                        wr.close();
                        rd.close();
                        if(!kp.Sentences.isEmpty()){
            				this.Recs.add(new Recommendation(tn,c*kp.Sentences.size(),this.content.Singular,item.content.Singular,mate.content,false,kp));
            				item.Recs.add(new Recommendation(tn,c*kp.Sentences.size(),item.content.Singular,this.content.Singular,mate.content,true,kp));
                        }
            		}
            		pair.searched=true;
            		for(Pair p:item.Pairs){
            			if(p.pairItem==this){
            				p.searched=true;
            				break;
            			}
            		}
        		}
            }
        	Collections.sort(this.Recs);
        	return true;
        }catch (Exception e) {
            System.out.println(e.toString());
            return false;
        } 
    }

	@Override
	public int compareTo(SearchItem s) {
		// TODO Auto-generated method stub
		int cop = this.ID-s.ID;
        return cop;
	}
}
