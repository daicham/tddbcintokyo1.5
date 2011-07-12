
public enum ItemType {
	COLA("ID1", 120, "�R�[��"),
	REDBULL("ID2", 200, "���b�h�u��"),
	WATER("ID3", 100, "��"),
	;
	
	public final String id;
	public final int price;
	public final String name;

	private ItemType(String id, int price, String name) {
		this.id = id;
		this.price = price;
		this.name = name;
	}

}
