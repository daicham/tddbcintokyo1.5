import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.List;

import org.junit.Test;


public class VendingMachineTest{
	VendingMachine machine = new VendingMachine();

	
	@Test(expected=IllegalArgumentException.class)
	public void test_addMoneyNull() {
		machine.addMoney(null);
	}

	@Test
	public void test_addMoney100() throws Exception {
		machine.addMoney(MoneyType.COIN_100);
		assertThat(machine.getMoneyList().size(), is(1));
		assertThat(machine.getMoneyList().get(0), is(MoneyType.COIN_100));
	}

	@Test
	public void test_calc() throws Exception {
		machine.addMoney(MoneyType.COIN_10);
		machine.addMoney(MoneyType.COIN_100);
		machine.addMoney(MoneyType.COIN_50);
		machine.addMoney(MoneyType.BILL_1000);
		machine.addMoney(MoneyType.COIN_500);
		
		assertThat(machine.calcTolalInsertedPrice(), is(1660));
	}

	@Test
	public void test_addItem() throws Exception {
		prepareCola5();
		
		assertThat(machine.getItemList().size(), is(5));
		assertThat(machine.getItemList().get(0), is(ItemType.COLA));
		assertThat(machine.getItemList().get(1), is(ItemType.COLA));
		assertThat(machine.getItemList().get(2), is(ItemType.COLA));
		assertThat(machine.getItemList().get(3), is(ItemType.COLA));
		assertThat(machine.getItemList().get(4), is(ItemType.COLA));
		assertThat(machine.getItemList().get(0).price, is(120));
	}

