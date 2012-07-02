package se.chalmers.snake.leveldatabase;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
class XMLReader {

        static Node find(Node node, String tagName) throws Exception {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeName().equals(tagName)) {
                    return nl.item(i);
                }
            }
            throw new Exception("Node has no childen with tag '" + tagName + "'");

        }

        static String attribute(Node node, String name) {
            if (node instanceof Element) {
                final Element e = (Element) node;
                if (e.hasAttribute(name)) {
                    return e.getAttribute(name);
                }
            }
            return null;
        }

        static int attributeInt(Node node, String name) {
            if (node instanceof Element) {
                final Element e = (Element) node;
                if (e.hasAttribute(name)) {
                    return Integer.parseInt(e.getAttribute(name));
                }
            }
            return 0;
        }

        static String val(Node node) {
            if (node == null) {
                throw new NullPointerException("Node not Exist");
            }
            String val = node.getFirstChild().getNodeValue();
            return val != null ? val : "";
        }
}
