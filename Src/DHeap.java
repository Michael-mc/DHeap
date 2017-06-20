/**
 * D-Heap
 */

public class DHeap
{
    /**
     * size holds the current size
     */
    int size;
    /**
     *  max_size hold the maximum array size
     */
    int max_size;
    /**
     * d holds the maximum number of children per node
     */
    int d;
    /**
     * array holds the actual items
     */
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
     * complexity is O(1), only returns a member
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
     *                   size = array.length()
     * Returns number of comparisons along the function run.
     * complexity is O(n*logd(n)), as we do insert n times
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
     * Complexity is O(n) as we're looking at every element once.
     */
    public boolean isHeap() {
        if (size < 2) return true;
        for (int i = 0; i < size; ++i) {
            if ((array[i] == null) )
                return false;
            if (array[i].getPos() != i)
                return false; // metadata failure
            if (array[i].getKey() < array[parent(i, d)].getKey())
                return false; // functionality failure
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
     * Complexity is O(1). small math operation
     */
    public static int parent(int i, int d) {
        return Math.max(0, (i-1) / d);
    }

    /**
     * DUPLICATE
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of
     * vertex i in a complete D-ary tree stored in an array.
     * Note that indices of arrays in Java start from 0.
     * Complexity is O(1). small math operation
     */
    public static int child(int i, int k, int d) {
        assert k <= d;
        assert k >= 1;
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
    *
    * Complexity is O(logd(n)) as this is the maximum height of the tree, which is traversed by heapifyUp
    */
    public int Insert(DHeap_Item item)
    {
        item.setPos(size);
        array[size] = item;
        int ops = heapifyUp(size);
        size++;
        assert isHeap();
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
    * Complexity is O(d*log_d(n)), as we'll be traversing the tree downwards, each time doing at most d comparisons.
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
     *        size > 0
     *
     * postcondition: isHeap()
     * Complexity is O(1). we have a pointer to the minimum value.
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
     * Complexity is O(log_d(n)) at the worst case of decreasing a leaf node, in which case we traverse the tree height.
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
     * Complexity is O(d*log_d(n)). worst case is that we traverse the tree height twice, once heapifying up, and the second heapfyingDown
     *                                 The heapify down is d*log(n), thus this is the complexity of this function as well.
     */
    public int Delete(DHeap_Item item)
    {
        size--;
        DHeap_Item replacement = array[size];
        switchItems(item, replacement);
        return heapifyUp(replacement.getPos()) + heapifyDown(replacement.getPos()); // delete min is still the same since heapifyUp returns with O(1)
    }

    /**
     * see heapDown_rec
     */
    private int heapifyDown(int item) {
        DHeap_Item i =array[item];
        int ops = heapDown_rec(i);
        return ops;
    }

    /**
     * see heapUp_rec
     */
    private int heapifyUp(int item) {
        int ops = heapUp_rec(array[item]);
        return ops;
    }

    /**
     * Moves an item upwards in the tree according to the Heap's laws.
     * done recursively by switching the node with the parent.
     *
     * Return the number of comparisons made so far.
     *
     * Complexity is O(log_d(n)) as we at most traverse the height of the tree.
     */
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

    /**
     * Moves an item downwards in the tree according to the Heap's laws.
     * done recursively by switching the node with it's minimum value child.
     *
     * Returns the number of comparisons made so far.
     *
     * Complexity is O(d*log_d(n)) as we at most traverse the height of the tree, doing up to d comparisons each time.
     */
    private int heapDown_rec(DHeap_Item item) {
        int minChild = child(item.getPos(), 1, d);
        if (minChild >= size) return 0; // no children;

        int comps = 0;
        for (int i = 2; i <= d; ++i) {
            int current = child(item.getPos(), i, d);
            if (current >= size) {
                break;
            }
            if (array[current].getKey() < array[minChild].getKey()) {
                minChild = current;
            }
            comps++;
        }
        if (array[minChild].getKey() >= item.getKey()) {
            return comps;
        }
        switchItems(array[minChild], item);
        return comps + heapDown_rec(item);


    }

    /**
     * switches the positions and the position of the two items both in the array and using setPos.
     * Complexity is O(1)
     */
    private void switchItems(DHeap_Item item1, DHeap_Item item2)
    {
        int temp = item1.getPos();
        item1.setPos(item2.getPos());
        array[item2.getPos()] = item1;

        item2.setPos(temp);
        array[temp] = item2;
    }

    /**
    * Sort the input array using heap-sort (build a heap, and
    * perform n times: get-min, del-min).
    * Sorting should be done using the DHeap, name of the items is irrelevant.
    *
    * Returns the number of comparisons performed.
    *
    * postcondition: array1 is sorted
    *
    * Complexity is O(nd*log_d(n)):
    * creating the heap is nlog_d(n)
    * deleting the items is more costly: dlog_d(n) per item
    * we're deleting all n elements, thus nd*log_d(n)
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
