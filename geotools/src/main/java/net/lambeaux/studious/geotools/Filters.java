package net.lambeaux.studious.geotools;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.filter.DefaultExpression;
import org.geotools.filter.LikeExpressionImpl;
import org.geotools.filter.v2_0.FESConfiguration;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.xml.Parser;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.xml.sax.SAXException;

public class Filters {
  private static final Parser PARSER = new Parser(new FESConfiguration());

  public static void main(String[] args) throws Exception {
    Filter filter = loadFilter("xml/filter2-document.xml");
    filter.accept(new PrintingVisitor(), null);
  }

  private static Filter loadFilter(String resource)
      throws IOException, SAXException, ParserConfigurationException {
    try (InputStream data = Filters.class.getClassLoader().getResourceAsStream(resource)) {
      return (Filter) PARSER.parse(data);
    }
  }

  private static class PrintingVisitor extends DefaultFilterVisitor {
    @Override
    public Object visit(And filter, Object data) {
      print("AND(...)");
      return super.visit(filter, data);
    }

    @Override
    public Object visit(Function expression, Object data) {
      String args =
          expression
              .getParameters()
              .stream()
              .map(Expression::toString)
              .reduce((l, r) -> l.concat(", " + r))
              .orElse("");
      print("FUNC:" + expression.getFunctionName() + "(" + args + ")");
      return super.visit(expression, data);
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object data) {
      print(
          "EQUAL("
              + filter.getExpression1().toString()
              + ", "
              + filter.getExpression2().toString()
              + ")");
      return super.visit(filter, data);
    }

    @Override
    public Object visit(PropertyIsLike filter, Object data) {
      if (filter instanceof LikeExpressionImpl) {
        LikeExpressionImpl likeExpression = (LikeExpressionImpl) filter;
        Object result = likeExpression.getPropertyExpression().accept(this, data);
        return likeExpression.getValueExpression().accept(this, result);
      }
      Expression expression = filter.getExpression();
      expression.evaluate(expression);
      print("LIKE(" + filter.getExpression().toString() + ", " + filter.getLiteral() + ")");
      return super.visit(filter, data);
    }

    private static void print(String msg) {
      System.out.println(msg);
    }
  }
}
