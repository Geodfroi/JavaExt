module javaxt {
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires java.desktop;
    requires org.apache.commons.imaging;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.collections4;

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports ch.azure.aurore.javaxt.IO.API;
    exports ch.azure.aurore.javaxt.IO.exceptions;
    exports ch.azure.aurore.javaxt.json.jsonFiles;
    exports ch.azure.aurore.javaxt.json.API;
    exports ch.azure.aurore.javaxt.collections;
    exports ch.azure.aurore.javaxt.strings;
    exports ch.azure.aurore.javaxt.images.API;
    exports ch.azure.aurore.javaxt.fxml;
    exports ch.azure.aurore.javaxt.sqlite.wrapper;
    exports ch.azure.aurore.javaxt.sqlite.wrapper.annotations;
    exports ch.azure.aurore.javaxt.reflection;
    exports ch.azure.aurore.javaxt.conversions;
    exports ch.azure.aurore.javaxt.generics;

    exports ch.azure.aurore.javaxt.sqlite to com.fasterxml.jackson.databind;
    exports ch.azure.aurore.javaxt.json to com.fasterxml.jackson.databind;
}