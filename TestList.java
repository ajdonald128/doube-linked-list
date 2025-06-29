
/**
 * @author Austin Donald - ajdonald
 * CIS175 - Fall 2021
 * Jun 27, 2025
 */
public class TestList {
	public static void main (String[] args) {
		StoutList list = new StoutList();
		list.add(4);
		list.add(7);
		list.add(9);
		list.add(10);
		list.add(100);
		list.add(50);
		list.add(2, 25);
		list.add(1, 30);
		System.out.println(list.toStringInternal());
		list.remove(3);
		list.remove(2);
		list.remove(1);
		System.out.println(list.toStringInternal());
		list.sort();
		System.out.println(list.toStringInternal());
	}
}
