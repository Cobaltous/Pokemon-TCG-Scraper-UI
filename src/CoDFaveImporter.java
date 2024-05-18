import java.util.ArrayDeque;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//https://www.pikawiz.com/cards

public class CoDFaveImporter {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Getting input");
		readFaves(scan.next());
		scan.close();
	}
	
	public CoDFaveImporter() {
		
	}
	
	static ArrayDeque<Integer> readFaves(String codOut) {
//		ArrayDeque<Integer> faves = new ArrayDeque<Integer>();
		
		try {
			JSONObject obj = new JSONObject(codOut);
			System.out.println("Object successfully converted");
//		    String faveStr = new JSONObject(codOut).getString("favorites");
//		    String faveStr = 
		    
//		    System.out.println("faveStr: \n" + faveStr);
//		    JSONObject faveObj = obj.getJSONObject("favorites");
//		    System.out.println("Got obj");
		    JSONArray faveArr = obj.getJSONArray("favorites");
//		    JSONArray faveArr = obj.getJSONObject("favorites").getJSONArray("favorites");
		    System.out.println("Got list of indices");
		    int rank = 1;
		    for(Object i : faveArr) {
		    	System.out.println(String.format("Rank %d: %s", rank++, i));
		    }
		    
		}
		catch (JSONException e){
			JOptionPane.showMessageDialog(null, "List given isn't in the right format (JSON). Have you tried directly copying your State from the Favorite Picker tool?");
		}
		
//		return faves;
		return null;
	}
}
