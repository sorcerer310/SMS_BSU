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
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * Servlet implementation class Info_Update
 */
@WebServlet("/infoupdate")
public class Info_Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Info_Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String table = request.getParameter("table");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String type = U.getRS(request, "type");
		
		StringBuffer sb = new StringBuffer();
		ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
		if(id.equals("0")){
			ldata.add(new ColumnData("title",Types.VARCHAR,title));
			ldata.add(new ColumnData("content",Types.VARCHAR,content));
			
			if(table.equals("info_study")){
				sb.append("insert into ").append(table).append(" (title,content,type) values (?,?,?)");
				ldata.add(new ColumnData("type",Types.VARCHAR,type));
			}
			else
				sb.append("insert into ").append(table).append(" (title,content) values (?,?)");
		}
		else {
			ldata.add(new ColumnData("title",Types.VARCHAR,title));
			ldata.add(new ColumnData("content",Types.VARCHAR,content));

			if(table.equals("info_study")){
				sb.append("update ").append(table).append(" set title=?,content=?,type=? where id=?");
				ldata.add(new ColumnData("type",Types.VARCHAR,type));
			}
			else
				sb.append("update ").append(table).append(" set title=?,content=? where id=?");
			ldata.add(new ColumnData("id",Types.INTEGER,id));
		}
		
		try {
			int c = ODB.update(sb.toString(), ldata);
			if(c>0)
				U.p(response, JSONMsg.info(3005));
			else
				U.p(response,JSONMsg.info(1017,table+"表保存数据失败"));
		} catch (ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			U.p(response, JSONMsg.info(1111,e.getMessage()));
		}
	}

}
