package com.xiaoyin.reftrends.service;

import com.xiaoyin.reftrends.alg.SimAlg;
import com.xiaoyin.reftrends.alg.SplitWords;

public class RefTrendsService {
	
	public static String gets(String word){
//		String trends = SplitWords.slitWords(word);
//		trends = SplitWords.extractKeyword(word);
		String trends = SimAlg.superExtrct(word);
		if(null != trends){
			System.out.println("相似度命中！");
			return "#"+trends+"#";
		}else{
			System.out.println("分词命中！");
			trends = SimAlg.simpleExtract(word);
			if("-1".equals(trends)){
				return "啊，没发现相关话题。。。来自火星吗？";
			}
		}
		
		return "#"+trends+"#";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
