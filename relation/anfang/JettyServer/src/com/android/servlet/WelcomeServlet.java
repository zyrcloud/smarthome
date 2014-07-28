package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.testjetty.testDB.DBTest;

/**
 * Servlet implementation class MyServlet
 */
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	android.content.ContentResolver resolver = null;
	android.content.Context androidContext = null;
	DBTest dbTest = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WelcomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    resolver = 
	    		(android.content.ContentResolver) 
	    			config.getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
	    androidContext = 
	    		(android.content.Context) 
	    			config.getServletContext().getAttribute("org.mortbay.ijetty.context");
	    dbTest = new DBTest(androidContext);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.write("weclome visit my page, visit user num:" + dbTest.addOne());
		out.write(" it running on c8812 phone");
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
