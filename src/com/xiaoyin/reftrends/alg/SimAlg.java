package com.xiaoyin.reftrends.alg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimAlg {
	
	//已知热门话题
	private static Map<String, String> doneMap = new HashMap<String, String>();
	
	private static Map<String, String> simMap = new HashMap<String, String>();
	
	static {
		try {
			ClassLoader cl = SimAlg.class.getClassLoader();
			String hotsrc;
			hotsrc = new File(cl.getResource("res/hot").toURI()).getAbsolutePath();
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(hotsrc),"UTF-8"));
			String str;
			while ((str = bfr.readLine()) != null) {
				String[] buf = str.split(":");
				String key = buf[0].trim();
				if(!key.contains("微博") && !key.contains("红包飞")){
					doneMap.put(key, SplitWords.extractKeywordString(buf[0].trim()));
				}
			}
			
			String hotvector;
			hotvector = new File(cl.getResource("res/hotvector").toURI()).getAbsolutePath();
			BufferedReader bfr1 = new BufferedReader(new InputStreamReader(new FileInputStream(hotvector),"UTF-8"));
			String str1;
			while ((str1 = bfr1.readLine()) != null) {
				String[] buf = str1.split(":");
				String key = buf[0].trim();
				if(!key.contains("微博") && !key.contains("红包飞")){
					simMap.put(key, getW(str1));
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String superExtrct(String status){
		String p = SplitWords.extractKeywordString(status).trim();
		for(String key : simMap.keySet()){
			double sv = simVal(p, simMap.get(key));
			if(sv>0.2){//注意阀值调整
				System.out.println("---"+status);
				System.out.println("------"+p);
				System.out.println("---"+key);
				System.out.println("------"+simMap.get(key));
				return key;
			}
		}
		return null;
	}
	
	//简单版的提取，走第一步
	public static String simpleExtract(String status){
		String zero = extract(status);
		if(!zero.equals("-1")){
			return zero;
		}
		String first = firstSimpleExtract(status);
		if(!first.equals("-1")){
			return first;
		}else{
			return secondSimpleExtract(status);
		}
	}
	
	//退后
	private static String secondSimpleExtract(String status){
		List<String> swords = SplitWords.extractKeywordList(status);
		for(String key1 : swords){
			for(String key2 : swords){
				String sub1 = key1 + key2;
				if(status.contains(sub1)){
					return sub1;
				}else{
					String sub2 = key2 + key1;
					if(status.contains(sub2)){
						return sub2;
					}
				}
				
			}
		}
		return "-1";
	}
	
	//优先
	private static String firstSimpleExtract(String status){
		List<String> swords = SplitWords.extractKeywordList(status);
		for(String key1 : swords){
			for(String key2 : swords){
				for(String key3 : swords){
					String sub1 = key1 + key2 + key3;
					if(status.contains(sub1)){
						return sub1;
					}
					String sub2 = key2 + key1 + key3;
					if(status.contains(sub2)){
						return sub2;
					}
					String sub3 = key1 + key3 + key2;
					if(status.contains(sub3)){
						return sub3;
					}
					String sub4 = key2 + key1 + key3;
					if(status.contains(sub4)){
						return sub4;
					}
					String sub5 = key2 + key3 + key1;
					if(status.contains(sub5)){
						return sub5;
					}
					String sub6 = key3 + key2 + key1;
					if(status.contains(sub6)){
						return sub6;
					}
				}
			}
		}
		return "-1";
	}
	
	public static String extract(String status){
		List<String> swords = SplitWords.extractKeywordList(status);
		for(String key1 : swords){
			for(String key2 : swords){
				if(!key1.equals(key2)){
					String sub1 = key1 + key2;
					if(doneMap.containsKey(sub1) && !existKey(sub1).equals("-1")){
						if(!existKey(sub1).equals("-1")){
							return existKey(sub1);
						}
						
					}else {
						String sub2 = key2 + key1;
						if(!existKey(sub2).equals("-1")){
							return existKey(sub2);
						}
					}
				}
			}
		}
		for(String key : swords){
			if(!existKey(key).equals("-1")){
				return existKey(key);
			}
		}
		return "-1";
	}
	
	private static String existKey(String key){
		if(doneMap.containsKey(key)){
			return key;
		}else{
			for(String name : doneMap.keySet()){
				if(name.contains(key)){
					return name;
				}
			}
		}
		return "-1";
	}
	
	public static double simVal(String s1, String s2) {
		if(s1.trim().length()==0 || s2.trim().length()==0){
			return 0.0d;
		}
		Set<String> all = new HashSet<String>();
		for(String one :  s1.trim().split(" ")){
			if(one!=null && one.trim().length()>0){
				all.add(one);	
			}
		}
		for(String one :  s2.trim().split(" ")){
			if(one!=null && one.trim().length()>0){
				all.add(one);	
			}
		}
		List<Integer> v1 = new ArrayList<Integer>();
		List<Integer> v2 = new ArrayList<Integer>();

		for (String one :  all) {
			if(s1.contains(one)){
				v1.add(1);
			}else{
				v1.add(0);
			}
		}
		
		for (String one :  all) {
			if(s2.contains(one)){
				v2.add(1);
			}else{
				v2.add(0);
			}
		}
		
		return sim(v1, v2);
	}

	//求余弦相似度
	private static double sim(List<Integer> vector1, List<Integer> vector2) {
		double result = 0;
		result = pointMulti(vector1, vector2) / sqrtMulti(vector1, vector2);
		return result;
	}

	private static double sqrtMulti(List<Integer> vector1, List<Integer> vector2) {
		double result = 0;
		result = squares(vector1) * squares(vector2);
		result = Math.sqrt(result);
		return result;
	}

	private static double squares(List<Integer> vector) {
		double result = 0;
		for (Integer integer : vector) {
			result += integer * integer;
		}
		return result;
	}

	private static double pointMulti(List<Integer> vector1, List<Integer> vector2) {
		double result = 0;
		for (int i = 0; i < vector1.size(); i++) {
			result += vector1.get(i) * vector2.get(i);
		}
		return result;
	}
	
	private static String getW(String allkey){
		String rt = "";
		String[] ss = allkey.split(":");
		String ks1 = ss[1].trim().substring(1,ss[1].length()-1);
		String[] ks2 = ks1.split(",");
		for(String one : ks2){
			String[] o = one.split("=");
			rt += (o[0]+" ");
		}
		return rt;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		doneMap.size();
//		System.out.println(existKey("爸爸"));
		
		String string1 = "中国 北京";
		String string2 = "";
		System.out.println(simVal(string1,string2));
		
//		String rt = "";
//		String s = "期末考试:{试题=1, 组图=1, 期末=2, 游戏=1, 学院=1, 卫生=1, 题=1, 叠衣=1, 考试=1}";
//		String[] ss = s.split(":");
//		String ks1 = ss[1].trim().substring(1,ss[1].length()-1);
//		String[] ks2 = ks1.split(",");
//		for(String one : ks2){
//			String[] o = one.split("=");
//			rt += (o[0]+" ");
//		}
//		System.out.println(rt);
	}

}


