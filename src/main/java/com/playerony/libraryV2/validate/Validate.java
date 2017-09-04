package com.playerony.libraryV2.validate;

import java.util.Scanner;

import com.playerony.libraryV2.exception.InputException;

public class Validate {
	private static final Scanner scanner = new Scanner(System.in);

	public static String getString(String message) {
		System.out.println(message);
		return scanner.next();
	}

	public static int getInt(String message) throws InputException {
		System.out.println(message);

		try {
			int result = scanner.nextInt();
			return result;
		} catch (Exception e) {
			throw new InputException();
		}
	}

	public static boolean checkPesel(String pesel) throws InputException {
		if (pesel.length() != 11)
			throw new InputException("Wrong length of pesel");

		return true;
	}

	public static boolean checkNumbersInString(String phrase) {
		String regex = "^[a-zA-Z]+$";
		if (phrase.matches(regex))
			return true;
		else
			return false;
	}
	
	public boolean checkInt(Integer num) {
	 	String regex = "[0-9]+";
		if (num.toString().matches(regex))
			return true;
		else
			return false;
	}
}
