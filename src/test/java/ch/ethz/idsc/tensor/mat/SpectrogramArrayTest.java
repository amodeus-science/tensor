// code by jph
package ch.ethz.idsc.tensor.mat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import junit.framework.TestCase;

public class SpectrogramArrayTest extends TestCase {
  public void testDimension() throws ClassNotFoundException, IOException {
    TensorUnaryOperator tensorUnaryOperator = Serialization.copy(SpectrogramArray.of(8, 8));
    Tensor tensor = tensorUnaryOperator.apply(Range.of(0, 128));
    assertEquals(Dimensions.of(tensor), Arrays.asList(16, 8));
  }

  public void testMathematicaDefault() {
    Tensor tensor = Tensor.of(IntStream.range(0, 2000) //
        .mapToDouble(i -> Math.cos(i * 0.25 + (i / 20.0) * (i / 20.0))) //
        .mapToObj(RealScalar::of));
    int windowLength = Unprotect.dimension1(SpectrogramArray.of(tensor));
    assertEquals(windowLength, 64);
  }

  public void testQuantity() {
    Tensor tensor = Tensor.of(IntStream.range(0, 2000) //
        .mapToDouble(i -> Math.cos(i * 0.25 + (i / 20.0) * (i / 20.0))) //
        .mapToObj(d -> Quantity.of(d, "m")));
    Tensor array = SpectrogramArray.of(tensor);
    Tensor array2 = array.map(QuantityMagnitude.SI().in("km"));
    int windowLength = Unprotect.dimension1(array2);
    assertEquals(windowLength, 64);
  }

  public void testStaticOps() {
    SpectrogramArray.of(Quantity.of(1, "s"), Quantity.of(100, "s^-1"));
    SpectrogramArray.of(Quantity.of(1, "s"), Quantity.of(100, "s^-1"), 10);
  }

  public void testStaticOpsFail() {
    try {
      SpectrogramArray.of(Quantity.of(0, "s"), Quantity.of(100, "s^-1"));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      SpectrogramArray.of(Quantity.of(1, "s"), Quantity.of(0.100, "s^-1"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testPreallocate() {
    for (int windowLength = 1; windowLength < 8; ++windowLength)
      for (int offset = 1; offset <= windowLength; ++offset) {
        TensorUnaryOperator tensorUnaryOperator = SpectrogramArray.of(windowLength, offset);
        for (int length = 10; length < 20; ++length) {
          Tensor signal = Range.of(0, length);
          tensorUnaryOperator.apply(signal);
        }
      }
  }

  public void testHighestOneBit() {
    int highestOneBit = Integer.highestOneBit(64 + 3);
    assertEquals(highestOneBit, 64);
  }

  public void testIterate() {
    List<Integer> list = IntStream.iterate(0, i -> i + 10).limit(10).boxed().collect(Collectors.toList());
    assertEquals(list, Arrays.asList(0, 10, 20, 30, 40, 50, 60, 70, 80, 90));
  }

  public void testFailWindowLength() {
    try {
      SpectrogramArray.of(0, 8);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailWindowLengthOffset() {
    try {
      SpectrogramArray.of(4, 8);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailOffset() {
    try {
      SpectrogramArray.of(4, 0);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testDimensionsFail() {
    TensorUnaryOperator tensorUnaryOperator = SpectrogramArray.of(32, 8);
    try {
      tensorUnaryOperator.apply(RealScalar.ONE);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      tensorUnaryOperator.apply(HilbertMatrix.of(32));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testScalarFail() {
    try {
      SpectrogramArray.of(RealScalar.ONE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testMatrixFail() {
    try {
      SpectrogramArray.of(HilbertMatrix.of(32));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
