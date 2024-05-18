import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PokemonTCGUI {
	static PokemonTCGUI app;
	
	boolean dataImported = false,
		favesRead = false;
	
	String[] dropOps = {"Favorites", "Avg Card Price"},
		outPieces;
	
	PokemonTCGCore core;
	
	JFrame appFrame;
	
	FlowLayout flow;
	
	JPanel findFavesPanel,
		controlPanel,
		indivResPanel,
		findBestPanel,
		centerContainerPanel;
	
	JTextArea favesField,
		indivField,
		resField;
	
	JScrollPane favesPane,
		indivPane,
		resPanel;
	
	JButton pullDataButton,
		feedbackButton,
		refreshDataButton,
		outLocButton,
		JSONButton, plainButton;
	
	JCheckBox forceRefreshData;
	
	JComboBox<String> favesVsPrice,
		monSelector;
	
	JLabel bgLabel;

	JFileChooser outLocChooser;
	
	String JSONText = "Read Faves From JSON",
		plainText = "Read Faves From Plain",
		gbgText = "Find Best Packs",
		bgText = "Best Packs: ",
		outFolder = "TCG Page Backups" + File.separator,
		outLocText = "Backup Page Data Folder: ",
		refreshText = "Load Page Data",
		forceRefreshText = "Force refresh data?";
	
	
	
	public PokemonTCGUI() {
		core = new PokemonTCGCore();

		flow = new FlowLayout();
		
		appFrame = new JFrame();
		
		
		
//		gbgPanel = new JPanel();
		
		
//		gbgPanel.setBorder(BorderFactory.createBevelBorder(0));
		
		JSONButton = new JButton(JSONText);
		JSONButton.setEnabled(false);
		JSONButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String faveText = core.readFavesFromJSON(favesField.getText());		
				if(!faveText.equals("")) {
					favesField.setText(faveText);
//					System.out.println("favesRead: " + favesRead);
					favesField.setCaretPosition(0);
//					pullDataButton.setEnabled(false);
					favesRead = true;
					if(favesVsPrice.getSelectedIndex() == 0) {
						resField.setText(core.getBestSetsForFavorites());
						resField.setCaretPosition(0);
					}
//					System.out.println("selected item: " + ((String)favesVsPrice.getSelectedItem()));
//					if(((String)favesVsPrice.getSelectedItem()).equals(dropOps[0])) {
//						pullDataButton.setEnabled(favesRead);
//					}
//					System.out.println("favesRead: " + favesRead);
				}
			}
		});
		
		plainButton = new JButton(plainText);
		plainButton.setEnabled(false);
		plainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				core.readFavesFromPlain(favesField.getText());		
				if(!favesField.getText().equals("")) {
//					System.out.println("favesRead: " + favesRead);
					favesField.setCaretPosition(0);
//					pullDataButton.setEnabled(false);
					favesRead = true;
					if(favesVsPrice.getSelectedIndex() == 0) {
						resField.setText(core.getBestSetsForFavorites());
						resField.setCaretPosition(0);
					}
//					System.out.println("selected item: " + ((String)favesVsPrice.getSelectedItem()));
//					if(((String)favesVsPrice.getSelectedItem()).equals(dropOps[0])) {
//						pullDataButton.setEnabled(favesRead);
//					}
//					System.out.println("favesRead: " + favesRead);
				}
			}
		});
		
//		pullDataButton = new JButton(gbgText);
//		pullDataButton.setEnabled(false);
//		pullDataButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				switch(favesVsPrice.getSelectedIndex()) {
//					case 0:
//						if(dataImported && favesRead) {
//							resField.setText(core.getBestSetsForFavorites());
//							resField.setCaretPosition(0);
//						}
//						else {
//							resField.setText("");
//						}
//						break;
//					
//					case 1:
//						if(dataImported) {
//							resField.setText(core.getBestSetsForAvgCardPrice());
//							resField.setCaretPosition(0);
//						}
//						else {
//							resField.setText("");
//						}
//						break;
//				}
//				
//				//asdfasdfasdfasdfasdf
//				
//				
//			}
//		});
		
		
		favesVsPrice = new JComboBox<String>(dropOps);
		favesVsPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("ind: " + favesVsPrice.getSelectedIndex());
				switch(favesVsPrice.getSelectedIndex()) {
					case 0:
						if(dataImported && favesRead) {
							resField.setText(core.getBestSetsForFavorites());
							resField.setCaretPosition(0);
						}
						else {
							resField.setText("");
						}
						break;
					
					case 1:
						if(dataImported) {
							resField.setText(core.getBestSetsForAvgCardPrice());
							resField.setCaretPosition(0);
						}
						else {
							resField.setText("");
						}
						break;
				}
			}
		});
		
		monSelector = new JComboBox<String>();
		monSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pokemon mon = core.getDex().getMonFromDefiniteString((String)monSelector.getSelectedItem());
				if(mon != null) {
					indivField.setText(mon.getCardSetStrings());
					indivField.setCaretPosition(0);
				}
			}
		});
		
		
		bgLabel = new JLabel(bgText);
		
		refreshDataButton = new JButton(refreshText);
		refreshDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				core.pullData(outFolder, forceRefreshData.isSelected());
				dataImported = true;
				JSONButton.setEnabled(true);
				plainButton.setEnabled(true);
