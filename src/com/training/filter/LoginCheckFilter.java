package com.training.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.training.model.Member;

public class LoginCheckFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
			
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
				
		Member member = (Member) session.getAttribute("member");
		// 判斷session裡是否有使用者登入資訊
		if(member != null){
			// 已登入(放行)
			chain.doFilter(request, response);
		}else{
			
			// 是否為登入請求
			String requestURI = httpRequest.getRequestURI();
			String action = httpRequest.getParameter("action");
			if(requestURI.endsWith("LoginAction.do") && "login".equals(action) ){//確定有值放前面 避免nullPoint
				chain.doFilter(request, response);
			}else if(requestURI.endsWith("LoginAction.do") && "memberAdd".equals(action)) {//確定為註冊 放行
				chain.doFilter(request, response);
			}else{
				// 未登入(導回登入頁)
				RequestDispatcher dispatcher = httpRequest.getRequestDispatcher("/FrontLogin.jsp");
				dispatcher.forward(request, response);				
			}
		}		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }
	
	@Override
	public void destroy() { }

}
