package com.sun.imageio.plugins.png;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.junit.Test;

public class PNGImageWriterBackportTest {

    @Test
    public void testPrecedence() {
        Iterator< ImageWriter > writer = ImageIO.getImageWritersByFormatName("png");
        assertTrue(writer.next() instanceof PNGImageWriterBackport);
        assertTrue(writer.next() instanceof PNGImageWriter);
    }

    @Test
    public void testCompressionLevels() throws IOException {

        BufferedImage img = ImageIO.read(getClass().getResourceAsStream("placeholder-text.gif"));

        byte[] bd  = toPng(img, null); // default
        byte[] b00 = toPng(img, 0.0f); // highest compression, slowest
        byte[] b01 = toPng(img, 0.1f);
        byte[] b02 = toPng(img, 0.2f);
        byte[] b03 = toPng(img, 0.3f);
        byte[] b04 = toPng(img, 0.4f);
        byte[] b05 = toPng(img, 0.5f);
        byte[] b06 = toPng(img, 0.6f);
        byte[] b07 = toPng(img, 0.7f);
        byte[] b08 = toPng(img, 0.8f);
        byte[] b09 = toPng(img, 0.9f);
        byte[] b10 = toPng(img, 1.0f); // lowest compression, fastest

        assertArrayEquals(bd, b05);
        assertArrayEquals(bd, b06);

        assertTrue(b00.length < b01.length);
        assertTrue(b01.length < b02.length);
        assertTrue(b02.length < b03.length);
        assertTrue(b03.length < b04.length);
        assertTrue(b04.length < b05.length);
        assertTrue(b05.length == b06.length);
        assertTrue(b06.length < b07.length);
        assertTrue(b07.length < b08.length);
        assertTrue(b08.length < b09.length);
        assertTrue(b09.length < b10.length);
    }

    private byte[] toPng(BufferedImage img, Float compressionQuality) throws IOException {

        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        if (compressionQuality != null) {
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(compressionQuality);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream stream = new MemoryCacheImageOutputStream(baos);
        try {
            writer.setOutput(stream);
            writer.write(null, new IIOImage(img, null, null), writeParam);
        } finally {
            stream.close();
            writer.dispose();
        }

        return baos.toByteArray();
    }
}
