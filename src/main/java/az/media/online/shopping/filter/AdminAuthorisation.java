package az.media.online.shopping.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminAuthorisation implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        String page = req.getParameter("page");

        if(session.getAttribute("admin") == null && !(uri.endsWith("login.jsp") || uri.endsWith("admin")
                || uri.endsWith("admin/") || (page != null && page.equals("admin-login-form")))) {
            System.out.println("sending to login.jsp");
            req.getRequestDispatcher("admin/login.jsp").forward(req, resp);
        } else {
            System.out.println("session is not null");
            filterChain.doFilter(req, resp);
        }
    }
}
