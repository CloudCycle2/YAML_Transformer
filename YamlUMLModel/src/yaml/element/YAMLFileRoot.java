package yaml.element;

import java.util.ArrayList;
import yaml.element.NodeTemplate;

public class YAMLFileRoot extends YAMLElement {
	private String tosca_definitions_version;
	private ArrayList<NodeTemplate> nodeTemplate = new ArrayList<NodeTemplate>();
}