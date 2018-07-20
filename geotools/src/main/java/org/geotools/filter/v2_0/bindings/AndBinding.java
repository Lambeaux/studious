package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.filter.v1_0.OGCAndBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:And.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="And" substitutionGroup="fes:logicOps" type="fes:BinaryLogicOpType"/&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class AndBinding extends OGCAndBinding {
  private FilterFactory factory;

  public AndBinding(FilterFactory filterfactory) {
    super(filterfactory);
    this.factory = filterfactory;
  }

  public QName getTarget() {
    return FES.And;
  }

  @Override
  public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
    //    List children = node.getChildren();
    //    return factory.and(children);
    return super.parse(instance, node, value);
  }
}
