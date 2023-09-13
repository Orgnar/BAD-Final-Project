module projectLabBAD1 {
	requires javafx.graphics;
	requires javafx.controls;
	requires jfxtras.labs;
	requires java.sql;
	requires javafx.base;
	requires mysql.connector.java;
	opens master;
	opens main;
	opens login;
	opens database;
	opens management;
	opens user;
}