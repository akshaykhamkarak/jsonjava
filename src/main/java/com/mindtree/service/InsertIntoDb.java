package com.mindtree.service;

import java.sql.SQLException;
import java.util.Set;

import com.mindtree.dao.InsertBooksDaoImpl;
import com.mindtree.model.Book;



public class InsertIntoDb {
	InsertBooksDaoImpl impl = new InsertBooksDaoImpl();
	public void insertBook(Set<Book> books) throws SQLException {
		impl.insertBooks(books);
	}

	public Set<Book> getBooks() throws SQLException {
		return impl.getBooks();
		
	}
}