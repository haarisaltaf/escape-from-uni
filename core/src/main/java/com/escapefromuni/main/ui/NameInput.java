package com.escapefromuni.main.ui;

import com.badlogic.gdx.Input.TextInputListener;

// Decided to redo this differently
public class NameInput implements TextInputListener {

	String currInput;

	@Override
	public void input (String text) {
		currInput = text;
	}

	public String getInput () {
		return currInput;
	}

	@Override
	public void canceled () {
		currInput = "NONE";
	}
}
