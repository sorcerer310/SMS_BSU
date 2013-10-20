package com.bsu.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bsu.system.db.ODB;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Info_Party_Work
 */
@WebServlet("/info_party_work")
public class Info_Party_Work extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Info_Party_Work() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from info_party_work where `delete`=0 ");
		HttpSession s = request.getSession(false);
		String unitid = "";
		try{
			unitid = s.getAttribute("unitid").toString();
		}catch(Exception e){
			U.p(response, JSONMsg.info(1111,"未从session中取得unitid,登陆可能已经超时"));
			return;
		}
		
		//判断单位id不在10个分公司之内返回错误消息,不能上传文件
		if(!unitid.equals("999")){
			String parentid = U.getParentidByUnitid(unitid);
			sb.append("and (unitid=").append(unitid)
					.append(" or unitid=999")
					.append(" or parentid=").append(parentid)
					.append(")");
		}
		
		CachedRowSetImpl rs = null;
		try{
			rs = ODB.query(sb.toString());
			U.pc(request, response, rs,new HashMap<String,String>());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
