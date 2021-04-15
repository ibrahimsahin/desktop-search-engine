import bbmlib.*;
import bbmlib.BbmWriter;
import  java.io.*;
import java.util.StringTokenizer;
public class Main{
	public int find_howmany_doc(String file){//this function find how many line in the documents
		BufferedReader read;
		File dosya=new File(file); 
		int count=0;
			try{
				read=new BufferedReader(new FileReader(dosya));
				while(read.ready()){
					read.readLine();//read the line
					count++;
				}
				read.close(); 
			}catch(Exception e){ 
				e.printStackTrace();         
			} 
		return count;
	}
	public int count_words(String str){//this function count how many word in the line
		int count=0;
		StringTokenizer st = new StringTokenizer(str," ");
	    while (st.hasMoreTokens()) {
	    	st.nextToken();
	    	count++;
	     }
		return count;
	}
	
	public int[][] create_matrix(LList [] lists,LList all_list,int column,int line){
		int [][] matrix=new int[column][line];//create matrix
		int find=0;
		int i=0,j=0;
		for(i=0;i<column;i++){
			Node n=all_list.find_index(i);//take the element big list
			String st1;
			st1=n.getData();
			for(j=0;j<line;j++){
				int size=lists[j].size();//calculate the size of little lists
				for(int k=0;k<size;k++){//scan the little lists
					Node m=lists[j].find_index(k);
					String st2;
					st2=m.getData();
					if(st1.equals(st2)){//if strings are equals
						find=1;
						break;
					}
				}
				if(find==1){
					matrix[i][j]=1;//assign this coordinates 1
					}
				else{
					matrix[i][j]=0;}//assign this coordinates 0
				find=0;
	
			}
		}
		return matrix;
	}
	
	public int stop_control(LList stop,String str){//this function control the stop words
		int control=0;
		int size=stop.size();//calculate the stop_words size
		for(int i=0;i<size;i++){
			if(stop.find_index(i).getData().equals(str)){
				control=1;//it is a stop word
				break;
				}
		}
		return control;
	}
	public String lower(String s){//lower the string
		String lowerCase = s.toLowerCase();  
		return lowerCase;
	}
	
	public void Load(LList stop_words){//this function reads the documents 
		int id=0,id2=0,id3=0;
		int control=0;
		String stop="stop_words.txt";
		String query="queries.txt";
		int count=find_howmany_doc("documents.txt");//find how many documents at total
		
		LList all_list=new LList();//hold all words
		LList [] lists =new LList[count-2];//hold each lists
		LList [] list_name=new LList[count];//hold name of the lists
		String read = null;
		BbmReader fileReader = BbmReader.getFileReader ("documents.txt");
		for(int i=0 ; i<count ; i++){
			read=fileReader.readLine();//read a line from documents.txt
			if(!read.equals(stop)&&!read.equals(query)){
				LList listname=new LList();//assign the listname_
				listname.append(read,id3);
				list_name[id3]=listname;
				id3++;
			}
			if(!read.equals(stop) && !read.equals(query)){//reading documents
				LList l=new LList();
				int count_line=find_howmany_doc(read);//find how many at documents
				String doc_line=null;
				BbmReader file= BbmReader.getFileReader (read);
				for(int j=0 ; j<count_line ; j++){
					doc_line=file.readLine();//read line every documents
					doc_line=lower(doc_line);//lower the string
					StringTokenizer st = new StringTokenizer(doc_line," 1234567890.,():$!?%;<>\"");//string tokenizer_
					while (st.hasMoreTokens()) {
				    	String str=st.nextToken();
			    		int have_stop=stop_control(stop_words,str);
				    	if(have_stop==0){
				    		l.append2(str,id2);//put the word to the list
				    		int find=all_list.append(str,id);//add to the list
				    		if(find==0)
				    			id++;//if there is not same word
				    		id2++;
				    	}
				    }
				}//end of for
				lists[control]=l;//create the little lists
				control++;
				id2=0;
			}//end of if
		}//end of for
		int size=all_list.size();//for matrix calculate the big list size
		int [][] matrix;
		matrix=create_matrix(lists,all_list,size,count-2);//get the matrix
		read_queries(all_list,lists,list_name,matrix,size,count-2);//read the commands form queries.txt_
	}
	
	public LList find_stops(){//this function create a lists for stop words
		String read = null;
		LList l=new LList();
		int id=0;
		int count=find_howmany_doc("stop_words.txt");//find how many documents at total
		BbmReader fileReader = BbmReader.getFileReader ("stop_words.txt");//read this file
		for(int i=0 ; i<count ; i++){
			read=fileReader.readLine();
			StringTokenizer st = new StringTokenizer(read," ");
			 while (st.hasMoreTokens()) {
			    	String str=st.nextToken();
			    	l.append(str, id);//assign to the list
			    	id++;
			 }
		}
		return l;//return the list
	}
	public int[][] create_space_model(LList all_list,LList lists[],int column,int line){
		int [][] matrix=new int[column][line];
		int count=0;
		for(int i=0;i<column;i++){//
			Node n=all_list.find_index(i);
			String st1;
			st1=n.getData();
			for(int j=0;j<line;j++){
				int size=lists[j].size();//calculate the little lists size
				for(int k=0;k<size;k++){//scan the little lists
					Node m=lists[j].find_index(k);
					String st2;
					st2=m.getData();
					if(st1.equals(st2))
						count++;
				}
				matrix[i][j]=count;
				count=0;
			}
		}
		
		return matrix;
	}
	
