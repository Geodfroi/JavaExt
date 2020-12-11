module JavaExt {
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.apache.commons.imaging;
    requires javatuples;

    exports ch.azure.aurore.IO.API;
    exports ch.azure.aurore.IO.exceptions;
    exports ch.azure.aurore.IO.jsonFiles;
    exports ch.azure.aurore.collections;
    exports ch.azure.aurore.strings;
    exports ch.azure.aurore.images.API;
}