package com.google.MavenProject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class CurrencyConverter {
	 public static void convert(String from, String to, BigDecimal amount) {
	        String url = "https://api.frankfurter.dev/v1/latest?base=" + from.toUpperCase() + "&symbols=" + to.toUpperCase();
	        OkHttpClient client = new OkHttpClient();

	        Request request = new Request.Builder().url(url).get().build();

	        try {
	            Response response = client.newCall(request).execute();
	            if (!response.isSuccessful()) {
	                throw new IOException("Unexpected response code: " + response.code());
	            }

	            String responseBody = response.body().string();
	            JSONObject jsonObject = new JSONObject(responseBody);
	            JSONObject rates = jsonObject.getJSONObject("rates");

	            if (!rates.has(to.toUpperCase())) {
	                System.out.println("Invalid currency code: " + to);
	                return;
	            }

	            BigDecimal rate = rates.getBigDecimal(to.toUpperCase());
	            BigDecimal convertedAmount = amount.multiply(rate);
	            System.out.println(amount + " " + from.toUpperCase() + " = " + convertedAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + " " + to.toUpperCase());

	        } catch (Exception e) {
	            System.err.println("Error: " + e.getMessage());
	        }
    }

    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);

        System.out.print("Enter currency to convert from (e.g., USD, EUR): ");
        String from = scanner.nextLine().toUpperCase();

        System.out.print("Enter currency to convert to (e.g., INR, GBP): ");
        String to = scanner.nextLine().toUpperCase();

        System.out.print("Enter amount to convert: ");
        BigDecimal amount = scanner.nextBigDecimal();

        convert(from, to, amount);
    }
}
