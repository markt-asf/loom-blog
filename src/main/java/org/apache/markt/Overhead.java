package org.apache.markt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/overhead")
public class Overhead extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String exp = request.getParameter("exp");
	    FixedSizeResponse fixedSizeResponse = FixedSizeResponse.valueOf("EXP_" + exp);
	    byte[] body = fixedSizeResponse.getData();
	    // Avoid chunking
	    response.setContentLength(body.length);
	    // Configure the content type and encoding
	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    // Write the data
	    response.getOutputStream().write(body);
	}
}
