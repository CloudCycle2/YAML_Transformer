package yaml.element;

import java.util.ArrayList;
import tosca.nodes.Root;

public class NodeTemplate extends YAMLElement {
	private String name;
	private ArrayList<Root> root = new ArrayList<Root>();
}