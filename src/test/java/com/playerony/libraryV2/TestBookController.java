package com.playerony.libraryV2;

import org.junit.Before;
import org.junit.Test;

import com.playerony.libraryV2.controller.BookController;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;

public class TestBookController {
	private BookController bookController;
	
	@Before
	public void initializer() {
		bookController = new BookController();
	}
	
	@Test
	public void testInsert() {
		System.out.println("Test testInsert");
		
		try {
			bookController.addBook();
		} catch (InputException | DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerator() {
		
	}

//	@Test
//	public void testUpdate() {
//		System.out.println("Test testUpdate");
//		
//		try {
//			bookController.updateBook();
//		} catch (InputException | DatabaseException e) {
//			e.printStackTrace();
//		}
//	}

//	@Test
//	public void testDelete() {
//		System.out.println("Test testDelete");
//
//		try {
//			bookController.deleteBook();
//		} catch (InputException | DatabaseException e) {
//			e.printStackTrace();
//		}
//	}
}
