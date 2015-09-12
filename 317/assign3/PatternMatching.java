import java.io.*;

/////////////////////////////////////////////////
//////////////// Pattern Matching ///////////////
/////////////////////////////////////////////////
/**
 * 
 * 
 * @author simple
 */
public abstract class PatternMatching
{
	public static void main(String[] args)
	{
		BufferedReader bufReader = null;
		String line;
		String t = new String();
		String p = new String();

		try
		{
			bufReader = new BufferedReader(new FileReader(new File("01hgp10.txt")));
			StringBuffer buf = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
			{
				buf.append(line);
			}
			t = buf.toString();

			System.out.println("Enter string to search for:");
			bufReader = new BufferedReader(new InputStreamReader(System.in));
			p = bufReader.readLine();
		}
		catch (Exception e)
		{
			System.out.println("reading error.");
		}

		long startTime = System.currentTimeMillis();

		//System.out.println("Brute force: " + bruteForce(t, p));
		//System.out.println("Boyer Moore: " + boyerMoore(t, p));
		//System.out.println("KMP: " + knuthMorrisPratt(t, p));
		System.out.println("Suffix Trie: " + compressedSuffixTrieSearch(t, p));
		//System.out.println("Huffman: " + huffmanTrieCompression(t));
		//System.out.println("LCS: " + longestCommonSubsequence(t, p));

		System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
	}

	public static int bruteForce(String t, String p)
	{
		for (int tIndex = 0; tIndex <= t.length() - p.length(); tIndex++)
		{
			int pIndex = 0;
			while (pIndex < p.length() && t.charAt(tIndex + pIndex) == p.charAt(pIndex))
			{
				pIndex++;
			}

			if (pIndex == p.length())
			{
				return (tIndex);
			}
		}

		return (-1);
	}

	public static int boyerMoore(String t, String p)
	{
		String alphabet = generateAlphabet(t);
		int[] lFunc = lastOccurenceFunction(p, alphabet);
		int tIndex = p.length() - 1;
		int pIndex = p.length() - 1;
		int l;

		do
		{
			if (t.charAt(tIndex) == p.charAt(pIndex))
			{
				if (pIndex == 0)
				{
					return (tIndex);
				}
				else
				{
					tIndex--;
					pIndex--;
				}
			}
			else
			{
				l = lFunc[alphabet.indexOf(String.valueOf(t.charAt(tIndex)))];
				tIndex = tIndex + p.length() - min(pIndex, 1 + l);
				pIndex = p.length() - 1;
			}
		}
		while (tIndex < t.length());

		return (-1);
	}

	public static int[] lastOccurenceFunction(String p, String alphabet)
	{
		int lFunc[] = new int[alphabet.length()];

		for (int index = 0; index < alphabet.length(); index++)
		{
			lFunc[index] = p.lastIndexOf(String.valueOf(alphabet.charAt(index)));
		}

		return (lFunc);
	}

	public static int knuthMorrisPratt(String t, String p)
	{
		int fFunc[] = failureFunction(p);
		int tIndex = 0;
		int pIndex = 0;

		while (tIndex < t.length())
		{
			if (t.charAt(tIndex) == p.charAt(pIndex))
			{
				if (pIndex == p.length() - 1)
				{
					return (tIndex - pIndex);
				}
				else
				{
					tIndex++;
					pIndex++;
				}
			}
			else
			{
				if (pIndex > 0)
				{
					pIndex = fFunc[pIndex - 1];
				}
				else
				{
					tIndex++;
				}
			}
		}

		return (-1);
	}

	public static int[] failureFunction(String p)
	{
		int fFunc[] = new int[p.length()];
		int pIndex = 1;
		int fIndex = 0;

		while (pIndex < p.length())
		{
			if (p.charAt(pIndex) == p.charAt(fIndex))
			{
				fFunc[pIndex] = fIndex + 1;
				pIndex++;
				fIndex++;
			}
			else if (fIndex > 0)
			{
				fIndex = fFunc[fIndex - 1];
			}
			else
			{
				fFunc[pIndex] = 0;
				pIndex++;
			}
		}

		return (fFunc);
	}

	public static int compressedSuffixTrieSearch(String t, String p)
	{
		long startTime = System.currentTimeMillis();
		
		SuffixTrie myTrie = new SuffixTrie(t);
		
		System.out.println("Time taken (create): " + (System.currentTimeMillis() - startTime));
		startTime = System.currentTimeMillis();
		
		int result = myTrie.search(p);
		
		System.out.println("Time taken (search): " + (System.currentTimeMillis() - startTime));

		return (result);
	}

	public static int huffmanTrieCompression(String t)
	{
		HuffmanTrie trie = new HuffmanTrie(t, generateAlphabet(t));

		return (trie.calcCompressedSize());
	}

	public static int longestCommonSubsequence(String t, String p)
	{
		int table[][] = new int[t.length() + 1][p.length() + 1];

		for (int tIndex = 0; tIndex <= t.length(); tIndex++)
		{
			table[tIndex][0] = 0;
		}
		for (int pIndex = 1; pIndex <= p.length(); pIndex++)
		{
			table[0][pIndex] = 0;
		}

		for (int tIndex = 1; tIndex <= t.length(); tIndex++)
		{
			for (int pIndex = 1; pIndex <= p.length(); pIndex++)
			{
				if (t.charAt(tIndex - 1) == p.charAt(pIndex - 1))
				{
					table[tIndex][pIndex] = table[tIndex - 1][pIndex - 1] + 1;
				}
				else
				{
					table[tIndex][pIndex] = max(table[tIndex - 1][pIndex], table[tIndex][pIndex - 1]);
				}
			}
		}

		return (table[t.length()][p.length()]);
	}

	public static String generateAlphabet(String t)
	{
		StringBuffer buf = new StringBuffer();

		for (int index = 0; index < t.length(); index++)
		{
			if (buf.indexOf(String.valueOf(t.charAt(index))) == -1)
			{
				buf.append(t.charAt(index));
			}
		}

		return (buf.toString());
	}

	public static int min(int a, int b)
	{
		return (a < b ? a : b);
	}

	public static int max(int a, int b)
	{
		return (a > b ? a : b);
	}
}