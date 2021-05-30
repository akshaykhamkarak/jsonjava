package com.mindtree.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mindtree.model.Author;
import com.mindtree.model.Book;
import com.mindtree.utility.DBConnection;



public class InsertBooksDaoImpl {
	
	public void insertBooks(Set<Book> books) throws SQLException {
		Connection con = new DBConnection().getConnection();
		Iterator<Book> booksList = books.iterator();
		Set<Author> authors = new LinkedHashSet<Author>();
		PreparedStatement pstmt = null;

		try {
			while (booksList.hasNext()) {
				Book book = booksList.next();
				String query = "insert into books values(?,?,?)";

				Set<Author> bookAuthors = book.getAuthors();
				int bookId = book.getBookId();
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, bookId);
				pstmt.setString(2, book.getBookName());
				pstmt.setInt(3, book.getBookPrice());
			
				int res=pstmt.executeUpdate();
				authors.addAll(bookAuthors);
				if(res==1) {
					insertAuthors(authors);
				}

				insertRecords(bookAuthors, bookId);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Author author : authors) {
			System.out.println(author);
		}

	

	}

	private void insertRecords(Set<Author> authors, int bookId) throws SQLException {
		System.out.println("enter the method");
		Connection con = new DBConnection().getConnection();
		PreparedStatement pstmt = null;
		String query = "insert into records(book_id,author_id) values(?,?)";

		try {
			//System.out.println("Records para"+bookId+"Author id:"+authors);
			for (Author author : authors) {
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, bookId);
				pstmt.setInt(2, author.getAuthorId());
				
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
		}

	}

	private void insertAuthors(Set<Author> authors) throws SQLException {
		Connection con = new DBConnection().getConnection();
		PreparedStatement pstmt = null;
		Iterator<Author> authorList = authors.iterator();
		int count=0;
		try {
			while (authorList.hasNext()) {
				Author author = authorList.next();
				System.out.println(author);
				String query = "insert into authors values(?,?) ";

				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, author.getAuthorId());
				pstmt.setString(2, author.getAuthorName());
				System.out.println(author.getAuthorId());
			pstmt.executeUpdate();
			count++;
				
		}
			System.out.println(count);
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}
	public Set<Book> getBooks() throws SQLException{
		Connection con = new DBConnection().getConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		Set<Book> books = new LinkedHashSet<>();
		try {
				String query = "select * from books";
				pstmt = con.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					int bookId = rs.getInt("book_id");
					String bookName = rs.getString("book_name");
					int bookPrice = rs.getInt("book_price");
					Set<Author> bookAuthors = new LinkedHashSet<>();
					String query2 = "SELECT a.author_id, a.author_name from authors a join records r where r.book_id="+bookId+" and a.author_id=r.author_id";
					pstmt2 = con.prepareStatement(query2);
					ResultSet rs2 = pstmt2.executeQuery();
					while(rs2.next()) {
						int authorId = rs2.getInt("author_id");
						String authorName = rs2.getString("author_name");
						bookAuthors.add(new Author(authorId, authorName));
					}
					books.add(new Book(bookId, bookName, bookPrice, bookAuthors));
					
				}

			return books;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
		}
		return null;
	}
	
}