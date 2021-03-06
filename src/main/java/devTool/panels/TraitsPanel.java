package devTool.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import devTool.DevStarReader;
import devTool.JsonBuilder;
import devTool.models.EditableChoice;
import devTool.models.EditableJob;
import devTool.models.EditableJobFlyer;
import devTool.models.EditableMission;
import devTool.models.EditableTrait;
import devTool.models.EditableTraitDependency;
import devTool.models.GameDataManager;

public class TraitsPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    JTextField txtField_traitName;
	JTextField txtField_traitId;
	private JList<EditableTrait> list_traitList;
	private ArrayListModel<EditableTrait> traitList;
	EditableTrait selectedTrait;

	MyDocumentListener docListener = new MyDocumentListener(this);

	public TraitsPanel() {
		initialize();
	}

	public void reloadFields() {
		txtField_traitName.setText(selectedTrait.name);
		txtField_traitId.setText(selectedTrait.id);
	}

	public void updateTraits() {
		GameDataManager dataManager = GameDataManager.getInstance();
		if (dataManager.getTraitsModel() != null)
		{
		    traitList = dataManager.getTraitsModel().traits;
	        list_traitList.setModel(traitList);
		}
	}

	public void initialize() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel_traitInfo = new JPanel();
		add(panel_traitInfo, BorderLayout.CENTER);
		panel_traitInfo.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(129dlu;default):grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel lblTraitName = new JLabel("Trait Name");
		panel_traitInfo.add(lblTraitName, "2, 2, right, default");

		txtField_traitName = new JTextField();
		
		panel_traitInfo.add(txtField_traitName, "4, 2, fill, default");
		txtField_traitName.setColumns(10);
		// txtField_traitName.addActionListener(this);
		txtField_traitName.getDocument().addDocumentListener(docListener);

		JLabel lblTraitId = new JLabel("Trait id");
		panel_traitInfo.add(lblTraitId, "2, 4, left, default");

		txtField_traitId = new JTextField();
		txtField_traitId.setEditable(false);

		panel_traitInfo.add(txtField_traitId, "4, 4, fill, default");
		txtField_traitId.setColumns(10);

		JLabel lblImage = new JLabel("Image ");
		panel_traitInfo.add(lblImage, "2, 6");

		JButton btn_selectImage = new JButton("Select Image");
		panel_traitInfo.add(btn_selectImage, "4, 6, center, default");

		JPanel panel_traitList = new JPanel();
		add(panel_traitList, BorderLayout.EAST);
		panel_traitList.setLayout(new BorderLayout(0, 0));

		list_traitList = new JList();
		list_traitList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectedTrait = list_traitList.getSelectedValue();
				reloadFields();
			}
		});
		panel_traitList.add(list_traitList);

		JPanel panel_buttons = new JPanel();
		panel_traitList.add(panel_buttons, BorderLayout.SOUTH);

		// listeners ---------------

		// adds a new trait.
		JButton btn_addTrait = new JButton("Add Trait");
		btn_addTrait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EditableTrait trait = new EditableTrait();
				trait.name = "unnamed trait";
				trait.id = UUID.randomUUID().toString();
				System.out.println(trait.name);
				traitList.add(trait);
			}
		});
		panel_buttons.add(btn_addTrait);

		// deletes a trait.
		JButton btn_deleteTrait = new JButton("Delete Trait");
		btn_deleteTrait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				traitList.remove(selectedTrait); //remove trait from TraitsModel.
				
				//remove the trait dependency from every single choice, in every job, in every mission.
				ArrayListModel<EditableMission> missions = GameDataManager.getInstance().getMissionsModel().getMissions();
				for(EditableMission mission: missions) { //for each mission.
					ArrayListModel<EditableJobFlyer> jobFlyers = mission.jobFlyers;
					
					for (EditableJobFlyer flyer: jobFlyers) { //for each job.
						EditableJob job = DevStarReader.readEditableJob(flyer.id);
						
						for (EditableChoice choice: job.choices) { //for each choice.
							
							ArrayList<EditableTraitDependency> toRemove = new ArrayList<EditableTraitDependency>();
							for (EditableTraitDependency trait: choice.traitDependencies) { //for each trait.
								
								//if trait dependency matches trait.
								if (trait.id.compareTo(selectedTrait.id) == 0) { 
									System.out.println("match found in choice: " + choice.id);
									toRemove.add(trait);
								}
							}
							choice.traitDependencies.removeAll(toRemove);
						}
						
						//save all files
						try {
							JsonBuilder.getInstance().buildJobFile(job);
							JsonBuilder.getInstance().buildTraitsFile();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		panel_buttons.add(btn_deleteTrait);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// save all fields to selectedTrait.
		this.selectedTrait.name = this.txtField_traitName.getText();
		System.out.println("actionlistener hit.");
	}

}

class MyDocumentListener implements DocumentListener {
	private TraitsPanel panel;

	public MyDocumentListener(TraitsPanel panel) {
		this.panel = panel;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		saveFields();
		System.out.println("changed update");
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		saveFields();
		System.out.println("insert update");
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		saveFields();
		System.out.println("remove update");
	}

	private void saveFields() {
		panel.selectedTrait.name = panel.txtField_traitName.getText();
	}
}
