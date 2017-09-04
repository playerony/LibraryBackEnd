package com.playerony.libraryV2;

import org.junit.Before;
import org.junit.Test;

import com.playerony.libraryV2.controller.MainController;
import com.playerony.libraryV2.exception.DatabaseException;

public class TestMainController {
	private MainController mainController;

	@Before
	public void initializer() {
		mainController = new MainController();
	}

	@Test
	public void testInsert() {
		System.out.println("Test insert");

		try {
			mainController.selectAuthorsAndBooks();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
}
