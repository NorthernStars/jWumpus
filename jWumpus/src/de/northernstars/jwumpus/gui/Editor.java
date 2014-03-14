package de.northernstars.jwumpus.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import de.northernstars.jwumpus.core.WumpusMap;
import de.northernstars.jwumpus.core.WumpusObjects;
import de.northernstars.jwumpus.gui.widgets.MapObject;
import de.northernstars.jwumpus.gui.widgets.PaletteButton;

import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import java.awt.Toolkit;
import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Editor extends JFrame {
	
	private static final Logger logger = LogManager.getLogger(Editor.class);
	
	private static WumpusMap map = null;
	private static File mapFile = null;
	private static WumpusObjects selectedTool = null;
	
	private JPanel contentPane;
	private static JPanel panelMap;
	private JPanel panelPalette;
	private JTextField txtColumns;
	private JTextField txtRows;
	public JMenuBar menuBar;
	public JMenu mnFile;
	public JMenuItem mntmNewMap;
	public JMenuItem mntmLoadMap;
	public JMenuItem mntmSaveMap;
	public JMenuItem mntmSaveMapAs;
	public JButton btnUpdateMapSize;
	
	/**
	 * Launches the editor.
	 */
	public static void showEditor() {
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
		setAlwaysOnTop(true);
		setTitle("Map editor");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Editor.class.getResource("/de/northernstars/jwumpus/gui/img/map.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmNewMap = new JMenuItem("New map");
		mntmNewMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMap();
			}
		});
		mnFile.add(mntmNewMap);
		
		mntmLoadMap = new JMenuItem("Load map");
		mnFile.add(mntmLoadMap);
		
		mntmSaveMap = new JMenuItem("Save map");
		mnFile.add(mntmSaveMap);
		
		mntmSaveMapAs = new JMenuItem("Save map as");
		mnFile.add(mntmSaveMapAs);
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		panelMap = new JPanel();
		panelMap.setBorder(new TitledBorder(null, "Map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelMap, "2, 2, 1, 5, fill, fill");
		panelMap.setLayout(new GridLayout(4, 4, 0, 0));
		
		JPanel panelMapSize = new JPanel();
		panelMapSize.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Map size", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelMapSize, "4, 2, fill, fill");
		panelMapSize.setLayout(new FormLayout(new ColumnSpec[] {
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
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblNewLabel1 = new JLabel("Columns:");
		lblNewLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		panelMapSize.add(lblNewLabel1, "2, 2, right, default");
		
		txtColumns = new JTextField();
		txtColumns.setText("5");
		panelMapSize.add(txtColumns, "4, 2, fill, default");
		txtColumns.setColumns(3);
		
		JLabel lblNewLabel2 = new JLabel("Rows:");
		lblNewLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		panelMapSize.add(lblNewLabel2, "2, 4, right, default");
		
		txtRows = new JTextField();
		txtRows.setText("5");
		panelMapSize.add(txtRows, "4, 4, fill, default");
		txtRows.setColumns(3);
		
		btnUpdateMapSize = new JButton("Update map size");
		panelMapSize.add(btnUpdateMapSize, "2, 6, 3, 1");
		
		panelPalette = new JPanel();
		panelPalette.setBorder(new TitledBorder(null, "Palette", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelPalette, "4, 4, fill, fill");
		panelPalette.setLayout(new GridLayout(0, 1, 5, 5));
		
		// update palette
		setPalette();
		
		// create new map
		newMap();

	}
	
	/**
	 * Sets palette objects
	 */
	private void setPalette(){
		
		// clear palette
		panelPalette.removeAll();
		
		for( WumpusObjects tool : WumpusObjects.values() ){			
			// add new PaletteButton to gui panel
			panelPalette.add( new PaletteButton(tool) );
		}
		
		// update gui panel
		panelPalette.validate();
		
	}
	
	/**
	 * Creates a new map
	 */
	private void newMap(){
		try{
			
			// create new map
			int rows = Integer.parseInt(txtRows.getText());
			int columns = Integer.parseInt(txtColumns.getText());
			map = new WumpusMap(rows, columns);
			
			logger.debug("Creating new map with " + rows +" rows and " + columns + " columns.");
			
			// update gui
			updateGuiMap();
			
		}catch (NumberFormatException e){
			logger.error("Rows or columns field contains no integer value.");
		}
	}
	
	/**
	 * Updates the current map on gui
	 */
	public static void updateGuiMap(){
		if( map != null ){
			logger.debug("Updating map: " + map.getMapName());
			
			// clear gui map
			panelMap.removeAll();
			
			// set new grid
			panelMap.setLayout(new GridLayout(map.getRows(), map.getColumns(), 0, 0));
			
			// add objects to map
			for( int row=0; row<map.getRows(); row++ ){
				for( int column=0; column<map.getColumns(); column++ ){
					panelMap.add( new MapObject(map.getWumpusMapObject(row, column)) );
				}
			}
			
			// redraw gui map panel
			panelMap.validate();			
		}
	}
	
	/**
	 * Sets the {@code WumpusObjects} tool selected from palette panel
	 * @param tool
	 */
	public static void setTool(WumpusObjects tool){
		selectedTool = tool;
	}
	
	/**
	 * @return Currently selected {@code WumpusObjects} tool or {@code null} if no tool selected
	 */
	public static WumpusObjects getTool(){
		return selectedTool;
	}

}
