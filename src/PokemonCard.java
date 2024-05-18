import java.util.ArrayDeque;

public class PokemonCard implements Card {
	ArrayDeque<Pokemon> featuredMons;
	double nmPrice;
	String cardName, setName, num;
	
	PokemonCard(ArrayDeque<Pokemon> featuredMons, String cardName, String setName, String num) {
		this.featuredMons = featuredMons;
		this.cardName = cardName;
		this.setName = setName;
		this.num = num;
	}
	
	PokemonCard(ArrayDeque<Pokemon> featuredMons, double nmPrice, String cardName, String setName, String num) {
		this.featuredMons = featuredMons;
		this.cardName = cardName;
		this.nmPrice = nmPrice;
		this.setName = setName;
		this.num = num;
	}
	
	@Override
	public String getNum() {
		return num;
	}

	@Override
	public String getName() {
		return cardName;
	}
	
	@Override
	public String getSetName() {
		return setName;
	}

	@Override
	public double getPrice() {
		return nmPrice;
	}
	
	ArrayDeque<Pokemon> getFeaturedMons() {
		return featuredMons;
	}

}
