import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FIX_DOCID {
	static String id,term;
	static long secondary_index_count=0,tertiary_index_count=0,secondary_offset=0,tertiary_offset=0,secondary_offset_tostore=0,tertiary_offset_tostore=0;;
	static BufferedWriter buffered_writer, buffered_writer_secondary, buffered_writer_tertiary;
	public static void main(String[] args) throws IOException {
		int secondary_limit=1000,tertiary_limit=100;
		File docid_term=new File("DOCID-TERM-PRIMARY1.txt"),docid_term_secondary=new File("DOCID-TERM-SECONDARY1.txt"),docid_term_tertiary=new File("DOCID-TERM-TERTIARY1.txt");
    	FileWriter docterm_writer = new FileWriter(docid_term.getAbsoluteFile()),docterm_writer_secondary = new FileWriter(docid_term_secondary.getAbsoluteFile()),docterm_writer_tertiary = new FileWriter(docid_term_tertiary.getAbsoluteFile());
    	buffered_writer=new BufferedWriter(docterm_writer);
    	buffered_writer_secondary=new BufferedWriter(docterm_writer_secondary);
    	buffered_writer_tertiary=new BufferedWriter(docterm_writer_tertiary);
    	if (!docid_term.exists())
            docid_term.createNewFile();
    	if (!docid_term_secondary.exists())
            docid_term_secondary.createNewFile();
    	if (!docid_term_tertiary.exists())
            docid_term_tertiary.createNewFile();
		
		FileReader read_tertiary_title = new FileReader("DOCID-TERM-PRIMARY.txt");
	     BufferedReader buffered_reader = new BufferedReader(read_tertiary_title);
	     String read_next_line;
	     
	     String regex="[^a-zA-Z0-9\n]";
	     try
	    	{  
	     while((read_next_line=buffered_reader.readLine())!=null)
	     {
	    	 id=read_next_line.split("-")[0];
	    	 term=read_next_line.split("-")[1].replaceAll(regex, " ");
	    	
	    	 if(id.length()!=0 && term.length()!=0)
	    	 {
	    	 String primary_docid_term_entry=id+"="+term;
	    	 buffered_writer.write(primary_docid_term_entry);
	    	 secondary_index_count++;
	    	 if(secondary_index_count%secondary_limit==0)
	    	 {
	    		 secondary_index_count=0;
	    		 
	    		 
	    		String write_record_to_secondary_file=id+"="+secondary_offset_tostore+"\n";
             	secondary_offset_tostore=secondary_offset;
             	buffered_writer_secondary.write(write_record_to_secondary_file);
             	tertiary_index_count++;
             	if(tertiary_index_count%tertiary_limit==0)
             	{
             		tertiary_index_count=0;
             		String write_record_to_tertiary_file=id+"="+tertiary_offset_tostore+"\n";
             		tertiary_offset_tostore=tertiary_offset;
             		buffered_writer_tertiary.write(write_record_to_tertiary_file);
             	}
             	tertiary_offset+=write_record_to_secondary_file.length();
             }
             secondary_offset+=primary_docid_term_entry.length();
	    	 }
	    	 }
	    	
	     }
	     catch(Exception e)
	    	{
	    	//	System.out.println(read_next_line);
	    	}
    	 String write_record_to_secondary_file=id+"="+secondary_offset_tostore+"\n";
    	 buffered_writer_secondary.write(write_record_to_secondary_file);
    	 String write_record_to_tertiary_file=id+"="+INDEXER_MAIN.tertiary_offset_tostore+"\n";
 		 buffered_writer_tertiary.write(write_record_to_tertiary_file);
	     buffered_reader.close();
	     buffered_writer.close();
	     buffered_writer_secondary.close();
	     buffered_writer_tertiary.close();
	     }
	     
	}

