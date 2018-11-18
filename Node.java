/***********************************************************
 * File name   : Node.java
 * Author      : Noopur R K
 * UFID        : 1980 - 9834
 * Description : This class implements the class node
 ***********************************************************/

import java.util.*;

public class Node 
{
	Node left_Sibling, right_Sibling, parent, child;
	int degree = 0;
	boolean childCut = false;
	private String Data;
	int key;
	
	Node(String Data, int key)
	{
		this.left_Sibling = this;
		this.right_Sibling = this;
		this.parent = null;
		this.child = null;
		this.Data = Data;
		this.key = key;
	}
	
	public String getDataTag()
	{
		return this.Data;
	}

}
//EOF