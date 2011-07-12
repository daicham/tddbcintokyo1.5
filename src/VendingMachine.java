import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VendingMachine {
	private List<MoneyType> moneyList = new ArrayList<MoneyType>();
	private List<ItemType> itemList = new ArrayList<ItemType>();
	private int totalSales = 0;
	private List<MoneyType> changeList = new ArrayList<MoneyType>();

	public List<MoneyType> getChangeList() {
		return changeList;
	}

	public List<MoneyType> getMoneyList() {
		return moneyList;
	}

	public void addMoney(MoneyType money) {
		if (money == null) {
			throw new IllegalArgumentException("おかしなおかねです");
		}
		moneyList.add(money);
	}

	public int calcTolalInsertedPrice() {
		int sum = 0;
		for(MoneyType money : moneyList) {
			sum += money.price;
		}
		return sum;
	}

	public void addItem(ItemType item) {
		if (item == null) {
			throw new IllegalArgumentException("おかしな商品です");
		}
		itemList.add(item);
	}

	public List<ItemType> getItemList() {
		return itemList;
	}

	public Set<ItemType> canBuy() {
		int sum = calcTolalInsertedPrice();
		Set<ItemType> canBuySet = new HashSet<ItemType>();
		for (ItemType item : itemList) {
			if (item.price <= sum) {
				canBuySet .add(item);
			}
		}
		return canBuySet;
	}

	public boolean buy(ItemType buyItem) {
		for (int i = 0; i < itemList.size(); i++) {
			ItemType item = itemList.get(i);
			int tolalInsertedPrice = calcTolalInsertedPrice();
			if (item == buyItem && buyItem.price <= tolalInsertedPrice && isEnoughChange(tolalInsertedPrice - buyItem.price)) {
				itemList.remove(i);
				totalSales  += buyItem.price;
				return true;
			}
		}
		return false;
	}

	public int getTotalSales() {
		return totalSales;
	}

	public void addChange(MoneyType money) {
		changeList.add(money);
	}

	public List<MoneyType> priceToMoney(int price) {
		ArrayList<MoneyType> result = new ArrayList<MoneyType>();
		
		price = priceToMoneyBase(price, result, MoneyType.BILL_1000);
		price = priceToMoneyBase(price, result, MoneyType.COIN_500);
		price = priceToMoneyBase(price, result, MoneyType.COIN_100);
		price = priceToMoneyBase(price, result, MoneyType.COIN_50);
		price = priceToMoneyBase(price, result, MoneyType.COIN_10);

		return result;
	}

	private int priceToMoneyBase(int price, ArrayList<MoneyType> result, MoneyType money) {
		while(price >= money.price){
			result.add(money);
			price -= money.price;
		}
		return price;
	}

	public boolean isEnoughChange(int price) {
		List<MoneyType> moneyList = priceToMoney(price);
		List<MoneyType> tempChangeList = new ArrayList<MoneyType>(changeList);
		
		for(MoneyType money : moneyList){
			if(!tempChangeList.contains(money)){
				return false;
			}
			
			tempChangeList.remove(money);
		}
		
		changeList = tempChangeList;
		return true;
	}

	/**
	 * 0～4の時は当たり、5～99の時はハズレ
	 * @param rand
	 * @return	当たりならtrue, ハズレならfalse
	 */
	public boolean isHitItem(int rand) {
		return rand < 5;
	}
	
	/**
	 * 0～99の乱数を返す
	 * @return
	 */
	public int getRandom(){
		return (int)(Math.random() * 100);
	}
}
