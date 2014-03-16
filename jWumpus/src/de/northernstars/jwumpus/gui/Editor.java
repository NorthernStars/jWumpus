package de.northernstars.jwumpus.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import de.northernstars.jwumpus.core.WumpusMap;
import de.northernstars.jwumpus.core.WumpusObjects;
import de.northernstars.jwumpus.gui.widgets.PaletteButton;

import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

@SuppressWarnings("serial")
public class Editor extends JFrame {
	
	private static final Logger logger = LogManager.getLogger(Editor.class);
	private static final String title = "Map Editor";
	
	private WumpusMap map = null;
	private File mapFile = null;
	private WumpusObjects selectedTool = null;
	
	private JPanel contentPane;
	private JPanel panelMap;
	private JTextField txtColumns;
	private JTextField txtRows;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmNewMap;
	private JMenuItem mntmOpenMap;
	private JMenuItem mntmSaveMap;
	private JMenuItem mntmSaveMapAs;
	private JButton btnUpdateMap;
	private JLabel lblNewLabel;
	private JTextField txtMapName;
	private JMenuItem mntmClose;
	private JScrollPane panelPaletteTop;
	private JPanel panelPalette;
	private JLabel lblPlayerArrows;
	private JTextField txtPlayerArrows;
	
	/**
	 * Launches the editor.
	 * @param wumpusMap	{@link WumpusMap} to load or null to load empty default map
	 */
	public static void showEditor(WumpusMap wumpusMap) {
		final WumpusMap mWumpusMap = wumpusMap;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editor frame = new Editor(mWumpusMap);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Editor(WumpusMap wumpusMap){
		this();
		
		if( wumpusMap != null ){
			map = wumpusMap;
			updateGuiMap();
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Editor() {
		setTitle(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Editor.class.getResource("/de/northernstars/jwumpus/gui/img/map.png")));
		setBounds(100, 100, 800, 650);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		mntmNewMap = new JMenuItem("New map");
		mntmNewMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNewMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMap();
			}
		});
		mnFile.add(mntmNewMap);
		
