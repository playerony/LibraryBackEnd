package com.playerony.libraryV2;

import org.junit.Before;
import org.junit.Test;

import com.playerony.libraryV2.controller.AuthorController;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;

public class TestAuthorController {
	private AuthorController authorController;

	@Before
	public void initializer() {
		authorController = new AuthorController();
	}
	
//	@Test
//	public void testSelectAll() {
//		System.out.println("Test testInjection");
//		
//		try {
//			authorController.showAuthors();
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
//	}

//	@Test
//	public void testInjection() {
//		System.out.println("Test testInjection");
//		
//		try {
//			authorController.showInjection();
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
//	}

//	@Test
//	public void testInsert() {
//		System.out.println("Test testInsert");
//		
//		try {
//			authorController.addAuthor(11);
//		} catch (InputException | DatabaseException e) {
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testInsertBatch() {
//		System.out.println("Test testInsertBatch");
//		
//		try {
//			List<Author> authors = new ArrayList<Author>();
//			authors.add(new Author("test1", "test1", "M", 20, "13245678912", 11));
//			authors.add(new Author("test1", "test1", "M", 21, "12346578912", 11));
//			
//			authorController.getAuthorDaoImpl().insertAuthors(authors);
//		} catch (DatabaseException | InputException e) {
//			e.printStackTrace();
//		}
//	}

//	@Test
//	public void testUpdate() {
//		System.out.println("Test testUpdate");
//		
//		try {
//			authorController.updateAuthor();
//		} catch (InputException | DatabaseException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testCreateCopy() {
//		System.out.println("Test testCreateCopy");
//
//		try {
//			authorController.copyTable();
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void testDelete() {
//		System.out.println("Test testDelete");
//
//		try {
//			authorController.deleteAuthor();
//		} catch (InputException | DatabaseException e) {
//			e.printStackTrace();
//		}
//	}
}
