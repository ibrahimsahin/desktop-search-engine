
public class LList
{
	public Node head; // pointer the list head

	public LList()
	{
		head = null; // at first list is empty
	}

	public Node find_index(int id){//this function take id and returns the word
		Node temp=head;
		while(temp.id!=id){
			temp=temp.getNext();
		}
		return temp;
		
		
	}
	public int find_word(String str){//this function take the word and returns id number
		Node temp=head;
		int count=0;
		while(!temp.getData().equals(str)){
			temp=temp.getNext();
			count++;
		}
		return count;
		
	}

	public int size() //how many element in the list
	{
		int count = 0;
		Node ptr = head;

		while (ptr != null) {
			count++;
			ptr = ptr.getNext();
		}

	return count;
	}
	public void append2(String object,int id){//add to the list
		Node newNode = new Node (object,id); //create new node
		Node temp = head;
		newNode.setNext(null); 
		if (head == null) { // if list is empty
			head = newNode; //new node is head
		} 
		else	
		{ // if it is not,add to the last
			
			while (temp.getNext() != null) { // scan all lists
				temp = temp.getNext();
			}
				temp.setNext(newNode); // add
		}
		
	}

	public int append (String object,int id) //add new element
	{
		int find=0;
		Node newNode = new Node (object,id); // create new node
		Node temp = head;
		String st1=newNode.getData();
			
		newNode.setNext(null);  
		if (head == null) { // if list is empty
			head = newNode; // new node is head
		} 
		else	
		{ // if it is not,add to the last
			
			while (temp.getNext() != null) { // scan all list
				String st2=temp.getData();
				if(!st1.equals(st2))//if there is same word,do not add
					temp = temp.getNext();//look next node
				else{
					find=1;
					break;
				}
			}
			if(find==0)
				temp.setNext(newNode); //add
		}
		return find;
	}	
}


 class Node
{
	public int id; 
	public String data; 
	public Node next;

public Node ()
{
	id=0;
	next = null;
	data= null;
}

public Node (String object,int id_)
{
	id=id_;
	data = object;
	next= null;
} 
public int getId() {//getters and setters
	return id;
}

public void setId(int id) {
	this.id = id;
}


public String getData () {
	return data; 
	}

public Node getNext () {
	return next;
	
}

public void setData (String object)
{ 
	data = object; 
	
}

public void setNext (Node node) {
	next = node; 
}
	
}
