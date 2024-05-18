import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NationalDex {

	/*
		An ArrayList that contains every single Pokemon in the
		order in which they were scraped from PDB. Never
		modified once the scraping is done and is used to
		rebuild the maps below for multiple games, the test
		programs I've written, and a myriad of minor calls below.
	*/
	HashMap<Integer, Pokemon> natDex;
	HashMap<String, CardSet> setDex;
	HashMap<String, Integer> str2DexNo;
	HashMap<String, ArrayList<String>> prefixes;
	
	/*
		Constructor that initializes the monList and all of the maps.
	*/
	NationalDex() {
		natDex = new HashMap<Integer, Pokemon>();
		setDex = new HashMap<String, CardSet>();
		str2DexNo = new HashMap<String, Integer>();
		prefixes = new HashMap<String, ArrayList<String>>();
	}

	/*
		Gets a Pokemon at the selected index from the monList.
	*/
	Pokemon getMon(int i) {
		return natDex.get(i);
	}
	
	void putMon(int i, Pokemon mon) {
		natDex.put(i, mon);
		str2DexNo.put(mon.getName(), i);
		int sep = mon.getName().indexOf(" ");
		if(sep != -1) {
			String prefix = mon.getName().substring(0, sep);
			ArrayList<String> prefixedMons = prefixes.get(prefix);
			if(prefixedMons == null) {
				prefixedMons = new ArrayList<String>();
			}
			prefixedMons.add(mon.name);
			prefixes.put(mon.getName().substring(0, mon.getName().indexOf(' ')), prefixedMons);
		}
	}
	
	void putSet(CardSet set) {
		setDex.put(set.getName(), set);
	}

	/*
		Returns the number of Pokemon contained in the natDex
		ArrayList.
	*/
	int getNatDexSize() {
		return natDex.size();
	}
	
	int getSetDexSize() {
		return setDex.size();
	}
	
	CardSet getSet(String setName) {
		return setDex.get(setName);
	}
	
	HashMap<Integer, Pokemon> getNatDex() {
		return natDex;
	}
	
	HashMap<String, CardSet> getSetDex() {
		return setDex;
	}
	
	ArrayDeque<Pokemon> findFeaturedMons(String cardName) {
		ArrayDeque<Pokemon> featuredMons = new ArrayDeque<Pokemon>();	
		int dexNo = -1;
		String[] pieces = cardName.split(" ");
		ArrayList<String> prefixedMons;
		for(int i = 0; i < pieces.length; ++i) {
			if(str2DexNo.get(pieces[i]) != null) {
				dexNo = str2DexNo.get(pieces[i]);
				featuredMons.add(natDex.get(dexNo));
			}
			if(i < pieces.length - 1) {
				prefixedMons = prefixes.get(pieces[i]);
				
				if(prefixedMons != null) {
					String monName = pieces[i] + ' ' + pieces[i + 1];
					if(prefixedMons.contains(monName)) {
						dexNo = str2DexNo.get(monName);
						featuredMons.add(natDex.get(dexNo));
					}
				}
			}
		}
		return featuredMons;
	}
	
	Pokemon getMonFromDefiniteString(String monName) {
		return natDex.get(str2DexNo.get(monName));
	}
	
	String[] getSortedMonArray() {
		String[] arr = new String[getNatDexSize()];
		for(int i : natDex.keySet()) {
			arr[i - 1] = natDex.get(i).getName();;
		}
		Arrays.sort(arr);
		return arr;
	}
	
	void refreshSetWeights() {
		for(CardSet set : setDex.values()) {
			set.setSetWeight(0);
		}
	}
}
