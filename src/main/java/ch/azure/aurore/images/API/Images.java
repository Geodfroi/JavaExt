package ch.azure.aurore.images.API;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Images {

    public static Optional<BufferedImage> toImage(byte[] array){
        if (array != null && array.length > 0){
            try {
                return Optional.of(Imaging.getBufferedImage(array));
            } catch (ImageReadException | IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public static File toFile(byte[] array, String filePath){

        try {
            Optional<BufferedImage> img = toImage(array);
            if (img.isPresent()){

                File file = new File(filePath);
                File parent = file.getParentFile();
                if (!parent.exists() && !parent.mkdirs()) {
                    throw new IllegalStateException("couldn't create parent directories: " + parent);
                }
                Imaging.writeImage(img.get(),file, ImageFormats.PNG, null);
                return file;
            }
        } catch (ImageWriteException | IOException | IllegalStateException e) {
            e.printStackTrace();
        }
        System.out.println("failed to export file");
        return null;
    }
}

//    public static Optional<byte[]> toByteArray(File file){
//        try {
//            BufferedImage image = Imaging.getBufferedImage(file);
//            try {
//                final Map<String, Object> params = new HashMap<>();
//                params.put(ImagingConstants.PARAM_KEY_COMPRESSION,
//                        PngConstants.PARAM_KEY_PNG_FORCE_TRUE_COLOR);
//                byte[] array = Imaging.writeImageToBytes(image, ImageFormats.PNG, null);
//                return Optional.of(array);
//            } catch (ImageWriteException e) {
//                e.printStackTrace();
//            }
//        } catch (ImageReadException | IOException e) {
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }
//
//    public static Optional<WritableImage> toFXImage(byte[] array){
//        Optional<BufferedImage> result = toImage(array);
//        if (result.isPresent()){
//            WritableImage wi = SwingFXUtils.toFXImage(result.get(), null);
//            return Optional.of(wi);
//        }
//        return Optional.empty();
//    }
