package com.xiaoyin.reftrends.alg;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fnlp.app.keyword.WordExtract;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.nlp.corpus.StopWords;

public class SplitWords {
	private static POSTagger pos;
	private static CWSTagger cws;
	private static WordExtract we;
	private static StopWords sw;
	static {
		try {
			ClassLoader cl = SplitWords.class.getClassLoader();
			String fPos = new File(cl.getResource("res/pos.m").toURI()).getAbsolutePath();
			String fSeg = new File(cl.getResource("res/seg.m").toURI()).getAbsolutePath();
			String fStopwords = new File(cl.getResource("res/StopWords.txt").toURI()).getAbsolutePath();

			cws = new CWSTagger(fSeg);
			pos	= new POSTagger(cws,fPos);
			sw = new StopWords(fStopwords);
			we = new WordExtract(cws, sw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//split word
	public static String slitWords(String word){
		return pos.tag(word);
	}
	
	//split wordr
	public static String slitWordsString(String word){
		String rt = "";
		String src = pos.tag(word);
		String[] ss = src.split(" ");
		for(String one : ss){
			String[] ones = one.split("/");
			if(ones[1].equals("名词") || ones[1].equals("人名") || ones[1].equals("专有名")){
				rt += (ones[0]+",");
			}
		}
		if(rt.endsWith(",")){
			rt = rt.substring(0, rt.length()-1);
		}
		return rt;
	}
	
	//split wordr
	public static List<String> slitWordsList(String word){
		List<String> rt = new ArrayList<String>();
		String src = pos.tag(word);
		String[] ss = src.split(" ");
		for(String one : ss){
			String[] ones = one.split("/");
			if(ones[1].equals("名词") || ones[1].equals("人名") || ones[1].equals("专有名")){
				rt.add(ones[0]);
			}
		}
		return rt;
	}

	//extract keyword
	public static String extractKeyword(String src){
		return we.extract(src, 100, true);
	}
	
	//extract keyword
	public static Map<String,Integer> extractKeywordMap(String src){
		return we.extract(src, 100);
	}
	
	//extract keyword
	public static String extractKeywordString(String src){
		String value = "";
		Map<String,Integer> map = we.extract(src, 100);
		  for (String key : map.keySet()) {
			  value += " " +key;
		  }
		return value;
	}
	
	//extract keyword
	public static List<String> extractKeywordList(String src){
		List<String> values = new ArrayList<String>();
		Map<String,Integer> map = we.extract(src, 100);
		  for (String key : map.keySet()) {
			  values.add(key);
//			  System.out.println(key);
		  }
		return values;
	}
	
	//extract keyword
	public static String[] extractKeywordArray(String src){
		String[] values = {};
		int i = 0;
		Map<String,Integer> map = we.extract(src, 100);
		  for (String key : map.keySet()) {
			  values[i] = key;
			  i++;
		  }
		return values;
	}
	
	//重要：生成短语对应的向量2--vector
	public static void genHot(){
		FileReader fr;
		FileWriter fw;
		try {
			ClassLoader cl = Crawl.class.getClassLoader();
			String hotsrc = new File(cl.getResource("res/hotsrc").toURI()).getAbsolutePath();
			fr = new FileReader(hotsrc);
			String hottarget = new File(cl.getResource("res/hot").toURI()).getAbsolutePath();
			hottarget="c:\\a";
			fw = new FileWriter(hottarget);
			BufferedReader bfr = new BufferedReader(fr);
			BufferedWriter bfw = new BufferedWriter(fw);
			String str;
			while ((str = bfr.readLine()) != null) {
//					str += (":"+Crawl.getSubjectWords(str));
					str += (" : "+ slitWordsString(str)+" : "+ slitWords(str)+" : "+ extractKeyword(str));
//					String[] buf = str.split(":");
					bfw.write(str);
				    bfw.newLine();
				    bfw.flush();
//					System.out.println(rs);
			}
			fr.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	//重要：生成短语对应的向量1---hot
	public static void genHotVector(){
		FileReader fr;
		FileWriter fw;
		try {
			ClassLoader cl = Crawl.class.getClassLoader();
			String hotsrc = new File(cl.getResource("res/hotsrc").toURI()).getAbsolutePath();
			fr = new FileReader(hotsrc);
			String hottarget = new File(cl.getResource("res/hotvector").toURI()).getAbsolutePath();
			hottarget="c:\\a";
			fw = new FileWriter(hottarget);
			BufferedReader bfr = new BufferedReader(fr);
			BufferedWriter bfw = new BufferedWriter(fw);
			String str;
			while ((str = bfr.readLine()) != null) {
					str += (":"+Crawl.getSubjectWords(str));
//					str += (" : "+ slitWordsString(str)+" : "+ slitWords(str)+" : "+ extractKeyword(str));
//					String[] buf = str.split(":");
					bfw.write(str);
				    bfw.newLine();
				    bfw.flush();
//					System.out.println(rs);
			}
			fr.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String text = "我的理想是成为技术大牛";
//		String rt = slitWords(text);
//		System.out.println(rt);
//		String ek = extractKeyword(text);
//		System.out.println(ek);
//		Map<String,Integer> ek2 = extractKeywordMap(text);
//		System.out.println(ek2);
//		String ek3 = extractKeywordString(text);
//		System.out.println(ek3);
//		String rt1 = slitWordsString(text);
//		System.out.println(rt1);
		
//		 genHot();
		genHotVector();
		
	}

}
