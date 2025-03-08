package VendingMachine.Service;

import VendingMachine.Connection.Connection_DB;

import java.math.BigDecimal;
import java.sql.*;

public class VendingMachineService {
    public static void showProductList() throws Exception {
        try (Connection connection = Connection_DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM vendingmachine.show_product_list()");
             ResultSet res = stmt.executeQuery()) {

            if (!res.isBeforeFirst()) {
                System.out.println("No products found.");
            } else {
                while (res.next()) {
                    System.out.printf("ID: %d, Name: %s, Price: %.2f, Quantity: %d%n",
                            res.getInt("product_id"),
                            res.getString("product_name"),
                            res.getBigDecimal("price"),
                            res.getInt("quantity"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int createProduct(String name, BigDecimal price, int quantity) throws Exception {
        try (Connection connection = Connection_DB.getConnection();
             CallableStatement stmt = connection.prepareCall("{ ? = call vendingmachine.create_product(?, ?, ?) }")) {

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, name);
            stmt.setBigDecimal(3, price);
            stmt.setInt(4, quantity);
            stmt.execute();
            return stmt.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean deleteProduct(int productID) throws Exception {
        try (Connection connection = Connection_DB.getConnection();
             CallableStatement stmt = connection.prepareCall("{ ? = call vendingmachine.delete_product(?) }")) {

            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setInt(2, productID);
            stmt.execute();
            return stmt.getBoolean(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String updateProduct(int productID, String new_name, BigDecimal new_price, int new_quantity) throws Exception {
        try (Connection connection = Connection_DB.getConnection();
             CallableStatement stmt = connection.prepareCall("{ ? = call vendingmachine.update_product(?, ?, ?, ?) }")) {

            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setInt(2, productID);
            stmt.setString(3, new_name);
            stmt.setBigDecimal(4, new_price);
            stmt.setInt(5, new_quantity);
            stmt.execute();
            return stmt.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String buyProduct(int productID, int quantity) throws Exception {
        try (Connection connection = Connection_DB.getConnection();
             CallableStatement stmt = connection.prepareCall("{ ? = call vendingmachine.buy_product(?, ?) }")) {

            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setInt(2, productID);
            stmt.setInt(3, quantity);
            stmt.execute();
            return stmt.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        showProductList();
        //System.out.println(buyProduct(2, 4));
        //System.out.println(deleteProduct(4));
        //System.out.println(updateProduct(3,null,new BigDecimal("3.33"),10));
        //System.out.println(createProduct("Ice-cream",new BigDecimal("5.5"),4));

    }
}
