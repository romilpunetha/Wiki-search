import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class INDEX_MERGE {
	static int file_count=0,secondary_limit=1000,tertiary_limit=100,init=0;
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
    	
    	BufferedReader br=new BufferedReader(new FileReader("file_count.txt"));
    	file_count=Integer.parseInt(br.readLine());
    	br.close();
    	
    	long start = System.currentTimeMillis();
    	long secondary_offset=0,tertiary_offset=0,secondary_offset_tostore=0,tertiary_offset_tostore=0;
    	Comparator<? super INDEX_POSTING_LIST> record_comparator= new INDEX_COMPARATOR();
        PriorityQueue<INDEX_POSTING_LIST> priority_queue =  new PriorityQueue<>(file_count,record_comparator);
        INDEX_POSTING_LIST score_calculation_object,list_records,second_last_word, head_list;
        int i=0,count=0,secondary_file_id=0,secondary_index_create_after_count=0,tertiary_index_create_after_count=0,primary_file_id;
        String term_from_file,list_from_record,record_word,record_list,posting_list = null,secondary_posting_list=null,secondary_term_list=null,write_record_to_file="",primary_post,primary_term;
        boolean is_queue_empty=false;
        boolean[] is_file_complete = new boolean[file_count+1];
        BufferedReader buffered_reader[] = new BufferedReader[file_count+1];
        File output_file=new File("PRIMARY_INDEX.txt"),secondary_output_file=new File("SECONDARY_INDEX.txt"),tertiary_output_file=new File("TERTIARY_INDEX.txt");
        FileWriter file_writer = new FileWriter(output_file.getAbsoluteFile()),secondary_file_writer = new FileWriter(secondary_output_file.getAbsoluteFile()),tertiary_file_writer = new FileWriter(tertiary_output_file.getAbsoluteFile());
        BufferedWriter buffered_writer=new BufferedWriter(file_writer),secondary_buffer_writer= new BufferedWriter(secondary_file_writer),tertiary_buffer_writer= new BufferedWriter(tertiary_file_writer);
        if (!output_file.exists())
          output_file.createNewFile();
        if (!secondary_output_file.exists())
          secondary_output_file.createNewFile();
        if (!tertiary_output_file.exists())
            tertiary_output_file.createNewFile();
        i=file_count;
        while(i>0)
        {
            is_file_complete[i]=false;
            buffered_reader[i] = new BufferedReader(new FileReader(i+init+".txt"));
            i--;
        }
        System.out.println("Done opening file");
        i=file_count;
       while(i>0)
        {
            if(is_file_complete[i]==false)
            {
            	posting_list=buffered_reader[i].readLine();
                if(posting_list.compareTo("")!=0)
                {
                    record_word=posting_list.substring(0,posting_list.indexOf('-'));
                    record_list=posting_list.substring(posting_list.indexOf('-')+1);
                    list_records= new INDEX_POSTING_LIST();
                    list_records.term=record_word;
                    list_records.post=record_list;
                    list_records.fid=i;
                    priority_queue.add(list_records);
                }
            }
            i--;
        }
       head_list=priority_queue.poll();
       primary_post=head_list.post;
       primary_term=head_list.term;
       primary_file_id=head_list.fid;
        while( is_queue_empty||count<file_count)
        {
            if(is_file_complete[primary_file_id])
            {
                if(!priority_queue.isEmpty())
                {
                    second_last_word=priority_queue.poll();
                    secondary_file_id=second_last_word.fid;
                    secondary_posting_list=second_last_word.post;
                    secondary_term_list=second_last_word.term;
                }
                is_queue_empty=false;
            }
            else
            {
                String record_from_file=buffered_reader[primary_file_id].readLine();
                if(record_from_file!=null){
                    term_from_file=record_from_file.substring(0,record_from_file.indexOf('-'));
                    list_from_record=record_from_file.substring(record_from_file.indexOf('-')+1);
                    score_calculation_object = new INDEX_POSTING_LIST();
                    score_calculation_object.term=term_from_file;
                    score_calculation_object.post=list_from_record;
                    score_calculation_object.fid=primary_file_id;
                    priority_queue.add(score_calculation_object);
                    second_last_word=priority_queue.poll();
                    secondary_file_id=second_last_word.fid;
                    secondary_posting_list=second_last_word.post;
                    secondary_term_list=second_last_word.term;
                }
                else{
                    is_file_complete[primary_file_id]=true;
                    count++;
                    buffered_reader[primary_file_id].close();
                    is_queue_empty=true;
                }
            }
            if(!is_queue_empty)
            {
                    if(count<file_count&&primary_term.equals(secondary_term_list))
                    {
                        primary_post+=secondary_posting_list;
                        primary_file_id=secondary_file_id;
                    }
                    else
                    {
                    	primary_post=SORT_POSTING_LIST.sorting_posting(primary_post);
                        write_record_to_file="";
                        write_record_to_file=primary_term+"-"+primary_post+"\n";
                        secondary_index_create_after_count++;
                        buffered_writer.write(write_record_to_file);
                        if(secondary_index_create_after_count%secondary_limit==0)
                        {
                        	secondary_index_create_after_count=0;
                        	String write_record_to_secondary_file=write_record_to_file.substring(0, write_record_to_file.indexOf("-")+1)+secondary_offset_tostore+"\n";
                        	secondary_offset_tostore=secondary_offset;
                            secondary_buffer_writer.write(write_record_to_secondary_file);
                            tertiary_index_create_after_count++;
                            if(tertiary_index_create_after_count%tertiary_limit==0)
                            {
                            	tertiary_index_create_after_count=0;
                            	String write_record_to_tertiary_file=write_record_to_secondary_file.substring(0, write_record_to_secondary_file.indexOf("-")+1)+tertiary_offset_tostore+"\n";
                            	tertiary_offset_tostore=tertiary_offset;
                            	tertiary_buffer_writer.write(write_record_to_tertiary_file);
                            }
                            tertiary_offset+=write_record_to_secondary_file.length();
                        }
                        secondary_offset+=write_record_to_file.length();
                        primary_term=secondary_term_list;
                        primary_post=secondary_posting_list;
                        primary_file_id=secondary_file_id;
                    }
                }
        }
        String write_record_to_secondary_file=write_record_to_file.substring(0, write_record_to_file.indexOf("-")+1)+secondary_offset_tostore+"\n";
        secondary_buffer_writer.write(write_record_to_secondary_file);
        String write_record_to_tertiary_file=write_record_to_secondary_file.substring(0, write_record_to_secondary_file.indexOf("-")+1)+tertiary_offset_tostore+"\n";
        tertiary_buffer_writer.write(write_record_to_tertiary_file);
                        buffered_writer.close();
                        secondary_buffer_writer.close();
                        tertiary_buffer_writer.close();
                        long end = System.currentTimeMillis();
               		 System.out.println((end - start)/1000 + " sec");
    }
}
