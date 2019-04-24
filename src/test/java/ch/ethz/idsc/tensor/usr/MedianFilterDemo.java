// code by jph
package ch.ethz.idsc.tensor.usr;

import java.io.File;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.img.MedianFilter;
import ch.ethz.idsc.tensor.io.Export;
import ch.ethz.idsc.tensor.io.HomeDirectory;
import ch.ethz.idsc.tensor.io.Import;

enum MedianFilterDemo {
  ;
  public static void main(String[] args) throws Exception {
    File file = HomeDirectory.file("1bab14b2.jpg");
    Tensor image = Import.of(file);
    for (int index = 0; index < 3; ++index)
      image.set(MedianFilter.of(image.get(Tensor.ALL, Tensor.ALL, index), 2), //
          Tensor.ALL, Tensor.ALL, index);
    Export.of(HomeDirectory.file("filtered.png"), image);
  }
}