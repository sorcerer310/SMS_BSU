package com.bsu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * 更新前进行检查的url,检查当前数据是否允许被修改或者删除
 * Servlet implementation class General_Update_Check
 */
@WebServlet("/general_update_check")
public class General_Update_Check extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public General_Update_Check() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String state = U.getRS(request, "state");								//获得要操作的状态
		String table = U.getRS(request, "table");								//获得要操作的表的数据
		String r_unitid = U.getRS(request, "unitid");
		String s_unitid = request.getSession(false).getAttribute("unitid").toString();
		
		if(state==null || table==null || r_unitid==null){
			U.p(response, JSONMsg.info(1004));
			return;
		}
		//对每种状态对应的操作
		switch(state){
		case "1":
			U.p(response, JSONMsg.info(3020));								//插入数据时不需要验证,直接通过
			return;
		case "0":
			if(!r_unitid.equals(s_unitid))
				U.p(response, JSONMsg.info(1020));
			else
				U.p(response, JSONMsg.info(3020));
			return;
		case "-1":
			if(!r_unitid.equals(s_unitid))
				U.p(response, JSONMsg.info(1020));
			else
				U.p(response, JSONMsg.info(3020));
			return;
		default:
			U.p(response, JSONMsg.info(1005));
			return;
		}
		
	}

}
