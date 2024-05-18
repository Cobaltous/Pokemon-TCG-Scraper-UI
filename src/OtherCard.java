
public class OtherCard implements Card {
	double nmPrice;
	String name, setName, num;
	
	public OtherCard(String name, String setName, String num) {
		this.name = name;
		this.setName = setName;
		this.num = num;
	}
	
	public OtherCard(double nmPrice, String name, String setName, String num) {
		this.nmPrice = nmPrice;
		this.name = name;
		this.setName = setName;
		this.num = num;
	}
	
	@Override
	public String getNum() {
		return num;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getSetName() {
		return setName;
	}

	@Override
	public double getPrice() {
		return nmPrice;
	}

}
