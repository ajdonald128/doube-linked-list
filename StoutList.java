import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
  /**
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
  /**
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
   */
  public Node head;
  
  /**
   * Dummy node for tail.
   */
  private Node tail;
  
  /**
   * Number of elements in the list.
   */
  private int size;
  
  /**
   * Constructs an empty list with the default node size.
   */
  public StoutList()
  {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize)
  {
    if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
   * @param head
   * @param tail
   * @param nodeSize
   * @param size
   */
  public StoutList(Node head, Node tail, int nodeSize, int size)
  {
	  this.head = head; 
	  this.tail = tail; 
	  this.nodeSize = nodeSize; 
	  this.size = size; 
  }

  @Override
  public int size()
  {
    // TODO Auto-generated method stub
    return size;
  }
  
  @Override
  public boolean add(E item)
  {
    // TODO Auto-generated method stub
	  Node currNode = head.next;
	  while (currNode != tail) {
		  for (int i = 0; i < currNode.data.length; i++) {
			  if (currNode.data[i] == null) {
				  currNode.addItem(item);
				  size += 1;
				  return true;
			  }
		  }
		  currNode = currNode.next;
	  }
	  if (currNode == tail) {
		  Node newNode = new Node();
		  newNode.addItem(item);
		  tail.previous.next = newNode;
		  newNode.previous = tail.previous;
		  tail.previous = newNode;
		  newNode.next = tail;
		  if (head.next == tail) {
			  head.next = newNode;
		  }
		  size += 1;
		  return true;
	  }
    return false;
  }

  @Override
  public void add(int pos, E item)
  {
    // TODO Auto-generated method stub
	  if (head.next == tail) {
		  add(item);
	  }
	  NodeInfo info = find(pos);
	  add(info.node, info.nodeIndex, item);
  }

  @Override
  public E remove(int pos)
  {
    // TODO Auto-generated method stub
	  NodeInfo info = find(pos);
	  E item = info.node.data[info.nodeIndex];
	  if (info.node == tail.previous && info.node.count == 1) {
		  tail.previous = info.node.previous;
		  info.node.previous.next = tail;
	  }
	  else if (info.node == tail.previous || info.node.count > (nodeSize / 2)) {
		  info.node.removeItem(info.nodeIndex);
	  }
	  else {
		  if (info.node.next.count > (nodeSize / 2)) {
			  info.node.removeItem(info.nodeIndex);
			  info.node.addItem(info.node.next.data[0]);
			  info.node.next.removeItem(0);
		  }
		  else {
			  info.node.removeItem(info.nodeIndex);
			  for (int i = 0; i < nodeSize / 2; i++) {
				  info.node.addItem(info.node.next.data[i]);
			  }
			  info.node.next.next.previous = info.node;
			  info.node.next = info.node.next.next;
		  }
	  }
	  size -= 1;
    return item;
  }

  /**
   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
   * Traverse the list and copy its elements into an array, deleting every visited node along 
   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
   * (possibly) the last one must be full of elements.  
   *  
   * Comparator<E> must have been implemented for calling insertionSort().    
   */
  public void sort()
  {
	  // TODO 
	  E[] arr = (E[]) new Comparable[size];
	  StoutListIterator iter = new StoutListIterator();
	  int i = 0;
	  while (iter.hasNext()) {
		  arr[i] = iter.next();
		  i += 1;
	  }
	  head.next = tail;
	  tail.previous = head;
	  size = 0;
	  Comparator<E> comp = new Comparator<E> () {

		@Override
		public int compare(E o1, E o2) {
			// TODO Auto-generated method stub
			return o1.compareTo(o2);
		}

	  };
	  insertionSort(arr, comp);
	  for (i = 0; i < arr.length; i++) {
		  add(arr[i]);
	  }
  }
  
  /**
   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  public void sortReverse() 
  {
	  // TODO 
	  E[] arr = (E[]) new Comparable[size];
	  StoutListIterator iter = new StoutListIterator();
	  int i = 0;
	  while (iter.hasNext()) {
		  arr[i] = iter.next();
		  i += 1;
	  }
	  head.next = tail;
	  tail.previous = head;
	  size = 0;
	  bubbleSort(arr);
	  for (i = 0; i < arr.length; i++) {
		  add(arr[i]);
	  }
  }
  
  @Override
  public Iterator<E> iterator()
  {
    // TODO Auto-generated method stub
    return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator()
  {
    // TODO Auto-generated method stub
    return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index)
  {
    // TODO Auto-generated method stub
    return new StoutListIterator(index);
  }
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal()
  {
    return toStringInternal(listIterator());
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }
	public NodeInfo add (Node node, int nodeIndex, E item) {
		  if (nodeIndex == 0) {
			  if ((node.previous == head && node == tail)|| node.previous.count == nodeSize) {
				  Node newNode = new Node();
				  newNode.addItem(item);
				  newNode.previous = head;
				  newNode.next = node;
				  node.previous = newNode;
				  head.next = newNode;
			  }
			  else {
				  node.previous.addItem(item);
			  }
		  }
		  else if (nodeIndex > 0 && node.count < nodeSize){
			  node.addItem(nodeIndex, item);
		  }
		  else if (nodeIndex > 0 && node.count == nodeSize) {
			  Node newNode = new Node();
			  newNode.previous = node;
			  newNode.next = node.next;
			  node.next = newNode;
			  for (int i = nodeSize / 2; i < nodeSize; i++) {
				  newNode.addItem((E) node.data[i]);
			  }
			  for (int i = nodeSize / 2; i < nodeSize; i++) {
				  node.removeItem(i);
			  }
			  if (nodeIndex <= nodeSize) {
				  node.addItem(nodeIndex, item);
			  }
			  else {
				  node.addItem(nodeIndex - (nodeSize / 2), item);
			  }
		  }
		  size += 1;
		  return new NodeInfo (node, nodeIndex);
	}

  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node
  {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable.
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      //useful for debugging
      //      System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
  
    /**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item)
    {
      if (count >= nodeSize)
      {
    	  return;
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      E item = data[offset];
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      --count;
    }    
  }
 
  private class StoutListIterator implements ListIterator<E>
  {
	// constants you possibly use ...   
	// instance variables ... 
	  private Node currNode;
	  private int globalIndex;
	  private int nodeIndex;
	  private boolean canSet;
	  private Node lastNode;
	  private int lastIndex;
    /**
     * Default constructor 
     */
    public StoutListIterator()
    {
    	// TODO
    	currNode = head.next;
    	globalIndex = 0;
    	nodeIndex = 0;
    	canSet = false;
    }

    /**
     * Constructor finds node at a given position.
     * @param pos
     */
    public StoutListIterator(int pos)
    {
    	// TODO 
    	canSet = false;
    	NodeInfo info = find(pos);
    	currNode = info.node;
    	nodeIndex = info.nodeIndex;
    	globalIndex = pos;
    }

    @Override
    public boolean hasNext()
    {
    	// TODO 
    	if (globalIndex < size) {
    		return true;
    	}
    	return false;
    }

    @Override
    public E next()
    {
    	// TODO 
    	if (!hasNext()) {
    		throw new NoSuchElementException();
    	}
    	if (nodeIndex < currNode.count) {
    		globalIndex += 1;
    		canSet = true;
    		lastNode = currNode;
    		lastIndex = nodeIndex;
    		E result = currNode.data[nodeIndex];
    		nodeIndex += 1;
    		return result;
    	}
    	nodeIndex = 0;
    	currNode = currNode.next;
    	while (nodeIndex >= currNode.count && currNode != tail) {
    		currNode = currNode.next;
    	}
    	if (currNode != tail) {
    		globalIndex += 1;
    		canSet = true;
    		lastNode = currNode;
    		lastIndex = nodeIndex;
    		E result = currNode.data[nodeIndex];
    		nodeIndex += 1;
    		return result;
    	}
    	else {
    		throw new NoSuchElementException();
    	}
    }

    @Override
    public void remove()
    {
    	// TODO 
    	if (!canSet) {
    		throw new IllegalStateException();
    	}
  	  NodeInfo info = new NodeInfo(lastNode, lastIndex);
  	  if (info.node == tail.previous && info.node.count == 1) {
  		  tail.previous = info.node.previous;
  		  info.node.previous.next = tail;
  		  currNode = tail.previous;
  		  nodeIndex = tail.previous.count;
  	  }
  	  else if (info.node == tail.previous || info.node.count > (nodeSize / 2)) {
  		  info.node.removeItem(info.nodeIndex);
  		  if (lastIndex < nodeIndex) {
  			  nodeIndex -= 1;
  		  }
  		  else if (lastIndex == nodeIndex && nodeIndex >= info.node.count) {
  			  if (info.node.count == 0) {
  				  currNode = info.node.next;
  				  nodeIndex = 0;
  			  }
  			  else {
  				  nodeIndex = info.node.count;
  			  }
  		  }
  	  }
  	  else {
  		  if (info.node.next.count > (nodeSize / 2) && info.node.next != tail) {
  			  info.node.removeItem(info.nodeIndex);
  			  info.node.addItem(info.node.next.data[0]);
  			  info.node.next.removeItem(0);
  	  		  if (lastIndex < nodeIndex) {
  	  			  nodeIndex -= 1;
  	  		  }
  	  		else if (lastIndex == nodeIndex && nodeIndex >= info.node.count) {
  	  			nodeIndex = info.node.count;
  	  		}
  		  }
  		  else if (info.node.next != tail){
  			  info.node.removeItem(info.nodeIndex);
  			  for (int i = 0; i < info.node.next.count; i++) {
  				  info.node.addItem(info.node.next.data[i]);
  			  }
  			  info.node.next.next.previous = info.node;
  			  info.node.next = info.node.next.next;
  	  		  if (lastIndex < nodeIndex) {
  	  			  nodeIndex -= 1;
  	  		  }
  	  		else if (lastIndex == nodeIndex && nodeIndex >= info.node.count) {
  	  			nodeIndex = info.node.count;
  	  		}
  		  }
  	  }
  	  size -= 1;
  	  globalIndex -= 1;
  	  canSet = false;
    }

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return globalIndex >= 0;
	}

	@Override
	public E previous() {
		// TODO Auto-generated method stub
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
		if (nodeIndex > 0 && nodeIndex <= currNode.count) {
			nodeIndex -= 1;
			globalIndex -= 1;
			canSet = true;
			lastNode = currNode;
			lastIndex = nodeIndex;
			return currNode.data[nodeIndex];
		}
		while (currNode != head && currNode.count <= 0) {
			currNode = currNode.previous;
			nodeIndex = currNode.count - 1;
		}
		if (currNode != head) {
			globalIndex -= 1;
			canSet = true;
			lastNode = currNode;
			lastIndex = nodeIndex;
			return currNode.data[nodeIndex];
		}
		else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public int nextIndex() {
		// TODO Auto-generated method stub
		return globalIndex;
	}

	@Override
	public int previousIndex() {
		// TODO Auto-generated method stub
		return globalIndex - 1;
	}

	@Override
	public void set(E e) {
		// TODO Auto-generated method stub
		if (!canSet) {
			throw new IllegalStateException();
		}
		lastNode.data[lastIndex] = e;
	}

	@Override
	public void add(E e) {
		// TODO Auto-generated method stub
		NodeInfo info = StoutList.this.add(currNode, nodeIndex, e);
		nodeIndex += 1;
		currNode = info.node;
		if (nodeIndex >= nodeSize) {
			nodeIndex = 0;
			currNode = currNode.next;
		}
		globalIndex += 1;
	}
    
    // Other methods you may want to add or override that could possibly facilitate 
    // other operations, for instance, addition, access to the previous element, etc.
    // 
    // ...
    // 
  }
  
	private class NodeInfo {
		public Node node;
		public int nodeIndex;
		public NodeInfo (Node node, int nodeIndex) {
			this.node = node;
			this.nodeIndex = nodeIndex;
		}
	}
	private NodeInfo find (int pos) {
		int lowerTotalCount = 0;
		Node node = head.next;
		while (node != tail) {
			if (lowerTotalCount + node.count > pos) {
				break;
			}
			lowerTotalCount += node.count;
			node = node.next;
		}
		int index = pos - lowerTotalCount;
		NodeInfo info = new NodeInfo(node, index);
		return info;
	}
  /**
   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
   * @param arr   array storing elements from the list 
   * @param comp  comparator used in sorting 
   */
  private void insertionSort(E[] arr, Comparator<? super E> comp)
  {
	  // TODO
	  for (int i = 1; i < arr.length; i++) {
		  E current = arr[i];
		  int j = i - 1;
		  while (j >= 0 && comp.compare(arr[j], current) > 0) {
			  arr[j + 1] = arr[j];
			  j -= 1;
		  }
		  arr[j + 1] = current;
	  }
  }
  
  /**
   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
   * description of bubble sort please refer to Section 6.1 in the project description. 
   * You must use the compareTo() method from an implementation of the Comparable 
   * interface by the class E or ? super E. 
   * @param arr  array holding elements from the list
   */
  private void bubbleSort(E[] arr)
  {
	  // TODO
	  boolean swapped = true;
	  while (swapped) {
		  swapped = false;
		  for (int i = 1; i < arr.length; i++) {
			  if (arr[i].compareTo(arr[i - 1]) > 0) {
				  E temp = arr[i - 1];
				  arr[i - 1] = arr[i];
				  arr[i] = temp;
				  swapped = true;
			  }
		  }
	  }
  }
 

}