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

import com.bsu.system.db.DB;
import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class Member_Party_Query
 */
@WebServlet("/member_party_query")
public class Member_Party_Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Member_Party_Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from member where type='党员' and `delete`=0 ");
				//.append(U.sc(request, U.cc(request)));
		//sql字符串部分
		String[] cn = U.cc(request);										//将带入参数转换为数组形式
		StringBuffer sbcond = new StringBuffer();			
		for(int i=0;i<cn.length;i++){	
			if(request.getParameter(cn[i])==null)
				continue;
			if(cn[i].equals("birthday") || cn[i].equals("join_work_date") || cn[i].equals("join_org_date") || cn[i].equals("positive_date"))
				sbcond.append(" and "+cn[i]+">=? and "+cn[i]+"<? ");
			else
				sbcond.append(" and "+cn[i]+"=?");
		}
		sb.append(sbcond);
		//ArrayList参数部分
		ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
		int len = cn.length;
		HashMap<String,Integer> hm = DB.tableInfo.get("member");
		for(int i=0;i<len;i++){
			if(request.getParameter(cn[i])==null)
				continue;
			if(cn[i].equals("birthday") || cn[i].equals("join_work_date") || cn[i].equals("join_org_date") || cn[i].equals("positive_date")){
				String[] dstr = request.getParameter(cn[i]).split("\\|");
				String bdstr = dstr[0];
				String edstr = dstr[1];
				
				ldata.add(new ColumnData(cn[i],hm.get(cn[i]),bdstr));
				ldata.add(new ColumnData(cn[i],hm.get(cn[i]),edstr));
			}else
				ldata.add(new ColumnData(cn[i],hm.get(cn[i]),request.getParameter(cn[i])));
		}
		
		
		CachedRowSetImpl rs = null;
		try {
			rs = ODB.query(sb.toString(),ldata);
			
			//定义导出csv的列对应的中文名
			HashMap<String,String> hm_cn = new HashMap<String,String>();
			hm_cn.put("name", "姓名");
			hm_cn.put("gender", "性别");
			hm_cn.put("nation", "民族");
			hm_cn.put("birthday", "生日");
			hm_cn.put("native_place", "籍贯");
			hm_cn.put("educational_background", "文化程度");
			hm_cn.put("join_work_date", "工作时间");
			hm_cn.put("join_org_date", "入党时间");
			hm_cn.put("positive_date", "转正时间");
			hm_cn.put("unit", "单位");
			hm_cn.put("administrative_duties", "行政职务");
			hm_cn.put("org1_duties", "党内职务");
			hm_cn.put("excellent_in_party", "先锋岗");
			hm_cn.put("area_of_responsibility", "责任区");
			hm_cn.put("belong_party", "党支部");
			hm_cn.put("excellent_party_member", "优秀党员");

			U.pc(request,response,rs,hm_cn); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
