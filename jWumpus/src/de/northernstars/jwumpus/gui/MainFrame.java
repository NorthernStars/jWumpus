package de.northernstars.jwumpus.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtSetpDelay;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/de/northernstars/jwumpus/gui/img/wumpus.png")));
		setTitle("jWumpus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 380);
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JPanel panelFieldOriginal = new JPanel();
		panelFieldOriginal.setBorder(new TitledBorder(null, "Complete map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelFieldOriginal, "2, 2, 1, 7, fill, fill");
		panelFieldOriginal.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panelAiMap = new JPanel();
		panelAiMap.setBorder(new TitledBorder(null, "AI map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelAiMap, "4, 2, 1, 7, fill, fill");
		panelAiMap.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panelMap = new JPanel();
		panelMap.setBorder(new TitledBorder(null, "Map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelMap, "6, 2, fill, fill");
		panelMap.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setMnemonic('l');
		panelMap.add(btnLoad, "2, 2");
		
		JButton btnEditor = new JButton("Editor");
		btnEditor.setMnemonic('e');
		panelMap.add(btnEditor, "4, 2");
		
		JPanel panelAutomaticControl = new JPanel();
		panelAutomaticControl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Automatic control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelAutomaticControl, "6, 4, fill, fill");
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
		
		JButton btnStart = new JButton("Start");
		panelAutomaticControl.add(btnStart, "2, 2");
		
		JButton btnReset = new JButton("Reset");
		btnReset.setMnemonic('r');
		panelAutomaticControl.add(btnReset, "4, 2");
		
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
		panelManualControl.setBorder(new TitledBorder(null, "Manual control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelManualControl, "6, 6, fill, fill");
		panelManualControl.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton btnBack = new JButton("Back");
		btnBack.setMnemonic('b');
		panelManualControl.add(btnBack, "2, 2");
		
		JButton btnNext = new JButton("Next");
		btnNext.setMnemonic('n');
		panelManualControl.add(btnNext, "4, 2");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, "6, 8, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
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
		
		JLabel lblStatus = new JLabel("unknown");
		panel.add(lblStatus, "4, 2");
		
		JLabel lblNewLabel1 = new JLabel("Current step:");
		lblNewLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel1, "2, 4");
		
		JLabel lblCurrentStep = new JLabel("0");
		panel.add(lblCurrentStep, "4, 4");
	}
}
