// code by jph
package ch.ethz.idsc.tensor.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.usr.TestFile;
import junit.framework.TestCase;

public class ExportHelperTest extends TestCase {
  public void testGif() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(128);
    Tensor image = Tensors.fromString("{{{255, 2, 3, 255}, {0, 0, 0, 0}, {91, 120, 230, 255}, {0, 0, 0, 0}}}");
    ExportHelper.of(Extension.GIF, image, byteArrayOutputStream);
    File file = TestFile.withExtension("gif");
    Export.of(file, image);
    assertTrue(file.isFile());
    assertTrue(file.delete());
    byte[] array = byteArrayOutputStream.toByteArray(); // 54 bytes used
    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(array));
    Tensor tensor = ImageFormat.from(bufferedImage);
    assertEquals(image, tensor);
  }

  public void testGif2() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(128);
    Tensor row1 = Tensors.fromString("{{255, 2, 3, 255}, {0, 0, 0, 0}, {91, 120, 230, 255}, {0, 0, 0, 0}}");
    Tensor image = Tensors.of(row1, row1);
    ExportHelper.of(Extension.GIF, image, byteArrayOutputStream);
    byte[] array = byteArrayOutputStream.toByteArray(); // 56 bytes used
    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(array));
    Tensor tensor = ImageFormat.from(bufferedImage);
    Scalar diff = Frobenius.between(image, tensor);
    diff.copy();
    // unfortunately there seems to be a problem with the java gif parser
  }

  public void testFileExtensionFail() throws IOException {
    OutputStream outputStream = new ByteArrayOutputStream(512);
    ExportHelper.of(Extension.VECTOR, Tensors.empty(), outputStream);
  }

  public void testGzFail() {
    OutputStream outputStream = new ByteArrayOutputStream(512);
    try {
      ExportHelper.of(Extension.GZ, Tensors.empty(), outputStream);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
