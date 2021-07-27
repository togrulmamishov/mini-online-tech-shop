package az.media.online.shopping.servlets;

import az.media.online.shopping.data.Product;
import az.media.online.shopping.data.User;
import az.media.online.shopping.data.DataSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class Controller extends HttpServlet {

    private static final ArrayList<String> cartList = new ArrayList<>();
    private ArrayList<Product> productList = new ArrayList<>();
    DataSource dataSource = new DataSource();
    HttpSession session;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        if (page == null || page.equals("index")) {
            productList = dataSource.fetch();
            session = req.getSession();
            session.setAttribute("cartlist", cartList);
            session.setAttribute("list", productList);

            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else {
            doPost(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        if (page.equals("login")) {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }

        if (page.equals("sign-up")) {
            req.getRequestDispatcher("signup.jsp").forward(req, resp);
        }

        if (page.equals("sign-up-form")) {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String username = req.getParameter("username");
            String address = req.getParameter("address");
            String pass1 = req.getParameter("password_1");
            String pass2 = req.getParameter("password_2");

            if (pass1.equals(pass2)) {
                if (!dataSource.emailExists(email)) {
                    if(!dataSource.usernameExists(username)) {
                        User user = new User();
                        user.setName(name);
                        user.setEmail(email);
                        user.setUsername(username);
                        user.setAddress(address);
                        user.setPassword(pass1);

                        dataSource.registerUser(user);

                        req.setAttribute("username", username);
                        req.setAttribute("msg", "Account created successfully");
                        req.getRequestDispatcher("login.jsp").forward(req, resp);
                    } else {
                        req.setAttribute("msg","This username is already registered");
                        req.setAttribute("name",name);
                        req.setAttribute("address",address);
                        req.setAttribute("email", email);
                        req.getRequestDispatcher("signup.jsp").forward(req, resp);
                    }
                } else {
                    req.setAttribute("msg","This email is already registered");
                    req.setAttribute("name",name);
                    req.setAttribute("address",address);
                    req.setAttribute("username", username);
                    req.getRequestDispatcher("signup.jsp").forward(req, resp);
                }
            } else {
                req.setAttribute("msg", "The two passwords do not match");
                req.setAttribute("name", name);
                req.setAttribute("address", address);
                req.setAttribute("email", email);
                req.setAttribute("username", username);
                req.getRequestDispatcher("signup.jsp").forward(req, resp);
            }
        }

        if (page.equals("login-form")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            User user = new User();
            if (dataSource.checkUser(username, password)) {
                session = req.getSession();
                session.setAttribute("session", session);

                ArrayList<User> users = dataSource.fetchUser();
                session.setAttribute("address", user.fetchAddress(users, username));
                session.setAttribute("email", user.fetchEmail(users, username));
                session.setAttribute("name", user.fetchName(users, username));
                session.setAttribute("username", username);
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            } else {
                req.setAttribute("msg", "Invalid Credentials");
                req.setAttribute("username", username);
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        }


        if (page.equals("logout")) {
            session = req.getSession();
            session.invalidate();

            session = req.getSession();
            cartList.clear();

            session.setAttribute("cartList", cartList);
            session.setAttribute("list", productList);

            req.getRequestDispatcher("index.jsp").forward(req, resp);

        }

        if (page.equals("mobiles") || page.equals("laptops") || page.equals("all-products")
                || page.equals("tvs") || page.equals("watches")) {
            productList = dataSource.fetch();
            req.setAttribute("list", productList);
            dataSource.close();

            if (page.equals("mobiles"))
                req.getRequestDispatcher("mobiles.jsp").forward(req, resp);
            if (page.equals("laptops"))
                req.getRequestDispatcher("laptops.jsp").forward(req, resp);
            if (page.equals("tvs"))
                req.getRequestDispatcher("tvs.jsp").forward(req, resp);
            if (page.equals("watches"))
                req.getRequestDispatcher("watches.jsp").forward(req, resp);
            if (page.equals("all-products"))
                req.getRequestDispatcher("all-products.jsp").forward(req, resp);
        }

        if (page.equals("showcart")) {
            req.getRequestDispatcher("cart.jsp").forward(req, resp);
        }

        if (page.equals("addtocart")) {
            String id = req.getParameter("id");
            String action = req.getParameter("action");

            Product product = new Product();
            if (product.check(cartList, id)) {
                JOptionPane.showMessageDialog(null,
                        "Product is already added to Cart", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                cartList.add(id);
                JOptionPane.showMessageDialog(null,
                        "Product successfully added to Cart", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            if (action.equals("index"))
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            if (action.equals("allproducts"))
                req.getRequestDispatcher("all-products.jsp").forward(req, resp);
            if (action.equals("laptops"))
                req.getRequestDispatcher("laptops.jsp").forward(req, resp);
            if (action.equals("mobiles"))
                req.getRequestDispatcher("mobiles.jsp").forward(req, resp);
            if (action.equals("watches"))
                req.getRequestDispatcher("watches.jsp").forward(req, resp);
            if (action.equals("tvs"))
                req.getRequestDispatcher("tvs.jsp").forward(req, resp);



        }

        if (page.equals("success")) {
            req.getRequestDispatcher("success.jsp").forward(req, resp);
        }

        if (page.equals("remove")) {
            String productId = req.getParameter("id");
            Product product = new Product();
            product.remove(cartList, productId);

            session = req.getSession();
            session.setAttribute("cartlist", cartList);
            req.getRequestDispatcher("cart.jsp").forward(req, resp);
        }

        if (page.equals("price-sort")) {
            String price = req.getParameter("sort");
            String action = req.getParameter("action");
            Product product = new Product();

            if (price.equals("low-to-high"))
                product.lowToHigh(productList);
            else
                product.highToLow(productList);

            session.setAttribute("list", productList);

            if (action.equals("index"))
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            if (action.equals("all-products"))
                req.getRequestDispatcher("all-products.jsp").forward(req, resp);
            if (action.equals("laptops"))
                req.getRequestDispatcher("laptops.jsp").forward(req, resp);
            if (action.equals("mobiles"))
                req.getRequestDispatcher("mobiles.jsp").forward(req, resp);
            if (action.equals("tvs"))
                req.getRequestDispatcher("tvs.jsp").forward(req, resp);
            if (action.equals("watches"))
                req.getRequestDispatcher("watches.jsp").forward(req, resp);
        }
    }
}
