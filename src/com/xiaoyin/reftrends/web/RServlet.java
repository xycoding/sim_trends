package com.xiaoyin.reftrends.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiaoyin.reftrends.service.RefTrendsService;

public class RServlet extends HttpServlet {

	private static final long serialVersionUID = -7914517206578082027L;
	
    @Override  
    public void doGet(HttpServletRequest req, HttpServletResponse resp)  
            throws ServletException, IOException { 
    	req.setCharacterEncoding("UTF-8");
    	String status = req.getParameter("status");

    	String rt = RefTrendsService.gets(status);
    	resp.setContentType("html/text");
    	resp.setCharacterEncoding("UTF-8");//编码kengdie
    	resp.setHeader("Cache-Control","no-cache");
        PrintWriter out = resp.getWriter();  
        out.println(rt);  
        out.flush();  
    }  
  
    @Override  
    public void doPost(HttpServletRequest req, HttpServletResponse resp)  
            throws ServletException, IOException {  
        doGet(req,resp);  
    } 

}
