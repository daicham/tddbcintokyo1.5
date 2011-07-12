
public enum MoneyType {
	COIN_10(10),
	COIN_50(50),
	COIN_100(100),
	COIN_500(500),
	BILL_1000(1000);
	
	public final int price;
	
	MoneyType(int price) {
		this.price = price;
	}
}
