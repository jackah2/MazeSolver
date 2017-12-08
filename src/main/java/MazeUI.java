import java.awt.EventQueue;
import java.awt.Font;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class MazeUI extends JFrame {

	private static final long serialVersionUID = 9060988509795832926L;
	
	private JPanel contentPane;
	
	private JTextField xDimField;
	private JTextField yDimField;
	
	private JLabel xDimLabel;
	private JLabel yDimLabel;
	
	private JButton generateButton;
	private JRadioButton huggingRadioButton;
	private JRadioButton recursiveRadioButton;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					setDefaultSize(24);
					
					MazeUI frame = new MazeUI();
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
	public MazeUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		generateButton = new JButton("Generate");
		generateButton.setEnabled(false);
		generateButton.setBounds(257, 21, 146, 187);
		contentPane.add(generateButton);
		
		recursiveRadioButton = new JRadioButton("Recursive");
		recursiveRadioButton.setBounds(0, 194, 201, 35);
		contentPane.add(recursiveRadioButton);
		
		huggingRadioButton = new JRadioButton("Hugging Wall");
		huggingRadioButton.setSelected(true);
		huggingRadioButton.setBounds(0, 159, 201, 35);
		contentPane.add(huggingRadioButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(recursiveRadioButton);
		group.add(huggingRadioButton);
		
		xDimLabel = new JLabel("X:");
		xDimLabel.setBounds(10, 10, 30, 26);
		contentPane.add(xDimLabel);
		
		yDimLabel = new JLabel("Y:");
		yDimLabel.setBounds(10, 57, 30, 26);
		contentPane.add(yDimLabel);
		
		xDimField = new JTextField();
		xDimField.setBounds(40, 7, 186, 32);
		contentPane.add(xDimField);
		xDimField.setColumns(10);
		
		yDimField = new JTextField();
		yDimField.setBounds(40, 54, 186, 32);
		contentPane.add(yDimField);
		yDimField.setColumns(10);
	}
	
	public static void setDefaultSize(int size) {
		Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
		Object[] keys = keySet.toArray(new Object[keySet.size()]);

		for (Object key : keys) {
			if (key != null && key.toString().toLowerCase().contains("font")) {
				System.out.println(key);
				Font font = UIManager.getDefaults().getFont(key);
				if (font != null) {
					font = font.deriveFont((float)size);
					UIManager.put(key, font);
				}
			}
		}
	}
}
