import java.util.ArrayList;

public class CardSet {
	String setName, releaseDate;
	double packPrice, totalCardPrice, setWeight, avgCardPrice = 0;
	ArrayList<Card> cardSet;
	
	public CardSet(String setName, String releaseDate, double totalCardPrice, ArrayList<Card> cardSet) {
		this.setName = setName;
		this.releaseDate = releaseDate;
		this.totalCardPrice = totalCardPrice;
		this.cardSet = cardSet;
		setWeight = 0;
	}
	
	public CardSet(String setName, String releaseDate, double totalCardPrice, double packPrice, ArrayList<Card> cardSet) {
		this.setName = setName;
		this.releaseDate = releaseDate;
		this.packPrice = packPrice;
		this.cardSet = cardSet;
		setWeight = 0;
	}
	
	public CardSet () {
		cardSet = new ArrayList<Card>();
	}
	
	void addCard(Card card) {
		cardSet.add(card);
	}
	
	void setSetWeight(double setWeight) {
		this.setWeight = setWeight;
	}
	
	void addSetWeight(double faveWeight) {
		setWeight += faveWeight;
	}
	void setAvgCardPrice(double avgCardPrice) {
		this.avgCardPrice = avgCardPrice;
	}
	
	Card getCard(int i) {
		return cardSet.get(i);
	}

	int getSetSize() {
		return cardSet.size();
	}
	
	String getName() {
		return setName;
	}
	
	String getReleaseDate() {
		return releaseDate;
	}
	
	ArrayList<Card> getContents() {
		return cardSet;
	}
	
	double getTotalPrice() {
		return totalCardPrice;
	}
	
	double getAvgCardPrice() {
		return avgCardPrice;
	}
	
	double getSetWeight() {
		return setWeight;
	}
	
//	String getMonAppearances(String monName) {
//		StringBuilder sb = new StringBuilder();
//		
//		for(Card card : cardSet) {
//			if(card.getName().contains(monName)) {
//				sb.append('\t');
//				sb.append(card.getNum());
//				sb.append('/');
//				sb.append(cardSet.size());
//				sb.append(' ');
//				sb.append(card.getName());
//				
//				if(card.getPrice() != 0) {
//					sb.append(": $");
//					sb.append(card.getPrice());
//				}
//				
//				sb.append('\n');
//			}
//		}
//		return sb.toString();
//	}
	
	ArrayList<Card> getCardSet() {
		return cardSet;
	}
}