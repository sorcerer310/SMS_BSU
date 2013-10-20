package com.bsu.servlet;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.tool.JSONMsg;

/**
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	} 

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<ColumnData> ldata = new ArrayList<ColumnData>();
		ldata.add(new ColumnData("no",Types.VARCHAR,request.getParameter("name")));				//设置no参数保存用户名
		ldata.add(new ColumnData("pwd",Types.VARCHAR,request.getParameter("pwd")));				//设置pwd参数保存密码
		
		try {
			int rc = ODB.update("insert into user (no,pwd) values (?,?)", ldata);
			if(rc>0)
				response.getWriter().print(JSONMsg.info(3002));
			else
				response.getWriter().print(JSONMsg.info(1002));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print(JSONMsg.info(1002,e.getMessage()));
		} 
	}
}