	public void read_queries(LList all_list,LList []lists,LList list_name[],int matrix[][],int column_edge,int line_edge){
		double first=0,second=0,sim=0,above=0,below=0;
		String read = null;
		LList l=new LList();
		int id=0,cnt = 0;
		int count=find_howmany_doc("queries.txt");//find how many documents at total
		BbmReader fileReader = BbmReader.getFileReader ("queries.txt");
		String filename="result.txt";
		BbmWriter fileWriter=BbmWriter.getFileWriter(filename);//file writer

		for(int i=0;i<count;i++){//edge is how many lines
			read=fileReader.readLine();//read the command(query)
			fileWriter.println(read);
			StringTokenizer st = new StringTokenizer(read," :\"");
			while (st.hasMoreTokens()) {//separate
				String str=st.nextToken();
				if(str.equals("bm")){//if query is bm_
					int count_words=count_words(read);//count how many words
					if(count_words-1==1){
							String word=st.nextToken();
							int satir=all_list.find_word(word);
							for(int j=0;j<line_edge;j++)//scan the matrix
								if(matrix[satir][j]==1)
									fileWriter.println(list_name[j].find_index(j).data);//if there is,assign 1
					}
					if(count_words-1==2){	
						String word=st.nextToken();
						word=st.nextToken();
						int satir=all_list.find_word(word);
						for(int j=0;j<line_edge;j++)
							if(matrix[satir][j]==0)
								fileWriter.println(list_name[j].find_index(j).data);	
						
					}
					if(count_words-1==3){
						String word1=st.nextToken();
						word1=lower(word1);
						String op=st.nextToken();
						String word2=st.nextToken();
						word2=lower(word2);
						int satir1=all_list.find_word(word1);
						int satir2=all_list.find_word(word2);
						if(op.equals("NOT")){//for NOT
							for(int j=0;j<line_edge;j++)
								if(matrix[satir1][j]==1&&matrix[satir2][j]==0)
									fileWriter.println(list_name[j].find_index(j).data);
						}
						if(op.equals("OR")){//for OR
							for(int j=0;j<line_edge;j++)
								if(matrix[satir1][j]==1 || matrix[satir2][j]==1)
									fileWriter.println(list_name[j].find_index(j).data);
							
						}
						if(op.equals("AND")){//for AND
							for(int j=0;j<line_edge;j++)
								if(matrix[satir1][j]==1 && matrix[satir2][j]==1)
									fileWriter.println(list_name[j].find_index(j).data);
						}
					
					}
				}//end of bm_
				if(str.equals("vsm_doc_sim")){//if query is vsm_doc_sim
					int list_number=0;
					int space_model[][]=create_space_model(all_list,lists,column_edge,line_edge);//get the model
					String word=st.nextToken();
					for(int j=0;j<line_edge;j++){//find the which document given 
						if(word.equals(list_name[j].find_index(j).data)){
							list_number=j;
							break;
						}
					}
					
					for(int j=0;j<line_edge;j++){//calculate according to model
						for(int k=0;k<column_edge;k++){
							above=above+space_model[k][j]*space_model[k][list_number];
							first=first+(space_model[k][j]*space_model[k][j]);
							second=second+(space_model[k][list_number]*space_model[k][list_number]);
							below=first*second;
							below=Math.sqrt(below);
						}
							sim=above/below;
							fileWriter.println(list_name[j].find_index(j).data+sim);//System.out.println(list_name[j].find_index(j).data+sim);
							sim=0;above=0;first=0;second=0;below=0;
					}	
				}//end of vsm_doc_sim
				if(str.equals("vsm_query_rel")){//if query is vsm_query_rel
					int count_words=count_words(read);
					while (st.hasMoreTokens()) {
					   	String s=st.nextToken();
					    l.append2(s,id);//assign the list the given words
					    id++;
					}
					int space_model[][]=create_space_model(all_list,lists,column_edge,line_edge);
					int array[]=new int[column_edge];
					for(int j=0;j<column_edge;j++){
						Node n=all_list.find_index(j);
						String st1=n.getData();
						for(int k=0;k<count_words-1;k++){
							Node m=l.find_index(k);
							String st2=m.getData();
							if(st1.equals(st2))
								cnt++;
						}
						array[j]=cnt;//assign the array how many is there is the lists
						cnt=0;
					}
					for(int j=0;j<line_edge;j++){//calculate according to model
						for(int k=0;k<column_edge;k++){
							above=above+space_model[k][j]*array[k];
							first=first+(space_model[k][j]*space_model[k][j]);
							second=second+(array[k]*array[k]);
							below=first*second;
							below=Math.sqrt(below);
						}
						if(below!=0)
							sim=above/below;
						fileWriter.println(list_name[j].find_index(j).data+sim);
						sim=0;above=0;first=0;second=0;below=0;
					}	
				}
			}//end of while
		}//end of for
		fileWriter.close();
		fileReader.close();
	}//end of function
	
	public static void main(String[] args){
		LList stop_list;
		Main mn=new Main();
		stop_list=mn.find_stops();//find the stop words firstly
		mn.Load(stop_list);//Load the data and command
		
	}
}