		mntmOpenMap = new JMenuItem("Open map");
		mntmOpenMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpenMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openMap();
			}
		});
		mnFile.add(mntmOpenMap);
		
		mntmSaveMap = new JMenuItem("Save map");
		mntmSaveMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveMap();
			}
		});
		mnFile.add(mntmSaveMap);
		
		mntmSaveMapAs = new JMenuItem("Save map as");
		mntmSaveMapAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmSaveMapAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveMapAs();
			}
		});
		mnFile.add(mntmSaveMapAs);
		
		mntmClose = new JMenuItem("Close");
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		mnFile.add(mntmClose);
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
		
		panelMap = new JPanel();
		panelMap.setBorder(new TitledBorder(null, "Map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelMap, "2, 2, 1, 3, fill, fill");
		panelMap.setLayout(new GridLayout(4, 4, 0, 0));
		
		JPanel panelMapData = new JPanel();
		panelMapData.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Map data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelMapData, "4, 2, fill, fill");
		panelMapData.setLayout(new FormLayout(new ColumnSpec[] {
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
		
		JLabel lblNewLabel1 = new JLabel("Columns:");
		lblNewLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		panelMapData.add(lblNewLabel1, "2, 4, right, default");
		
		txtColumns = new JTextField();
		txtColumns.setText("5");
		panelMapData.add(txtColumns, "4, 4, fill, default");
		txtColumns.setColumns(3);
		
		JLabel lblNewLabel2 = new JLabel("Rows:");
		lblNewLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		panelMapData.add(lblNewLabel2, "2, 2, right, default");
		
		txtRows = new JTextField();
		txtRows.setText("5");
		panelMapData.add(txtRows, "4, 2, fill, default");
		txtRows.setColumns(3);
		
		btnUpdateMap = new JButton("Update map");
		btnUpdateMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int mapDimension[] = getMapDimensionFromGui();
				int playerArrows = getPlayerArrowsFromGui();
				String mapName = txtMapName.getText();
				
				if( map != null
						&& mapDimension[0] > 0 && mapDimension[1] > 0 ){
					map.setRows(mapDimension[0]);
					map.setColumns(mapDimension[1]);
					map.setMapName(mapName);
					map.setPlayerArrows(playerArrows);
					updateGuiMap();
				}
			}
		});
		
		lblNewLabel = new JLabel("Name:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panelMapData.add(lblNewLabel, "2, 6, right, default");
		
		txtMapName = new JTextField();
		txtMapName.setText("Wumpus World");
		panelMapData.add(txtMapName, "4, 6, fill, default");
		txtMapName.setColumns(10);
		
		lblPlayerArrows = new JLabel("Player arrows:");
		lblPlayerArrows.setHorizontalAlignment(SwingConstants.RIGHT);
		panelMapData.add(lblPlayerArrows, "2, 8, right, default");
		
		txtPlayerArrows = new JTextField();
		txtPlayerArrows.setText("1");
		panelMapData.add(txtPlayerArrows, "4, 8, fill, default");
		txtPlayerArrows.setColumns(10);
		panelMapData.add(btnUpdateMap, "2, 10, 3, 1");
		
		panelPaletteTop = new JScrollPane();
		panelPaletteTop.setBorder(new TitledBorder(null, "Palette", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelPaletteTop, "4, 4, fill, fill");
		
		panelPalette = new JPanel();
		panelPaletteTop.setViewportView(panelPalette);
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
			panelPalette.add( new PaletteButton(this, tool) );
		}
		
		// update gui panel
		panelPalette.repaint();
		panelPalette.validate();
		
	}
	
	/**
	 * @return {@link Integer} array {@code [rows, cloumns]} with dimension of map, set in gui.
	 */
	private int[] getMapDimensionFromGui(){
		try{
			int rows = Integer.parseInt(txtRows.getText());
			int columns = Integer.parseInt(txtColumns.getText());
			return new int[]{rows, columns};
		}catch (NumberFormatException e){
			logger.error("Rows or columns field contains no integer value.");
		}
		
		return new int[]{0,0};
	}
	
	/**
	 * @return {@link Integer} number of arrows for player on map
	 */
	private int getPlayerArrowsFromGui(){
		try{
			int arrows = Integer.parseInt(txtPlayerArrows.getText());
			return arrows;
		}catch (NumberFormatException e){
			logger.error("Player arrows field contains no integer value.");
		}
		
		return 0;
	}
	
	/**
	 * Creates a new map
	 */
	private void newMap(){
		// create new map
		int mapDimension[] = getMapDimensionFromGui();
		String mapName = txtMapName.getText();
		map = new WumpusMap(mapDimension[0], mapDimension[1], mapName);
		mapFile = null;
		
		logger.debug("Created new map with " + mapDimension[0] +" rows and " + mapDimension[1] + " columns.");
		
		// update gui
		updateGuiMap();
	}
	
	/**
	 * Opens a map from file
	 */
	private void openMap(){
		// create file chooser dialog and open it
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("jWumpus map files (.map)", "map");
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);		
		chooser.setAcceptAllFileFilterUsed(false);
		
		if( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ){
			mapFile = chooser.getSelectedFile();
			
			try{
				if( mapFile.exists() && mapFile.canRead() ){
					map = (new Gson()).fromJson(new FileReader(mapFile), WumpusMap.class);
					updateGuiMap();
				}
				else{
					logger.error("Can not read from file " + mapFile.getPath());
				}
			}catch (FileNotFoundException e){
				logger.error("Can not find file " + mapFile.getPath());
			}
		}
	}
	
	/**
	 * Saves map to set mapFile
	 */
	private void saveMap(){
		if( mapFile != null ){
			try{
				
				// check if file exsits
				if( !mapFile.exists() ){
					// check file extension
					if( !mapFile.getPath().endsWith(".map") ){
						mapFile = new File(mapFile.getPath() + ".map");
					}
					
					// check if file is readable
					if( !mapFile.createNewFile() ){
						logger.error("Could not create file " + mapFile.getPath());
						return;
					}
				}
				
				// write data to file
				if( mapFile.canWrite() ){
					BufferedWriter out = new BufferedWriter( new FileWriter(mapFile) );
					out.write( (new Gson()).toJson(map) );
					out.close();
				}
				else{
					logger.error("Can not write to file " + mapFile.getPath());
				}
				
			}catch (IOException e){
				logger.error("Error while writing data to file " + mapFile.getPath());
			}				
		}
		else{
			saveMapAs();
		}
	}
	
	/**
	 * Saves map as new file
	 */
	private void saveMapAs(){
		// create file chooser dialog and open it
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("jWumpus map files (.map)", "map");
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);		
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setSelectedFile(new File("wumpus world.map"));
		
		if( chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ){
			// save file			
			mapFile = chooser.getSelectedFile();
			saveMap();
		}
	}
		
	
	/**
	 * Updates the current map on gui
	 */
	public void updateGuiMap(){
		if( map != null ){
			
			// show map dimension and frame title
			txtRows.setText( Integer.toString(map.getRows()) );
			txtColumns.setText( Integer.toString(map.getColumns()) );
			txtMapName.setText( map.getMapName() );
			setTitle(title + " - " + map.getMapName());
			
			// update gui map
			MainFrame.updateGuiMap(this, map, panelMap);
		}
	}
	
	/**
	 * Sets the {@code WumpusObjects} tool selected from palette panel
	 * @param tool
	 */
	public void setTool(WumpusObjects tool){
		selectedTool = tool;
	}
	
	/**
	 * @return Currently selected {@code WumpusObjects} tool or {@code null} if no tool selected
	 */
	public WumpusObjects getTool(){
		return selectedTool;
	}

}
