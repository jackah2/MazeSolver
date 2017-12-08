import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Objects;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

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

	private int xDim = -1, yDim = -1;
	private boolean validCoords = false;
	private MazeSolver solver = null;

	private ActionListener generateListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!validCoords) return;
			solver = new MazeSolver(xDim, yDim, getSolveType());
			solver.displayMaze();

			xDimField.setEnabled(false);
			yDimField.setEnabled(false);

			// Stops listening as a generate button and turns into a solve button
			generateButton.removeActionListener(this);
			generateButton.addActionListener(solveListener);
			generateButton.setText("Solve");
		}
	};

	private ActionListener solveListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			MazeSolver.SolveType type = getSolveType();
			solver.setSolveType(type);
			
			solver.displaySolve();

			// Stops listening as a solve button and turns into a reset button
			generateButton.removeActionListener(this);
			generateButton.addActionListener(resetListener);
			generateButton.setText("Reset");
		}
	};

	private ActionListener resetListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			solver.resetMaze();

			// Stops listening as a reset button and turns into a solve button
			generateButton.removeActionListener(this);
			generateButton.addActionListener(solveListener);
			generateButton.setText("Solve");
		}
	};

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
					frame.setAlwaysOnTop(true);

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
		generateButton.setToolTipText("Set the X and Y dimensions of the maze first!");
		contentPane.add(generateButton);
		
		generateButton.addActionListener(generateListener);
		

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

		ChangeListener dimListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateGenerateButton();
			}
		};

		addChangeListener(xDimField, dimListener);
		addChangeListener(yDimField, dimListener);
	}

	/**
	 * Updates the generate button to be enabled or disabled according to the X and Y values.
	 */
	private void updateGenerateButton() {

		try {
			xDim = Integer.valueOf(xDimField.getText());
			yDim = Integer.valueOf(yDimField.getText());
		} catch (NumberFormatException exc) {
			generateButton.setEnabled(false);
			validCoords = false;
			return;
		}

		if (xDim == -1 || yDim == -1) {
			generateButton.setEnabled(false);
			validCoords = false;
			return;
		}

		generateButton.setEnabled(true);
		validCoords = true;
	}
	
	private MazeSolver.SolveType getSolveType() {
		if (huggingRadioButton.isSelected()) return MazeSolver.SolveType.HUG_LEFT;
		if (recursiveRadioButton.isSelected()) return MazeSolver.SolveType.RECURSIVE;
		return null;
	}

	public static void setDefaultSize(int size) {
		Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
		Object[] keys = keySet.toArray(new Object[keySet.size()]);

		for (Object key : keys) {
			if (key != null && key.toString().toLowerCase().contains("font")) {
				Font font = UIManager.getDefaults().getFont(key);
				if (font != null) {
					font = font.deriveFont((float)size);
					UIManager.put(key, font);
				}
			}
		}
	}

	public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
		Objects.requireNonNull(text);
		Objects.requireNonNull(changeListener);
		DocumentListener dl = new DocumentListener() {
			private int lastChange = 0, lastNotifiedChange = 0;

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				lastChange++;
				SwingUtilities.invokeLater(() -> {
					if (lastNotifiedChange != lastChange) {
						lastNotifiedChange = lastChange;
						changeListener.stateChanged(new ChangeEvent(text));
					}
				});
			}
		};
		text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
			Document d1 = (Document)e.getOldValue();
			Document d2 = (Document)e.getNewValue();
			if (d1 != null) d1.removeDocumentListener(dl);
			if (d2 != null) d2.addDocumentListener(dl);
			dl.changedUpdate(null);
		});
		Document d = text.getDocument();
		if (d != null) d.addDocumentListener(dl);
	}
}
