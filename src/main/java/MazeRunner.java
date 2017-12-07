import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MazeRunner extends JPanel implements ActionListener{

	private JButton actionButton;
	private JTextField xDimField, yDimField;
	private JLabel xDimLabel, yDimLabel;

	private static final String ACTION = "action";

	public MazeRunner() {
		actionButton = new JButton("Generate");
		actionButton.setEnabled(false);
		actionButton.setHorizontalTextPosition(AbstractButton.CENTER);
		actionButton.setVerticalTextPosition(AbstractButton.CENTER);
		actionButton.setActionCommand(ACTION);
		actionButton.setSize(600,200);

		xDimLabel = new JLabel("X: ");
		xDimField = new JTextField("   ");

		yDimLabel = new JLabel("Y: ");
		yDimField = new JTextField("   ");

		add(actionButton);
		add(xDimLabel);
		add(xDimField);
		add(yDimLabel);
		add(yDimField);
	}


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException
						| InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}

				setDefaultSize(30);

				createAndDisplayUI();
			}
		});
	}

	private static void createAndDisplayUI() {
		JFrame frame = new JFrame("Maze Solver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MazeRunner runnerPane = new MazeRunner();
		runnerPane.setOpaque(true);
		frame.setContentPane(runnerPane);

		frame.pack();
		frame.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public static void test() {
		
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
