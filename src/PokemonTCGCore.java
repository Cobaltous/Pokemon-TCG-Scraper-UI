import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//https://www.pikawiz.com/cards

public class PokemonTCGCore	{
	
	PokemonDBScraper dbScraper;
	PikawizScraper pikaScraper;
	
	NationalDex natDex;
	
	ArrayDeque<Pokemon> faves;
	
	
	public PokemonTCGCore() {
		
		faves = new ArrayDeque<Pokemon>();
		
	}
	
	public void pullData(String outFolder, boolean manualRefresh) {
		dbScraper = new PokemonDBScraper(outFolder);
		natDex = dbScraper.getDex(manualRefresh);
		
		pikaScraper = new PikawizScraper(outFolder);
		pikaScraper.getDex(manualRefresh, natDex);
		
	}

	
	String readFavesFromJSON(String faveString) {
//		System.out.println("String received: " + faveString);
		StringBuilder sb = new StringBuilder();
		try {
			JSONObject obj = new JSONObject(faveString);
//			System.out.println("Object successfully converted");
		    
		    JSONArray faveArr = obj.getJSONArray("favorites");
		    
		    ArrayDeque<Pokemon> tempFaves = new ArrayDeque<Pokemon>();
		    
//		    int rank = 1;
		    Pokemon mon;
		    for(Object i : faveArr) {
		    	int dexNo = Integer.parseInt((String)i);
		    	
		    	mon = natDex.getMon(dexNo);
		    	
		    	tempFaves.add(mon);

//		    	sb.append(String.format("Rank %d: %s\n", rank++, mon.getName()));
		    	sb.append(String.format("%s\n", mon.getName()));
		    	
		    }
		    
			faves = tempFaves;
		    
//			System.out.println("New faves parsed: " + faves.size());
		}
		catch (JSONException e){
			JOptionPane.showMessageDialog(null, "List given isn't in the right format (JSON)."
					+ "Have you tried directly copying your State from the Favorite Picker tool?\n"
					+ "You can just throw in the whole thing, it should be able to parse it.");
		}
		
//		return faves;
		return sb.toString();
	}
	

	void readFavesFromPlain(String faveString) {
		Scanner scan = new Scanner(faveString);
//		String line;
	    
		if(scan != null) {
		    ArrayDeque<Pokemon> tempFaves = new ArrayDeque<Pokemon>();
			
		    try {
				while(scan.hasNext()) {
//					line = scan.nextLine();
//					tempFaves.add(natDex.getMonFromDefiniteString(line.substring(line.indexOf(": ") + 2)));
					tempFaves.add(natDex.getMonFromDefiniteString(scan.nextLine()));
				}
				
				faves = tempFaves;
//				System.out.println("New faves parsed: " + faves.size());
		    	
		    }
		    catch(NullPointerException e) {
				JOptionPane.showMessageDialog(null, "List hasn't been structured properly. Make sure to place each Pokemon on "
						+ " their own lines and to spell their names correctly. Otherwise, use the Favorite Picker output.");
		    }
		}
		scan.close();
	}
	
	int splitMonAndForm(String faveMon) {
//		faves = new ArrayDeque<Pokemon>();
		for(int i = 0; i < faveMon.length(); ++i) {
			if(!Character.isDigit(faveMon.charAt(i))) {
				return i;
			}
		}
		return faveMon.length();
//		return natDex.get(Integer.parseInt(faveMon.substring(0, formStart)));
//		faves.add(mon);
//		return mon;
	}
	
	double floorRelToZero(double d) {
		if(d < 0) {
			return Math.ceil(d);
		}
		return Math.floor(d);
	}
	
	Pokemon get(int index) {
		return natDex.getMon(index);
	}
	
//	int getCandidatesLeft() {
//		return candidatesLeft;
//	}
//	
//	String[] getDexArray() {
//		String[] dexNames = new String[natDex.getDexSize()];
//		Pokemon mon;
//		for(int i = 0; i < natDex.getDexSize(); ++i) {
//			mon = natDex.get(i);
//			dexNames[i] = mon.getName();
//		}
//		return dexNames;
//	}
	
	
	NationalDex getDex() {
		return natDex;
	}
	
	int getDexSize() {
		return natDex.getNatDexSize();
	}
	
