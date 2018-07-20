package net.lambeaux.studious.geotools;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.filter.LikeFesFilterImpl;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.filter.v2_0.FESConfiguration;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.xml.Parser;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Intersects;
import org.xml.sax.SAXException;

public class Filters {
  private static final Parser PARSER_V1 = new Parser(new OGCConfiguration());

  private static final Parser PARSER_V2 = new Parser(new FESConfiguration());

  public static void main(String[] args) throws Exception {
    //    Filter filterV1e1 = loadFilter("xml/filter-document.xml", PARSER_V1);
    Filter likeWorking = loadFilter("xml/filter2-like-working.xml", PARSER_V2);
    //    Filter likeBroken = loadFilter("xml/filter2-like-broken.xml", PARSER_V2);

    PrintingVisitor visitor = new PrintingVisitor();
    //    filterV1e1.accept(visitor, null);
    likeWorking.accept(visitor, null);
    //    likeBroken.accept(visitor, null);
  }

  private static Filter loadFilter(String resource, Parser parser)
      throws IOException, SAXException, ParserConfigurationException {
    try (InputStream data = Filters.class.getClassLoader().getResourceAsStream(resource)) {
      return (Filter) parser.parse(data);
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
    public Object visit(Intersects filter, Object data) {
      print(
          "INTERSECTS("
              + filter.getExpression1().toString()
              + ", "
              + filter.getExpression2().toString()
              + ")");
      return super.visit(filter, data);
    }

    @Override
    public Object visit(DWithin filter, Object data) {
      print(
          "DWITHIN("
              + filter.getExpression1().toString()
              + ", "
              + filter.getExpression2().toString()
              + ", "
              + filter.getDistance()
              + " "
              + filter.getDistanceUnits()
              + ")");
      return super.visit(filter, data);
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
      if (filter instanceof LikeFesFilterImpl) {
        LikeFesFilterImpl fesLike = (LikeFesFilterImpl) filter;
        Expression property = fesLike.getPropertyExpression();
        Expression value = fesLike.getValueExpression();
        print("fes:LIKE(" + property.toString() + ", " + value.evaluate(value) + ")");
        Object result = fesLike.getPropertyExpression().accept(this, data);
        return fesLike.getValueExpression().accept(this, result);
      }
      print("LIKE(" + filter.getExpression().toString() + ", " + filter.getLiteral() + ")");
      return super.visit(filter, data);
    }

    private static void print(String msg) {
      System.out.println(msg);
    }
  }
}
