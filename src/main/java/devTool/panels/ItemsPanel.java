package devTool.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import devTool.JsonBuilder;
import devTool.models.GameDataManager;
import devTool.models.item.EditableItem;
import devTool.models.item.EditableTraitBoost;
import devTool.models.item.ItemsModel;

public class ItemsPanel extends JPanel {
	private JTextField txtField_itemName;
	private JTextField txtField_price;
	private JTextField txtField_imgId;
	private JList<EditableItem> jList_items;
	private JList<EditableTraitBoost> jList_traits;
	private EditableItem selectedItem;
	private JTextField txtField_description;
	private JTextField txtField_itemId;
	private EditableTraitBoost selectedTraitBoost;
	private JTextField txtField_maxEquip;
	private JLabel lbl_imageDisplay;

	/**
	 * Create the panel.
	 */
	public ItemsPanel() {
		initialize();
	}

	public void updateItems() {
		ItemsModel itemsModel = GameDataManager.getInstance().getItemsModel();
		jList_items.setModel(itemsModel.items);
		txtField_maxEquip.setText(String.valueOf(itemsModel.maxEquipped));
	}
	
	private void updateCanvas() {
		lbl_imageDisplay.setIcon(null);
		if (selectedItem.imageId == null)
			return;

		String path = JsonBuilder.getInstance().getBaseDir() + "/assets/" + selectedItem.imageId;
		Image image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Double factor = 200.0 / image.getWidth(null);
		image = image.getScaledInstance((int) (image.getWidth(null) * factor), (int) (image.getHeight(null) * factor),
				Image.SCALE_DEFAULT);
		ImageIcon icon = new ImageIcon(image);
		lbl_imageDisplay.setIcon(icon);

		repaint();
		revalidate();
	}

	public void initialize() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel_itemList = new JPanel();
		add(panel_itemList, BorderLayout.WEST);
		panel_itemList.setLayout(new BorderLayout(0, 0));

		jList_items = new JList();
		jList_items.setBorder(new LineBorder(new Color(186, 85, 211)));
		panel_itemList.add(jList_items, BorderLayout.CENTER);

		JPanel panel_btns = new JPanel();
		panel_itemList.add(panel_btns, BorderLayout.SOUTH);
		panel_btns.setLayout(new BoxLayout(panel_btns, BoxLayout.X_AXIS));

		JButton btn_newItem = new JButton("New Item");
		panel_btns.add(btn_newItem);

		JButton btn_removeItem = new JButton("Remove Item");
		panel_btns.add(btn_removeItem);

		JPanel panel_generalInfo = new JPanel();
		panel_itemList.add(panel_generalInfo, BorderLayout.NORTH);
		panel_generalInfo.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel lbl_maxEquip = new JLabel("Max num of Equips: ");
		panel_generalInfo.add(lbl_maxEquip, "2, 2, right, default");

		txtField_maxEquip = new JTextField();
		panel_generalInfo.add(txtField_maxEquip, "4, 2, fill, default");
		txtField_maxEquip.setColumns(10);

		JPanel panel = new JPanel();
		panel.setForeground(new Color(0, 255, 0));
		panel.setBackground(new Color(0, 255, 0));
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_saveBtn = new JPanel();
		panel_saveBtn.setBackground(new Color(0, 255, 127));
		panel.add(panel_saveBtn);

		JButton btn_save = new JButton("Save");

		panel_saveBtn.add(btn_save);
		btn_save.setBackground(Color.GREEN);
		btn_save.setForeground(Color.BLACK);

		JPanel panel_info = new JPanel();
		panel.add(panel_info);
		panel_info.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel lbl_itemName = new JLabel("Item name");
		panel_info.add(lbl_itemName, "2, 2, left, default");

		txtField_itemName = new JTextField();
		panel_info.add(txtField_itemName, "6, 2, fill, default");
		txtField_itemName.setColumns(10);

		JLabel lbl_description = new JLabel("Description");
		panel_info.add(lbl_description, "2, 4, left, default");

