package com.android.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.datatype.GlobalValues;
import com.android.testjetty.testDB.DBTest;

/**
 * Servlet implementation class SystemInitServlet
 */
public class SystemInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


//	android.content.ContentResolver resolver = null;
//	android.content.Context androidContext = null;
//	DBTest dbTest = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SystemInitServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("enter SystemInitServlet::init");
		GlobalValues.contentResolver =
	    		(android.content.ContentResolver)
	    			config.getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
		GlobalValues.context =
	    		(android.content.Context)
	    			config.getServletContext().getAttribute("org.mortbay.ijetty.context");

//	    dbTest = new DBTest(androidContext);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