//				monSelector = new JComboBox<String>();
				monSelector.removeAllItems();
				
				for(String s : core.getDex().getSortedMonArray()) {
					monSelector.addItem(s);
				}
			}
		});
		
		outLocChooser = new JFileChooser(new File("."));
		
		outLocButton = new JButton(outLocText + outFolder);
		
		outLocButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    	    outLocChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	    outLocChooser.setAcceptAllFileFilterUsed(false);
	    	    if(outLocChooser.showOpenDialog(outLocChooser) == JFileChooser.APPROVE_OPTION) {
		    		outFolder = outLocChooser.getSelectedFile().toString() + File.separator;
		    		
		    		outLocChooser.setCurrentDirectory(new File(outFolder));
		    		
		    		//I don't even remember what this is for.
		    		outPieces = outFolder.replace("\\", "\\\\").split("\\\\");
		    		outLocButton.setText(outLocText + outPieces[outPieces.length - 1]  + File.separator);	    	    	
	    	    }
	    	}
			
		});
		
		forceRefreshData = new JCheckBox();
		forceRefreshData.setText(forceRefreshText);	
		
		
		
		favesField = new JTextArea(getTotallyNotTheDevsFavorites());
		favesField.setLineWrap(true);
		favesField.setWrapStyleWord(true);
		favesField.setTabSize(4);
		
		indivField = new JTextArea();
		indivField.setLineWrap(true);
		indivField.setWrapStyleWord(true);
		indivField.setTabSize(4);
		
		resField = new JTextArea();
		resField.setLineWrap(true);
		resField.setWrapStyleWord(true);
		resField.setTabSize(4);
		
		favesField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent ie) {
				JSONButton.setEnabled(true);
				favesRead = false;
			}
			
			@Override
			public void changedUpdate(DocumentEvent ce) {
				
			}
			
			@Override
			public void removeUpdate(DocumentEvent re) {
				
			}
		});
		

		findFavesPanel = new JPanel();
		controlPanel = new JPanel();
		indivResPanel = new JPanel();
		findBestPanel = new JPanel();
		centerContainerPanel = new JPanel();

		findFavesPanel.setBorder(BorderFactory.createBevelBorder(0));
		controlPanel.setBorder(BorderFactory.createBevelBorder(0));
		indivResPanel.setBorder(BorderFactory.createBevelBorder(0));
		findBestPanel.setBorder(BorderFactory.createBevelBorder(0));
		centerContainerPanel.setBorder(BorderFactory.createBevelBorder(0));
		
		
		
		favesPane = new JScrollPane(favesField);
		favesPane.setPreferredSize(new Dimension(150, 320));
		
		indivPane = new JScrollPane(indivField);
		indivPane.setPreferredSize(new Dimension(350, 268));
		
		resPanel = new JScrollPane(resField);
		resPanel.setPreferredSize(new Dimension(400, 350));

		
		
		findFavesPanel.setPreferredSize(new Dimension(200, 400));
		findFavesPanel.add(JSONButton, BorderLayout.NORTH);
		findFavesPanel.add(favesPane, BorderLayout.CENTER);
		findFavesPanel.add(plainButton, BorderLayout.SOUTH);
		
		controlPanel.setPreferredSize(new Dimension(500, 100));
		controlPanel.add(outLocButton, BorderLayout.NORTH);
		controlPanel.add(refreshDataButton, BorderLayout.WEST);
		controlPanel.add(forceRefreshData, BorderLayout.EAST);
//		controlPanel.add(pullDataButton, BorderLayout.CENTER);
		
		indivResPanel.setPreferredSize(new Dimension(500, 350));
		indivResPanel.add(monSelector);
		indivResPanel.add(indivPane);

		centerContainerPanel.setPreferredSize(new Dimension(500, 400));
		centerContainerPanel.add(controlPanel, BorderLayout.NORTH);
		centerContainerPanel.add(indivResPanel, BorderLayout.SOUTH);
		
		findBestPanel.setPreferredSize(new Dimension(450, 200));
		findBestPanel.add(favesVsPrice, BorderLayout.NORTH);
		findBestPanel.add(bgLabel, BorderLayout.NORTH);
		findBestPanel.add(resPanel, BorderLayout.SOUTH);
		
//		appFrame.add(gbgPanel, BorderLayout.NORTH);
		appFrame.add(findFavesPanel, BorderLayout.WEST);
		appFrame.add(centerContainerPanel, BorderLayout.CENTER);
//		appFrame.add(indivPane, BorderLayout.SOUTH);
		appFrame.add(findBestPanel, BorderLayout.EAST);
		
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.setTitle("TCG Favorite/Price Optimizer");
		
		appFrame.pack();
		appFrame.setVisible(true);
	}
	
	public String getTotallyNotTheDevsFavorites() {
		return "{favorites:[\"282\",\"359\",\"350\",\"549\",\"654\",\"123\",\"497\",\"389\",\"373\",\"471\"]}";
	}
	
	public static void main(String[] args) {
		app = new PokemonTCGUI();
	}
}