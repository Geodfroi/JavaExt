package ch.azure.aurore.javaxt.IO.API;

import java.io.File;
import java.nio.file.Path;

public enum FileExtension {
    NONE(""),
    AVI(".avi"),
    EPUB(".epub"),
    MOBI(".mobi"),
    PDF(".pdf"),
    SQLITE(".sqlite"),
    UNDEFINED(""),

    EBOOK(EPUB, MOBI, PDF);

    private final String str;
    private final FileExtension[] children;

    FileExtension(String str) {
        this.str = str;
        children = new FileExtension[0];
    }

    FileExtension(FileExtension... array) {
        str = "";
        children = array;
    }

    public static FileExtension parseString(String str) {
        for (FileExtension f : values()) {
            if (f.str.equalsIgnoreCase(str))
                return f;
        }
        return FileExtension.UNDEFINED;
    }

    public static FileExtension getExtension(String filename) {
        if (!filename.contains("."))
            return FileExtension.NONE;

        String str = filename.substring(filename.lastIndexOf("."));
        return parseString(str);
    }

    public static FileExtension getExtension(Path path) {
        return FileExtension.getExtension(path.toAbsolutePath().toString());
    }

    public static FileExtension getExtension(File file) {
        return FileExtension.getExtension(file.getAbsolutePath());
    }

    public static boolean isExtension(File file, FileExtension ext) {
        FileExtension fileExt = FileExtension.getExtension(file.getAbsolutePath());
        return fileExt.equals(ext) || ext.hasChild(fileExt);
    }

    public static boolean isExtension(Path path, FileExtension ext) {
        FileExtension fileExt = FileExtension.getExtension(path.toAbsolutePath().toString());
        return fileExt.equals(ext) || ext.hasChild(fileExt);
    }

    private boolean hasChild(FileExtension fileExt) {
        for (FileExtension c : children) {
            if (c.equals(fileExt))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return str;
    }
}
