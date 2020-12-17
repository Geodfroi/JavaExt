module JavaExt {
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.apache.commons.imaging;
    requires org.apache.commons.io;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports ch.azure.aurore.IO.API;
    exports ch.azure.aurore.IO.exceptions;
    exports ch.azure.aurore.IO.jsonFiles;
    exports ch.azure.aurore.collections;
    exports ch.azure.aurore.strings;
    exports ch.azure.aurore.images.API;
    exports ch.azure.aurore.tuples;
    exports ch.azure.aurore.fxml;
    exports ch.azure.aurore.sqlite;
    exports ch.azure.aurore.reflection;
}