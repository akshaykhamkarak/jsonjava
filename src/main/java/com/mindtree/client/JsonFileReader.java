package com.mindtree.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import com.google.gson.Gson;
import com.mindtree.model.Author;
import com.mindtree.model.Book;
import com.mindtree.service.InsertIntoDb;
import com.mindtree.service.XMLServicesImpl;


public class JsonFileReader {
	public static void main(String args[]) throws SQLException {
		JSONParser parser = new JSONParser();
		InsertIntoDb service = new InsertIntoDb();
		Object obj;
		try {
			String url="https://mocki.io/v1/b06f4eb6-e790-4d66-aed1-b5b4c25a930a";
			URL obj1 = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj1.openConnection();
			int responseCode = con.getResponseCode();
			System.out.println("\n Sending 'GET' request to URL : "+url);
			System.out.println("Response Code : " + responseCode);
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String inputLine;
		
			
			StringBuffer res = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				res.append(inputLine);
			}

			
			String p = res.toString();
//		
			
			
			obj = parser.parse(p);
			JSONArray jsonArray = (JSONArray) obj;
			Iterator<JSONObject> bookObjects = jsonArray.iterator();
			JSONObject jsonObject1;
			Set<Book> books = new LinkedHashSet<Book>();
			while (bookObjects.hasNext()) {
				jsonObject1 = (JSONObject) bookObjects.next();
				int bookId = Integer.parseInt(jsonObject1.get("id").toString());
				String bookName = (String) jsonObject1.get("title");
				int bookPrice = Integer.parseInt(jsonObject1.get("price").toString());
				JSONArray authors = (JSONArray) jsonObject1.get("authors");
				Iterator<JSONObject> iterator = authors.iterator();
				JSONObject jsonObject2;
				Set<Author> authorsList = new LinkedHashSet<Author>();
				while (iterator.hasNext()) {
					jsonObject2 = (JSONObject) iterator.next();
					int authorId = Integer.parseInt(jsonObject2.get("id").toString());
					String authorName = (String) jsonObject2.get("name");
					authorsList.add(new Author(authorId, authorName));
				}
				books.add(new Book(bookId, bookName, bookPrice, authorsList));

			}
			service.insertBook(books);
			Set<Book> readBooks = service.getBooks();
			new XMLServicesImpl().readToXml(readBooks);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
		
			e.printStackTrace();
		}

	}
}