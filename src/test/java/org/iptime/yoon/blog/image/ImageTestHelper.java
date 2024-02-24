package org.iptime.yoon.blog.image;


import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author rival
 * @since 2024-02-20
 */
public class ImageTestHelper {
    public static byte[] createSampleImage(int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();

        // Fill with a sample color or pattern
        graphics2D.setColor(Color.BLUE); // Example: Fill with blue
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.dispose();

        // Convert BufferedImage to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    public static MultipartFile generateTestImageFile(int width, int height) throws IOException {
        byte[] imageBytes = createSampleImage(width, height);
        return  createMultipartFile(imageBytes, "testImage", "testImage.png", "image/png");
    }

    public static MultipartFile createMultipartFile(byte[] imageBytes, String name, String originalFileName, String contentType) {
        return new MockMultipartFile(name, originalFileName, contentType, imageBytes);
    }
}
