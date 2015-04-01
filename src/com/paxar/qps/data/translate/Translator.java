package com.paxar.qps.data.translate;
import java.io.*;
/**
 * <p>Title: Translator</p>
 *
 * <p>Description: This is the Autoimport file encode translator.  To be used with PCMate trans.dll for decoding.</p>
 *
 * @author jerome
 * @version 1.0
 */
public class Translator
{
    public Translator()
    {
    }

    //
    // Encryption keys
    // Keys have to be the same as trans.dll
    //
    // Default encryption key of prime numbers for XOR operation
    final static char default_keys[] = { 59, 157, 227, 41, 109, 233, 19, 89, 173, 11, 83, 223, 71, 197, 139, 101 };
    // Default remainder key of large prime numbers for checksum calculation
    final static char default_remain_div[] = { 251, 233, 199, 241 };

    // Runtime keys of above
    // You can extend this class or substitue your own keys
    protected static char keys[] = default_keys ;
    protected static char remain_div[] = default_remain_div;

    // Maximum file size to encrypt
    final static int max_file_size = 1024 * 1024;

    /**
     * Reset to default keys
     */
    public static void setKeys()
    {
        keys = default_keys;
        remain_div = default_remain_div;
    }

    /**
     * Set to use your own keys
     * @param new_keys char[]
     * @param new_remain_div char[]
     */
    public static void setKeys(char new_keys[], char new_remain_div[]) throws RuntimeException
    {
        if ( ( new_keys == null ) || ( new_keys.length <= 1 ) )
            throw new RuntimeException( "Translator: New encryption key too short" );
        if ( ( new_remain_div == null ) || ( new_remain_div.length <= 1 ) )
            throw new RuntimeException( "Translator: New remainder key too short" );
        keys = new_keys;
        remain_div = new_remain_div;
    }


    /**
     * Casting program to convert byte[] to char[]
     * @param src byte[]
     * @param length int length of source string
     * @return byte[]
     */
    public static byte[] translate(byte src[], int length)
    {
        char srcc[] = new char[length];
        for (int i=0; i<srcc.length; i++)
            srcc[i] = (char) src[i];

        char dest[] = translate(srcc);

        byte destb[] = new byte[dest.length];
        for (int i=0; i<dest.length; i++)
            destb[i] = (byte) dest[i];

        return destb;
    }

    /**
     * This the is main translate program together with checksum generation
     * @param src char[] source string
     * @return char[] output string with checksum appended
     */
    public static char[] translate(char src[])
    {
        int remain[] = new int[remain_div.length];
        char dest[]=new char[src.length+remain_div.length];

        for (int i=0; i<src.length; i++)
        {
            dest[i] = (char)(src[i] ^ keys[i % keys.length]);

            // Checksum Calculation
            for (int j=0; j < remain.length; j++)
              if (j != (i % remain.length))
              {
                  remain[ j ] = remain[ j ] + checksumValue( src[ i ] ) + (i * checksumValue(keys[0]) % checksumValue(keys[1]));
                  remain[ j ] = remain[j] % remain_div[ j ];
              }
            // System.out.println(" " + i + " " + remain[0] + " " + remain[1] + " " + remain[2] + " " + remain[3] + " " + checksumValue(src[i]));
        }

        // Append Checksum
        for (int i=0; i < remain.length; i++)
            dest[dest.length-remain.length+i] = (char) remain[i];

        return dest;
    }

    /**
     * Calculate the character value for checksum calculation
     * This program is required to convert char as byte value, so that the same as calculated in C++
     * @param c char
     * @return int
     */
    public static int checksumValue(char c)
    {
        return ((int) c) & 0x00FF;
    }

    /**
     * Open a source file and save the result file to destination
     * @param src_file String
     * @param dest_file String
     * @throws Exception
     */
    public static void translateFile(String src_file, String dest_file) throws IOException
    {
        FileInputStream i = new FileInputStream(new File(src_file));
        byte src[]= new byte[max_file_size];
        int filelen=i.read(src);
        if (filelen == max_file_size)
            throw new IOException("Translator: Source file size exceeds limit");
        i.close();
        FileOutputStream o = new FileOutputStream(new File(dest_file));
        o.write(translate(src, filelen));
        o.flush();
        o.close();
    }

    /**
     * Command line testing program
     * @param argv String[]
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception
    {
        if (argv.length!=2)
        {
            System.out.println("Usage: Translator <source> <destination>");
            return;
        }
        translateFile(argv[0], argv[1]);
    }
}
