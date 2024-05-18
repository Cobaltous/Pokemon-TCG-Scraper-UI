import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
	This gets all the necessary information that Squirdle uses
	when being played and, therefore, the information needed
	to solve it by scraping two pages from PokemonDB.
*/
public class PikawizScraper {	
	
	/*
		NationalDex object which is returned back to the solver
		once filled.
	*/

	/*
		The URLs for the two pages that are scraped in order to
		get the information Squirdle uses when played. Used and
		explained some more above getDex().
	*/
	String cardSetURL = "https://www.pikawiz.com/cards",
		pageBackupFN = "Pikawiz Card Set Page.html",
		cardSetFolder = "Card Set Pages" + File.separator,
		outFolder, currentURL;
	
	NationalDex natDex;
	
	FileWriter wizWriter;
	
	int pageTimeout = 10000;
	long stalenessLimit = (long)(3600000 * 24);
	

	/*
		Empty constructor.
	*/
	public PikawizScraper(String outFolder) {
		this.outFolder = outFolder;
	}

	/*
		This method creates a new NationalDex object and fills it
		with data scraped from two pages of PokemonDB, which list
		the generations each Pokemon belong to and their characteristics,
		respectively. It then returns the NationalDex object once filled.
	*/
	void getDex(boolean manualRefresh, NationalDex natDex) {		
		String pageFN;

		try {
			currentURL = cardSetURL;
			
			Document page = new Document(""), subpage = new Document("");
			
			File mainFolder = new File(outFolder);
			
			if(!mainFolder.exists()) {
				mainFolder.mkdir();
				System.out.println("Created folder to store HTML page backups.");
			}
			
			File pageBackup = new File(outFolder + pageBackupFN);
			
			boolean shouldRefresh = manualRefresh;
			
			if(!shouldRefresh) {
				if(pageBackup.exists()) {
					if(pageBackup.lastModified() > stalenessLimit) {
						page = Jsoup.parse(pageBackup);
						
						if(page.body().text().substring(0, 3).equals("404")) {
							throw new FailedBackupReadException();
						}
						
						System.out.println("Pikawiz DB page backup loaded");
					}
					else {
						System.out.println("Page backup file appears to be stale and potentially out of date.");
						shouldRefresh = true;
					}
				}
				else {
					System.out.println("Page backup file doesn't exist.");
					shouldRefresh = true;
				}
			}
			
			
			if(shouldRefresh) {
				System.out.println("Grabbing new version of page...");
				System.out.println("Current URL: " + currentURL);
				

				JOptionPane.showMessageDialog(null, "Pulling live versions of all card sets. This may take a while and I don't feel like getting a progress bar to work right now.\n"
						+ "Just give it a minute to run, I swear it'll still be running in the background. If you want a live feed, open that folder where the backups will go.");
				
				page = Jsoup.parse(new URL(currentURL), pageTimeout);
				
				if(page.body().text().substring(0, 3).equals("404")) {
					throw new HTML404Exception();
				}
				
				wizWriter = new FileWriter(pageBackup);
				wizWriter.write(page.toString());
				wizWriter.flush();
				wizWriter.close();
				System.out.println("Pikawiz page refreshed");
			}
			
			File subFolder = new File(outFolder + cardSetFolder);
			
			if(!subFolder.exists()) {
				subFolder.mkdir();
				System.out.println("Created folder to store card set HTML backups.");
			}
			
			Elements sets = page.select("[class=\"card-set-container\"]");
			
//			System.out.println("number of set elements: " + sets.size());
			
			
			
			
//			ArrayList<URL> indSetURLs = new ArrayList<URL>();
			
//			JFrame frame = new JFrame("Card Set Progress");
//			JOptionPane pane = new JOptionPane(String.format("Set %d/%d", 0, sets.size()));
//			JProgressBar prog = new JProgressBar(0, sets.size());
//			pane.add(prog, 1);
//			JDialog dialog = pane.createDialog(frame, "asdf");
//			dialog.setVisible(true);
			
			String subpageBackupFN, setName, releaseDate, cardName, priceStr, cardNum;
			double nmPrice, totalCardPrice;
			ArrayList<Card> setContents;
			ArrayDeque<Pokemon> featuredMons;
			Card card;
			for(Element set : sets) {
				for(Element href : set.children()) {
					totalCardPrice = 0;
					
					setName = href.selectFirst("[class=\"set-name\"]").selectFirst("b").text();
					
					releaseDate = href.selectFirst("[class=\"set-release\"]").text();
					
					subpageBackupFN = setName + ".html";

					File subpageBackup = new File(outFolder + cardSetFolder + subpageBackupFN);
					
					shouldRefresh = manualRefresh;
										
					if(!shouldRefresh) {
						if(subpageBackup.exists()) {
							if(pageBackup.lastModified() > stalenessLimit) {
								subpage = Jsoup.parse(subpageBackup);
								System.out.println(setName + " backup loaded");
							}
							else {
								System.out.println(setName + " backup file appears to be stale and potentially out of date.");
								shouldRefresh = true;
							}
						}
						else {
							System.out.println(setName + " backup file doesn't exist.");
							shouldRefresh = true;
						}
					}
					
					if(shouldRefresh) {
						System.out.println("Grabbing new version of " + setName + "...");
						currentURL = cardSetURL + '/' + href.attr("href").split("/")[1];
						subpage = Jsoup.parse(new URL(currentURL), pageTimeout);
						pageFN = outFolder + cardSetFolder + setName + ".html";
						wizWriter = new FileWriter(new File(pageFN));
						wizWriter.write(subpage.toString());
						wizWriter.flush();
						wizWriter.close();
					}
					
					setContents = new ArrayList<Card>();
					featuredMons = new ArrayDeque<Pokemon>();

					
					//class^="" means to find classes that start with whatever's within the ""
					for(Element cardData : subpage.selectFirst("[class=\"card-list\"]").select("[class^=\"card-list-item-cards\"]")) {
						
						nmPrice = 0;
						
						cardName = cardData.selectFirst("[class=\"card-list-name-txt\"]").text();
						
						featuredMons = natDex.findFeaturedMons(cardName);
						
						cardNum = cardData.selectFirst("[class=\"card-list-num\"]").text();
						
						priceStr = cardData.selectFirst("[class=\"set-price-item\"]").text();
						
						if(!priceStr.equals("")) {
							nmPrice = Double.parseDouble(cardData.selectFirst("[class=\"set-price-item\"]").text().substring(1).replaceAll(",", ""));
						}
						
						totalCardPrice += nmPrice;
						
						if(featuredMons.size() != 0) {
							card = new PokemonCard(featuredMons, nmPrice, cardName, setName, cardNum);
							for(Pokemon mon : featuredMons) {
								if(!mon.isInSet(setName)) {
									mon.addCardSet(setName);									
								}
								mon.addCard(card);
							}
							setContents.add(card);
						}
						else {
							setContents.add(new OtherCard(nmPrice, cardName, setName, cardNum));
						}
					}
					
					natDex.putSet(new CardSet(setName, releaseDate, totalCardPrice, setContents));
//					pane.setMessage(String.format("Set %d/%d", 0, sets.size()));
//					prog.setValue(natDex.getSetDexSize());
				}
			}
//			dialog.dispose();
			JOptionPane.showMessageDialog(null, "Successfully loaded all card data!");
		}
		
		catch(FailedBackupReadException e) {
			JOptionPane.showMessageDialog(null, "Your backup file for " + currentURL + " seems to be invalid. Delete it or force a refresh and try again.");
		}
		
		catch(HTML404Exception e) {
			JOptionPane.showMessageDialog(null, "Pikawiz threw a 404 error in loading " + currentURL + ". Cool your jets and try again in a while, they probably rate limited you.");
		}
		
		catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not load backup file of " + currentURL + ". Try pulling a fresh version by checking that box.");
		}
		
		catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Could not load stat wiki page to start scraping " + currentURL + ". Check your internet connection?");
		}
	}

}