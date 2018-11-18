/*******************************************************
 * File name   : keywordCounter.java
 * Author      : Noopur R K
 * UFID        : 1980 - 9834
 * Description : This class implements the class node
 *******************************************************/

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class keywordcounter 
{
	public static void main(String args[])
	{
		String  pathToInputfile = args[0];
		File file = new File("output_file.txt");
		BufferedWriter writer = null;

		//Hash Map for Storing the hashTag and the node
		HashMap<String,Node> DataHashMap = new HashMap();

		fibonacciHeap fibHeap = new fibonacciHeap();

		try {
			BufferedReader bufferReader = new BufferedReader(new FileReader(pathToInputfile));
			String rawData = bufferReader.readLine();

			Pattern stringPattern = Pattern.compile("([$])([a-z_]+)(\\s)(\\d+)");
			Pattern numeralPattern = Pattern.compile("(\\d+)");
			Pattern stopPattern = Pattern.compile("([s|S]+)top");
			boolean stopFlag = false;
			
			writer = new BufferedWriter( new FileWriter(file));
			

			while ((rawData != null) && (false == stopFlag)) 
			{
				Matcher stringMatcher = stringPattern.matcher(rawData);
				Matcher numeralMatcher = numeralPattern.matcher(rawData);
				Matcher stopMatcher = stopPattern.matcher(rawData);

				boolean stringResult = stringMatcher.find();
				boolean numeralResult = numeralMatcher.find();
				boolean stopResult = stopMatcher.find();
							
				if(stringResult)
				{
					String Data = stringMatcher.group(2);
					int value = Integer.parseInt(stringMatcher.group(4));

					//Only is the key is not present in the hash map, will it be inserted.
					if(!DataHashMap.containsKey(Data))
					{
						//create a node and populate the data in the data field and the value in the 
						//key field. Insert this node in the hash map and the node in the fibonacci heap
						Node node = new Node(Data,value);
						fibHeap.insertNode(node);
						DataHashMap.put(Data, node);
					}

					//the data is already present in the hash map
					//increment the value of the key already present in the hash map and the fib heap
					else
					{
						fibHeap.IncreaseKeyValue(DataHashMap.get(Data), value);
					}
				}
				
				else if(numeralResult)
				{
					int removeNumberOfNodes = Integer.parseInt(numeralMatcher.group(1));
					ArrayList<Node> removedNodes = new ArrayList<Node>(removeNumberOfNodes);

					for(int index = 0; index < removeNumberOfNodes ; ++index)
					{
						//remove the maximum node from the heap
						Node node = fibHeap.removeMaximumNode();
						//remove the node from the hash map
						if(node != null)
						{
							DataHashMap.remove(node.getDataTag(),node);
							removedNodes.add(node);

							if (index < removeNumberOfNodes - 1) 
							{
								writer.write(node.getDataTag() + ",");
							}

							else 
							{
								writer.write(node.getDataTag());
							}	
						}
					}

					for(Node iterate : removedNodes)
					{
						fibHeap.insertNode(iterate);
						DataHashMap.put(iterate.getDataTag(),iterate);
					}

					//go to new line in writer pointer
					writer.newLine();	
				}
				
				else if (stopResult)
				{
					stopFlag = true;	
					try 
		            {
		                writer.close();
		            }//try
		            catch (IOException ioEx) 
		            {
		            	System.out.println(ioEx);
		            }
				}
				//Go to Next Line
				rawData = bufferReader.readLine();
			}
			bufferReader.close();
			writer.close();
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
//EOF
