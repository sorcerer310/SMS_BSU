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
 * Servlet implementation class Info_Study
 */
@WebServlet("/infostudy")
public class Info_Study extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Info_Study() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String type = U.getRS(request, "type");
			if(type==null){
				U.p(response, JSONMsg.info(2222,"缺少type参数"));
				return;
			}
		
			StringBuffer sql = new StringBuffer();
			sql.append("select * from info_study where 1=1 ");

			CachedRowSetImpl rs = null;
			ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
			//如果type代入的参数为0,则不加type参数查询
			if(!type.equals("0")){
				sql.append(" and `type`=? ");
				ldata.add(new ColumnData("type",Types.VARCHAR,type));
			}
			
			if(request.getParameter("id")!= null){
				String id = request.getParameter("id").toString();
				sql.append(" and id=?");
				ldata.add(new ColumnData("id",Types.INTEGER,id));
			}
			
			sql.append(" order by date desc");
			try {
				rs = ODB.query(sql.toString(),ldata);
				U.p(response, JSONParser.parseJson(rs));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}

}
