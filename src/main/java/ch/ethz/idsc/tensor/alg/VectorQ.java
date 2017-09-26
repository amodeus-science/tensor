// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.ScalarQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Unprotect;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/VectorQ.html">VectorQ</a> */
public enum VectorQ {
  ;
  /** @param tensor
   * @return true if tensor is a vector */
  public static boolean of(Tensor tensor) {
    return !ScalarQ.of(tensor) && tensor.stream().allMatch(ScalarQ::of);
  }

  /** @param tensor
   * @param length non-negative
   * @return true if tensor is a vector with given length */
  public static boolean ofLength(Tensor tensor, int length) {
    if (length < 0)
      throw new RuntimeException("length " + length);
    return tensor.length() == length && of(tensor);
  }

  /** @param tensor
   * @throws Exception if given tensor is not a vector */
  public static void orThrow(Tensor tensor) {
    if (tensor.length() != 0 && Unprotect.dimension1(tensor) != Scalar.LENGTH)
      throw TensorRuntimeException.of(tensor);
  }
}