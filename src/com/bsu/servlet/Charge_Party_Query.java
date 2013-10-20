package com.bsu.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Charge_Party_Query
 */
@WebServlet("/charge_party_query")
public class Charge_Party_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Charge_Party_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from charge where type='党费' and `delete`=0 ")
				.append(U.sc(request, U.cc(request)));
		
		CachedRowSetImpl rs = null;
		try {
			rs = ODB.query(sb.toString(),U.lcd(request, "charge",U.cc(request)));
			
			//定义导出csv的列对应的中文名
			HashMap<String,String> hm_cn = new HashMap<String,String>();
			hm_cn.put("name", "姓名");
			hm_cn.put("unit", "单位");
			hm_cn.put("join_org_date", "入党时间");
			hm_cn.put("identity", "身份");
			hm_cn.put("ispay", "是否缴纳");
			hm_cn.put("income", "工资基数");
			hm_cn.put("proportion", "缴纳比例");
			hm_cn.put("charge_month", "缴纳党费/月");
			hm_cn.put("charge_quarter", "缴纳党费/季");
			
			U.pc(request,response,rs,hm_cn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