	String getBestSetsForFavorites() {
		StringBuilder sb;
		natDex.refreshSetWeights();
		CardSet curr;
		HashMap<String, String> cardsInSets = new HashMap<String, String>();
		HashMap<String, CardSet> usedSets = new HashMap<String, CardSet>();
		
		double largest = 0,
			faveWeight,
			setWeight;
		int i = faves.size() + 1;
		for(Pokemon fave : faves) {
//			faveWeight = (startingPortion / 2) + (startingPortion / 2 / i);
//			System.out.println("i: " + i);
			faveWeight = --i / (double)faves.size();
//			faveWeightLeft -= faveWeight;
//			System.out.println("faveWeight: " + faveWeight);
			
			

			for(Card card : fave.getCardAppearances()) {
				curr = natDex.getSet(card.getSetName());

//				curr = usedSets.get(card.getSetName()) == null ? natDex.getSet(card.getSetName()) : usedSets.get(card.getSetName());
				
//				System.out.println("null> : " + (cardsInSets.get(card.getSetName()) == null));
//				sb = new StringBuilder(String.format("%s    %s %s%s\n",
//						(cardsInSets.get(card.getSetName()) == null) ? "" : cardsInSets.get(card.getSetName()),
//						card.getNum(),
//						card.getName(),
//						card.getPrice() != 0 ? (": $" + card.getPrice()) : ""));
//				sb = new StringBuilder((cardsInSets.get(card.getSetName()) == null) ? "" : cardsInSets.get(card.getSetName()));
//				sb.append("    ");
//				sb.append(card.getNum());
//				sb.append(' ');
//				sb.append(card.getName());
//				
//				if(card.getPrice() != 0) {
//					sb.append(": $");
//					sb.append(card.getPrice());
//				}
				
//				sb.append('\n');
				
				cardsInSets.put(card.getSetName(), String.format("%s    %s %s%s\n",
						(cardsInSets.get(card.getSetName()) == null) ? "" : cardsInSets.get(card.getSetName()),
						card.getNum(),
						card.getName(),
						card.getPrice() != 0 ? (": $" + card.getPrice()) : ""));
				curr.addSetWeight(faveWeight);
				
				setWeight = curr.getSetWeight();
				if(largest < setWeight) {
					largest = setWeight;
				}
				usedSets.put(card.getSetName(), curr);
			}
		}
		
		
		
		sb = new StringBuilder();
		CardSet[] finalSets = (CardSet[])usedSets.values().toArray(new CardSet[usedSets.size()]);
		Arrays.sort(finalSets, new CardSetWeightComparator());
		
		for(CardSet set : finalSets) {
//			System.out.println(set.setName + " weight: " + set.getSetWeight() + " / " + largest);
			sb.append(String.format("%.2f%%: %s - %s\n%s\n",
//					df.format(set.getSetWeight() / largest * 100),
					set.getSetWeight() / largest * 100,
					set.getName(),
					natDex.getSet(set.getName()).getReleaseDate(),
					cardsInSets.get(set.getName())));
//			sb.append(df.format(set.getSetWeight() / largest * 100));
//			sb.append("%: ");
//			sb.append(set.getName());
//			sb.append(" - ");
//			sb.append(natDex.getSet(set.getName()).getReleaseDate());
//			sb.append('\n');
//			sb.append(cardsInSets.get(set.getName()));
//			sb.append('\n');
			
			
			
			
//			System.out.println("faveWeightLeft: " + faveWeightLeft);
//			for(String setName : fave.setAppearances) {
//				cardSet = natDex.getSetDex().get(setName);
//				hits = hitsPerSet.get(cardSet);
//				if(hits == -1) {
//					hitsPerSet.put(cardSet, 1);
//				}
//				else {
//					hitsPerSet.put(cardSet, ++hits);
//				}
//			}
//			++i;
		}
		
//		for(CardSet hitSet : hitsPerSet.keySet()) {
//			for(Pokemon fave : faves) {
//				
//			}
//		}
		return sb.toString();
	}
	
	String getBestSetsForAvgCardPrice() {
//		HashMap<CardSet, Double> avgCardValues = new HashMap<CardSet, Double>();
		int hadPriceData, setTotal;
		for(CardSet set : natDex.getSetDex().values()) {
			hadPriceData = 0;
			setTotal = 0;
			for(Card card : set.getCardSet()) {
				if(card.getPrice() > 0) { 
					++hadPriceData;
					setTotal += card.getPrice();
				}
			}
			set.setAvgCardPrice(setTotal / (double)hadPriceData);
		}
		CardSet[] finalSets = (CardSet[])natDex.getSetDex().values().toArray(new CardSet[natDex.getSetDexSize()]);
		Arrays.sort(finalSets, new CardSetAvgCardPriceComparator());
		StringBuilder sb = new StringBuilder();
		for(CardSet set : finalSets) {
			sb.append(String.format("%s - %s\n\t%.2f\n",
					set.getName(),
					set.getReleaseDate(),
					set.getAvgCardPrice()));
//			sb.append(set.getName());
//			sb.append(" - ");
//			sb.append(set.getReleaseDate());
//			sb.append("\n\t");
//			sb.append
		}
		return sb.toString();
	}
}

class CardSetWeightComparator implements Comparator<CardSet> {

	@Override
	public int compare(CardSet set1, CardSet set2) {
		return - Double.compare(set1.getSetWeight(), set2.getSetWeight());
	}
	
}

class CardSetAvgCardPriceComparator implements Comparator<CardSet> {

	@Override
	public int compare(CardSet set1, CardSet set2) {
		return - Double.compare(set1.getAvgCardPrice(), set2.getAvgCardPrice());
	}
	
}