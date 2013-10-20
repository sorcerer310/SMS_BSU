package com.bsu.system.tool;

import java.io.*;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.bsu.system.db.DB;
import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.parser.CSVParser;
import com.bsu.system.parser.JSONParser;
import com.sun.rowset.CachedRowSetImpl;

/**
 * 工具类,用来实现一些快捷的操作方法
 * @author fengchong
 *
 */
public class U {

	/**
	 * 通过字段名与request获得sql条件
	 * @param request	request对象
	 * @param cn			所有的字段名
	 * @return
	 */
	public static String sc(HttpServletRequest request,String[] cn){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<cn.length;i++){
			if(request.getParameter(cn[i])==null)
				continue;
			sb.append(" and "+cn[i]+"=?");
		}
		return sb.toString();
	}
	
	/**
	 * cc:collect column
	 * 收集所有条件字段,返回条件列的字符串数组
	 * @param request
	 * @return
	 */
	public static String[] cc(HttpServletRequest request){
		ArrayList<String> list = new ArrayList<String>();
		Enumeration<String> en =  request.getParameterNames();
		while(en.hasMoreElements()){
			String s = en.nextElement();
			if(s.equals("csv") || s.equals("_"))
				continue;
			list.add(s);
		}
		return list.toArray(new String[]{});
	}
	/**
	 * lcd:list column data
	 * 根据参数名,参数类型,与request获得的值组织ColumnData数据
	 * @param request	request对象
	 * @param tname		对应表名
	 * @param cn			所有列的字符串数组
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<ColumnData> lcd(HttpServletRequest request,String tname,String[] cn) throws Exception{
		ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
		int len = cn.length;
		HashMap<String,Integer> hm = DB.tableInfo.get(tname);
		for(int i=0;i<len;i++){
			if(request.getParameter(cn[i])==null)
				continue;
			ldata.add(new ColumnData(cn[i],hm.get(cn[i]),request.getParameter(cn[i])));
		}
		return ldata;
	}

	/**
	 * p:print
	 * 向页面输出数据
	 * @param response			response对象
	 * @param s						要输出的文字
	 * @throws IOException
	 */
	public static void p(HttpServletResponse response,String s) throws IOException{
//		response.getOutputStream().print(s);
		response.getWriter().print(s);
	}
	/**
	 * pc:print csv
	 * 从request参数中判断是向页面输出json数据还是提供csv文件下载
	 * @param request		
	 * @param response		
	 * @param rs					数据集
	 * @param colname		要到出csv的字段信息
	 * @throws JSONException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void pc(HttpServletRequest request,HttpServletResponse response,CachedRowSetImpl rs,HashMap<String,String> colname) throws IOException, SQLException, JSONException{
		String csv = request.getParameter("csv");
		if(csv!=null && csv.equals("true")){
			ServletOutputStream out = null;
			try{
				//设置输出csv的头信息
				response.setContentType("text/csv");
				String disposition = "attachment; fileName=data.csv";
				response.setHeader("Content-Disposition", disposition);
				//获得输出对象
				out = response.getOutputStream();
				//获得数据
				byte[] blobData = CSVParser.parseCsv(rs,colname).getBytes();
				out.write(blobData);
				out.flush();
				out.close();
			}catch(Exception e){
				throw e;
			}finally{
				if(out != null)
					out.close();
			}
		}
		else
			p(response, JSONParser.parseJson(rs));
	}
	
	/**
	 * rl:remove last
	 * 移除字符串中最后一个字符并返回对应的字符串
	 * @param sb	要处理的字符
	 * @return
	 */
	public static String rl(StringBuffer sb){
		return sb.substring(0, sb.length()-1);
	}
	/**
	 * pp:print param
	 * 打印出所有传入的参数
	 * @return
	 */
	public static String pp(HttpServletRequest request){
		Enumeration<String> e = request.getParameterNames();
		StringBuffer sb = new StringBuffer();
		try {
			while(e.hasMoreElements()){
				String key = e.nextElement();
				sb.append(key).append("=").append(request.getParameter(key)).append("&");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return sb.toString();
	}

    /**
     * 判断是数据是否匹配某模式
     * @param reg   正则表达式
     * @param data  数据
     * @return      返回是否匹配
     */
    public static boolean r_im(String reg,String data){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }
    /**
     * 返回匹配的字符数据
     * @param reg   正则表达式
     * @param data  数据
     * @return      返回匹配的数据
     */
    public static String r_gm(String reg,String data){
        String retval = "";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        if(matcher.find())
            retval = matcher.group(1);
        return retval;
    }
    
	/**
	 * 通过字段名返回request参数，如果该字段名的参数不存在则返回null
	 * @param request	request对象
	 * @param colname	字段名
	 * @return
	 */
	public static String getRS(HttpServletRequest request, String colname){
		String colval = null;
		if(request.getParameter(colname)!=null && !request.getParameter(colname).equals(""))
			colval = request.getParameter(colname).toString();
		return colval;
	}
    
	/**
	 * 判断一个数组在另一个数组中的位置
	 * @param b			源数组
	 * @param s			要搜索的数组
	 * @param start 	开始搜索的位置
	 * @return				返回所在位置
	 */
    public static int byteIndexOf(byte[] b,byte[] s,int start)
    {
        int i;
        if(s.length==0)                                                         //如果要搜索的数组长度为0，直接返回0
            return 0;
        int max=b.length-s.length;                                              //获得要搜索的位置最大值
        if(max<0)                                                               //如果最大值小于0返回-1 不可搜索
            return -1;
        if(start>max)                                                           //如果开始位置大雨搜索位置最大值，返回-1不可搜索
            return -1;
        if (start<0)                                                            //如果开始位置小于0，设置开始位置为0
            start=0;
        search:                                                                 //开始搜索
        for(i=start;i<=max;i++)
        {
            if (b[i]==s[0])                                                     //如果找到了搜索字符的开始字符对整个搜索字符开始进行比较
            {
                int k=1;
                while(k<s.length)
                {
                    if(b[k+i]!=s[k])
                        continue search;                                        //如字节数组比较不成功返回search，否则继续比较下一搜索字节数组的字节
                    k++;
                }
                return i;
            }
        }
        return -1;
    }
    /**
     * 党务信息专用,通过unitid获得parentid
     * @param uid
     * @return
     */
	public static String getParentidByUnitid(String uid){
		StringBuffer sb = new StringBuffer();
		sb.append("select id,item,unitid from info_party_work where parentid=0 and unitid = ?");
		ArrayList<ColumnData> ldata = new ArrayList<ColumnData>();
		ldata.add(new ColumnData("unitid",Types.INTEGER,uid));
		String pid = null;
		CachedRowSetImpl rs = null;
		try{
			rs = ODB.query(sb.toString(),ldata);
			if(rs.next())
				pid = String.valueOf(rs.getInt("id")); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return pid;
	}
    
}
