/**
 * D-Heap
 */

public class DHeap
{
    private int size, max_size, d;
    private DHeap_Item[] array;

	// Constructor
	// m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
               max_size = m_size;
			   d = m_d;
               array = new DHeap_Item[max_size];
               size = 0;
    }
	
	/**
	 * public int getSize()
	 * Returns the number of elements in the heap.
	 */
	public int getSize() {
		return size;
	}
	
  /**
     * public int arrayToHeap()
     *
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * preconidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * 				  size = array.length()
     * Returns number of comparisons along the function run. 
	 */
    public int arrayToHeap(DHeap_Item[] array1) 
    {
        array = new DHeap_Item[array1.length];
        max_size = array1.length;
        size = 0;
        int total = 0;
        for (int i = 0; i < array.length; ++i) {
        	total += Insert(array1[i]);
		}
		return total;
    }

    /**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *   
     */
    public boolean isHeap() {
        if (size < 2) return true;
        for (int i = 0; i < size; ++i) {
			if ((array[i] == null) || (array[i].getPos() != i)) return false; // metadata failure
			if (array[i].getKey() < array[parent(i, d)].getKey()) return false; // functionality failure
		}
		return true;
	}


 /**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of 
     * vertex i in a complete D-ary tree stored in an array. 
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) {
    	return (i != 0) ? (i-1)/d : 0;
    }

    public static int child(int i, int k, int d) {
    	return d * i + k;
	}

    /**
    * public int Insert(DHeap_Item item)
    *
	* Inserts the given item to the heap.
	* Returns number of comparisons during the insertion.
	*
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) 
    {
    	item.setPos(size);
    	array[size] = item;
    	int ops = heapifyUp(size++);

		return ops;
    }

 /**
    * public int Delete_Min()
    *
	* Deletes the minimum item in the heap.
	* Returns the number of comparisons made during the deletion.
    * 
	* precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    public int Delete_Min()
    {
    	return Delete(array[0]);
    }


    /**
     * public DHeap_Item Get_Min()
     *
	 * Returns the minimum item in the heap.
	 *
     * precondition: heapsize > 0
     *               isHeap()
     *		size > 0
     * 
     * postcondition: isHeap()
     */
    public DHeap_Item Get_Min()
    {
	return array[0];
    }
	
	/**
	 * public int Decrease_Key(DHeap_Item item, int delta)
	 *
	 * Decerases the key of the given item by delta.
	 * Returns number of comparisons made as a result of the decrease.
	 *
	 * precondition: item.pos < size;
	 *               item != null
	 *               isHeap()
	 *
	 * postcondition: isHeap()
	 */
	public int Decrease_Key(DHeap_Item item, int delta)
	{
		item.setKey(item.getKey() - delta);
		return heapifyUp(item.getPos());
	}
	
	  /**
     * public int Delete(DHeap_Item item)
     *
	 * Deletes the given item from the heap.
	 * Returns number of comparisons during the deletion.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item)
    {
		size--;
		int newPos = item.getPos();
		array[newPos] = array[size];
		array[newPos].setPos(newPos);

		return heapifyDown(newPos);
    }

    private int heapifyDown(int item) {
        return heapDown_rec(array[item]);
    }

    private int heapifyUp(int item) {
        return heapUp_rec(array[item]);
    }

    private int heapUp_rec(DHeap_Item item) {
        if (item.getPos() == 0) return 0;
        int parent = parent(item.getPos(), d);
        if (item.getKey() < array[parent].getKey()) {
            switchItems(item, array[parent]);
            if (parent != 0) {
                return 1 + heapUp_rec(item);
            }
        }
        return 1;
    }

    private int heapDown_rec(DHeap_Item item) {
		int minChild = child(item.getPos(), 1, d);
		if (minChild >= size) return 0; // no children;

		int comps = 0;
		for (int i = 2; i <= d; ++i) {
			int current = child(item.getPos(), i, d);
			if (current >= size) break;
			if (array[minChild].getKey() > array[current].getKey()) {
				minChild = current;
			}
			comps++;
		}
		if (array[minChild].getKey() == item.getKey()) {
			return comps;
		}
		switchItems(array[minChild], item);
		return comps + heapDown_rec(item);


    }

	private void switchItems(DHeap_Item item1, DHeap_Item item2)
    {
        int temp = item1.getPos();
        item1.setPos(item2.getPos());
        item2.setPos(temp);
    }

    /**
	* Sort the input array using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, name of the items is irrelevant.
	* 
	* Returns the number of comparisons performed.
	* 
	* postcondition: array1 is sorted 
	*/
	public static int DHeapSort(int[] array1, int d) {
		DHeap heap = new DHeap(d, array1.length);
		DHeap_Item []items = DHeap_Item.fromIntArray(array1);
		int total = heap.arrayToHeap(items);

		for (int i = 0; i < array1.length; ++i) {
			DHeap_Item min = heap.Get_Min();
			array1[i] = min.getKey();
			total += heap.Delete_Min();
		}

		return total;
	}
}
