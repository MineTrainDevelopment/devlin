package de.minetrain.devlinbot.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * This is an custom {@link ArrayList} that is responsible for Handling a random item picking.
 * @author MineTrain/Justin
 * @since Version 0.8
 */
public class RandomArrayList<Item> extends ArrayList<Item>{
	private static final long serialVersionUID = -3391954070767272115L;
	public static final Random RANDOM = new Random();
	private Object lastPickedItem = ""; //Variable to store the last picked item.

	/**
	 * Default Constructor.
	 */
	public RandomArrayList(){ }
	
	/**
	 * Constructor to cast a {@link Collection} to an {@link RandomArrayList}
	 * @param arrayList
	 */
	public RandomArrayList(Collection<Item> arrayList) {
		super.addAll(arrayList);
	}
	
	/**
	 * Get a random item from this list.
	 * <br> NOTE: Should the randomizer pick the same item as the last time, it will pick a new one.
	 * 
	 * @return A random item in this list;
	 */
	public Item get() {
		Item output = super.get(RANDOM.nextInt(0, super.size()));
		Item item = lastPickedItem.equals(output) ? get() : output;
		lastPickedItem = item;
		return item;
	}
	
	/**
	 * Get a random item from this list and removes the picked item from it.
	 * <br> NOTE: Should the randomizer pick the same item as the last time, it will pick a new one.
	 * 
	 * @return A random item in this list;
	 */
	public Item getAndRemove() {
		Item output = super.get(RANDOM.nextInt(0, super.size()));
		Item item = lastPickedItem.equals(output) ? get() : output;
		super.remove(item);
		return item;
	}
	
	/**
	 * Get a random item from this list.
	 * <br> Shout the
	 * <br> NOTE: If the "index" value should be smaller or greater than the size of this list,
	 * 		it will pick a random item instead.
	 * 
	 * @param index of the item to return;
	 * @return the item at the specified position in this list ore a random item in this list;
	 */
	@Override
	public Item get(int index) {
		Item item = index != 0 ? super.get(index) : get();
		lastPickedItem = item;
		return item;
	}
	
}
