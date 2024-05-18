import java.util.ArrayList;

public class Pokemon
{
	String name;
	ArrayList<String> forms, setAppearances;
	int dexNo;
	double faveWeight;
	CardSet cardAppearances;
	
	Pokemon(String name)	{
		this.name = name;
		forms = new ArrayList<String>();
		setAppearances = new ArrayList<String>();
		cardAppearances = new CardSet();
	}
	
	Pokemon(String name, String form) {
		this.name = name;
		forms = new ArrayList<String>();
		forms.add(form);
		setAppearances = new ArrayList<String>();
		cardAppearances = new CardSet();
	}
	
	void addForm(String form) {
		forms.add(form);
	}
	
	void addCardSet(String setName) {
		setAppearances.add(setName);
	}
	
	void addCard(Card card) {
		cardAppearances.addCard(card);
	}
	
	void setFaveWeight(double faveWeight) {
		this.faveWeight = faveWeight;
	}
	
	boolean isInSet(String setName) {
		return setAppearances.contains(setName);
	}
	
	String getName() {
		return name;
	}
	
	String getCardSetStrings() {
		String lastSet = "";
		StringBuilder sb = new StringBuilder();
		for(Card card : cardAppearances.getCardSet()) {
			if(!card.getSetName().equals(lastSet)) {
				lastSet = card.getSetName();
				sb.append(lastSet);
				sb.append('\n');
			}
			sb.append('\t');
			sb.append(card.getNum());
			sb.append(' ');
			sb.append(card.getName());
			
			if(card.getPrice() != 0) {
				sb.append(": $");
				sb.append(card.getPrice());
			}
			
			sb.append('\n');
		}
		return sb.toString();
	}
	
	ArrayList<Card> getCardAppearances() {
		return cardAppearances.getCardSet();
	}
}
