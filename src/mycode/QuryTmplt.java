package mycode;

public class QuryTmplt{//singular and plural

	public String[] Queries;//用于构造网络查询
	public String[] RegexPatt;//用于从html中获取含有关键词的语句片
	public String[] SyntcPatt;//用于从语句片中提取关联概念
	public String[] Results;//用于存储搜索结果
	
	public QuryTmplt(Content obj, String envr){
		
		if(obj.Plural.equals(obj.Singular)){
			Queries = new String[2];
			RegexPatt = new String[2];
			SyntcPatt = new String[2];
			Results = new String[2];
			
			Queries[0]="/search?q=%22how+to+*+"+obj.Plural.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";//复数形式可以在这里处理,对应的正则也要修改
			Queries[1]="/search?q=%22cannot+*+"+obj.Plural.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";
			
			RegexPatt[0]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Plural+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";//以obj为中心的标黑句加后一个单词,包含html换行标记
			RegexPatt[1]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Plural+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";
			
			SyntcPatt[0]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedPlu+"/NN(?![ \\w]+/NN))";//(?![ \\w]+/NN))";//对于dll案例,由于其后经常跟着file,不能贸然舍弃
			SyntcPatt[1]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedPlu+"/NN(?![ \\w]+/NN))";//(?![ \\w]+/NN))";
			
			Results[0]="";
			Results[1]="";
		}
		else{
			Queries = new String[4];
			RegexPatt = new String[4];
			SyntcPatt = new String[4];
			Results = new String[4];
			
			Queries[0]="/search?q=%22how+to+*+"+obj.Plural.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";//复数形式可以在这里处理,对应的正则也要修改
			Queries[1]="/search?q=%22how+to+*+"+obj.Singular.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";
			Queries[2]="/search?q=%22cannot+*+"+obj.Plural.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";
			Queries[3]="/search?q=%22cannot+*+"+obj.Singular.replace(" ", "+")+"%22+"+envr+"&hl=en&num=100";
			
			RegexPatt[0]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Plural+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";//以obj为中心的标黑句加后一个单词,包含html换行标记
			RegexPatt[1]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Singular+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";
			RegexPatt[2]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Plural+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";
			RegexPatt[3]="(?<=<b>)[ \\w]*(</b> <br>  <b>)?[ \\w]*"+obj.Singular+"</b>((?=[\\.,;:\\?!]|</a>)| (<br>  )?\\w+)";
			
			SyntcPatt[0]="\\b\\w+(?=/(VB ((?!/IN|TO)[ /\\w])*|(JJ|VBN) )"+obj.TaggedPlu+"/NN(?![ \\w]+/NN))";//对于dll案例,由于其后经常跟着file,不能贸然舍弃
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
