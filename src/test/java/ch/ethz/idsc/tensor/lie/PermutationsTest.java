// code by jph
package ch.ethz.idsc.tensor.lie;

import java.util.Arrays;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.io.StringTensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class PermutationsTest extends TestCase {
  public void testEmpty() {
    assertEquals(Permutations.of(Tensors.vector()), Tensors.fromString("{{}}")); // 0! == 1
  }

  public void testThree() {
    Tensor res = Permutations.of(Tensors.vector(1, 2, 3));
    assertEquals(res.get(0), Tensors.vector(1, 2, 3));
    assertEquals(res.get(1), Tensors.vector(1, 3, 2));
    assertEquals(res.length(), 6);
  }

  public void testThree2() {
    Tensor res = Permutations.of(Tensors.vector(1, 2, 1));
    assertEquals(res.get(0), Tensors.vector(1, 2, 1));
    assertEquals(res.get(1), Tensors.vector(1, 1, 2));
    assertEquals(res.get(2), Tensors.vector(2, 1, 1));
    assertEquals(res.length(), 3);
  }

  public void testThree1() {
    Tensor res = Permutations.of(Tensors.vector(1, 1, 1));
    assertEquals(res.length(), 1);
  }

  public void testFour2() {
    Tensor res = Permutations.of(Tensors.vector(2, -1, 2, -1));
    assertEquals(res.get(0), Tensors.vector(2, -1, 2, -1));
    assertEquals(res.get(1), Tensors.vector(2, -1, -1, 2));
    assertEquals(res.get(2), Tensors.vector(2, 2, -1, -1));
    assertEquals(res.get(3), Tensors.vector(-1, 2, 2, -1));
    assertEquals(res.length(), 6);
  }

  public void testStrings() {
    Tensor vector = StringTensor.vector("a", "b", "a");
    Tensor tensor = Permutations.of(vector);
    assertEquals(tensor, Tensors.fromString("{{a, b, a}, {a, a, b}, {b, a, a}}"));
  }

  public void testMatrix() {
    assertEquals(Dimensions.of(Permutations.of(IdentityMatrix.of(3))), Arrays.asList(6, 3, 3));
    assertEquals(Dimensions.of(Permutations.of(IdentityMatrix.of(3).extract(0, 2))), Arrays.asList(2, 2, 3));
  }

  public void testFail() {
    try {
      Permutations.of(RealScalar.ONE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
