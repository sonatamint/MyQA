package mycode;

import java.io.*;

public class Pair implements Comparable<Pair>, Serializable {

	public SearchItem pairItem;
	public int cooccurrence;
	public boolean searched;//标记这一对item是否被推荐过
	
	public int compareTo(Pair p) {
		int cop = p.cooccurrence-this.cooccurrence;
        return cop;
	}
	
	public Pair(SearchItem pit, int c){
		this.pairItem=pit;
		this.cooccurrence=c;
		this.searched=false;
	}
}
