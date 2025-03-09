package VendingMachine.Frontend;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class VendingMachineServer {

    // Mahsulotlar va ularning soni
    private static Map<String, Integer> products = new HashMap<>();

    static {
        products.put("Soda", 10);
        products.put("Chips", 15);
        products.put("Candy", 20);
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new VendingMachineHandler());
        server.createContext("/buy", new PurchaseHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on http://localhost:8000");
    }

    // Vending Machine interfeysi
    static class VendingMachineHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><head><style>" +
                    "body { font-family: Arial, sans-serif; text-align: center; }" +
                    "h1 { color: #333; }" +
                    "form { background-color: #f4f4f4; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }" +
                    "label { font-size: 18px; }" +
                    "select { padding: 10px; font-size: 16px; margin-top: 10px; }" +
                    "input[type='submit'] { padding: 10px 20px; font-size: 16px; margin-top: 20px; background-color: #4CAF50; color: white; border: none; border-radius: 5px; }" +
                    "input[type='submit']:hover { background-color: #45a049; }" +
                    "</style></head><body>" +
                    "<h1>Welcome to Vending Machine</h1>" +
                    "<form action='/buy' method='get'>" +
                    "<label for='item'>Select item: </label>" +
                    "<select name='item' id='item'>";

            for (Map.Entry<String, Integer> entry : products.entrySet()) {
                response += "<option value='" + entry.getKey() + "'>" + entry.getKey() + " (" + entry.getValue() + " left)</option>";
            }

            response += "</select><br><br>" +
                    "<label for='quantity'>Select quantity: </label>" +
                    "<input type='number' name='quantity' id='quantity' min='1' max='10' required><br><br>" +
                    "<input type='submit' value='Buy'>" +
                    "</form>" +
                    "</body></html>";

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Buyurtma qabul qilish
    static class PurchaseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String[] params = query.split("&");
            String item = params[0].split("=")[1];
            int quantity = Integer.parseInt(params[1].split("=")[1]);

            String response = "<html><body>";

            if (products.containsKey(item) && products.get(item) >= quantity) {
                products.put(item, products.get(item) - quantity);
                response += "<h2>You have successfully purchased " + quantity + " " + item + "(s)</h2>";
            } else {
                response += "<h2>Not enough stock for " + item + ".</h2>";
            }

            response += "<br><br><a href='/'>Back to Machine</a>" +
                    "</body></html>";

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
