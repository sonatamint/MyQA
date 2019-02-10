package mycode;

import java.io.*;

public class Recommendation implements Comparable<Recommendation>, Serializable{
	
    public int sectionNo;//�����ڵڼ����Ƽ�
	public String Name;
	public KnowledgePiece knowledge;
    public int priority;
    public String pairName;
    public String mateName;
    public boolean mateForthis;//mate�Ƿ�����ڵ�ǰ����Recommend()������item
    
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
