package av.misc;

public class AverageTT {
	public double value;
	public int sum;
	public AverageTT(double newValue) {
		value=newValue;
		sum=1;
	}
	public void add(double newValue) {
		value = (value*sum + newValue) / (sum+1);
		sum++;
	}
}
