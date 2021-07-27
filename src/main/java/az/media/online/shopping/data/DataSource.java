package az.media.online.shopping.data;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class DataSource {

    private final String DB_NAME = "online_shopping";
    private final String URL = "jdbc:mysql://127.0.0.1:3306/" + DB_NAME;
    private final String USERNAME = "root";
    private final String PASSWORD = "1234";
    private final String TABLE_USER = "users";
    private final String TABLE_PRODUCT = "products";

    private Connection conn;
    private PreparedStatement preparedStatement;
    private final ProtectPass protectPass = new ProtectPass();
    String encPass;


    public void open() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public boolean emailExists(String email) {
        open();
        int count=0;
        try {
            preparedStatement = conn.prepareStatement("SELECT email FROM " + TABLE_USER +
                    " WHERE email = ?");
            preparedStatement.setString(1,email);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                count = 1;
            }
            if(count == 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return true;
    }

    public boolean usernameExists(String user) {
        open();
        int count=0;
        try {
            preparedStatement = conn.prepareStatement("SELECT username FROM " + TABLE_USER +
                    " WHERE username = ?");
            preparedStatement.setString(1,user);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                count = 1;
            }
            if(count == 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return true;
    }

    public ArrayList<Product> fetch() {
        ArrayList<Product> list = new ArrayList<>();
        open();
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM " + TABLE_PRODUCT);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                int id = results.getInt("id");
                String name = results.getString("name");
                String price = results.getString("price");
                String featured = results.getString("featured");
                String category = results.getString("category");
                String image = results.getString("image");

                Product product = new Product();
                product.setId(id);
                product.setName(name);
                product.setPrice(price);
                product.setFeatured(featured);
                product.setCategory(category);
                product.setImage(image);
                list.add(product);
                product = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return list;
    }

    public void deleteProduct(String id) {
        open();
        try {
            preparedStatement = conn.prepareStatement("DELETE FROM " + TABLE_PRODUCT + " WHERE id = ?");
            preparedStatement.setString(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    public void addProduct(Product product) {
        open();
        try {
            preparedStatement = conn.prepareStatement("INSERT INTO " + TABLE_PRODUCT +
                    "(name, price, category, featured, image) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1,product.getName());
            preparedStatement.setString(2,product.getPrice());
            preparedStatement.setString(3,product.getCategory());
            preparedStatement.setString(4,product.getFeatured());
            preparedStatement.setString(5,product.getImage());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    public Product fetchProduct(String id) {
        open();
        Product product = new Product();
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM " + TABLE_PRODUCT +
                                                    " WHERE id=?");
            preparedStatement.setString(1,id);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                product.setId(results.getInt("id"));
                product.setName(results.getString("name"));
                product.setCategory(results.getString("category"));
                product.setPrice(results.getString("price"));
                product.setFeatured(results.getString("featured"));
                product.setImage(results.getString("image"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return product;
    }

    public void updateProduct(Product product) {
        open();
        try {
            preparedStatement = conn.prepareStatement("UPDATE " + TABLE_PRODUCT +
                    " SET name=?, price=?, category=?, featured=? WHERE id=?");
            preparedStatement.setString(1,product.getName());
            preparedStatement.setString(2,product.getPrice());
            preparedStatement.setString(3,product.getCategory());
            preparedStatement.setString(4,product.getFeatured());
            preparedStatement.setInt(5,product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }



    public ArrayList<User> fetchUser() {
        ArrayList<User> list = new ArrayList<>();
        open();
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM " + TABLE_USER);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                String address = results.getString("address");
                String email = results.getString("email");
                String username = results.getString("username");
                String name = results.getString("name");
                int id = results.getInt("id");
                String password = results.getString("password");

                User user = new User();
                user.setAddress(address);
                user.setEmail(email);
                user.setUsername(username);
                user.setName(name);
                user.setId(id);
                user.setPassword(password);

                list.add(user);
                user = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return list;
    }

    public void registerUser(User user) {
        Date now = new Date(System.currentTimeMillis());
        String regDate = now.toString();
        encPass = protectPass.encryptString(user.getPassword());
        open();
        try {
            preparedStatement = conn.prepareStatement("INSERT INTO " + TABLE_USER +
                    " (name,email,username,address," +
                    "password,registration_date) VALUES(?,?,?,?,?,?)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, encPass);
            preparedStatement.setString(6, regDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        close();
    }

    public boolean checkUser(String username, String password) {
        open();
        encPass = protectPass.encryptString(password);
        try {
            preparedStatement = conn.prepareStatement("SELECT username,password FROM " + TABLE_USER +
                    " WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet results = preparedStatement.executeQuery();
            results.next();
            String pass = results.getString(2);
            close();
            return encPass.equals(pass);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class ProtectPass {

        String encryptString(String pass) {
            try {
                final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
                final byte[] hashBytes = digest.digest(pass.getBytes(StandardCharsets.UTF_8));
                return bytesToHex(hashBytes);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Encryption failed " + e.getMessage());
                return null;
            }
        }

        private String bytesToHex(byte[] hash) {
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
    }
}
