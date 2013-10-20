package com.bsu.servlet;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Member_Poor_Help
 */
@WebServlet("/member_poor_help")
public class Member_Poor_Help extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Member_Poor_Help() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from poor_member_help where 1=1 and parentid=?");
		String pid = request.getParameter("parentid");
		ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
		ldata.add(new ColumnData("parentid",Types.INTEGER,pid));
		
		CachedRowSetImpl rs = null;
		try {
			rs = ODB.query(sb.toString(),ldata);
			U.p(response, JSONParser.parseJson(rs));
		} catch (Exception e) {
			e.printStackTrace();
			U.p(response, JSONMsg.info(1111,e.getMessage()));
		} 
	}

}
