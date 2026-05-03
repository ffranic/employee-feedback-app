module hr.javafx.tvz.projektni_zadatak {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    requires okhttp3;
    requires org.json;
    requires org.slf4j;
    requires static lombok;
    requires annotations;


    opens hr.javafx.tvz.projektni_zadatak to javafx.fxml;
    exports hr.javafx.tvz.projektni_zadatak.application;
    opens hr.javafx.tvz.projektni_zadatak.application to javafx.fxml;
    exports hr.javafx.tvz.projektni_zadatak.controller;
    opens hr.javafx.tvz.projektni_zadatak.controller to javafx.fxml;
}