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
import javax.servlet.http.HttpSession;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Info_Party_Work_Update
 */
@WebServlet("/info_party_work_update")
public class Info_Party_Work_Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Info_Party_Work_Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String item = request.getParameter("item");											//文件名
		String memo = request.getParameter("memo");									//备注
		String blobid = request.getParameter("blobid");									//大对象id
		String unitid = request.getParameter("unitid");										//记录该文件属于哪个unit
		HttpSession s = request.getSession(false);										
		if(s.getAttribute("userid")==null){
			U.p(response, JSONMsg.info(1111,"未从session中取得userid,登陆可能已经超时"));
			return;
		}
		String userid = s.getAttribute("userid").toString();									//用户id
		//String unitid = s.getAttribute("unitid").toString();									//用户对应单位id
		//判断单位id不在10个分公司之内返回错误消息,不能上传文件
		if(!unitid.equals("1") && !unitid.equals("2") && !unitid.equals("3") && !unitid.equals("4") && !unitid.equals("5") && !unitid.equals("6") && !unitid.equals("7") && 
				!unitid.equals("8") && !unitid.equals("9") && !unitid.equals("10") ){
			U.p(response, JSONMsg.info(1016));
			return;
		}
			
		String parentid = U.getParentidByUnitid(unitid);										//根据unitid获得parentid;
		if(parentid==null){
			U.p(response, JSONMsg.info(1015));
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into info_party_work (item,memo,parentid,blobid,userid) values (?,?,?,?,?)");
		
		ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
		ldata.add(new ColumnData("item",Types.VARCHAR,item));
		ldata.add(new ColumnData("memo",Types.VARCHAR,memo));
		ldata.add(new ColumnData("parentid",Types.INTEGER,parentid));
		ldata.add(new ColumnData("blobid",Types.INTEGER,blobid));
		ldata.add(new ColumnData("userid",Types.INTEGER,userid));
		
		try {
			int c = ODB.update(sb.toString(), ldata);
			U.p(response, JSONMsg.info(3003,"插入了"+String.valueOf(c)+"条数据"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
