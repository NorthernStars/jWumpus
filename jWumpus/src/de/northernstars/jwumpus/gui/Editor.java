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
	
	private WumpusMap map = null;
	private File mapFile = null;
	private WumpusObjects selectedTool = null;
	
	private JPanel contentPane;
	private static JPanel panelMap;
	private JPanel panelPalette;
	private static JTextField txtColumns;
	private static JTextField txtRows;
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
	
	public Editor(WumpusMap wumpusMap){
		super();
		map = wumpusMap;
		updateGuiMap();
	}
	
	/**
	 * Create the frame.
	 */
	public Editor() {
		setAlwaysOnTop(true);
		setTitle("Map editor");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Editor.class.getResource("/de/northernstars/jwumpus/gui/img/map.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 650);
		
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
		mntmSaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
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
		panelMapSize.add(lblNewLabel1, "2, 4, right, default");
		
		txtColumns = new JTextField();
		txtColumns.setText("5");
		panelMapSize.add(txtColumns, "4, 4, fill, default");
		txtColumns.setColumns(3);
		
		JLabel lblNewLabel2 = new JLabel("Rows:");
		lblNewLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		panelMapSize.add(lblNewLabel2, "2, 2, right, default");
		
		txtRows = new JTextField();
		txtRows.setText("5");
		panelMapSize.add(txtRows, "4, 2, fill, default");
		txtRows.setColumns(3);
		
		btnUpdateMapSize = new JButton("Update map size");
		btnUpdateMapSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int mapDimension[] = getMapDimensionFromGui();
				if( map != null
						&& mapDimension[0] > 0 && mapDimension[1] > 0 ){
					map.setRows(mapDimension[0]);
					map.setColumns(mapDimension[1]);
					updateGuiMap();
				}
			}
		});
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
			panelPalette.add( new PaletteButton(this, tool) );
		}
		
		// update gui panel
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
	 * Creates a new map
	 */
	private void newMap(){
		// create new map
		int mapDimension[] = getMapDimensionFromGui();
		map = new WumpusMap(mapDimension[0], mapDimension[1]);		
		logger.debug("Created new map with " + mapDimension[0] +" rows and " + mapDimension[1] + " columns.");
		
		// update gui
		updateGuiMap();
	}
	
	/**
	 * Saves map
	 */
	private void saveMap(){
		
	}
	
	/**
	 * Updates the current map on gui
	 */
	public void updateGuiMap(){
		if( map != null ){
			
			// show map dimension
			txtRows.setText( Integer.toString(map.getRows()) );
			txtColumns.setText( Integer.toString(map.getColumns()) );
			
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
