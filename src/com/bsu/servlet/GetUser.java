package com.bsu.servlet;

import java.io.IOException;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.*;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class GetUser
 */
@WebServlet("/getuser")
public class GetUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//根据列名生成带条件的sql
		StringBuffer sb = new StringBuffer();
		sb.append("select us.id,us.no,us.pwd,us.groupid,us.name,us.unitid,un.unit as unit ") 
				.append("from user us ")
				.append("inner join unit un on us.unitid = un.id where 1=1 and `delete`=0 ")
				.append(U.sc(request, U.cc(request)));
		
		CachedRowSetImpl rs = null;
		try {
			//带入sql与查询条件值并返回结果
			rs = ODB.query(sb.toString(),U.lcd(request, "user",U.cc(request)));
			//打印解析为json数据的返回结果
			U.p(response, JSONParser.parseJson(rs));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
