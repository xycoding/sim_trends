package com.xiaoyin.reftrends.alg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Crawl {
	
	private static Integer errorCount = 0;
	
	//httpclient
    public static String getURL(String url,String charsetName){
        HttpClient httpclient = new DefaultHttpClient();
        String content = "";
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            StringBuilder sb = new StringBuilder();
            BufferedReader red = new BufferedReader(new InputStreamReader(entity.getContent(), charsetName));
            String line;
            while ((line = red.readLine()) != null) {
                sb.append(line);
            }
            content = sb.toString();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        return content;
    }
    
    //jsoup parse html
    public static Map<String,String> parseHtml(String source){
		Document doc = Jsoup.parse(source);
		doc.getElementById("div");
    	return null;
    }
    
    public static void parseTrends(){
		FileReader fr;
		FileWriter fw;
		try {
			ClassLoader cl = Crawl.class.getClassLoader();
			String hotsrc = new File(cl.getResource("res/hotsrc").toURI()).getAbsolutePath();
			fr = new FileReader(hotsrc);
			String hottarget = new File(cl.getResource("res/hot").toURI()).getAbsolutePath();
//			hottarget="c:\\a";
			fw = new FileWriter(hottarget);
			BufferedReader bfr = new BufferedReader(fr);
			BufferedWriter bfw = new BufferedWriter(fw);
			String str;
			int count = 0;
			while ((str = bfr.readLine()) != null) {
				if(str.indexOf("#")>0 && str.indexOf("#")!=str.lastIndexOf("#")){
					String[] array = str.split("#");
					String rs = array[1]+":"+SplitWords.extractKeywordString(array[1]);
					bfw.write(rs);
				    bfw.newLine();
				    bfw.flush();
					System.out.println(rs);
					count++;
				}
			}
			System.out.println("========="+count);
			fr.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    }
    
    //想办法获取相关主题词
    public static Map<String,Integer> getSubjectWords(String src){
    	Map<String,Integer> map = new HashMap<String,Integer>();
    	String q = SplitWords.slitWordsString(src);
    	String content = readURL("http://uvs.youdao.com/search?type=mark&site=cms.news.163.com&sort=time&qenc=UTF-8&start=0&length=2&q="+q,"utf-8");
    	content = content.substring(content.indexOf("{"));
    	content = content.substring(0,content.lastIndexOf(","));
    	content+="]}";
//    	System.out.println(content);
    	@SuppressWarnings("rawtypes")
		Map mapContent = fromJson(content, Map.class);
    	if(mapContent!=null){
    		@SuppressWarnings("unchecked")
			List<Map<String,String>> list = (List<Map<String,String>>)mapContent.get("hits");
        	for(Map<String,String> m : list){
        		List<String> splits = SplitWords.slitWordsList(m.get("title"));
        		for(String split : splits){
        			if(!map.containsKey(split)){
        				map.put(split, 1);
        			}else{
        				map.put(split, map.get(split)+1);
        			}
        		}
        	}
    	}

    	List<KeyUnit> ks = new ArrayList<KeyUnit>();
    	for(String key : map.keySet()){
    		KeyUnit ku = new KeyUnit();
    		ku.setKey(key);
    		ku.setCount(map.get(key));
    		ks.add(ku);
    	}
        Collections.sort(ks, new Comparator<KeyUnit>() {
	            public int compare(KeyUnit o1, KeyUnit o2) {
	            	return o2.getCount().compareTo(o1.getCount());
	            }
            });
    	return map;
    }
    
    private static String readURL(String urladdress, String encode) {
        
    	BufferedReader in = null;
    	try {
            URL url = new URL(urladdress);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setUseCaches(false);
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), encode));
            StringBuffer buf = new StringBuffer(10240);
            char[] chars = new char[10240];
            int charsRead = 0;

            while ((charsRead = in.read(chars, 0, chars.length)) != -1) {
                buf.append(chars, 0, charsRead);
            }
            return buf.toString();
        } catch (Exception e) {
            return "";
        } finally{
        	if(in != null){
        		try {
					in.close();
				} catch (IOException e) {
				}
        		in = null;
        	}
        	
        }
    }
    
	private static <T> T fromJson(String json, Class<T> t) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json,t);
		} catch (Exception e) {
			System.out.println(errorCount++);
		}
		return null;
	}

    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String c = getURL("http://huati.weibo.com/?ctg1=99&ctg2=0&prov=0&sort=day&p=25&t=1", "utf-8");
//		System.out.println(c);
//		String c = getURL("http://www.soso.com/q?utf-8=ie&query=%E5%B9%B4%E7%BB%88%E6%80%BB%E7%BB%93", "utf-8");
//		System.out.println(c);
//		c=readURL("http://huati.weibo.com/?ctg1=99&ctg2=0&prov=0&sort=day&p=25&t=1", "utf-8");
//		System.out.println(c);
//		parseTrends();
//		String  q = SplitWords.slitWordsString("坚毅不屈2013");
//		String body = getURL("http://uvs.youdao.com/search?type=mark&site=cms.news.163.com&sort=time&qenc=UTF-8&start=0&length=100&q="+q,"utf-8");
//		System.out.println(body);
		
//		String src = "我/人称代词 的/结构助词 理想/名词 是/动词 成为/动词 技术/名词 大牛/名词";
//		String[] ss = src.split(" ");
//		for(String one : ss){
//			String[] ones = one.split("/");
//			if(ones[1].equals("名词")){
//				System.out.println(ones[0]);
//			}
////		}
//		String r = getSubjectWords("我爱祖国和人民");
//		System.out.println(r);
	}

}
