
/********************************************************
 * File name   : Node.java
 * Author      : Noopur R K
 * UFID        : 1980 - 9834
 * Description : This class implements the class node
 *******************************************************/

import java.util.*;

public class fibonacciHeap 
{
	private Node MaxNode;
	private int NumberOfNodes;
	
	/************************************************************
	 * Method       : insertNode
	 * Parameters   : Node node 
	 * Description  : Inserts the given node in the fibonacci heap.
	 ***********************************************************/
	public void insertNode(Node node)
	{
		if(MaxNode != null)
		{ 
			node.left_Sibling = MaxNode;
			node.right_Sibling = MaxNode.right_Sibling;
			MaxNode.right_Sibling = node;
			
			if(node.right_Sibling != null)
			{
				node.right_Sibling.left_Sibling = node;
			}
			
			if(node.right_Sibling == null)
			{
				node.right_Sibling = MaxNode;
				MaxNode.left_Sibling = node;
			}
			
			if(node.key > MaxNode.key)
			{
				MaxNode = node;
			}
		}
		
		else
		{
			MaxNode = node;
		}
		++NumberOfNodes;
	}
	
	
	/***********************************************************
	 * Method      : cut
	 * Parameters  : Node node, Node node_parent
	 * Description : Cuts the node from its parent
	 ***********************************************************/
	
    public void cut(Node node1, Node node2)
    {
        // removes x from child of y and decreases the degree of y
        node1.left_Sibling.right_Sibling = node1.right_Sibling;
        node1.right_Sibling.left_Sibling = node1.left_Sibling;
        node2.degree--;

        // reset y.child if necessary
        if (node2.child == node1) {
            node2.child = node1.right_Sibling;
        }

        if (node2.degree == 0) {
            node2.child = null;
        }

        // add x to root list of heap
        node1.left_Sibling = MaxNode;
        node1.right_Sibling = MaxNode.right_Sibling;
        MaxNode.right_Sibling = node1;
        node1.right_Sibling.left_Sibling = node1;

        // set parent of x to nil
        node1.parent = null;

        // set mark to false
        node1.childCut = false;
    }

	/***********************************************************
	 * Method       : cascadingCut
	 * Parameters   : Node node 
	 * Description  : performs the cascading cut operation on the fibonacci heap
	 ***********************************************************/
	public void cascadingCut(Node node)
    {
        Node x = node.parent;

        //if there is a parent
        if (x != null) {
            // if y is unmarked, set it marked
            if (!node.childCut) {
                node.childCut = true;
            } else {
                // it's marked, cut it from parent
                cut(node, x);

                // cut its parent as well
                cascadingCut(x);
            }
        }
    }

	
	/***********************************************************
	 * Method : IncreaseKeyValue
	 * Parameters : Node node, int value
	 * Description : Increases the value of the key.
	 ***********************************************************/
	public void IncreaseKeyValue(Node node, int value)
	{
		node.key += value;
		
		Node parent = node.parent;
		
		if(node.parent != null)
		{
			if(parent.key < node.key)
			{
				cut(node,parent);
				cascadingCut(parent);
			}
		}
		
		if(node.key > MaxNode.key)
		{
			MaxNode = node;
		}
	}
	
	/***********************************************************
	 * Method : makeChildNode
	 * Parameters : Node node1,Node node2
	 * Description : Performs the compare link and makes the child and parent node
	 ***********************************************************/
	public void makeChildNode(Node node1,Node node2) 
	{
		// remove y from root list of heap
        node1.left_Sibling.right_Sibling = node1.right_Sibling;
        node1.right_Sibling.left_Sibling = node1.left_Sibling;

        // make y a child of x
        node1.parent = node2;

        if (node2.child == null) 
        {
            node2.child = node1;
            node1.right_Sibling = node1;
            node1.left_Sibling = node1;
        } 
        else 
        {
            node1.left_Sibling = node2.child;
            node1.right_Sibling = node2.child.right_Sibling;
            node2.child.right_Sibling = node1;
            node1.right_Sibling.left_Sibling = node1;
        }

        // increase degree of x by 1
        node2.degree++;
        
        // make mark of y as false
        node1.childCut = false;
	}
	
