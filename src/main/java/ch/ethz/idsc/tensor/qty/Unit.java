// code by jph
package ch.ethz.idsc.tensor.qty;

import java.util.Map;

import ch.ethz.idsc.tensor.Scalar;

/** implementations are immutable */
public interface Unit {
  /** Example: cd*m*s */
  static final String JOIN_DELIMITER = "*";
  /** Example: A*kg^-2 */
  static final String POWER_DELIMITER = "^";
  /** holds the dimension-less unit ONE */
  static final Unit ONE = of("");

  /** @param string, for instance "m*s^-2"
   * @return */
  static Unit of(String string) {
    return UnitHelper.MEMO.lookup(string);
  }

  /***************************************************/
  /** function negate is equivalent to {@link #multiply(Scalar)} with factor -1
   * 
   * Example: in order to compute the reciprocal of a quantity, the exponents
   * of the elemental units are negated. 1 / (X[kg*m^2]) is accompanied by the
   * calculation [kg*m^2] -> [kg^-1*m^-2]
   * 
   * @return */
  Unit negate();

  /** "addition" of units is performed in order to compute a product of quantities.
   * For example, X[m*s] * Y[s^2] requires to collect all elemental units and add
   * their exponents: [m*s] + [s^2] -> [m*s^3]
   * 
   * If the resulting exponent equals 0, the elemental unit is removed altogether.
   * 
   * @param unit
   * @return */
  Unit add(Unit unit);

  /** [kg*m^2] * 3 -> [kg^3*m^6]
   * 
   * @param factor
   * @return */
  Unit multiply(Scalar factor);

  /** Example: Unit.of("kg^2*m^-1*s") returns an unmodifiable map with the entry set
   * {"kg" -> 2, "m" -> -1, "s" -> 1}
   * 
   * @return unmodifiable map with elemental units as keys and their exponents as values */
  Map<String, Scalar> map();
}
