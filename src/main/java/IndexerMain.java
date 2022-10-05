import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;
@SuppressWarnings("unused")
public class IndexerMain {
	static long secondary_index_count=0,tertiary_index_count=0,secondary_offset=0,tertiary_offset=0,secondary_offset_tostore=0,tertiary_offset_tostore=0;;
	static BufferedWriter buffered_writer, buffered_writer_secondary, buffered_writer_tertiary;
	public static Map<String,Integer> stopword = new HashMap<String,Integer>();
    static Scanner read=null;
    public static Map<String, HashMap<String, IndexerTags>> index = new TreeMap<String,HashMap<String, IndexerTags>>();
    public static int create_page_count=1;
    
    public static void main(String[] args) throws IOException {
    	
    	
    	    File file_count=new File("file_count.txt");
    	    FileWriter file_writer = new FileWriter(file_count.getAbsoluteFile());
    	    BufferedWriter br=new BufferedWriter(file_writer);
    	    if(!file_count.exists())
    	    {
    	    	file_count.createNewFile();
    	    }
    	
    	    
    	File docid_term=new File("DOCID-TERM-PRIMARY.txt"),docid_term_secondary=new File("DOCID-TERM-SECONDARY.txt"),docid_term_tertiary=new File("DOCID-TERM-TERTIARY.txt");
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
        long start = System.currentTimeMillis();
        try{
            read= new Scanner(new File("stopword.txt"));
            read.useDelimiter("\n");
            while(read.hasNext()){
            	String temp=read.next();
                stopword.put(temp.trim(),1);
            }
        	
         File inputFile=new File(args[0]);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            IndexerSaxParser IndexerSaxParser = new IndexerSaxParser();
            saxParser.parse(inputFile, IndexerSaxParser);
            long end = System.currentTimeMillis();
            buffered_writer.close();
            buffered_writer_secondary.close();
            buffered_writer_tertiary.close();
            create_page_count--;
            String s="";
            s+=create_page_count;
            br.write(s);
            br.close();
            System.out.println((end - start)/1000 + " sec");
            
        }
        catch(Exception e){
        	e.printStackTrace();
        }
    }
}
