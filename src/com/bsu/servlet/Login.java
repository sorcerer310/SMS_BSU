package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.JSONMsg;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		List<ColumnData> ldata = new ArrayList<ColumnData>();
		ldata.add(new ColumnData("no",Types.VARCHAR,name));
		ldata.add(new ColumnData("pwd",Types.VARCHAR,pwd));
		CachedRowSetImpl rs = null;
		try {
			rs =  ODB.query("select * from user where no=? and pwd=?", ldata);
			//如果验证成功打印获得数据,否则打印1001错误信息
			if(rs.next())
			{
				int id = rs.getInt("id");
				HttpSession s =  request.getSession(true);
				s.setAttribute("name", name);												//将用户名加入session
				s.setAttribute("pwd", pwd);													//将密码加入session
				s.setAttribute("userid", rs.getInt("id"));									//获得用户id
				s.setAttribute("unitid", rs.getInt("unitid"));								//用户单位id
				s.setAttribute("groupid", rs.getInt("groupid"));						//权限组id
				response.getWriter().print(JSONMsg.info(3001));
			}
			else
				response.getWriter().print(JSONMsg.info(1001));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print(JSONMsg.info(1001,e.getMessage()));
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

}
