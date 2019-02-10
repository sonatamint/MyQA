package mycode;

import java.io.*;

public class Content implements Serializable{
	
	public final String Singular;
	public final String Plural;
	public final String TaggedPlu;//���ڹ�����ȡ���ʵ�����
	public final String TaggedSin;//���ڹ�����ȡ���ʵ�����
	
	public  Content(String sglr, PlrSgl converter){//sglr������ʽ,&tagged-Сд,�����ո�ָ�,��β���޿ո�
		this.Singular=sglr;
		if(sglr.contains(" ")){
			String[] temp=sglr.split(" ");
			String end=temp[temp.length-1];
			String p=converter.pluralize(end);
			String forward="";
			for(int i=0;i<temp.length-1;i++){
				forward+=temp[i]+" ";
			}
			this.Plural=forward+p;
			this.TaggedSin=this.Singular.replaceAll(" ", "/NN ");
			this.TaggedPlu=this.Plural.replaceAll(" ", "/NN ");
		}
		else{
			this.Plural=converter.pluralize(sglr);
			this.TaggedSin=this.Singular;
			this.TaggedPlu=this.Plural;
			
		}
	}
}
