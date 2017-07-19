// code by jph
package ch.ethz.idsc.tensor.alg;

import java.util.Map;
import java.util.function.Function;

import ch.ethz.idsc.tensor.Tensor;

/** {@link TensorMap} is equivalent to Mathematica::Map
 * but was given a different name in order not to conflict with {@link Map} */
public enum TensorMap {
  ;
  /** applies function to all entries of tensor at given level
   * 
   * @param function
   * @param tensor
   * @param level
   * @return */
  public static Tensor of(Function<Tensor, ? extends Tensor> function, Tensor tensor, int level) {
    if (0 < level)
      return Tensor.of(tensor.flatten(0).map(entry -> of(function, entry, level - 1)));
    return function.apply(tensor);
  }
}