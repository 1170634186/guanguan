package com.yc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.yc.bean.User;
import com.yc.biz.BizException;
import com.yc.biz.UserBiz;

import util.BeanUtils;

@WebServlet("/user.s")		
public class UserServlet extends HttpServlet{
		private static final long serialVersionUID =1L;
		
		protected void doGet(HttpServletRequest request ,HttpServletResponse response)
				throws ServletException,IOException{
			String op=request.getParameter("op");
			if("login".equals(op)){
				login(request,response);
			}else if("query".equals(op)){
			query(request,response);
			}else if("add".equals(op)){
				add(request,response);
			}else if("find".equals(op)){
				find(request,response);
				}
			
		}
		
		private void find(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException ,IOException{
			UserBiz ubiz=new UserBiz();
			response.setCharacterEncoding("utf-8");
			String id=request.getParameter("id");
			User user=ubiz.findById(id);
			String userString=JSON.toJSONString(user);
			response.getWriter().append(userString);
			
			
			
		}

		private void add(HttpServletRequest request, HttpServletResponse response)
				throws ServletException ,IOException{
			 UserBiz ubiz=new UserBiz();
			  User user=BeanUtils.asBean(request, User.class);
			  
			  try{
				  String repwd =request.getParameter("repwd");
				  ubiz.add(user,repwd);
			  }catch(BizException e){
				  e.printStackTrace();
				  request.setAttribute("msg", e.getMessage());
			  }finally {
				query(request,response);
			}
			
		}




		private void query(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException ,IOException{
			  UserBiz ubiz=new UserBiz();
			  User user =BeanUtils.asBean(request, User.class);
			  request.setAttribute("userList", ubiz.find(user));
			  request.getRequestDispatcher("manage-user.jsp").forward(request, response);
		}




		private void login(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException ,IOException{
			String username =request.getParameter("username");
			String userpwd =request.getParameter("userpwd");
			
			UserBiz ubiz =new UserBiz();
			User user=null;
			
			try{
				user =ubiz.login(username, userpwd);
			}catch(BizException e){
				e.printStackTrace();
				request.setAttribute("msg", e.getMessage());
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return; 
			}
			
			
			if(user == null){
				request.setAttribute("msg", "”√ªß√˚√‹¬Î¥ÌŒÛ£°");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}else{
				request.getSession().setAttribute("loginedUser", user);
				response.sendRedirect("index.jsp");
			}
			
			
		}
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
			           throws ServletException,IOException{
				doGet(request,response);
			}
	
	}

