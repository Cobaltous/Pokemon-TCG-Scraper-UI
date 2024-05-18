import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/*
	This gets all the necessary information that Squirdle uses
	when being played and, therefore, the information needed
	to solve it by scraping two pages from PokemonDB.
*/
public class PokemonDBScraper {	
	
	/*
		NationalDex object which is returned back to the solver
		once filled.
	*/
	NationalDex natDex;

	/*
		The URLs for the two pages that are scraped in order to
		get the information Squirdle uses when played. Used and
		explained some more above getDex().
	*/
	String statURL = "https://pokemondb.net/pokedex/stats/height-weight",
			pageBackupFN = "PokemonDB Dex Page.html",
			outFolder;
	
	FileWriter dbWriter;

	int pageTimeout = 10000;
	long stalenessLimit = (long)(3600000 * 24 * 7);
	
//	ArrayDeque<Pokemon> monList;

	/*
		Empty constructor.
	*/
	public PokemonDBScraper(String outFolder) {
		this.outFolder = outFolder;
//		monList = new ArrayDeque<Pokemon>();
	}

	/*
		This method creates a new NationalDex object and fills it
		with data scraped from two pages of PokemonDB, which list
		the generations each Pokemon belong to and their characteristics,
		respectively. It then returns the NationalDex object once filled.
	*/
	NationalDex getDex(boolean manualRefresh) {
		natDex = new NationalDex();
		
		
//		HashMap<Integer, ArrayList<Pokemon>> mons = new HashMap<Integer, ArrayList<Pokemon>>();
		
		
		//Reminder that forms are now stored in the Pokemon class itself. Regulars should be "None".
		//Basculegion will also be a problem, as will Dark Pokemon - fuck.
//		ArrayList<Pokemon> formList;
		

		try {
			
			Document page = new Document("");
			
			File folder = new File(outFolder);
			
			if(!folder.exists()) {
				folder.mkdir();
				System.out.println("Created folder to store HTML page backups.");
			}
			
			File pageBackup = new File(outFolder + pageBackupFN);
			
			boolean shouldRefresh = manualRefresh;
			
			if(!shouldRefresh) {
				if(pageBackup.exists()) {
					if(pageBackup.lastModified() > stalenessLimit) {
						page = Jsoup.parse(pageBackup);
						System.out.println("PokemonDB page backup loaded");
						
						if(page.body().html().substring(0, 3).equals("404")) {
							throw new FailedBackupReadException();
						}
					}
					else {
						System.out.println("Page backup file appears to be stale and potentially out of date.");
						shouldRefresh = true;
					}
	//				dbReader = new FileReader(pageBackup);
				}
				else {
					System.out.println("Page backup file doesn't exist.");
					shouldRefresh = true;
				}
			}
			
			
			if(shouldRefresh) {
				System.out.println("Grabbing new version of page...");
				page = Jsoup.parse(new URL(statURL), pageTimeout);
				
				if(page.body().html().substring(0, 3).equals("404")) {
					throw new HTML404Exception();
				}
				
				dbWriter = new FileWriter(pageBackup);
				dbWriter.write(page.toString());
				dbWriter.flush();
				dbWriter.close();
				System.out.println("PokemonDB page refreshed");
			}
			
			
			
			
			
			Element monE, nameField, table = page.selectFirst("tbody");
//			Elements types;
			
			Pokemon curr;
			
//			int dexNo = 0, gen = 0;
			int dexNo = 0;
//			double height = 0, weight = 0;
//			String name = "", type1 = "", type2, typeCombo, form;
			String name = "", form;
			for(int i = 0; i < table.childrenSize(); ++i) {
//				type2 = "None";
				form = "";
				monE = table.child(i);
//				gen = -1;
				try {
					dexNo = Integer.parseInt(monE.child(0).select("span").get(0).html());
					nameField = monE.child(1).selectFirst("a");
					name = nameField.html();
					if(monE.child(1).selectFirst("small") != null) {
						form = monE.child(1).selectFirst("small").html();
					}
					
//					height = Double.parseDouble(monE.child(4).html());
//					weight = Double.parseDouble(monE.child(6).html());
					
//					types = monE.child(2).select("a");
//					type1 = types.get(0).html();
					
//					if(types.size() > 1) {
//						type2 = types.get(1).html();
//					}

//					System.out.println("current mon: " + name);
//					System.out.println("form: " + form);
					curr = natDex.getMon(dexNo);
					if(curr == null) {
						curr = new Pokemon(name, "None");
					}
					if(form != null) {
//						curr.addForm(form);
						curr.addForm(form);
						
					}
					
//					formList = mons.get(dexNo);
//					if(formList == null) {
//						formList = new ArrayList<Pokemon>();
//					}
//					
//					formList.add(curr);
//					mons.put(dexNo, formList);
					//This is down here so I can use its size from 0-max as indices
					//without having to keep track of it in a separate variable
					
//					monList.add(curr);
					natDex.putMon(dexNo, curr);
					
				}
				//This is here to catch any mons that have been officialy revealed but have not
				//made game appearances and therefore not been given Pokedex numbers.
				catch(NumberFormatException e) {
					continue;
				}
				
//				natDex.setMonList(monList);
			}
		}
		
		catch(FailedBackupReadException e) {
			JOptionPane.showMessageDialog(null, "Your backup file for " + statURL + " seems to be invalid. Delete it or force a refresh and try again.");
		}
		
		catch(HTML404Exception e) {
			JOptionPane.showMessageDialog(null, "PokemonDB threw a 404 error in loading " + statURL + ". Cool your jets and try again in a while, they probably rate limited you.");
		}
		
		catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not load backup file of  (" + statURL + "). Try pulling a fresh version by unchecking that box.");
			
		}
		
		catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Could not load stat wiki page to start scraping (" + statURL + "). Check your internet connection?");
		}
		
		return natDex;
	}


	/*
		I had to write this method to more manually assign gens to Pokemon with
		alternate forms, like those with Mega Evolutions and regional variants.
		I don't know how I'd do this via another HTML retrieval and I will have
		to manually update this when another game releases regional forms,
		which sucks big time.
	*/
	int getFormGen(String form) {
		String[] bits = form.split(" ");
		String formMaybe = bits[0];
		switch(formMaybe) {
			case "Mega" :
				return 6;
				
			case "Primal" :
				return 6;
				
			case "Alolan":
				return 7;
			
			case "Galarian":
				return 8;
				
			case "Hisuian":
				return 8;
				
			case "Paldean":
				return 9;
		}
		
		if(bits.length > 1) {
			formMaybe = bits[1];
			switch(formMaybe) {
				case "Cloak":
					return 4;
					
				case "Necrozma":
					return 7;
			
				case "Breed":
					return 9;
				}
		}
		
		switch(form) {
			case "Bloodmoon":
				return 9;
		}
		
		return -1;
	}

}
