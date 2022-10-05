import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class PostingOffset
{
		String posting_word;
		String posting_offset;
		PostingOffset(String word, String offset)
		{
		this.posting_word=word;
		this.posting_offset=offset;
		}
}

public class SearchQueryMain {
	
	public static void get_docid(String term,String field_query) throws IOException
	{
		 
		String word_offset_secondary[]=get_offset(term,tertiary_index).split("-");
		long secondary_offset=Long.parseLong(word_offset_secondary[1]);
		read_secondary_index.seek(secondary_offset);
		secondary_index.clear();
		for(int i = 0; i<= IndexMerge.tertiary_limit; i++)
		{
			String read_from_secondary_file=read_secondary_index.readLine();
			if(read_from_secondary_file==null)
			{
				break;
			}
			String temp[]=read_from_secondary_file.split("-");
			PostingOffset temp_obj=new PostingOffset(temp[0],temp[1]);
			secondary_index.add(temp_obj);
		}
		String word_offset_primary[]=get_offset(term,secondary_index).split("-");
		long primary_offset=Long.parseLong(word_offset_primary[1]);
		read_primary_index.seek(primary_offset);
		primary_index.clear();
		for(int i = 0; i<= IndexMerge.secondary_limit; i++)
		{
			String read_from_primary_file=read_primary_index.readLine();
			if(read_from_primary_file==null)
			{
				break;
			}
			String temp[]=read_from_primary_file.split("-");
			PostingOffset temp_obj=new PostingOffset(temp[0],temp[1]);
			primary_index.add(temp_obj);
		}
		String result[]=get_offset(term,primary_index).split("-");
		if(term.compareTo(result[0])==0)
		{
		final_posting_lists.add(field_query+"-"+result[0]+"-"+result[1]);
		}
		else 
		{
			System.out.println("'"+term+"' not found");
		}
	}
	
	public static String get_offset(String term, ArrayList<PostingOffset> index)
	{
		int start=0,end=index.size();
		while(start<end)
		{
			int mid=(start+end)/2;
			int val=term.compareTo(index.get(mid).posting_word);
			if(val==0)
			{
				return index.get(mid).posting_word+"-"+index.get(mid).posting_offset;
			}
			else if(val<0)
			{
				end=mid;
			}
			else 
			{
				start=mid+1;
			}
		}
		return index.get(start).posting_word+"-"+index.get(start).posting_offset;
	}
	static ArrayList<String> final_posting_lists=new ArrayList<>();
	static ArrayList<PostingOffset> primary_index=new ArrayList<>();
	static ArrayList<PostingOffset> secondary_index=new ArrayList<>();
	static ArrayList<PostingOffset> tertiary_index=new ArrayList<>();
	static RandomAccessFile read_primary_index,read_secondary_index;
	public static Map<String,Integer> stopword = new HashMap<String,Integer>();
	static Scanner read=null;
	public static long end,start;
	public static void main(String[] args) throws Exception {
		read_primary_index = new RandomAccessFile("PRIMARY_INDEX.txt","r");
        read_secondary_index = new RandomAccessFile("SECONDARY_INDEX.txt","r");
		String term;
		IndexerStemmer stemmer_object = new IndexerStemmer();
		read= new Scanner(new File("stopword.txt"));
        read.useDelimiter("\n");
        while(read.hasNext()){
        	String temp=read.next();
            stopword.put(temp.trim(),1);
        }
        FileReader read_tertiary_index = new FileReader("TERTIARY_INDEX.txt");
        BufferedReader buffered_reader = new BufferedReader(read_tertiary_index);
        String read_next_line;
        while((read_next_line=buffered_reader.readLine())!=null)
        {
        	String term_offset[]=read_next_line.split("-");
        	tertiary_index.add(new PostingOffset(term_offset[0],term_offset[1]));
        }
        buffered_reader.close();
        System.out.println("Tertiary index created");
        
        FileReader read_secondary_title = new FileReader("DOCID-TERM-SECONDARY.txt");
        buffered_reader = new BufferedReader(read_secondary_title);
        
        System.out.println("Creating DOC-ID secondary index");

        while((read_next_line=buffered_reader.readLine())!=null)
        {
        	String term_offset[]=read_next_line.split("-");
        	SearchRanking.secondary_title.add(new PostingOffset(term_offset[0],term_offset[1]));
        	
        }
        buffered_reader.close();
        System.out.println("DOC-ID secondary index created");
        
        SearchRanking.read_primary_title=new RandomAccessFile("DOCID-TERM-PRIMARY.txt","r");
        String regex="[^a-zA-Z0-9:]";
        System.out.println("Enter search query, q to quit :");
		BufferedReader buffer_reader=new BufferedReader(new InputStreamReader(System.in));
		String search_query;
		while((search_query=buffer_reader.readLine())!="q")
		{
		start=0;
		start = System.currentTimeMillis();  // starting time calculation
		search_query=search_query.replaceAll(regex," ").toLowerCase();
		char is_field_query='f';
		
		String query_tokens[]=search_query.split(" ");
		for(String token: query_tokens)
		{
		
			read_primary_index.seek(0);
			read_secondary_index.seek(0);
			is_field_query='f';
		if(token.contains("t:") || token.contains("b:") || token.contains("i:") || token.contains("c:") || token.contains("r:") || token.contains("e:"))
				is_field_query='t';
		switch(is_field_query)
		{
		case 't':
			term=token.substring(2).trim();
			if(stopword.containsKey(term))
				break;
			stemmer_object.add(term.toCharArray(), term.length());
			term=stemmer_object.stem();
			if(token.contains("t:"))
				get_docid(term,"t");
			else if(token.contains("b:") || token.contains("r:")||token.contains("e:"))
				get_docid(term,"b");
			else if(token.contains("i:"))
				get_docid(term,"i");
			else if(token.contains("c:"))
				get_docid(term,"c");
			break;
		case 'f':
			term=token.trim();
			if(stopword.containsKey(term))
				break;
			stemmer_object.add(term.toCharArray(), term.length());
			term=stemmer_object.stem();
			get_docid(term,"z");
			break;
		}
		}
		SearchRanking.get_rank(final_posting_lists);
		final_posting_lists.clear();
		
		}
		read_primary_index.close();
		read_secondary_index.close();
		SearchRanking.read_secondary_title.close();
		SearchRanking.	read_primary_title.close();
	}
}
