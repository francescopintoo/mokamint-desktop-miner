module mokamint.desktop.miner {
	exports it.univr.mokamintminer.services;
	exports it.univr.mokamintminer.gui;
	opens it.univr.mokamintminer.gui to javafx.fxml;

	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
}