	/***********************************************************
	 * Method : degreeWiseMergeNodes
	 * Parameters : void
	 * Description : performs the degree wise merge of the nodes in the fibonacci heap
	 ***********************************************************/
	public void degreeWiseMergeNodes()
	{
		int sizeOfDegreeTable = 45;
		List<Node> degreeTable = new ArrayList<Node>(sizeOfDegreeTable);

		// Initialize degree table
		for (int i = 0; i < sizeOfDegreeTable; i++) 
		{
			degreeTable.add(null);
		}
		
		int numOfRootNodes = 0;
		Node i = MaxNode;
		
		if(i != null)
		{
			numOfRootNodes++;
			i = i.right_Sibling;
			
			while (i != MaxNode)
			{
				numOfRootNodes++;
				i = i.right_Sibling;
			}
		}
		
		
		while(numOfRootNodes > 0)
		{
			int degree = i.degree;
			Node next = i.right_Sibling;
			
			for(;;)
			{
				Node j = degreeTable.get(degree);
				
				if(j == null)
				{
					break;
				}
				
				if(i.key < j.key)
				{
					Node temp = j;
                    j = i;
                    i = temp;
				}
				
				//make y the child of x as x key value is greater
                makeChildNode(j, i);

                //set the degree to null as x and y are combined now
                degreeTable.set(degree, null);
                degree++;
			}
			
			degreeTable.set(degree, i);
			i = next;
			--numOfRootNodes;
		}
		
		//Deleting the max node
        MaxNode = null;

        // combine entries of the degree table
        for (int k = 0; k < sizeOfDegreeTable; k++) 
        {
        	Node j = degreeTable.get(k);
        	if (j == null) 
        	{
        		continue;
        	}
        	
        	//till max node is not null
            if (MaxNode != null) 
            {
                // First remove node from root list.
                j.left_Sibling.right_Sibling = j.right_Sibling;
                j.right_Sibling.left_Sibling = j.left_Sibling;

                // Now add to root list, again.
                j.left_Sibling = MaxNode;
                j.right_Sibling = MaxNode.right_Sibling;
                MaxNode.right_Sibling = j;
                j.right_Sibling.left_Sibling = j;

                // Check if this is a new maximum
                if (j.key > MaxNode.key) 
                {
                    MaxNode = j;
                }
            } 
            else 
            {
                MaxNode = j;
            }
        }
	}
	
	
	/***********************************************************
	 * Method : removeMaximumNode
	 * Parameters : void
	 * Description : performs the remove maximum from the fibonacci heap operation.
	 ***********************************************************/
	public Node removeMaximumNode()
	{
		Node toRemove = MaxNode;
		if(toRemove != null)
		{
			int numberOfChildren = toRemove.degree;

			Node childNode = toRemove.child;
			Node tempRight;

			while((numberOfChildren > 0) && (childNode != null))
			{
				tempRight = childNode.right_Sibling;
				
				//remove child
				childNode.left_Sibling.right_Sibling = childNode.right_Sibling;
				childNode.right_Sibling.left_Sibling = childNode.left_Sibling;

				//add the ChildNode to the top level list
				childNode.left_Sibling = MaxNode;
				childNode.right_Sibling = MaxNode.right_Sibling;
				MaxNode.right_Sibling = childNode;
				childNode.right_Sibling.left_Sibling = childNode;

				childNode.parent = null;
				childNode = tempRight;
				
				--numberOfChildren;	
			}

			//remove toRemove from the top level of the heap
			toRemove.left_Sibling.right_Sibling = toRemove.right_Sibling;
			toRemove.right_Sibling.left_Sibling = toRemove.left_Sibling;
			toRemove.degree = 0;
			toRemove.child = null;
			toRemove.parent = null;
             

			if(toRemove == toRemove.right_Sibling)
			{
				MaxNode = null;
			}
			else
			{
				MaxNode = toRemove.right_Sibling;
				degreeWiseMergeNodes();
			}
			
			--NumberOfNodes;
			toRemove.left_Sibling = toRemove;
			toRemove.right_Sibling = toRemove;
			return toRemove;
			
		}
		return null;
	}	
	
	/***********************************************************
	 * Method : display
	 * Parameters : void
	 * Description : Display the contents of the fib heap
	 ***********************************************************/
	public void display()
	{	
		Node start = MaxNode;
		System.out.println("Display: " + start.getDataTag());
		
		start = start.right_Sibling;

		while (start != MaxNode)
		{
			System.out.println("Display: " + start.getDataTag());
			start = start.right_Sibling;
		}
	}
}
//EOF
