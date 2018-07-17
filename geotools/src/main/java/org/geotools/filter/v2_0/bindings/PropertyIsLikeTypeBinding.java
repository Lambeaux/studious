package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.filter.LikeExpressionImpl;
import org.geotools.filter.v1_0.OGCPropertyIsLikeTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

public class PropertyIsLikeTypeBinding extends OGCPropertyIsLikeTypeBinding {
  FilterFactory factory;

  public PropertyIsLikeTypeBinding(FilterFactory factory) {
    super(factory);
    this.factory = factory;
  }

  @Override
  public QName getTarget() {
    return FES.PropertyIsLikeType;
  }

  @Override
  public Object getProperty(Object object, QName name) throws Exception {
    PropertyIsLike isLike = (PropertyIsLike) object;

    if (FES.expression.equals(name)) {
      return new Object[] {
        isLike.getExpression(),
        isLike.getLiteral() != null ? factory.literal(isLike.getLiteral()) : null
      };
    }

    return super.getProperty(object, name);
  }

  @Override
  public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
    PropertyName name = (PropertyName) node.getChildValue(PropertyName.class);

    String wildcard = (String) node.getAttributeValue("wildCard");
    String single = (String) node.getAttributeValue("singleChar");
    String escape = (String) node.getAttributeValue("escape");
    boolean matchCase = true;

    if (node.getAttributeValue("matchCase") != null) {
      matchCase = (Boolean) node.getAttributeValue("matchCase");
    }

    if (escape == null) {
      // 1.1 uses "escapeChar", suppot that too
      escape = (String) node.getAttributeValue("escapeChar");
    }

    // (CODICE) With the 2.0 schema, must support "expression" on RHS
    Function valueExpression = (Function) node.getChildValue(Function.class);
    if (valueExpression != null) {
      LikeExpressionImpl likeExpression = new LikeExpressionImpl(valueExpression);
      likeExpression.setExpression(name);
      likeExpression.setLiteral(valueExpression.evaluate(valueExpression).toString());

      likeExpression.setMatchCase(matchCase);

      likeExpression.setWildCard(wildcard);
      likeExpression.setSingleChar(single);
      likeExpression.setEscape(escape);

      return valueExpression;
    }
    Literal literal = (Literal) node.getChildValue(Literal.class);

    // (CODICE) With the 2.0 schema, "literal" can be null
    String pattern = (literal == null) ? null : literal.toString();
    return factory.like(name, pattern, wildcard, single, escape, matchCase);
  }
}
