package com.chegy.configer;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.chegy.model.User;
/*
 * 用户登录统一验证
 */
public class LoginInterceptor implements HandlerInterceptor{
	
	//拦截检查，返回true代表要进行拦截，返回false代表不进行拦截。
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
          
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//获取请求的URL
    	String url = request.getRequestURI();
    	
    	//login.jsp 或登录请求放行，不拦截
    	if(url.indexOf("/login")>=0 || url.indexOf("/login2")>=0 ) {
    		return true;
    	}
    	
    	//获取session
    	HttpSession session = request.getSession();
    	Object obj = session.getAttribute("user");
    	System.out.println("sessionId = "+session.getId());

    	if(obj!=null) {
    		return true;
    	}
    	//没有登录且不是登录页面，转发到登录页面，并给出提示错误信息
    	request.setAttribute("msg", "还没登录，请先登录");
    	request.getRequestDispatcher("/login").forward(request, response);
    	return false;
    }
    
}
