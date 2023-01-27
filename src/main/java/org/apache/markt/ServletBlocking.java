package org.apache.markt;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/blocking")
public class ServletBlocking extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Service.blocking();
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getOutputStream().println("OK");
    }
}
