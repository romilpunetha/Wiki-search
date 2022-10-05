import java.util.Comparator;

public class IndexComparator implements Comparator<IndexPostingList> {
	@Override
	public int compare(IndexPostingList a, IndexPostingList b)
	{
		return a.term.compareTo(b.term);
	}

}
