package de.northernstars.jwumpus.examples;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.northernstars.jwumpus.gui.FrameLoadedListener;
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

@SuppressWarnings("serial")
public class HumanInterface extends JFrame {
	
	private JPanel contentPane;
	public JLabel lblLastActionSuccess;
	public JLabel lblNewLabel_1;
	public JPanel panel;
	public JProgressBar pgbRemainingTime;
	public JLabel lblRemainingTime;
	public JButton btnUp;
	public JButton btnDown;
	public JButton btnLeft;
	public JButton btnRight;
	public JLabel lblNewLabel;
	public JLabel lblPlayerState;
	public JPanel panelMap;

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
		setTitle("Human Interface");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 465, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
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
				RowSpec.decode("8dlu"),
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		lblNewLabel_1 = new JLabel("Last action success:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel_1, "2, 2");
		
		lblLastActionSuccess = new JLabel("");
		contentPane.add(lblLastActionSuccess, "4, 2");
		
		lblNewLabel = new JLabel("Player state:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel, "2, 4");
		
		lblPlayerState = new JLabel("UNKNOWN");
		contentPane.add(lblPlayerState, "4, 4");
		
		lblRemainingTime = new JLabel("Remaining time:");
		lblRemainingTime.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblRemainingTime, "2, 6");
		
		pgbRemainingTime = new JProgressBar();
		pgbRemainingTime.setMaximum(5000);
		contentPane.add(pgbRemainingTime, "4, 6");
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Next action", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, "2, 8, 3, 1, fill, fill");
		panel.setLayout(new GridLayout(1, 1, 0, 0));
		
		btnUp = new JButton("Up");
		panel.add(btnUp);
		
		btnDown = new JButton("Down");
		panel.add(btnDown);
		
		btnLeft = new JButton("Left");
		panel.add(btnLeft);
		
		btnRight = new JButton("Right");
		panel.add(btnRight);
		
		panelMap = new JPanel();
		contentPane.add(panelMap, "2, 10, 3, 1, fill, fill");
		
		// call listener
		if( listener != null ){
			listener.frameLoaded(this, this.getClass());
		}
	}

}
