package com.paxar.utilities;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
import java.io.*;

public class LinuxCommand
{
    public String strOut;
    public final static String LINUX_COMMAND = "";
    public final static String WINDOWS_COMMAND = "C:\\Cygwin\\bin\\";
    public final static String LINUX_LOCATION = "";
    public final static String WINDOWS_LOCATION = "C:\\projects\\dbdserver\\";
    public final static String os = System.getProperty( "os.name" ).toLowerCase( );

    public LinuxCommand(String strCommand)
    {
        try
        {
            String execStr = LINUX_COMMAND;
            if ( os.indexOf( "windows" ) > -1 || os.indexOf( "nt" ) > -1 )
            {
                execStr = WINDOWS_COMMAND;
            }
//            String[] command = {"sh","-c", strCommand};
            String command = execStr + strCommand;
            String c2 = "C:\\Cygwin\\bin\\grep.exe \"GAPS=\" \"C:\\paxar3\\gapprop.20080214205403\"";
            final Process process = Runtime.getRuntime().exec(command);
            new Thread()
            {
                public void run()
                {
                    try{
                        InputStream is = process.getInputStream();
                        BufferedReader br = new BufferedReader( new InputStreamReader(is));
                        strOut= br.readLine();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }.start();
            new Thread()
            {
                public void run()
                {
                    try{
                        InputStream is = process.getErrorStream();
                        byte[] buffer = new byte[1024];
                        for(int count = 0; (count = is.read(buffer)) >= 0;)
                        {
                            System.err.write(buffer, 0, count);
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }.start();

            int returnCode = process.waitFor();
//            System.out.println("Return code = " + returnCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

