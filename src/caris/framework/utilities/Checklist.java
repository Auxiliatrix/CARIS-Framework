package caris.framework.utilities;

import java.util.ArrayList;
import java.util.List;

public class Checklist<T> {
	
	private Checkpoint<T>[] checkpoints;
	
	@SafeVarargs
	public Checklist(Checkpoint<T>...checkpoints) {
		this.checkpoints = checkpoints;
	}
	
	public Inspection check(T t) {
		List<Boolean> checklistResults = new ArrayList<Boolean>();
		for( Checkpoint<T> checkpoint : checkpoints ) {
			checklistResults.add(checkpoint.inspect(t));
		}
		return new Inspection(checklistResults);
	}
	
	public interface Checkpoint<T> {
		public boolean inspect(T t);
	}
	
	public class Inspection {
		private List<Boolean> checkpointResults;
		
		public Inspection(List<Boolean> checkpointResults) {
			this.checkpointResults = checkpointResults;
		}
		
		public boolean get(int index) {
			return checkpointResults.get(index);
		}
		
		public int size() {
			return checkpointResults.size();
		}
		
		public boolean all() {
			for( boolean result : checkpointResults ) {
				if( !result ) {
					return false;
				}
			}
			return true;
		}
		
		public boolean any() {
			for( boolean result : checkpointResults ) {
				if( result ) {
					return true;
				}
			}
			return false;
		}
		
		public Inspection subList(int fromIndex, int toIndex) {
			return new Inspection(checkpointResults.subList(fromIndex, toIndex));
		}
	}
	
}
