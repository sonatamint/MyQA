package mycode;

public class QuryTmplt{//singular and plural

	public String[] Queries;//���ڹ��������ѯ
	public String[] RegexPatt;//���ڴ�html�л�ȡ���йؼ��ʵ����Ƭ
	public String[] SyntcPatt;//���ڴ����Ƭ����ȡ��������
	public String[] Results;//���ڴ洢�������
	
	public QuryTmplt(Content obj, String envr){
		
		if(obj.Plural.equals(obj.Singular)){
			Queries = new String[2];
			RegexPatt = new String[2];
			SyntcPatt = new String[2];
			Results = new String[2];
			
			Queries[0]="/search?q=%22how+to+*+"+obj.Plural.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";//������ʽ���������ﴦ��,��Ӧ������ҲҪ�޸�
			Queries[1]="/search?q=%22cannot+*+"+obj.Plural.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";
			
			RegexPatt[0]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Plural+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";//��objΪ���ĵı�ھ�Ӻ�һ������,����html���б��
			RegexPatt[1]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Plural+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";
			
			SyntcPatt[0]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedPlu+"/NN(?![ \\w]+/NN))";//(?![ \\w]+/NN))";//����dll����,������󾭳�����file,����óȻ����
			SyntcPatt[1]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedPlu+"/NN(?![ \\w]+/NN))";//(?![ \\w]+/NN))";
			
			Results[0]="";
			Results[1]="";
		}
		else{
			Queries = new String[4];
			RegexPatt = new String[4];
			SyntcPatt = new String[4];
			Results = new String[4];
			
			Queries[0]="/search?q=%22how+to+*+"+obj.Plural.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";//������ʽ���������ﴦ��,��Ӧ������ҲҪ�޸�
			Queries[1]="/search?q=%22how+to+*+"+obj.Singular.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";
			Queries[2]="/search?q=%22cannot+*+"+obj.Plural.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";
			Queries[3]="/search?q=%22cannot+*+"+obj.Singular.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";
			
			RegexPatt[0]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Plural+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";//��objΪ���ĵı�ھ�Ӻ�һ������,����html���б��
			RegexPatt[1]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Singular+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";
			RegexPatt[2]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Plural+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";
			RegexPatt[3]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Singular+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";
			
			SyntcPatt[0]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedPlu+"/NN(?![ \\w]+/NN))";//����dll����,������󾭳�����file,����óȻ����
			SyntcPatt[1]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedSin+"/NN(?![ \\w]+/NN))";
			SyntcPatt[2]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedPlu+"/NN(?![ \\w]+/NN))";
			SyntcPatt[3]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedSin+"/NN(?![ \\w]+/NN))";
			
			Results[0]="";
			Results[1]="";
			Results[2]="";
			Results[3]="";
		}
	}
}
