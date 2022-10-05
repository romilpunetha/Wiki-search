import java.util.Comparator;

public class SCORE_CALCULATION_VARIABLES {
	 	int score;
	    String posting_list;
}
class sortdoc implements Comparator<SCORE_CALCULATION_VARIABLES>
{
    @Override
    public int compare(SCORE_CALCULATION_VARIABLES a, SCORE_CALCULATION_VARIABLES b)
    {
        return b.score-a.score;
    }
}
