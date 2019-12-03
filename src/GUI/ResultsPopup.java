package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import MainLogic.DataProcesser;
import Utils.FileUtils;

public class ResultsPopup extends JFrame {

	private static final long serialVersionUID = 1L;

	private final int WIDTH = 500, HEIGHT = 105;

	private String rule;

	private int dci, dii, adci, adii;

	private ArrayList<Integer> methods;

	public ResultsPopup(String rule, int dci, int dii, int adci, int adii, ArrayList<Boolean> results) {
		this.rule = rule;
		this.dci = dci;
		this.dii = dii;
		this.adci = adci;
		this.adii = adii;

		if (results != null)
			methods = getMethods(results);

		initWindow();
	}

	private void initWindow() {
		setTitle("Resultados (" + rule + ")");
		setResizable(false);

		Font f = new Font("Arial", Font.PLAIN, 16);

		MainPanel mainPanel = new MainPanel(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		JPanel topPanel, botPanel;

		topPanel = new JPanel(new BorderLayout());
		botPanel = new JPanel(new BorderLayout());

		topPanel.setOpaque(false);
		botPanel.setOpaque(false);

		JTextArea qualityDisplay = new JTextArea();
		qualityDisplay.setEditable(false);
		qualityDisplay.setOpaque(false);

		qualityDisplay.setBorder(new EmptyBorder(10, 10, 10, 10));
		qualityDisplay.setForeground(Color.WHITE);
		/* ============== */

		qualityDisplay.setFont(f);

		int total = dci + dii + adci + adii;
		String quality = "DCI: " + dci + " || DII: " + dii + " || ADCI: " + adci + " || ADII: " + adii + "\nTotal: "
				+ total;
		qualityDisplay.setText(quality);

		if (methods != null) {
			Button showMethods = new Button("Dete��o de Defeitos");
			showMethods.setPreferredSize(new Dimension(200, 40));

			showMethods.addActionListener((e) -> showDefects());
			showMethods.setFont(f);

			botPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

			botPanel.add(showMethods, BorderLayout.EAST);
			mainPanel.add(botPanel, BorderLayout.SOUTH);
		}

		topPanel.add(qualityDisplay, BorderLayout.CENTER);

		mainPanel.add(topPanel, BorderLayout.CENTER);

		setContentPane(mainPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private ArrayList<Integer> getMethods(ArrayList<Boolean> results) {
		ArrayList<Integer> methodIds = new ArrayList<>();

		int i = 0;
		while (i != results.size()) {
			if (results.get(i))
				methodIds.add(i + 1);

			i++;
		}

		return methodIds;
	}

	private void showDefects() {
		JFrame frame = new JFrame();
		frame.setTitle("M�todos Detectados - (" + rule + ")");
		frame.setResizable(false);

		MainPanel mainPanel = new MainPanel(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT + 100));

		DefaultListModel<String> res = new DefaultListModel<>();
		JList<String> results = new JList<>(res);
		JScrollPane scroll = new JScrollPane(results);

		results.setFont(new Font("Arial", Font.PLAIN, 16));
		int indexOfMethods = FileUtils.getCellIndexByText("method");

		for (int x : methods) {
			String method = DataProcesser.getInstance().getCurrentSheet().getRow(x).getCell(indexOfMethods)
					.getStringCellValue().split("\\(")[0];

			res.addElement("ID: " + x + " - " + method + "(...)");
		}

		mainPanel.add(scroll);

		frame.setContentPane(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
