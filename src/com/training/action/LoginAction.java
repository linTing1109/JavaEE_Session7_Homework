package com.training.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.dao.BeverageMembersDao;
import com.training.formbean.LoginRegisterForm;
import com.training.model.Goods;
import com.training.model.Member;

import net.sf.json.JSONObject;

public class LoginAction extends DispatchAction {
	
	private BeverageMembersDao beverageMembersDao = BeverageMembersDao.getInstance();
	
	//login:會員登入
    public ActionForward login(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		
    	ActionForward actFwd = null;
    	HttpSession session = request.getSession();
    	//接收頁面來的帳密資料
    	String inputID = request.getParameter("id");
        String inputPwd = request.getParameter("pwd");
        String loginMsg=null;
        //使用會員ID撈db是否有member
        Member member = beverageMembersDao.queryMemberById(inputID);
        
    	if(member != null) {//會員ID存在
    		String id = member.getIdentificationNo();    		
    		String pwd = member.getPassword();
    		
    		if(id.equals(inputID) && pwd.equals(inputPwd)) { //會員ID&帳密符合 導入成功頁面
    			//將membeer 存入sessionScope  以供LoginCheckFilter
    			session.setAttribute("member", member);
    			actFwd = mapping.findForward("success");        			
    		} else {
                // 會員ID&帳密錯誤,導向失敗頁面fail 重新登入.
    			loginMsg="密碼錯誤，請重新輸入！";
    			actFwd = mapping.findForward("fail");
    		}
    	} else {//會員ID不存在
    		loginMsg="無此帳戶名稱";
			actFwd = mapping.findForward("fail");
    	}
    	session.setAttribute("loginMsg", loginMsg);
    	return actFwd;
    }
    
    //logout:會員登出
    public ActionForward logout(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();
		session.removeAttribute("member");//清空會員資料		
		session.removeAttribute("carGoods");//登出需清空購物車
		session.removeAttribute("total");//避免確認完金額 並沒有結帳 直接離開
    	return mapping.findForward("fail");//導回原先登入頁面
    }
    
    //memberAdd:會員增加
    public ActionForward memberAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();
    	String meberAddMsg=null;
    	LoginRegisterForm memberAddForm=(LoginRegisterForm)form;//form接收到的表單
    	Member member =new Member();
    	BeanUtils.copyProperties(member, memberAddForm);//將memberAddForm複製到member
    	
        Member memberCheck = beverageMembersDao.queryMemberById(member.getIdentificationNo());//先判斷是否有帳號
        if(memberCheck != null) {
        	meberAddMsg="註冊失敗-帳號已存在!請重新登入";
        }else {
        	int addNo=beverageMembersDao.createMember(member);
        	meberAddMsg= (addNo!=0) ? "註冊成功!請回登入頁重新登入" : "註冊失敗";
        }
        session.setAttribute("meberAddMsg", meberAddMsg);
		return mapping.findForward("add");//不管有沒有成功都是在註冊頁面 給使用者看畫面後自行決定
	}
    
}
