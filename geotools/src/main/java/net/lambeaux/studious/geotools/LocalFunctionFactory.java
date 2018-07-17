package net.lambeaux.studious.geotools;

import static net.lambeaux.studious.geotools.TestFunction.NAME;

import java.util.Collections;
import java.util.List;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.opengis.feature.type.Name;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class LocalFunctionFactory implements FunctionFactory {
  @Override
  public List<FunctionName> getFunctionNames() {
    return Collections.singletonList(NAME);
  }

  @Override
  public Function function(String s, List<Expression> list, Literal literal) {
    return function(new NameImpl(s), list, literal);
  }

  @Override
  public Function function(Name name, List<Expression> list, Literal literal) {
    if (name.getLocalPart().equals(NAME.getName())) {
      return new TestFunction(list, literal);
    }
    return null;
  }
}
