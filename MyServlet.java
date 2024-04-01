package MyWeatherApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/MyServlet")

public class MyServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String city = request.getParameter("city");
		//System.out.println(city);
		
		String apiKey = "8d8f13ae699a8b8a1c7ff780e2801aeb";
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" +city+ "&appid=" +apiKey;
		
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		try (Scanner sc = new Scanner(reader)) {
			StringBuilder responseContent = new StringBuilder();
			
			while(sc.hasNext()) {
				responseContent.append(sc.nextLine());
			}
			
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
			System.out.println(jsonObject);
			
			long dateTimeStamp = jsonObject.get("dt").getAsLong()* 1000;
			String date = new Date(dateTimeStamp).toString();
			
			double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
			int temperatureCelcius = (int)(temperatureKelvin-273.15);
			
			int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
			
			double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
			
			String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
			
			request.setAttribute("date", date);
			request.setAttribute("temp", temperatureCelcius);
			request.setAttribute("humidity", humidity);
			request.setAttribute("windSpeed", windSpeed);
			request.setAttribute("weatherCondition", weatherCondition);
			request.setAttribute("city", city);
			
		}
			request.getRequestDispatcher("index.jsp").forward(request, response);
			
		
		
	}

}
