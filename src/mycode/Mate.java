package mycode;

import java.io.*;

public class Mate implements Comparable<Mate>, Serializable{

	public String content;
	public int times;
	
	public Mate(String term){
		this.content=term;
		this.times=1;
	}
    public int compareTo(Mate m) { //int compareTo(Object o): 比较当前实例对象与对象 o ，如果位于对象 o 之前，返回负值，如果两个对象在排序中位置相同，则返回 0 ，如果位于对象 o 后面，则返回正值
        int cop = m.times-this.times;
        return cop;
     }
}
