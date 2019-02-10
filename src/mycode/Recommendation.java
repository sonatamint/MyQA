package mycode;

import java.io.*;

public class Recommendation implements Comparable<Recommendation>, Serializable{
	
    public int sectionNo;//隶属于第几次推荐
	public String Name;
	public KnowledgePiece knowledge;
    public int priority;
    public String pairName;
    public String mateName;
    public boolean mateForthis;//mate是否归属于当前调用Recommend()方法的item
    
	public int compareTo(Recommendation r) {
		int cop = r.priority-this.priority;
        return cop;
	}
    
    public Recommendation(int section, int score, String name, String pairname, String matename, boolean mateforthis, KnowledgePiece knowledgepiece){
        this.sectionNo=section;
    	this.priority=score;
        this.Name=name;
        this.pairName=pairname;
        this.mateName=matename;
        this.mateForthis=mateforthis;
        this.knowledge=knowledgepiece;
    }
}
