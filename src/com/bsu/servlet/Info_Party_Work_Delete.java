package com.bsu.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.DB;
import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * Servlet implementation class Info_Party_Work_Delete
 */
@WebServlet("/info_party_work_delete")
public class Info_Party_Work_Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Info_Party_Work_Delete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String blobid = request.getParameter("blobid");
		
		StringBuffer sql_blob = new StringBuffer();
		sql_blob.append("delete from sysblob where id = ?");
		ArrayList<ColumnData> ldata_blob = new ArrayList<ColumnData>();
		ldata_blob.add(new ColumnData("id",Types.INTEGER,blobid));
		
		StringBuffer sql_info = new StringBuffer();
		sql_info.append("delete from info_party_work where id=?");
		ArrayList<ColumnData> ldata_info = new ArrayList<ColumnData>();
		ldata_info.add(new ColumnData("id",Types.INTEGER,id));
		
		Connection conn = null;
		try {
			conn = DB.getConnection();
			conn.setAutoCommit(false);
			ODB.update(sql_blob.toString(), ldata_blob, conn);
			ODB.update(sql_info.toString(), ldata_info, conn);
			conn.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//出现异常回滚
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			U.p(response, JSONMsg.info(1111,e.getMessage()));
		}finally{
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		U.p(response, JSONMsg.info(3004));
	}

}
