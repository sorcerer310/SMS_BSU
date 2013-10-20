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
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class GetGroupFunc
 */
@WebServlet("/getgroupfunc")
public class GetGroupFunc extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGroupFunc() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupno = request.getParameter("groupno");
		StringBuffer sb = new StringBuffer();
		sb.append("select * from vgroupfunc where 1=1 ")
			.append(U.sc(request, U.cc(request)));
		
		ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
		if(groupno!=null)
			ldata.add(new ColumnData("groupno",Types.VARCHAR,groupno));
		
		CachedRowSetImpl rs = null;
		try{
			rs = ODB.query(sb.toString(),ldata);
			U.p(response, JSONParser.parseJson(rs));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
