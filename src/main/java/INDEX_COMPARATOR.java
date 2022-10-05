import java.util.Comparator;

public class INDEX_COMPARATOR implements Comparator<INDEX_POSTING_LIST> {
	@Override
	public int compare(INDEX_POSTING_LIST a,INDEX_POSTING_LIST b)
	{
		return a.term.compareTo(b.term);
	}

}
