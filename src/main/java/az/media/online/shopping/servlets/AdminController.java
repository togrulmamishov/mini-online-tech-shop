package az.media.online.shopping.servlets;

import az.media.online.shopping.data.DataSource;
import az.media.online.shopping.data.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;

public class AdminController extends HttpServlet {
    private final DataSource dataSource = new DataSource();
    private HttpSession session;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");

        if (page == null) {
            req.getRequestDispatcher("admin/login.jsp").forward(req, resp);
        } else {
            doPost(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");

        if (page.equals("admin-login-form")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            if (username.equals("admin") && password.equals("admin")) {
                session = req.getSession();
                session.setAttribute("admin", "adminLog");
                req.getRequestDispatcher("admin/index.jsp").forward(req, resp);
            } else {
                req.setAttribute("msg", "Invalid Credentials");
                req.setAttribute("username", username);
                req.getRequestDispatcher("admin/login.jsp").forward(req, resp);
            }
        }

        if (page.equals("logout")) {
            if (session != null)
                session.invalidate();
            req.getRequestDispatcher("admin/login.jsp").forward(req, resp);
        }

        if (page.equals("delete")) {
            String id = req.getParameter("id");
            dataSource.deleteProduct(id);

            JOptionPane.showMessageDialog(null, "Product deleted successfully",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            req.getRequestDispatcher("admin/index.jsp").forward(req, resp);
        }

        if (page.equals("index")) {
            req.getRequestDispatcher("admin/index.jsp").forward(req, resp);
        }

        if (page.equals("addproduct")) {
            req.getRequestDispatcher("admin/addProduct.jsp").forward(req, resp);
        }

        if (page.equals("edit")) {
            String id = req.getParameter("id");
            Product product = dataSource.fetchProduct(id);

            req.setAttribute("p", product);
            req.getRequestDispatcher("admin/editProduct.jsp").forward(req, resp);
        }

        if (page.equals("edit_product")) {
            String id = req.getParameter("id");
            String name = req.getParameter("name");
            String price = req.getParameter("price");
            String category = req.getParameter("category");
            String featured = req.getParameter("featured");

            if (!(category.equals("laptops") || category.equals("mobiles")
                    || category.equals("tvs") || category.equals("watches"))) {
                req.setAttribute("msg", "Category '" + category + "' doesn't exist");
            } else {
                Product product = new Product();
                product.setId(Integer.parseInt(id));
                product.setName(name);
                product.setPrice(price);
                product.setCategory(category);
                product.setFeatured(featured);

                dataSource.updateProduct(product);
                JOptionPane.showMessageDialog(null, "Product details updated successfully",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            req.getRequestDispatcher("admin/index.jsp").forward(req, resp);
        }

        if (page.equals("add_product")) {
            String name = req.getParameter("name");
            String price = req.getParameter("price");
            String category = req.getParameter("category");
            String featured = req.getParameter("featured");
            String img = req.getParameter("image");
            if (!(category.equals("laptops") || category.equals("mobiles")
                    || category.equals("tvs") || category.equals("watches"))) {
                req.setAttribute("msg", "Category '" + category + "' doesn't exist");
            } else {
                Product product = new Product();
                product.setName(name);
                product.setPrice(price);
                product.setCategory(category);
                product.setFeatured(featured);
                product.setImage("img/" + img);

                dataSource.addProduct(product);
                JOptionPane.showMessageDialog(null, "Product added successfully", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            req.getRequestDispatcher("admin/addProduct.jsp").forward(req, resp);
        }
    }
}
