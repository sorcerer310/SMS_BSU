package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Servlet implementation class GetFunction
 */
@WebServlet("/getmenufunction")
public class GetMenuFunction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMenuFunction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String name = request.getParameter("no");
		String name = request.getSession().getAttribute("name").toString();
		List<ColumnData> ldata = new ArrayList<ColumnData>();
		ldata.add(new ColumnData("no",Types.VARCHAR,name));
		CachedRowSetImpl rs = null;
		
		StringBuffer sb_allfunc = new StringBuffer();							//获得该用户所有功能名 
		sb_allfunc.append("select f.parentno,f.no,f.uri from func f ")
			.append("inner join groupfunc gf on f.id = gf.funcid ")
			.append("inner join user u on u.groupid = gf.groupid ")
			.append("where u.no =?");
		
		/**
		 * 分两步取数据组装json.限定菜单只有2级
		 * 1:检索所有的父类功能名.将父类功能名对应的子功能组装成json单元
		 * 2:组装父级字符串children属性设置为组装成的json单元.
		 * 3:组装根级功能json
		 * 4:将根级功能与带子节点的功能组装到一起
		 */
		try {
			rs = ODB.query(sb_allfunc.toString(),ldata);
			int id = 1;																				//自增id
			Map<String,ArrayList<FuncData>> pfunc = new HashMap<String,ArrayList<FuncData>>();	//保存父级功能	
			//收集所有功能数据
			while(rs.next())
			{
				if(rs.getString("parentno")==null)
				{
					//如果没有根级功能创建根级功能的数组
					if(pfunc.get("rootfunc")==null)
						pfunc.put("rootfunc", new ArrayList<FuncData>());
					pfunc.get("rootfunc").add(new FuncData(rs.getString("no"),rs.getString("uri"),""));					//增加根级别功能
				}
				else
				{
					//有根级目录的功能
					if(pfunc.get(rs.getString("parentno"))==null)
						pfunc.put(rs.getString("parentno"), new ArrayList<FuncData>());
					pfunc.get(rs.getString("parentno")).add(new FuncData(rs.getString("no"),rs.getString("uri"),rs.getString("parentno")));
				}
			}
			
			//保存每个根节点与其子节点
			Map<String,String> jsonfunc = new HashMap<String,String>();
			//获得所有根目录数据
			Iterator<String> pkey = pfunc.keySet().iterator();
			while(pkey.hasNext()){
				String root =  pkey.next();
				if(!root.equals("rootfunc"))
				{
					//组合所有2级功能并保存到jsonfunc中
					StringBuffer sb = new StringBuffer();
					sb.append("[");
					for(FuncData fd : pfunc.get(root))
					{
						//一个节点的数据
						sb.append("{\"id\":").append(id)
							.append(",\"text\":\"").append(fd.getNo())	.append("\"")
							.append(",\"attributes\":\"").append(fd.getUri()).append("\"")
							.append("},");
						id++;
					}
					String lnodejson = sb.substring(0,sb.length()-1);
					lnodejson += "]";
					//保存当前根节点下所有节点的json数据
					jsonfunc.put(root, lnodejson);
				}
			}
			
			//所有节点的json
			StringBuffer sb_json = new StringBuffer();
			sb_json.append("[");
			

			//组装2级功能结点
			Iterator<String> rootfold = pfunc.keySet().iterator();
			while(rootfold.hasNext()){
				String key = rootfold.next();
				if(!key.equals("rootfunc")){
					sb_json.append("{\"id\":").append(id)
						.append(",\"text\":\"").append(key).append("\"")
						//组装子节点
						.append(",\"children\":").append(jsonfunc.get(key)).append("},");
					id++;
				}
			}

			//组装根级功能节点,如果不存在根节点功能不增加对应的json数据
			if(pfunc.containsKey("rootfunc"))
			{
				Iterator<FuncData> rootnode = pfunc.get("rootfunc").iterator();
				while(rootnode.hasNext()){
					FuncData fd = rootnode.next();									//获得所有根结点
					sb_json.append("{\"id\":").append(id)
						.append(",\"text\":\"").append(fd.getNo()).append("\"")
						.append(",\"attributes\":\"").append(fd.getUri()).append("\"")
						.append("},");
					id++;
				} 
			}
			
			String s_json = sb_json.substring(0,sb_json.length()-1);
			s_json+="]";
			
			response.getWriter().print(s_json);
		
		} catch (SQLException | ParseException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
/**
 * 功能数据
 * @author fengchong
 *
 */
class FuncData{
	private String no = "";
	private String uri = "";
	private String parentno = "";
	public FuncData(String pn,String pu,String ppn){
		no = pn;
		uri = pu;
		parentno = ppn;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getParentno() {
		return parentno;
	}
	public void setParentno(String parentno) {
		this.parentno = parentno;
	}
}