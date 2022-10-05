public class IndexerParser {
    static String curId;
    static StringBuilder title=new StringBuilder();
    static StringBuilder text=new StringBuilder();
    static StringBuilder info=new StringBuilder();
    static StringBuilder cat=new StringBuilder();
    public static void parse_id(String id_data) throws Exception{
        curId=""+id_data;
    }
    public static void parse_title(StringBuilder title_data) throws Exception{

    	
        for(int i=0;i<title_data.length();i++)
        {
            char cur=title_data.charAt(i);
            if((cur>=65&&cur<=90)||(cur>=97&&cur<=122)||(cur>=48&&cur<=57))
            {
                title.append(cur);
            }
            else
            {
                String lower=title.toString().toLowerCase();
                lower=lower.trim();
                if(!(lower.length()==0||lower.isEmpty())){
                    IndexerCreateMap.title_mapping(lower);
                }
                title.setLength(0);
            }
        }
        title.setLength(0);
    }

    public static void parse_text(StringBuilder text_data) throws Exception{
    	int len=text_data.length();
        for(int i=0;i<len;i++)
        {
            char cur=text_data.charAt(i);
            if(cur=='{')
            {
                if((i+9)<len && "{infobox".contains(text_data.substring(i+1, i+9).toLowerCase()))
                { 
                	boolean info_box_parent=true;
                    boolean add_info=false;
                    i+=9;
                    int count=0;
                    while(info_box_parent&&i<len){
                        char current=text_data.charAt(i);
                        if(current=='<')
                        {
                            while(i<len && current!='>')
                            {
                                i++;
                                current=text_data.charAt(i);
                            }
                            i++;
                        }
                        if(current=='['&&text_data.charAt(i+1)=='['){
                            add_info=true;
                            i+=1;
                        }
                        else if(current==']'&&text_data.charAt(i+1)==']'){
                            add_info=false;
                        }
                        if(current=='{'){
                            count-=1;
                        }
                        else if(current=='}'){
                            count+=1;
                        }
                        if(count==2){
                            info_box_parent=false;
                            info.setLength(0);
                        }
                        if(add_info){ 
                            if((current>=65&&current<=90)||(current>=97&&current<=122)||(current>=48&&current<=57))
                            {
                                info.append(current); 
                                if(i==len-1)
                                {
                                	String lower=info.toString().toLowerCase();
                                    lower=lower.trim();
                                  
                                    if(!(lower.length()==0||lower.isEmpty()))
                                    {
                                        IndexerCreateMap.info_mapping(lower);
                                    }
                                    info.setLength(0);
                                }
                            }
                            else
                            {
                                String lower=info.toString().toLowerCase();
                                lower=lower.trim();
                                if(!(lower.length()==0||lower.isEmpty())){
                                    IndexerCreateMap.info_mapping(lower);
                                }
                                info.setLength(0);
                            }
                        }
                        i++;
                    }
                }
                else if("{cite".contains(text_data.substring(i+1, i+6).toLowerCase())&&(i+6)<len){
                	boolean cite_box_parent=true;
                    i+=6;
                    int count=0;
                    while(cite_box_parent&&i<len){
                        char current=text_data.charAt(i);
                        if(current=='{'){
                            count--;
                        }
                        else if(current=='}'){
                            count++;
                        }
                        if(count==2){
                            cite_box_parent=false;
                        }
                        i++;
                    }
                }
                else if("{gr".contains(text_data.substring(i+1, i+4).toLowerCase())&&(i+4)<len){
                    boolean gr_box_parent=true;
                    i+=4;
                    int count=0;
                    while(gr_box_parent&&i<len){
                        char current=text_data.charAt(i);
                        if(current=='{'){
                            count--;
                        }
                        else if(current=='}'){
                            count++;
                        }
                        if(count==2){
                            gr_box_parent=false;
                        }
                        i++;
                    }
                }
                else if("{coord".contains(text_data.substring(i+1, i+7).toLowerCase())&&(i+7)<len){
                    boolean coord_box_parent=true;
                    i+=7;
                    int count=0;
                    while(coord_box_parent&&i<len){
                        char current=text_data.charAt(i);
                        if(current=='{'){
                            count--;
                        }
                        else if(current=='}'){
                            count++;
                        }
                        if(count==2){
                            coord_box_parent=false;
                        }
                        i++;
                    }
                }
            }
            else if(cur=='[')
            {
                if((i+11)<len && "[category:".contains(text_data.substring(i+1, i+11).toLowerCase())){
                
                	boolean cat_box_parent=true;
                    boolean add_cat=true;
                    i+=11;
                    int count=0;
                    while(cat_box_parent&&i<len){
                        char current=text_data.charAt(i);
                        if(current=='{'){
                            count--;
                        }
                        else if(current=='}'){
                            count++;
                        }
                        if(count==2){
                            cat_box_parent=false;
                            cat.setLength(0);
                        }
                        if(add_cat){
                            if((current>=65&&current<=90)||(current>=97&&current<=122)||(current>=48&&current<=57))
                            {
                                cat.append(current);
                                if(i==len-1)
                                {
                                	String lower=cat.toString().toLowerCase();
                                    lower=lower.trim();
                                  
                                    if(!(lower.length()==0||lower.isEmpty()))
                                    {
                                        IndexerCreateMap.cat_mapping(lower);
                                    }
                                    cat.setLength(0);
                                }
                            }
                            else
                            {
                                String lower=cat.toString().toLowerCase();
                             //   System.out.println("category = "+lower);
                                lower=lower.trim();
                                if(!(lower.length()==0||lower.isEmpty())){
                                    IndexerCreateMap.cat_mapping(lower);
                                }
                                cat.setLength(0);
                                add_cat=false;
                            }
                        }
                        i++;
                    }
                }
                else if("[image:".contains(text_data.substring(i+1, i+8).toLowerCase())&&(i+8)<len){
                    boolean image_box_parent=true;
                    i+=8;
                    int count=0;
                    while(image_box_parent&&i<len){
                        char current=text_data.charAt(i);
                        if(current=='['){
                            count--;
                        }
                        else if(current==']'){
                            count++;
                        }
                        if(count==2){
                            image_box_parent=false;
                        }
                        i++;
                    }
                }
                else if("[file:".contains(text_data.substring(i+1, i+7).toLowerCase())&&(i+7)<len)
                {
                    boolean file_box_parent=true;
                    i+=7;
                    int count=0;
                    while(file_box_parent&&i<len){
                        char current=text_data.charAt(i);
                        if(current=='['){
                            count--;
                        }
                        else if(current==']'){
                            count++;
                        }
                        if(count==2){
                            file_box_parent=false;
                        }
                        i++;
                    }
                }
            }
            else if(cur=='<')
            {
                if((i+4)<len && "!--".contains(text_data.substring(i+1, i+4).toLowerCase())){
                    i+=4;
                    int end=text_data.indexOf("-->",i);
                    if(end+2<len&&end>0){
                        i=end+2;
                    }
                }
                else if((i+8)<len && "gallery".contains(text_data.substring(i+1, i+8).toLowerCase())){
                    i+=8;
                    int end=text_data.indexOf("</gallery>",i);
                    if(end+10<len&&end>0){
                        i=end+10;
                    }
                }
                else
                {
                    char current = text_data.charAt(i);
                    while(i<len && current!=' ')
                    {
                        current=text_data.charAt(i);
                        i++;
                    }
                }
            }
            else
            {
                char current = text_data.charAt(i);
                if(current=='#')
                {
                    while(i<len && current!=' ')
                    {
                        current=text_data.charAt(i);
                        i++;
                    }
                }
                else
                {
                    if((current>=65&&current<=90)||(current>=97&&current<=122))
                    {
                        text.append(current);
                        if(i==len-1)
                        {
                        	String lower=text.toString().toLowerCase();
                            lower=lower.trim();
                          
                            if(!(lower.length()==0||lower.isEmpty()))
                            {
                                IndexerCreateMap.text_mapping(lower);
                            }
                            text.setLength(0);
                        }
                    }
                    else
                    {
                        String lower=text.toString().toLowerCase();
                        lower=lower.trim();
                      
                        if(!(lower.length()==0||lower.isEmpty()))
                        {
                            IndexerCreateMap.text_mapping(lower);
                        }
                        text.setLength(0);
                    }
                }
            }
            }
        }
    }
