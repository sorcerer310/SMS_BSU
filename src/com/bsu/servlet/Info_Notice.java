package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class GetNotice
 */
@WebServlet("/infonotice")
public class Info_Notice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Info_Notice() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sql = "select * from info_notice ";
		CachedRowSetImpl rs = null;
		ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
		if(request.getParameter("id")!= null){
			String id = request.getParameter("id").toString();
			sql += " where id=?";
			ldata.add(new ColumnData("id",Types.INTEGER,id));
		}

		sql += " order by date desc";
		
		try {
			rs = ODB.query(sql,ldata);
			U.p(response, JSONParser.parseJson(rs));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
