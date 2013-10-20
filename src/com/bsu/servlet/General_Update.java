package com.bsu.servlet;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.DB;
import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * Servlet implementation class General_Update
 */
@WebServlet("/general_update")
public class General_Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public General_Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println(U.pp(request));
			
			if(request.getParameter("state")==null){
				U.p(response, JSONMsg.info(1004));
				return;
			}
			int state = Integer.parseInt(request.getParameter("state"));								//当前数据的状态，1为插入新数据，0为更新数据，-1为删除数据
			
			if(request.getParameter("table")==null){
				U.p(response, JSONMsg.info(1006));
				return; 
			}
			String table = request.getParameter("table");													//获得要操作的表名
			HashMap<String, Integer> hm = DB.tableInfo.get(table.replace("`", ""));				//获得表对应所有列的类型信息
			StringBuffer sbsql = new StringBuffer();															//sql语句头部分
			StringBuffer sb2 = new StringBuffer();																//sql语句第2部分
			StringBuffer sb3 = new StringBuffer();																//sql语句第3部分
			ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();						//参数类型与值部分
			
			switch(state){
			case 1:
				sbsql.append("insert into ").append(table);
				break;
			case 0:
				sbsql.append("update ").append(table).append(" set ");
				break;
			case -1:
				sbsql.append("delete from ").append(table).append(" where 1=1");
				break;
			default:
				U.p(response, JSONMsg.info(1005, "state参数为"+String.valueOf(state)));
				return;
			}
			
			Enumeration<String> e = request.getParameterNames();									//获得所有参数名		
			while(e.hasMoreElements()){
				String cn = e.nextElement();																			//获得列名
				//当字段为table与state时越过
				if(cn.equals("table") || cn.equals("state") || cn.equals("_"))
					continue;
				switch(state){
				case 1:
					if(cn.equals("id"))
						continue;
					sb2.append(cn).append(",");																		//组装sql列名部分
					sb3.append("?,");																						//组装sql值部分
					ldata.add(new ColumnData(cn,hm.get(cn),request.getParameter(cn)));			//获得sql中所有参数
					break;
				case 0:
					//当列不为id时更新所有的列数据到数据库中
					if(cn.equals("id"))
						continue;
					sb2.append(cn).append("=?,");
					ldata.add(new ColumnData(cn,hm.get(cn),request.getParameter(cn)));			//获得sql中所有参数
					break;
				case -1:
					sb2.append(" and "+cn+"=? ");
					ldata.add(new ColumnData(cn,hm.get(cn),request.getParameter(cn)));			//获得sql中所有参数
					break;
				}
			}
			
			switch(state){
			case 1:
				sbsql.append(" (").append(U.rl(sb2))
				.append(") values (").append(U.rl(sb3))
				.append(")");
				int id = ODB.insert(sbsql.toString(), ldata);
				U.p(response, JSONParser.parseSimpleJson("id", id));
				//U.p(response,JSONMsg.info(1006));
				break;
			case 0:
				sb3.append(" where id=?");
				sbsql.append(U.rl(sb2)).append(sb3);
				if(request.getParameter("id")==null){
					U.p(response, JSONMsg.info(1007));
					return;
				}
				ldata.add(new ColumnData("id",hm.get("id"),request.getParameter("id")));
				int c = ODB.update(sbsql.toString(), ldata);
				U.p(response, JSONMsg.info(3003, "更新了"+String.valueOf(c)+"条数据"));
				break;
			case -1:
				sbsql.append(sb2);
				int c1 = ODB.update(sbsql.toString(), ldata);
				U.p(response, JSONMsg.info(3004, "删除了"+String.valueOf(c1)+"条数据"));
				break;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			U.p(response, JSONMsg.info(1111,e1.getMessage()));
		}
		

	}

}
