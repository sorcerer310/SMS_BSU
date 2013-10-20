package com.bsu.servlet;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

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
 * Servlet implementation class Member_Poor
 */
@WebServlet("/member_poor_query")
public class Member_Poor_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Member_Poor_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from poor_member where `delete`=0 ")
				.append(U.sc(request, U.cc(request)));
		
		CachedRowSetImpl rs = null;
		try {
			rs = ODB.query(sb.toString(),U.lcd(request, "poor_member",U.cc(request)));
			
			//定义导出csv的列对应的中文名
			HashMap<String,String> hm_cn = new HashMap<String,String>();
			hm_cn.put("unit", "所属单位");
			hm_cn.put("problem", "困难类别");
			hm_cn.put("name", "姓名");
			hm_cn.put("gender", "性别");
			hm_cn.put("nation", "民族");
			hm_cn.put("pi", "政治面貌");
			hm_cn.put("birthday", "生日");
			hm_cn.put("idnumber", "身份证号");
			hm_cn.put("health", "健康状况");
			hm_cn.put("disability", "残疾类别");
			hm_cn.put("modelworkers", "劳模类型");
			hm_cn.put("house", "住房类型");
			hm_cn.put("phone", "电话");
			hm_cn.put("join_work_date", "工作时间");
			hm_cn.put("marriage", "婚姻状况");
			hm_cn.put("account", "户口类型");
			hm_cn.put("address", "家庭住址");
			hm_cn.put("income", "月均收入");
			hm_cn.put("problem_reason", "至困主要原因");
			
			U.pc(request,response,rs,hm_cn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