		txtField_description = new JTextField();
		panel_info.add(txtField_description, "6, 4, fill, default");
		txtField_description.setColumns(10);

		JLabel lbl_itemId = new JLabel("ID");
		panel_info.add(lbl_itemId, "2, 6");

		txtField_itemId = new JTextField();
		txtField_itemId.setEditable(false);
		panel_info.add(txtField_itemId, "6, 6, fill, default");
		txtField_itemId.setColumns(10);

		JButton btn_selectImage = new JButton("Select Image");
		
		panel_info.add(btn_selectImage, "2, 8");

		txtField_imgId = new JTextField();
		txtField_imgId.setEditable(false);
		panel_info.add(txtField_imgId, "6, 8, fill, default");
		txtField_imgId.setColumns(10);

		JLabel lbl_price = new JLabel("Price");
		panel_info.add(lbl_price, "2, 10, left, default");

		txtField_price = new JTextField();
		panel_info.add(txtField_price, "6, 10, fill, default");
		txtField_price.setColumns(10);
		
		lbl_imageDisplay = new JLabel("");
		panel.add(lbl_imageDisplay);

		JPanel panel_traitList = new JPanel();
		panel.add(panel_traitList);
		panel_traitList.setLayout(new BorderLayout(0, 0));

		jList_traits = new JList();
		jList_traits.setBorder(new LineBorder(new Color(220, 20, 60)));
		panel_traitList.add(jList_traits);

		JPanel panel_traitBtns = new JPanel();
		panel_traitList.add(panel_traitBtns, BorderLayout.SOUTH);

		JButton btn_addTrait = new JButton("New Trait");
		panel_traitBtns.add(btn_addTrait);

		JButton btn_removeTrait = new JButton("Remove Trait");
		panel_traitBtns.add(btn_removeTrait);

		// ------------------------ listeners

		// save item.
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedItem != null) {
					selectedItem.name = txtField_itemName.getText();
					selectedItem.description = txtField_description.getText();
					selectedItem.price = Integer.valueOf(txtField_price.getText());
				}
				
				GameDataManager.getInstance().getItemsModel().maxEquipped = Integer
						.valueOf(txtField_maxEquip.getText());
			}
		});
		
		// selecting an image.
		btn_selectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedItem == null) return;
				
				JFileChooser fc = new JFileChooser();
				FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
				fc.addChoosableFileFilter(imageFilter);
				int result = fc.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					String fileName = JsonBuilder.getInstance().createImageFile(fc.getSelectedFile(), selectedItem.id);
					if (fileName != null) {
						selectedItem.imageId = fileName;
						JOptionPane.showMessageDialog(null, "Image saved successfully.");
						updateCanvas();
					} else {
						JOptionPane.showMessageDialog(null, "Image save Error!!");
					}
				}
			}
		});

		// open trait boost picker.
		btn_addTrait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TraitBoostPicker(selectedItem);
			}
		});

		// delete traitboost.
		btn_removeTrait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedItem.traitBoosts.remove(selectedTraitBoost);
				selectedItem = null;
			}
		});

		// select a trait boost.
		jList_traits.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedTraitBoost = jList_traits.getSelectedValue();
			}
		});

		// clicked on item.
		jList_items.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedTraitBoost = null;
				selectedItem = jList_items.getSelectedValue();
				txtField_itemName.setText(selectedItem.name);
				txtField_description.setText(selectedItem.description);
				txtField_itemId.setText(selectedItem.id);
				txtField_price.setText(String.valueOf(selectedItem.price));
				jList_traits.setModel(selectedItem.traitBoosts);
				updateCanvas();
			}
		});

		// add item.
		btn_newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditableItem item = new EditableItem();
				item.name = "unnamed item";
				item.id = UUID.randomUUID().toString();
				GameDataManager.getInstance().getItemsModel().items.add(item);
			}
		});

		// remove item.
		btn_removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameDataManager.getInstance().getItemsModel().items.remove(selectedItem);
				txtField_itemName.setText(null);
				txtField_price.setText(null);
				txtField_imgId.setText(null);
			}
		});
	}
}
