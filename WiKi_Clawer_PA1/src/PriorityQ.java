/**
* This is the implementation of the priority queue by using
* max heap structure. 
*
*@author Hongyi Bian
*@author Yuanbo Zheng
*/
public class PriorityQ {
	
	private Tuple[] heap;
	private int size;
	
	public PriorityQ() 
	{
		heap = new Tuple[1];
		size = 0;
	}
	
	/**
	 * This method is used to add a new tuple to the queue.
	 * O(n) time complexity 
	 */
	public void add(String s, int p) 
	{
		Tuple newTuple = new Tuple(s, p);
		if(size == 0) {
			heap = new Tuple[2];
			heap[1] = newTuple;
			size++;
		}
		else if(heap.length > size+1 ) {
			size++;
			heap[size] = newTuple;
			heapifyUp(heap, size);			
		}
		else {
			int newLength = heap.length+1;
			Tuple[] temp = new Tuple[newLength];
			System.arraycopy(heap, 1, temp, 1, size);
			heap = new Tuple[newLength];
			System.arraycopy(temp, 1, heap, 1, size);
			size++;
			heap[size] = newTuple;
			heapifyUp(heap,size);
		}
	}
	
	/**
	 * This method is used to return the value with max priority.
	 * O(1) time complexity 
	 * @return String the value with max priority.
	 */
	public String returnMax()
	{
		return heap[1].getVal();
	}
	
	/**
	 * This method is used to extract the value with max priority.
	 * Also maintains the max heap property after extraction
	 * O(log n) time complexity 
	 * @return String the value with max priority.
	 */
	public String extractMax() 
	{
		String max = heap[1].getVal();
		heap[1] = heap[size];
		size = size - 1;
		heapifyDown(heap, 1);
		
		return max;
	}
	
	/**
	 * This method is used to remove the element at index i.
	 * Also maintains the max heap property after extraction
	 * O(log n) time complexity 
	 */
	public void remove(int i) 
	{
		if(i==0) {
			return;
		}
		swap(heap[i], heap[size]);
		size = size - 1;
		heapifyDown(heap, i);
	}
	
	/**
	 * This method is used to decrement the priority of ith element
	 * O(log n) time complexity 
	 */
	public void decrementPriority(int i, int k) 
	{
		int currentKey = heap[i].getKey();
		heap[i].setKey(currentKey - k);
		heapifyDown(heap, i);		
	}
	
	/**
	 * This method is used to return an array of keys
	 * O(n) time complexity 
	 */
	public int[] priorityArray() 
	{
		int[] keys = new int[size+1];
		for(int i=1; i<=size; i++) {
			keys[i] = heap[i].getKey();
		}
		return keys;
	} 
	
	/**
	 * This method is used to check if the queue is empty
	 * O(1) time complexity 
	 * @return Boolean if the queue is empty
	 */
	public boolean isEmpty()
	{
		if(size==0) return true;
		else return false;
	}
	
	/**
	 * This method is used to return the ith tuple's key
	 * O(1) time complexity 
	 * @return int the ith tuple's key in heap
	 */
	public int getKey(int i) 
	{
		return heap[i].getKey();
	}
	
	/**
	 * This method is used to return the ith tuple's value
	 * O(1) time complexity 
	 * @return String the ith tuple's value in heap
	 */
	public String getValue(int i) 
	{
		return heap[i].getVal();
	}
	
	
	//*--------------------------*//
	//* 						 *//
	//* self added methods below *//
	//*							 *//
	//*--------------------------*//
	
	
	/**
	 * This method is used to return the size of queue
	 * O(1) time complexity 
	 * @return int the size of queue
	 */
	protected int getSize() 
	{
		return size;
	}
	
	
	/**
	 * This method is used to heapify the heap from index up
	 * O(log n) time complexity 
	 */
	private void heapifyUp(Tuple[] heap, int current_index) 
	{
		int parent = 0;
		if(current_index > 1) {
			parent = current_index/2;
			if(heap[current_index].getKey() > heap[parent].getKey()) {
				swap(heap[current_index], heap[parent]);
				heapifyUp(heap, parent);
			}
		}
	}
	
	/**
	 * This method is used to heapify the heap from index down
	 * O(log n) time complexity 
	 */
	private void heapifyDown(Tuple[] heap, int current_index) 
	{
		int maxOfchild_index = 0;
		int left_key = 0;
		int right_key = 0;
		int left_index = 0;
		int right_index = 0;
		
		if( 2*current_index > size ) {
			return;
		}
		else if( 2*current_index < size ) {
			left_index = 2*current_index;
			right_index = left_index + 1;
			left_key = heap[left_index].getKey();
			right_key = heap[right_index].getKey();
			
			if( left_key > right_key ) maxOfchild_index = left_index;
			else maxOfchild_index = right_index;
		}
		else if ( 2*current_index == size ) {
			maxOfchild_index = 2*current_index;
		}
		
		if ( heap[maxOfchild_index].getKey() > heap[current_index].getKey() ) {
			swap(heap[maxOfchild_index], heap[current_index]);
			heapifyDown(heap, maxOfchild_index);
		}
	}
	
	/**
	 * This method is used to swap two tuples
	 * O(1) time complexity 
	 */
	private void swap(Tuple a, Tuple b) 
	{
		String tempVal = b.getVal();
		int tempKey = b.getKey();
		b.setVal(a.getVal());
		b.setKey(a.getKey());
		a.setVal(tempVal);
		a.setKey(tempKey);
	}

	/**
	 * Inner class represent a tuple
	 */
	protected class Tuple
	{
	    private String val;
	    private int key;
	 
	    protected Tuple(String val, int key){
	        this.val = val;
	        this.key = key; 
	    }
	    protected String getVal() {
	    	return val;
	    }
	    protected int getKey() {
	    	return key;
	    }
	    protected void setVal(String newVal) {
	    	val = newVal;
	    }
	    protected void setKey(int newKey) {
	    	key = newKey;
	    }
	}
	
}
