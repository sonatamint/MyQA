package mycode;

import java.io.*;

public class Mate implements Comparable<Mate>, Serializable{

	public String content;
	public int times;
	
	public Mate(String term){
		this.content=term;
		this.times=1;
	}
    public int compareTo(Mate m) { //int compareTo(Object o): �Ƚϵ�ǰʵ����������� o �����λ�ڶ��� o ֮ǰ�����ظ�ֵ���������������������λ����ͬ���򷵻� 0 �����λ�ڶ��� o ���棬�򷵻���ֵ
        int cop = m.times-this.times;
        return cop;
     }
}
