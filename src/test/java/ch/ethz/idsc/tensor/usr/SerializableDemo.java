// code by jph
package ch.ethz.idsc.tensor.usr;

import java.io.IOException;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.io.Export;
import ch.ethz.idsc.tensor.io.ImportTest;

/** export to binary files in test resources */
/* package */ enum SerializableDemo {
  ;
  public static void main(String[] args) throws IOException {
    Tensor tensor = Tensors.of(RealScalar.ONE, ComplexScalar.of(2, 3), RealScalar.of(3.15));
    Export.object(ImportTest.IO_OBJECT_TENSOR, tensor);
    Export.object(ImportTest.IO_OBJECT_UNMODIFIABLE, tensor.unmodifiable());
    Tensor viewtensor = Unprotect.references(tensor);
    Export.object(ImportTest.IO_OBJECT_VIEWTENSOR, viewtensor);
  }
}
