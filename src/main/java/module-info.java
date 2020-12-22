module JavaExt {
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.apache.commons.imaging;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.collections4;

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports ch.azure.aurore.IO.API;
    exports ch.azure.aurore.IO.exceptions;
    exports ch.azure.aurore.json.jsonFiles;
    exports ch.azure.aurore.collections;
    exports ch.azure.aurore.strings;
    exports ch.azure.aurore.images.API;
    exports ch.azure.aurore.fxml;
    exports ch.azure.aurore.sqlite.wrapper;
    exports ch.azure.aurore.sqlite.wrapper.annotations;
    exports ch.azure.aurore.reflection;
    exports ch.azure.aurore.conversions;
    exports ch.azure.aurore.generics;

    exports ch.azure.aurore.sqlite to com.fasterxml.jackson.databind;
}