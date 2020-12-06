module JavaExt {
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.apache.commons.imaging;

    exports ch.azure.aurore.IO.API;
    exports ch.azure.aurore.IO.exceptions;
    exports ch.azure.aurore.IO.jsonFiles;
    exports ch.azure.aurore.Lists;
    exports ch.azure.aurore.Strings;
    exports ch.azure.aurore.images.API;

}