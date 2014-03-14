package de.northernstars.jwumpus.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Editor extends JFrame {

	private JPanel contentPane;
	private JTextField txtColumns;
	private JTextField txtRows;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editor frame = new Editor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Editor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JPanel panelMap = new JPanel();
		panelMap.setBorder(new TitledBorder(null, "Map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelMap, "2, 2, 1, 3, fill, fill");
		panelMap.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panelControl = new JPanel();
		panelControl.setBorder(new TitledBorder(null, "Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelControl, "4, 2, fill, fill");
		panelControl.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setMnemonic('l');
		panelControl.add(btnLoad, "2, 2, 3, 1");
		
		JButton btnSave = new JButton("Save");
		btnSave.setMnemonic('s');
		panelControl.add(btnSave, "2, 4, 3, 1");
		
		JButton btnNew = new JButton("New");
		btnNew.setMnemonic('n');
		panelControl.add(btnNew, "2, 6, 3, 1");
		
		JLabel lblNewLabel1 = new JLabel("Columns:");
		lblNewLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		panelControl.add(lblNewLabel1, "2, 8, right, default");
		
		txtColumns = new JTextField();
		panelControl.add(txtColumns, "4, 8, fill, default");
		txtColumns.setColumns(3);
		
		JLabel lblNewLabel2 = new JLabel("Rows:");
		lblNewLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		panelControl.add(lblNewLabel2, "2, 10, right, default");
		
		txtRows = new JTextField();
		panelControl.add(txtRows, "4, 10, fill, default");
		txtRows.setColumns(3);
		
		JPanel panelPalette = new JPanel();
		panelPalette.setBorder(new TitledBorder(null, "Palette", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelPalette, "4, 4, fill, fill");
		panelPalette.setLayout(new FormLayout(new ColumnSpec[] {},
			new RowSpec[] {}));
	}
}
