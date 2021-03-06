package devTool.panels;

import java.awt.Component;
import java.util.List;

import javax.swing.JFrame;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import devTool.models.EditableChoice;

/**
 * 
 * @author peakyDicers
 *
 */
public class ChoicesGraph extends JFrame {
	private static final long serialVersionUID = -302682455085417699L;
	private String headChoiceId = "";
	Graph graph;
	List<EditableChoice> choices;

	public ChoicesGraph(List<EditableChoice> choices) {
		this.choices = choices;
		graph = new SingleGraph("Tutorial 1");
		populateGraph();

		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		View view = viewer.addDefaultView(false); // false indicates "no JFrame".

		add((Component) view);
		setSize(600, 600);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void setHeadChoiceId(String id) {
		headChoiceId = id;
		this.populateGraph();
	}

	public void populateGraph() {
		graph.clear();
		graph.addAttribute("ui.stylesheet", "node {fill-color: red;}");
		graph.addAttribute("ui.stylesheet", "node.start {fill-color: green;}");
		for (EditableChoice c : choices) {
			Node n = graph.addNode(c.id);
			n.addAttribute("ui.label", c.name);

			if (headChoiceId != null && c.id.compareTo(headChoiceId) == 0)
				n.addAttribute("ui.class", "start");
			else
				n.addAttribute("ui.class", "base");

		}

		// connect the dots
		for (EditableChoice c : choices) {
			for (String childId : c.children) {
				graph.addEdge(c.id + childId, c.id, childId, true);
			}
		}
	}
}