	private void prepareCola5() {
		machine.addItem(ItemType.COLA);
		machine.addItem(ItemType.COLA);
		machine.addItem(ItemType.COLA);
		machine.addItem(ItemType.COLA);
		machine.addItem(ItemType.COLA);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void test_addItemNull() {
		machine.addItem(null);
	}

	@Test
	public void test_canBuy() throws Exception {
		prepareCola5();

		machine.addMoney(MoneyType.COIN_500);
		assertThat(machine.canBuy().size(), is(1));
		assertThat(machine.canBuy(), hasItem(ItemType.COLA));
	}
	
	@Test
	public void test_buy500() throws Exception {
		prepareCola5();
		prepareChange();
		
		machine.addMoney(MoneyType.COIN_500);

		assertThat(machine.buy(ItemType.COLA), is(true));
		assertThat(machine.getItemList().size(), is(4));
		assertThat(machine.getTotalSales(), is(120));
	}
	@Test
	public void test_buy100() throws Exception {
		prepareCola5();
		
		machine.addMoney(MoneyType.COIN_100);

		assertThat(machine.buy(ItemType.COLA), is(false));
		assertThat(machine.getItemList().size(), is(5));
		assertThat(machine.getTotalSales(), is(0));
	}
	@Test
	public void test_cantBuy() throws Exception {
		machine.addMoney(MoneyType.COIN_500);

		assertThat(machine.buy(ItemType.COLA), is(false));
		assertThat(machine.getItemList().size(), is(0));
		assertThat(machine.getTotalSales(), is(0));
	}
	@Test
	public void test_buy() throws Exception {
		prepareCola5();
		prepareChange();
		
		assertThat(machine.getChangeList().size(), is(45));
		
		machine.addMoney(MoneyType.COIN_500);
		
		assertThat(machine.buy(ItemType.COLA), is(true));
	}

	@Test
	public void test_buy_NotEnoughChange() throws Exception {
		prepareCola5();
				
		machine.addMoney(MoneyType.COIN_500);
		
		assertThat(machine.buy(ItemType.COLA), is(false));
	}

	private void prepareChange() {
		machine.addChange(MoneyType.BILL_1000);
		machine.addChange(MoneyType.BILL_1000);
		machine.addChange(MoneyType.BILL_1000);
		machine.addChange(MoneyType.BILL_1000);
		machine.addChange(MoneyType.BILL_1000);
		
		for (int i = 0; i < 10; i++) {
			machine.addChange(MoneyType.COIN_10);
			machine.addChange(MoneyType.COIN_100);
			machine.addChange(MoneyType.COIN_500);
			machine.addChange(MoneyType.COIN_50);
		}
	}

	@Test
	public void test_priceToMoney() throws Exception {
		List<MoneyType> actual = machine.priceToMoney(100);
		assertThat(actual.size(), is(1));
		assertThat(actual.get(0), is(MoneyType.COIN_100));
	}

	@Test
	public void test_priceToMoney2() throws Exception {
		List<MoneyType> actual = machine.priceToMoney(1230);
		assertThat(actual.size(), is(6));
		assertThat(actual.get(0), is(MoneyType.BILL_1000));
		assertThat(actual.get(1), is(MoneyType.COIN_100));
		assertThat(actual.get(2), is(MoneyType.COIN_100));
		assertThat(actual.get(3), is(MoneyType.COIN_10));
		assertThat(actual.get(4), is(MoneyType.COIN_10));
		assertThat(actual.get(5), is(MoneyType.COIN_10));
	}
	
	@Test
	public void test_isEnoughChange_NotEnough() throws Exception {
		assertThat(machine.isEnoughChange(100), is(false));
		assertThat(machine.getChangeList().size(), is(0));
	}

	@Test
	public void test_isEnoughChange_Enough() throws Exception {
		machine.addChange(MoneyType.COIN_100);
		assertThat(machine.isEnoughChange(100), is(true));
		assertThat(machine.getChangeList().size(), is(0));
	}

	@Test
	public void test_isEnoughChange_Enough2() throws Exception {
		machine.addChange(MoneyType.COIN_100);
		machine.addChange(MoneyType.COIN_100);
		assertThat(machine.isEnoughChange(100), is(true));
		assertThat(machine.getChangeList().size(), is(1));
	}

	@Test
	public void test_isEnoughChange_Enough3() throws Exception {
		machine.addChange(MoneyType.COIN_100);
		assertThat(machine.isEnoughChange(300), is(false));
		assertThat(machine.getChangeList().size(), is(1));
	}
	
	@Test
	public void test_buyRedBull() throws Exception {
		prepareStock();
		prepareChange();
		
		assertThat(machine.getChangeList().size(), is(45));
		
		printChange();
		machine.addMoney(MoneyType.COIN_500);
		
		assertThat(machine.buy(ItemType.REDBULL), is(true));
		printChange();
		assertThat(machine.getChangeList().size(), is(42));
	}
	
	private void prepareStock() {
		for(int i = 0; i < 5; i++){
			machine.addItem(ItemType.COLA);
		}
		for(int i = 0; i < 5; i++){
			machine.addItem(ItemType.REDBULL);
		}
		for(int i = 0; i < 5; i++){
			machine.addItem(ItemType.WATER);
		}
	}
	
	private void printChange(){
		System.out.println("---------");
		for(MoneyType money : MoneyType.values()){
			System.out.println(money + "=" +calcChange(money));
		}
	}

	private int calcChange(MoneyType money){
		int  sum = 0;
		
		for(MoneyType changeMoney : machine.getChangeList()){
			if(changeMoney == money){
				sum ++;
			}
		}
		
		return sum;
	}
	
	@Test
	public void test_isHitItem_Hit() throws Exception {
		assertThat(machine.isHitItem(4), is(true));
	}

	@Test
	public void test_isHitItem_Miss() throws Exception {
		assertThat(machine.isHitItem(5), is(false));
	}
	
	@Test
	public void getRandom(){
		for(int i = 0; i < 1000; i++){
			int rand = machine.getRandom();
			if(rand < 0 || rand > 99){
				fail("óêêîÇ™îÕàÕäOÇ≈Ç∑:" + rand);
			}
		}
	}
}

