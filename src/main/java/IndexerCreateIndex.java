import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map.Entry;

public class IndexerCreateIndex {
    static void write_to_file() throws Exception{
    	System.out.println(IndexerMain.index.size());
        try
        {
        	System.out.println("Creating File");
            File file = new File(IndexerMain.create_page_count+".txt");
            IndexerMain.create_page_count++;
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Entry<String, HashMap<String, IndexerTags>> entry : IndexerMain.index.entrySet())
            {
                String word_name=entry.getKey();
                HashMap<String, IndexerTags> word_index = new HashMap<String, IndexerTags>(entry.getValue());
                bw.write(word_name+"-");
                for(Entry<String, IndexerTags> loc_entry:word_index.entrySet())
                {
                    String document_name = loc_entry.getKey();
                    IndexerTags obj = loc_entry.getValue();
                    int idf=obj.title+obj.body+obj.category+obj.info_box;
                    String append=document_name+":"+idf;
                    if(obj.title!=0)
                        append+="t"+obj.title;
                    if(obj.body!=0)
                        append+="b"+obj.body;
                    if(obj.category!=0)
                        append+="c"+obj.category;
                    if(obj.info_box!=0)
                       append+="i"+obj.info_box;
                   append+=";";
                    bw.write(append);
                }
                bw.write("\n");
            }
            bw.close();
            IndexerMain.index.clear();
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
}
