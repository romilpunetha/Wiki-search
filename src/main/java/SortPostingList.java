import java.util.Comparator;
import java.util.PriorityQueue;

public class SortPostingList {
public static String sorting_posting(String posting_list)
{
	int pqsize=1000,posting_list_size=500, counter=0;
	ScoreCalculationVariables temp;
	 Comparator<? super ScoreCalculationVariables> p1=new sortdoc();
	String sorted_posting_list="";
	if(posting_list=="")
		return sorted_posting_list;
	PriorityQueue<ScoreCalculationVariables> postingq =  new PriorityQueue<>(pqsize,p1);
	String s2[]=posting_list.split(";");
	int size=s2.length;
	sorted_posting_list+=size+"|";
	for(int i=0;i<size;i++)
	{
		int resultant_score=calculate_score(s2[i]);
		temp=new ScoreCalculationVariables();
		temp.score=resultant_score;
		temp.posting_list=s2[i];
		postingq.add(temp);
	}
	while(!postingq.isEmpty() && counter<posting_list_size)
	{
		sorted_posting_list+=postingq.poll().posting_list+";";
		counter++;
	}
	return sorted_posting_list;
}

public static int calculate_score(String posting_token)
{
	int score=0,i=0,size=posting_token.length(),t=20,b=10,c=5,info=30;
	String val="";
	while(i<size && posting_token.charAt(i)>='0' && posting_token.charAt(i)<='9')
	{
		i++;
	}
	while(i<size)
	{
		switch(posting_token.charAt(i))
		{
		case 't':
			val="0";
			i++;
			while(i<size && posting_token.charAt(i)>='0' && posting_token.charAt(i)<='9')
			{
				val+=posting_token.charAt(i);
				i++;
			}
			if(Integer.parseInt(val)!=0)
			{
				score+=t*Integer.parseInt(val);
			}
			break;
		case 'b':
			val="0";
			i++;
			while(i<size && posting_token.charAt(i)>='0' && posting_token.charAt(i)<='9')
			{
				val+=posting_token.charAt(i);
				i++;
			}
			if(Integer.parseInt(val)!=0)
			{
				score+=b*Integer.parseInt(val);
			}
			break;
		case 'c':
			val="0";
			i++;
			while(i<size && posting_token.charAt(i)>='0' && posting_token.charAt(i)<='9')
			{
				val+=posting_token.charAt(i);
				i++;
			}
			if(Integer.parseInt(val)!=0)
			{
				score+=c*Integer.parseInt(val);
			}
			break;
		case 'i':
			val="0";
			i++;
			while(i<size && posting_token.charAt(i)>='0' && posting_token.charAt(i)<='9')
			{
				val+=posting_token.charAt(i);
				i++;
			}
			if(Integer.parseInt(val)!=0)
			{
				score+=info*Integer.parseInt(val);
			}
			break;
		default:i++;
				break;
		}
	}
	return score;
}
}
