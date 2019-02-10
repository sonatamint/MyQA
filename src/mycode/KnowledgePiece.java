package mycode;

import java.util.ArrayList;
import java.io.*;

public class KnowledgePiece implements Serializable{
	
	public ArrayList<String> Sentences;
    public ArrayList<String> Urls;
    
    public KnowledgePiece(){    	
    	this.Sentences=new ArrayList<String>();
        this.Urls=new ArrayList<String>();
    }

}
