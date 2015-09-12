import java.math.BigInteger;

/////////////////////////////////////////////////
/////////////////// Fibonacci ///////////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public class Fibonacci
{
	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();

		//System.out.println("Result: " + fibonacci(50));
		System.out.println("Result: " + fastestFibonacci(999999999));

		System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
	}

	static public BigInteger fibonacci(int n)
	{
		BigInteger a = new BigInteger("0");
		BigInteger b = new BigInteger("1");

		if (n == 0)
		{
			return (a);
		}
		else if (n == 1)
		{
			return (b);
		}

		return (fibonext(a, b, n - 1));
	}

	static private BigInteger fibonext(BigInteger a, BigInteger b, int n)
	{
		BigInteger c = a.add(b);
		a = b;
		b = c;

		if (n > 1)
		{
			return (fibonext(a, b, n - 1));
		}
		else
		{
			return (b);
		}
	}

	static public BigInteger fastestFibonacci(int n)
	{
		BigInteger a = new BigInteger("0");
		BigInteger b = new BigInteger("1");
		BigInteger c;

		if (n == 0)
		{
			return (a);
		}
		else if (n == 1)
		{
			return (b);
		}

		for (; n > 1; n--)
		{
			c = a.add(b);
			a = b;
			b = c;
		}

		return (b);
	}
}
