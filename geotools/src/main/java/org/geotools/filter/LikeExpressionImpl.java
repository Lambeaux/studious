package org.geotools.filter;

import org.opengis.filter.expression.Expression;

/**
 * An implementation of {@link org.opengis.filter.PropertyIsLike} that supports expressions on the
 * right hand side, in accordance to the Filter 2.0 schema. Permits visitors to traverse a function
 * chain if necessary.
 */
public class LikeExpressionImpl extends LikeFilterImpl {
  private final Expression valueExpression;

  public LikeExpressionImpl(Expression valueExpression) {
    this.valueExpression = valueExpression;
  }

  public LikeExpressionImpl(
      Expression expr,
      String pattern,
      String wildcardMulti,
      String wildcardSingle,
      String escape,
      Expression valueExpression) {
    super(expr, pattern, wildcardMulti, wildcardSingle, escape);
    this.valueExpression = valueExpression;
  }

  public LikeExpressionImpl(MatchAction matchAction, Expression valueExpression) {
    super(matchAction);
    this.valueExpression = valueExpression;
  }

  public LikeExpressionImpl(
      Expression expr,
      String pattern,
      String wildcardMulti,
      String wildcardSingle,
      String escape,
      MatchAction matchAction,
      Expression valueExpression) {
    super(expr, pattern, wildcardMulti, wildcardSingle, escape, matchAction);
    this.valueExpression = valueExpression;
  }

  public Expression getPropertyExpression() {
    return super.getExpression();
  }

  public Expression getValueExpression() {
    return valueExpression;
  }
}
