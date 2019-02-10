package mycode;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsCrawler {

	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
		
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  "e91881194428ccc35224d567e28bcf57");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	    
	}
	
	public static boolean SearchBing(String query){
    	try {
        	InetAddress addr = InetAddress.getByName("cn.bing.com");
        	System.out.println(query);//
    		//System.out.println(this.template.RegexPatt[i]);//
    		Socket socket = new Socket(addr, 80);//创建Socket与搜索引擎服务器进行通信
    		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));
    		wr.write("GET " + query + " HTTP/1.0\n");
            wr.write("Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/msword, application/vnd.ms-powerpoint, */*\n");
            wr.write("Accept-Language: en-us\n");
            wr.write("Accept-Encoding: gzip, deflate\n");
            wr.write("User-Agent: Mozilla/4.0\n");
            wr.write("Host: cn.bing.com\n");
            wr.write("\n");
            wr.flush();
            //接收服务器返回的查询结果
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line="";
            int t=1;
            while (true) {
                line = rd.readLine();
                writeFile("D:/BingLines/"+t+".txt", line);
                t++;
                //System.out.println(line);//
                if(line.length()>4000){//
                	//if(line.contains("class=\"g\"")){//更准确地提取结果所在行
                		System.out.println(line);//
                		break;
                		/*
                		Pattern pattern = Pattern.compile(this.template.RegexPatt[i],Pattern.CASE_INSENSITIVE);
                		Matcher matcher = pattern.matcher(line);
                		while(matcher.find()){
                			this.template.Results[i]+=matcher.group().replace("</b>", "").replace("<br>  ", "").replace("<b>", "").toLowerCase()+"\n";
                		}
                		break;
                		*/
                	//}//
                }
            }
            wr.close();
            rd.close();
            return true;
        }catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
	
	static void writeFile(String pathto, String content) throws Exception {
		File fl = new File(pathto);
		File dir = fl.getParentFile();
		if(!dir.exists()){
			dir.mkdirs();
		}
		FileWriter fout = new FileWriter(fl);
		BufferedWriter bwr = new BufferedWriter(fout);
		bwr.write(content);
		bwr.close();
		fout.close();
	}
	
	
	public static void main(String[] args) {
		
		String query = "/search?q=先进区%20后报关";
		//String query = "/s?wd=先进区%20后报关";//for baidu, 但是百度对搜索结果加密
		SearchBing(query);
		//String httpUrl = "http://apis.baidu.com/weixinxi/extracter/extracter";
		//String httpArg = "url=http%3A%2F%2Fwww.weixinxi.wang%2Fblog%2Faitrcle.html%3Fid%3D105";
		//String httpArg = "url=http%3A%2F%2Ffinance.sina.com.cn%2Fchina%2Fdfjj%2F20131031%2F141117181024.shtml";
		//String httpArg = "url=http%3A%2F%2Fgzdaily.dayoo.com%2Fhtml%2F2014-09%2F30%2Fcontent_2761811.htm";
		//String jsonResult = request(httpUrl, httpArg);
		//System.out.println(jsonResult);
	}
}
