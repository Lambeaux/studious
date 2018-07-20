package net.lambeaux.studious.geotools;

import java.util.List;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

public class TestFunction implements Function {
  public static final FunctionName NAME =
      new FunctionNameImpl(
          "net.lambeaux.test", String.class, FunctionNameImpl.parameter("value", String.class));

  private final List<Expression> parameters;

  private final Literal fallback;

  public TestFunction(List<Expression> parameters, Literal fallback) {
    this.parameters = parameters;
    this.fallback = fallback;
  }

  @Override
  public String getName() {
    return NAME.getName();
  }

  @Override
  public FunctionName getFunctionName() {
    return NAME;
  }

  @Override
  public List<Expression> getParameters() {
    return parameters;
  }

  @Override
  public Literal getFallbackValue() {
    return fallback;
  }

  @Override
  public Object evaluate(Object object) {
    return evaluate(object, String.class);
  }

  @Override
  public <T> T evaluate(Object object, Class<T> context) {
    Object wrappedValue;
    Expression wrappedExpression = parameters.get(0);
    if (wrappedExpression instanceof PropertyName) {
      wrappedValue = wrappedExpression.toString();
    } else {
      wrappedValue = wrappedExpression.evaluate(object, context);
    }
    if (context.isInstance(wrappedValue)) {
      return context.cast(wrappedValue);
    }
    return null;
  }

  @Override
  public Object accept(ExpressionVisitor visitor, Object extraData) {
    return visitor.visit(this, extraData);
  }

  @Override
  public String toString() {
    // Returning literal value of first argument (the "wrapped" value; additional args are metadata)
    return parameters.get(0).toString();
  }
}
