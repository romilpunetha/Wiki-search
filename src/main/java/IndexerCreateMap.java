import java.util.HashMap;
public class IndexerCreateMap {
    public static void title_mapping(String str) throws Exception{
    	
        if(!IndexerMain.stopword.containsKey(str))
    	{
            IndexerStemmer stemmer_object = new IndexerStemmer();
            stemmer_object.add(str.toCharArray(),str.length());
            str=stemmer_object.stem();
            if(IndexerMain.index.get(str)==null){
                HashMap<String, IndexerTags> sub_map=new HashMap<>();
                IndexerMain.index.put(str, sub_map);
            }
            if(IndexerMain.index.get(str)==null || !(IndexerMain.index.get(str).containsKey(IndexerParser.curId))){
                IndexerMain.index.get(str).put(IndexerParser.curId, new IndexerTags());
            }
            IndexerMain.index.get(str).get(IndexerParser.curId).title++;
        }
    }
    public static void info_mapping(String str) throws Exception{
    	
        if(!IndexerMain.stopword.containsKey(str))
    	{
            IndexerStemmer stemmer_object = new IndexerStemmer();
            stemmer_object.add(str.toCharArray(),str.length());
            str=stemmer_object.stem();
            if(!(str.length()<=2 && str.isEmpty())){
                if(IndexerMain.index.get(str)==null){
                    HashMap<String, IndexerTags> sub_map= new HashMap<>();
                    IndexerMain.index.put(str,sub_map);
                }
                if(IndexerMain.index.get(str)==null || !(IndexerMain.index.get(str).containsKey(IndexerParser.curId))){
                    IndexerMain.index.get(str).put(IndexerParser.curId, new IndexerTags());
                }
                IndexerMain.index.get(str).get(IndexerParser.curId).info_box++;
            }
        }
    }
    public static void cat_mapping(String str) throws Exception{
    	
        if(!IndexerMain.stopword.containsKey(str))
    	{
        	
            IndexerStemmer stemmer_object = new IndexerStemmer();
            stemmer_object.add(str.toCharArray(),str.length());
            str=stemmer_object.stem();
            if(!(str.length()<=2)){
                if(IndexerMain.index.get(str)==null){
                    HashMap<String, IndexerTags> sub_map;
                    sub_map = new HashMap<>();
                    IndexerMain.index.put(str, sub_map);
                }
                if(IndexerMain.index.get(str)==null || !(IndexerMain.index.get(str).containsKey(IndexerParser.curId))){
                    IndexerMain.index.get(str).put(IndexerParser.curId, new IndexerTags());
                }
                IndexerMain.index.get(str).get(IndexerParser.curId).category++;
            }
        }
    }
    public static void text_mapping(String str) throws Exception{
    	
        if(!IndexerMain.stopword.containsKey(str))
    	{
            IndexerStemmer stemmer_object = new IndexerStemmer();
            stemmer_object.add(str.toCharArray(),str.length());
            str=stemmer_object.stem();
            if(!(str.length()<=2)){
                if(IndexerMain.index.get(str)==null){
                    HashMap<String, IndexerTags> sub_map;
                    sub_map = new HashMap<>();
                    IndexerMain.index.put(str, sub_map);
                }
                if(IndexerMain.index.get(str)==null || !(IndexerMain.index.get(str).containsKey(IndexerParser.curId))){
                    IndexerMain.index.get(str).put(IndexerParser.curId, new IndexerTags());
                }
                IndexerMain.index.get(str).get(IndexerParser.curId).body++;
            }
        }
    }
}
