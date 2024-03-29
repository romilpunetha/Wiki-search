import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

class DocCount
{
	String doc;
double score;
int tf;
DocCount(int tf, double idf, String doc)
{
	this.score=tf*idf;
	this.tf=tf;
	this.doc=doc;
	
}
}


class RankComparator implements Comparator<DocCount>
{
    @Override
    public int compare(DocCount a, DocCount b)
    {
    	return (int)(b.score-a.score);
    }
}

public class SearchRanking
{
	
	public static String get_offset(int term, ArrayList<PostingOffset> index)
	{
		int start=0,end=index.size();
		while(start<end)
		{
			int mid=(start+end)/2;
			int val=Integer.parseInt(index.get(mid).posting_word);
			
			if(val==term)
			{
				return index.get(mid).posting_word+"-"+index.get(mid).posting_offset;
			}
			else if(term<val)
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
	
	public static String get_docname(String temp) throws IOException
	{
		/*String word_offset_secondary[]=get_offset(Integer.parseInt(temp),tertiary_title).split("-");
		long secondary_offset=Long.parseLong(word_offset_secondary[1]);
		read_secondary_title.seek(0);
		read_secondary_title.seek(secondary_offset);
		secondary_title.clear();
		for(int i=0;i<=IndexerSaxParser.tertiary_limit;i++)
		{
			String read_from_secondary_file=read_secondary_title.readLine();
			if(read_from_secondary_file==null)
			{
				break;
			}
			String tempe[]=read_from_secondary_file.split("-");
			PostingOffset temp_obj=new PostingOffset(tempe[0],tempe[1]);
			secondary_title.add(temp_obj);
		}
		*/
		String word_offset_primary[]=get_offset(Integer.parseInt(temp),secondary_title).split("-");
		long primary_offset=Long.parseLong(word_offset_primary[1]);
		read_primary_title.seek(0);
		read_primary_title.seek(primary_offset);
		primary_title.clear();
		for(int i = 0; i<= IndexerSaxParser.secondary_limit; i++)
		{
			String read_from_primary_file=read_primary_title.readLine();
			if(read_from_primary_file==null)
			{
				break;
			}
			String tempe[]=read_from_primary_file.split("-");
			PostingOffset temp_obj=new PostingOffset(tempe[0],tempe[1]);
			primary_title.add(temp_obj);
		}
		
		String result[]=get_offset(Integer.parseInt(temp),primary_title).split("-");
		return result[1];
	}
	
	static ArrayList<PostingOffset> primary_title=new ArrayList<>();
	static ArrayList<PostingOffset> secondary_title=new ArrayList<>();
	//static ArrayList<PostingOffset> tertiary_title=new ArrayList<>();
	static RandomAccessFile read_primary_title,read_secondary_title;
	
public static void get_rank(ArrayList<String> final_posting_lists) throws IOException 
	{
	
	/*read_secondary_title=new RandomAccessFile("DOCID-TERM-SECONDARY.txt","r");
	
	 FileReader read_tertiary_title = new FileReader("DOCID-TERM-TERTIARY.txt");
     BufferedReader buffered_reader = new BufferedReader(read_tertiary_title);
     String read_next_line;
     while((read_next_line=buffered_reader.readLine())!=null)
     {
     	String term_offset[]=read_next_line.split("-");
     	tertiary_title.add(new PostingOffset(term_offset[0],term_offset[1]));
     }
     buffered_reader.close();*/
	
	
	Comparator<? super DocCount> comp=new RankComparator();
	PriorityQueue<DocCount> final_rank=new PriorityQueue<>(comp);
	HashMap<String, DocCount> rank_map=new HashMap<>();
	for(String postings:final_posting_lists)
		{
		String temp[]=postings.split("-");
		int occurance=Integer.parseInt(temp[2].substring(0, temp[2].indexOf("|")));
		double idf=Math.log(1+ 160000000/occurance);
		boolean is_field_query=false;
		if(!temp[0].equals("z"))
		{
			is_field_query=true;
		}
		String pslist=temp[2].substring(temp[2].indexOf("|")+1);
		String temp2[]=pslist.split(";");
		for(String t:temp2)
			{
			String term=t.split(":")[0];
			String contains_tf=t.split(":")[1];
			String tf="";
			int i=0;
			while(contains_tf.charAt(i)>=48 && contains_tf.charAt(i)<=57)
			{
				tf+=contains_tf.charAt(i);
				i++;
			}
			DocCount rank_var=new DocCount(Integer.parseInt(tf),idf,term);
			if(is_field_query && t.contains(temp[0]))
			{
				if(rank_map.containsKey(term))
				{
					double prev_score=rank_map.get(term).score;
					rank_map.remove(term);
					rank_var.score+=2*prev_score;
					rank_map.put(term,rank_var);
			
				}
				else 
				{
					rank_map.put(term,rank_var);
				}
				
			}
			else if(!is_field_query)
			{
				if(rank_map.containsKey(term))
				{
					double prev_score=rank_map.get(term).score;
					rank_map.remove(term);
					rank_var.score+=2*prev_score;
					rank_map.put(term,rank_var);
				}
				else 
				{
					rank_map.put(term,rank_var);
				}
			}
			}
		}
	for(Entry<String, DocCount> t:rank_map.entrySet())
	{
		final_rank.add(t.getValue());
	}
	SearchQueryMain.end= System.currentTimeMillis();
	for(int p=0;p<10 && !final_rank.isEmpty();p++)
	{
		DocCount temp=final_rank.poll();
		String doc_name=get_docname(temp.doc);
		if(doc_name.toLowerCase().contains("wikipedia"))
		{
			p--;
		}
		else
		{
		System.out.println(temp.doc+" : "+doc_name);
		}
	}
	System.out.println((SearchQueryMain.end - SearchQueryMain.start)/1000 + " sec");
	SearchQueryMain.end=0;

	}
}
