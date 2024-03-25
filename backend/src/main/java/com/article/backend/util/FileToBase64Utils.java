package com.article.backend.util;

import com.article.backend.exception.FileStorageException;
import com.article.backend.exception.NotAnImageFileException;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileToBase64Utils {
    private FileToBase64Utils() {}

    private static String getRealMimeType(MultipartFile file) {
        AutoDetectParser parser = new AutoDetectParser();
        Detector detector = parser.getDetector();
        try {
            Metadata metadata = new Metadata();
            TikaInputStream stream = TikaInputStream.get(file.getInputStream());
            return detector.detect(stream, metadata).getSubtype();
        } catch (IOException e) {
            return MimeTypes.OCTET_STREAM;
        }
    }

    public static String getBase64ImageFromFile(MultipartFile file) {
        try (InputStream input = file.getInputStream();
             ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream()) {
            BufferedImage bufferedImage = ImageIO.read(input);
            if (bufferedImage == null) {
                throw new NotAnImageFileException("Not an image.");
            } else {
                String realMimeType = getRealMimeType(file);
                ImageIO.write(bufferedImage, realMimeType, imageOutputStream);
            }
            return Base64.getEncoder().encodeToString(imageOutputStream.toByteArray());
        } catch (IOException e) {
            throw new FileStorageException("Could not store the file. Please try again!");
        }
    }
}
