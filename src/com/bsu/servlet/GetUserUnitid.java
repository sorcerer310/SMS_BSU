package com.bsu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.bsu.system.tool.U;

/**
 * Servlet implementation class GetUserUnitid
 */
@WebServlet("/getuserunitid")
public class GetUserUnitid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserUnitid() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			if(request.getSession(false)==null){
				U.p(response, "0");
				return;
			}
			String unitid = request.getSession(false).getAttribute("unitid").toString();
			JSONObject jo = new JSONObject();
			try {
				jo.put("unitid",unitid);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			U.p(response, jo.toString());
	}

}
