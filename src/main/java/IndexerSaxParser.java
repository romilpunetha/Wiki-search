import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class IndexerSaxParser extends DefaultHandler{
    boolean page=false;
    boolean title=false;
    boolean realid=false;
    boolean revision=false;
    boolean id=false;
    boolean text=false;
    static int page_limit=25000,secondary_limit=1000,tertiary_limit=100;
    static String title_data = null,id_data;
    static StringBuilder title_d ;
    int counter=0;
    
    @Override
    public void startElement(String uri,String localName,String qName,Attributes attri) throws SAXException{
        if(qName.equalsIgnoreCase("page")){
            page=true;
            counter++;
        }
        if(qName.equalsIgnoreCase("title")){
            title=true;
        }
        if(qName.equalsIgnoreCase("revision")){
            revision=true;
        }
        if(qName.equalsIgnoreCase("id") && !revision){
            id=true;
        }
        if(qName.equalsIgnoreCase("text")){
            text=true;
        }
    }
    @Override
    public void characters(char ch[], int start, int length) throws SAXException
    {
        try{
            if(title){
                title_data=new String(ch,start,length);
            }
            else if(id){
                id_data=new String(ch,start,length);
                IndexerParser.parse_id(id_data);
                String regex="[^a-zA-Z0-9]";
                title_d = new StringBuilder(title_data.replaceAll(regex, " "));
                if(title_data.length()!=0)
                {
                IndexerParser.parse_title(title_d);
                String primary_docid_term_entry=id_data+"-"+title_d+"\n";
                IndexerMain.buffered_writer.write(primary_docid_term_entry);
                IndexerMain.secondary_index_count++;
               
                if(IndexerMain.secondary_index_count%secondary_limit==0)
                {
                	IndexerMain.secondary_index_count=0;
                	String write_record_to_secondary_file=primary_docid_term_entry.substring(0, primary_docid_term_entry.indexOf("-")+1)+ IndexerMain.secondary_offset_tostore+"\n";
                	IndexerMain.secondary_offset_tostore= IndexerMain.secondary_offset;
                	IndexerMain.buffered_writer_secondary.write(write_record_to_secondary_file);
                	IndexerMain.tertiary_index_count++;
                	if(IndexerMain.tertiary_index_count%tertiary_limit==0)
                	{
                		IndexerMain.tertiary_index_count=0;
                		String write_record_to_tertiary_file=write_record_to_secondary_file.substring(0, write_record_to_secondary_file.indexOf("-")+1)+ IndexerMain.tertiary_offset_tostore+"\n";
                		IndexerMain.tertiary_offset_tostore= IndexerMain.tertiary_offset;
                		IndexerMain.buffered_writer_tertiary.write(write_record_to_tertiary_file);
                	}
                	IndexerMain.tertiary_offset+=write_record_to_secondary_file.length();
                }
                IndexerMain.secondary_offset+=primary_docid_term_entry.length();
                }
            }
            else if(text){
                StringBuilder text_data= new StringBuilder(new String(ch,start,length));
                IndexerParser.parse_text(text_data);
            }
        }
        catch(Exception e){
        }
    }

    public void endElement(String uri,String localName,String qName) throws SAXException{
        if(qName.equalsIgnoreCase("page")){
            page=false;
            if(counter==page_limit)
            {
            	try {
					IndexerCreateIndex.write_to_file();
				} catch (Exception e) {
					e.printStackTrace();
				}
            	counter=0;
            }
        }
        if(qName.equalsIgnoreCase("title")){
            title=false;
        }
        if(qName.equalsIgnoreCase("revision")){
            revision=false;
        }
        if(qName.equalsIgnoreCase("id")){
            id=false;
        }
        if(qName.equalsIgnoreCase("text")){
            text=false;
        }
        if(qName.equalsIgnoreCase("mediawiki")){
            try {
            	 String primary_docid_term_entry=id_data+"-"+title_d+"\n";
            	 String write_record_to_secondary_file=primary_docid_term_entry.substring(0, primary_docid_term_entry.indexOf("-")+1)+ IndexerMain.secondary_offset_tostore+"\n";
            	 IndexerMain.buffered_writer_secondary.write(write_record_to_secondary_file);
            	 String write_record_to_tertiary_file=write_record_to_secondary_file.substring(0, write_record_to_secondary_file.indexOf("-")+1)+ IndexerMain.tertiary_offset_tostore+"\n";
         		 IndexerMain.buffered_writer_tertiary.write(write_record_to_tertiary_file);
                 IndexerCreateIndex.write_to_file();
            } catch (Exception ex) {
                Logger.getLogger(IndexerSaxParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            IndexerParser.title.setLength(0);
            IndexerParser.text.setLength(0);
            IndexerParser.info.setLength(0);
            IndexerParser.cat.setLength(0);
        }
    }
}
