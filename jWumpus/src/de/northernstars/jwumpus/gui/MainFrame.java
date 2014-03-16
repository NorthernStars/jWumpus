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

import de.northernstars.jwumpus.core.PlayerState;
import de.northernstars.jwumpus.core.WumpusMap;
import de.northernstars.jwumpus.core.WumpusMapObject;
import de.northernstars.jwumpus.gui.listener.FrameLoadedListener;
import de.northernstars.jwumpus.gui.listener.JWumpusControl;
import de.northernstars.jwumpus.gui.widgets.MapObject;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Toolkit;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static final Logger logger = LogManager.getLogger(MainFrame.class);
	private static final String title = "jWumpus";
	private JWumpusControl control;
	
	private File mapFile;
	private WumpusMap map;
	private WumpusMap aiMap;
	
	private JPanel contentPane;
	private JTextField txtSetpDelay;
	private JPanel panelMapAI;
	private JPanel panelMapOriginal;
	private JButton btnStart;
	private JButton btnReset;
	private JLabel lblPlayerState;
	private JLabel lblCurrentStep;
	private JButton btnNext;

	/**
	 * Launch the application.
	 * @param listener {@link FrameLoadedListener} or {@code null} if no callback
	 */
	public static void showMainFrame(FrameLoadedListener listener) {		
		final FrameLoadedListener mListener = listener;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame(mListener);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param listener {@link FrameLoadedListener} or {@code null} if no callback
	 */
	public MainFrame(FrameLoadedListener listener) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/de/northernstars/jwumpus/gui/img/wumpus.png")));
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 300, 800, 600);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		panelMapOriginal = new JPanel();
		panelMapOriginal.setBorder(new TitledBorder(null, "Complete map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelMapOriginal, "2, 2, 1, 5, fill, fill");
		panelMapOriginal.setLayout(new GridLayout(1, 0, 0, 0));
		
		panelMapAI = new JPanel();
		panelMapAI.setBorder(new TitledBorder(null, "AI map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelMapAI, "4, 2, 1, 5, fill, fill");
		panelMapAI.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panelAutomaticControl = new JPanel();
		panelAutomaticControl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Automatic steps", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelAutomaticControl, "6, 2, fill, fill");
		panelAutomaticControl.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JPanel panelAutomaticControlSettings = new JPanel();
		panelAutomaticControl.add(panelAutomaticControlSettings, "2, 4, 3, 1, fill, fill");
		
		JLabel lblStepDelay = new JLabel("Step delay");
		panelAutomaticControlSettings.add(lblStepDelay);
		
		txtSetpDelay = new JTextField();
		panelAutomaticControlSettings.add(txtSetpDelay);
		txtSetpDelay.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSetpDelay.setText("500");
		txtSetpDelay.setColumns(5);
		
		JLabel lblNewLabel = new JLabel("ms");
		panelAutomaticControlSettings.add(lblNewLabel);
		
		JPanel panelManualControl = new JPanel();
		panelManualControl.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Manual steps", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelManualControl, "6, 4, fill, fill");
		panelManualControl.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( control != null ){
					control.nextAIStep();
					btnReset.setEnabled(true);
				}
				else{
					logger.error("No JWumpusControl listener set!");
				}
			}
		});
		btnNext.setMnemonic('n');
		panelManualControl.add(btnNext, "2, 2");
		
		btnStart = new JButton("Start");
		btnStart.setMnemonic('s');
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( control != null ){
					
					if( btnStart.getText().contains("Start") ){
						if( control.startAI(getStepDelay()) ){
							btnStart.setText("Pause");
							btnStart.setMnemonic('p');
							btnReset.setEnabled(false);
							btnNext.setEnabled(false);
						}
					}
					else if( btnStart.getText().contains("Resume") ){
						control.resumeAI();
						btnStart.setText("Pause");
						btnStart.setMnemonic('p');
						btnReset.setEnabled(false);
						btnNext.setEnabled(false);
					}
					else{
						control.pauseAI();
						btnStart.setText("Resume");
						btnStart.setMnemonic('r');
						btnReset.setEnabled(true);
						btnNext.setEnabled(true);
					}
				}
				else{
					logger.error("No JWumpusControl listener set!");
				}
			}
		});
		panelAutomaticControl.add(btnStart, "2, 2");
		
		btnReset = new JButton("Reset");
		btnReset.setEnabled(false);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStart.setText("Start");
				btnStart.setMnemonic('s');
				
				if( control != null ){
					control.resetAI();
					btnReset.setEnabled(false);
					btnNext.setEnabled(true);
					loadMap();
				}
				else{
					logger.error("No JWumpusControl listener set!");
				}
			}
		});
		btnReset.setMnemonic('r');
		panelAutomaticControl.add(btnReset, "4, 2");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, "6, 6, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblNewLabel2 = new JLabel("Status:");
		lblNewLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel2, "2, 2");
		
		lblPlayerState = new JLabel("UNKNOWN");
		panel.add(lblPlayerState, "4, 2");
		
		JLabel lblNewLabel1 = new JLabel("Current step:");
		lblNewLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel1, "2, 4");
		
		lblCurrentStep = new JLabel("0");
		panel.add(lblCurrentStep, "4, 4");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('f');
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadMap = new JMenuItem("Load map");
		mntmLoadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openMap();
			}
		});
		mntmLoadMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mnFile.add(mntmLoadMap);
		
		JMenuItem mntmShowEditor = new JMenuItem("Show editor");
		mntmShowEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Editor.showEditor(null);
			}
		});
		mntmShowEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmShowEditor);
		
		JMenuItem mntmShowMapInEditor = new JMenuItem("Show map in editor");
		mntmShowMapInEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Editor.showEditor(map);
			}
		});
		mntmShowMapInEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmShowMapInEditor);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( control != null ){
					control.close();
				}
				
				dispose();
			}
		});
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmClose);
		
		// check if to call listener
		if( listener != null ){
			listener.frameLoaded(this, this.getClass());
		}
	}
	
	/**
	 * @return Step delay set on gui
	 */
	private long getStepDelay(){
		try{
			return Long.parseLong(txtSetpDelay.getText());
		}catch (NumberFormatException err){
			logger.error("Step delay is not a number");
		}
		
		return 0;
	}
	
	/**
	 * Sets {@link JWumpusControl} object for gui callbacks
	 * @param control
	 */
	public void setJWumpusControl(JWumpusControl control){
		this.control = control;
	}
	
	/**
	 * Sets current status
	 * @param currentStep	Current step {@link Integer}
	 * @param playerState	{@link PlayerState}
	 */
	public void setStatus(int currentStep, PlayerState playerState){
		lblCurrentStep.setText( Integer.toString(currentStep) );
		lblPlayerState.setText( playerState.name() );
		
		if( playerState == PlayerState.UNKNOWN ){
			btnStart.setText("Start");
			btnStart.setMnemonic('s');
			btnReset.setEnabled(true);
			btnNext.setEnabled(true);
		}
	}
	
	/**
	 * Loads map from mapFile
	 */
	private void loadMap(){
		try{
			// check if map can get loaded
			if( mapFile != null && mapFile.exists() && mapFile.canRead() ){
				setMap( (new Gson()).fromJson(new FileReader(mapFile), WumpusMap.class) );
				logger.debug("Loaded map " + map.getMapName());
				
				// call JWumpusControl listener
				if( control != null ){
					control.mapLoaded(map);
				}
				else{
					logger.error("No JWumpusControl listener set!");
				}
			}
			else{
				logger.error("Can not read from file " + mapFile.getPath());
			}
		}catch (FileNotFoundException e){
			logger.error("Can not find file " + mapFile.getPath());
		}
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
		chooser.setSelectedFile(new File("wumpus world.map"));
		
		if( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ){
			mapFile = chooser.getSelectedFile();
			loadMap();
		}
	}
	
	/**
	 * Sets {@link WumpusMap} of AI
	 * @param aiMap {@link WumpusMap}
	 */
	public void setAiMap(WumpusMap aiMap){
		if( aiMap != null ){
			this.aiMap = new WumpusMap(aiMap);
		}
		else{
			this.aiMap = null;
		}
		updateAiMap();
	}
	
	/**
	 * Sets {@link WumpusMap}
	 * @param map {@link WumpusMap}
	 */
	public void setMap(WumpusMap map){
		this.map = new WumpusMap(map);
		updateOriginalGuiMap();
	}
	
	/**
	 * Updates original gui map
	 */
	private void updateOriginalGuiMap(){
		updateGuiMap(map, panelMapOriginal);
		setTitle(title + " - " + map.getMapName());
	}
	
	/**
	 * Updates ai gui map
	 */
	private void updateAiMap(){
		updateGuiMap(aiMap, panelMapAI);
	}
	
	/**
	 * Updates a non-editable map panel
	 * @param map		{@link WumpusMap} to show
	 * @param panel		{@link JPanel} where to show {@code map}
	 */
	public static void updateGuiMap(WumpusMap map, JPanel panel){
		updateGuiMap(null, map, panel);
	}
	
	/**
	 * Updates a map panel
	 * @param editor	{@link Editor} frame if map is editable, {@code null} otherwise
	 * @param map		{@link WumpusMap} to show
	 * @param panel		{@link JPanel} where to show {@code map}
	 */
	public static void updateGuiMap(Editor editor, WumpusMap map, JPanel panel){
		// clear gui map
		panel.removeAll();
		
		if( map != null && panel != null ){		
			// update maps dimension			
			if( !map.getCheckDimension() ){
				map.updateDimensions();
			}
			
			// check map dimension
			if( (map.getRows() == 0 && map.getColumns() == 0)
					|| map.getRows() < 0 || map.getColumns() < 0){
				logger.error("No valid map dimensions!");
				return;
			}
			
			// set new grid
			panel.setLayout(new GridLayout(map.getRows(), map.getColumns(), 0, 0));
			
			// add objects to map
			for( int row=map.getRows()-1; row>=0; row-- ){
				for( int column=0; column<map.getColumns(); column++ ){
					WumpusMapObject mapObject = map.getWumpusMapObject(row, column);
					if( mapObject == null ){
						mapObject = new WumpusMapObject(row, column);
					}
					panel.add( new MapObject(editor, map, mapObject) );
				}
			}			
		}
		
		// redraw gui map panel
		panel.repaint();
		panel.validate();
	}
	
}
