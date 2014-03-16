package de.northernstars.jwumpus.examples;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.northernstars.jwumpus.gui.Editor;
import de.northernstars.jwumpus.gui.listener.FrameLoadedListener;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class HumanInterface extends JFrame {
	
	public static String title = "Human Interface";
	
	private JPanel contentPane;
	public JPanel panelMap;
	public JProgressBar pgbRemainingTime;
	public JLabel lblPlayerState;
	public JLabel lblPlayerArrows;
	public JLabel lblLastActionSuccess;
	public JButton btnShootUp;
	public JButton btnShootDown;
	public JButton btnShootLeft;
	public JButton btnShootRight;
	public JButton btnMoveUp;
	public JButton btnMoveDown;
	public JButton btnMoveLeft;
	public JButton btnMoveRight;
	public JMenuBar menuBar;
	public JMenu mnFile;
	public JMenuItem mntmLoadMap;
	public JMenuItem mntmReset;
	public JMenuItem mntmShowEditor;
	public JMenuItem mntmClose;
	

	/**
	 * Launch the application.
	 */
	public static void showHumanINterface(FrameLoadedListener listener) {
		final FrameLoadedListener vListener = listener;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HumanInterface frame = new HumanInterface(vListener);
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
	public HumanInterface(FrameLoadedListener listener) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 500, 300);
		
		menuBar = new JMenuBar();
		menuBar.setVisible(false);
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnFile.setMnemonic('f');
		menuBar.add(mnFile);
		
		mntmLoadMap = new JMenuItem("Load map");
		mntmLoadMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mnFile.add(mntmLoadMap);
		
		mntmReset = new JMenuItem("Reset");
		mntmReset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mnFile.add(mntmReset);
		
		mntmShowEditor = new JMenuItem("Show editor");
		mntmShowEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Editor.showEditor(null);
			}
		});
		mntmShowEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmShowEditor);
		
		mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmClose);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
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
				RowSpec.decode("8dlu"),
				RowSpec.decode("85dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblNewLabel_1 = new JLabel("Last action success:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel_1, "2, 2");
		
		lblLastActionSuccess = new JLabel("");
		contentPane.add(lblLastActionSuccess, "4, 2");
		
		JLabel lblNewLabel = new JLabel("Player state:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel, "2, 4");
		
		lblPlayerState = new JLabel("UNKNOWN");
		contentPane.add(lblPlayerState, "4, 4");
		
		JLabel lblNewLabel_2 = new JLabel("Player arrows:");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel_2, "2, 6");
		
		lblPlayerArrows = new JLabel("0");
		contentPane.add(lblPlayerArrows, "4, 6");
		
		JLabel lblRemainingTime = new JLabel("Remaining time:");
		lblRemainingTime.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblRemainingTime, "2, 8");
		
		pgbRemainingTime = new JProgressBar();
		pgbRemainingTime.setMaximum(5000);
		contentPane.add(pgbRemainingTime, "4, 8");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Next action", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, "2, 10, 3, 1, fill, fill");
		panel.setLayout(new GridLayout(0, 4, 0, 5));
		
		btnMoveLeft = new JButton("Move left");
		btnMoveLeft.setIcon(new ImageIcon(HumanInterface.class.getResource("/de/northernstars/jwumpus/examples/img/left_grey.png")));
		panel.add(btnMoveLeft);
		
		btnMoveUp = new JButton("Move up");
		btnMoveUp.setIcon(new ImageIcon(HumanInterface.class.getResource("/de/northernstars/jwumpus/examples/img/up_grey.png")));
		panel.add(btnMoveUp);
		
		btnMoveDown = new JButton("Move down");
		btnMoveDown.setIcon(new ImageIcon(HumanInterface.class.getResource("/de/northernstars/jwumpus/examples/img/down_grey.png")));
		panel.add(btnMoveDown);
		
		btnMoveRight = new JButton("Move right");
		btnMoveRight.setIcon(new ImageIcon(HumanInterface.class.getResource("/de/northernstars/jwumpus/examples/img/right_grey.png")));
		panel.add(btnMoveRight);
		
		btnShootLeft = new JButton("Shoot left");
		panel.add(btnShootLeft);
		
		btnShootUp = new JButton("Shoot up");
		panel.add(btnShootUp);
		
		btnShootDown = new JButton("Shoot down");
		panel.add(btnShootDown);
		
		btnShootRight = new JButton("Shoot right");
		panel.add(btnShootRight);
		
		panelMap = new JPanel();
		contentPane.add(panelMap, "2, 12, 3, 1, fill, fill");
		
		// call listener
		if( listener != null ){
			listener.frameLoaded(this, this.getClass());
		}
	}

}
