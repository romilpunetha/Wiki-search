import java.util.HashMap;
public class INDEXER_CREATE_MAP {
    public static void title_mapping(String str) throws Exception{
    	
        if(!INDEXER_MAIN.stopword.containsKey(str))
    	{
            INDEXER_STEMMER stemmer_object = new INDEXER_STEMMER();
            stemmer_object.add(str.toCharArray(),str.length());
            str=stemmer_object.stem();
            if(INDEXER_MAIN.index.get(str)==null){
                HashMap<String,INDEXER_TAGS> sub_map=new HashMap<>();
                INDEXER_MAIN.index.put(str, sub_map);
            }
            if(INDEXER_MAIN.index.get(str)==null || !(INDEXER_MAIN.index.get(str).containsKey(INDEXER_PARSER.curId))){
                INDEXER_MAIN.index.get(str).put(INDEXER_PARSER.curId, new INDEXER_TAGS());
            }
            INDEXER_MAIN.index.get(str).get(INDEXER_PARSER.curId).title++;
        }
    }
    public static void info_mapping(String str) throws Exception{
    	
        if(!INDEXER_MAIN.stopword.containsKey(str))
    	{
            INDEXER_STEMMER stemmer_object = new INDEXER_STEMMER();
            stemmer_object.add(str.toCharArray(),str.length());
            str=stemmer_object.stem();
            if(!(str.length()<=2 && str.isEmpty())){
                if(INDEXER_MAIN.index.get(str)==null){
                    HashMap<String,INDEXER_TAGS> sub_map= new HashMap<>();
                    INDEXER_MAIN.index.put(str,sub_map);
                }
                if(INDEXER_MAIN.index.get(str)==null || !(INDEXER_MAIN.index.get(str).containsKey(INDEXER_PARSER.curId))){
                    INDEXER_MAIN.index.get(str).put(INDEXER_PARSER.curId, new INDEXER_TAGS());
                }
                INDEXER_MAIN.index.get(str).get(INDEXER_PARSER.curId).info_box++;
            }
        }
    }
    public static void cat_mapping(String str) throws Exception{
    	
        if(!INDEXER_MAIN.stopword.containsKey(str))
    	{
        	
            INDEXER_STEMMER stemmer_object = new INDEXER_STEMMER();
            stemmer_object.add(str.toCharArray(),str.length());
            str=stemmer_object.stem();
            if(!(str.length()<=2)){
                if(INDEXER_MAIN.index.get(str)==null){
                    HashMap<String,INDEXER_TAGS> sub_map;
                    sub_map = new HashMap<>();
                    INDEXER_MAIN.index.put(str, sub_map);
                }
                if(INDEXER_MAIN.index.get(str)==null || !(INDEXER_MAIN.index.get(str).containsKey(INDEXER_PARSER.curId))){
                    INDEXER_MAIN.index.get(str).put(INDEXER_PARSER.curId, new INDEXER_TAGS());
                }
                INDEXER_MAIN.index.get(str).get(INDEXER_PARSER.curId).category++;
            }
        }
    }
    public static void text_mapping(String str) throws Exception{
    	
        if(!INDEXER_MAIN.stopword.containsKey(str))
    	{
            INDEXER_STEMMER stemmer_object = new INDEXER_STEMMER();
            stemmer_object.add(str.toCharArray(),str.length());
            str=stemmer_object.stem();
            if(!(str.length()<=2)){
                if(INDEXER_MAIN.index.get(str)==null){
                    HashMap<String,INDEXER_TAGS> sub_map;
                    sub_map = new HashMap<>();
                    INDEXER_MAIN.index.put(str, sub_map);
                }
                if(INDEXER_MAIN.index.get(str)==null || !(INDEXER_MAIN.index.get(str).containsKey(INDEXER_PARSER.curId))){
                    INDEXER_MAIN.index.get(str).put(INDEXER_PARSER.curId, new INDEXER_TAGS());
                }
                INDEXER_MAIN.index.get(str).get(INDEXER_PARSER.curId).body++;
            }
        }
    }
}
