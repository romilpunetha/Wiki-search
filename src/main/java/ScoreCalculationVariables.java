import java.util.Comparator;

public class ScoreCalculationVariables {
	 	int score;
	    String posting_list;
}
class sortdoc implements Comparator<ScoreCalculationVariables>
{
    @Override
    public int compare(ScoreCalculationVariables a, ScoreCalculationVariables b)
    {
        return b.score-a.score;
    }
}
