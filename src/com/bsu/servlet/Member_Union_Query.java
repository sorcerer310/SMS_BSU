package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Member_Union_Query
 */
@WebServlet("/member_union_query")
public class Member_Union_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Member_Union_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from member where type='工会会员'  and `delete`=0 ")
			.append(U.sc(request, U.cc(request)));

		CachedRowSetImpl rs = null;
		try {
			rs = ODB.query(sb.toString(),U.lcd(request, "member",U.cc(request)));
			
			//定义导出csv的列对应的中文名
			HashMap<String,String> hm_cn = new HashMap<String,String>();
			hm_cn.put("name", "姓名");
			hm_cn.put("gender", "性别");
			hm_cn.put("nation", "民族");
			hm_cn.put("birthday", "生日");
			hm_cn.put("native_place", "籍贯");
			hm_cn.put("join_work_date", "工作时间");
			hm_cn.put("join_org_date", "入会时间");
			hm_cn.put("unit", "单位");
			hm_cn.put("administrative_duties", "行政职务");
			hm_cn.put("org2_duties", "工会职务");
			hm_cn.put("belong_union", "所属分会");
			hm_cn.put("representative", "职工代表");
						
			U.pc(request,response, rs,hm_cn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
