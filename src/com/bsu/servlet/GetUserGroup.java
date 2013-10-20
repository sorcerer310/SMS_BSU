package com.bsu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bsu.system.db.ODB;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class GetUserGroup
 */
@WebServlet("/getusergroup")
public class GetUserGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserGroup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		//sb.append("select * from `group`");
		sb.append("select u.id as id,u.no as no,u.name as name,u.groupid as groupid,g.no as groupno ")
				.append("from user u ").append("inner join `group` g on u.groupid = g.id;");
		
		CachedRowSetImpl rs = null;
		try{
			rs = ODB.query(sb.toString());
			
			JSONArray ja = new JSONArray();
			while(rs.next()){
				JSONObject jo = new JSONObject();
				jo.put("id", rs.getInt("id"));
				jo.put("no", rs.getString("no"));
				jo.put("name", rs.getString("name"));
				jo.put("groupid", rs.getInt("groupid"));
				//此处需要用索引检索列的值,否则会抛出列名无效的异常.
				//mysql的bug.不能解决
				//jo.put("groupno", rs.getString("groupno"));
				jo.put("groupno", rs.getString(5));
				ja.put(jo);
			}

			U.p(response,ja.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
