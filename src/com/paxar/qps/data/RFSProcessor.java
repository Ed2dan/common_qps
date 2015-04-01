package com.paxar.qps.data;
import java.util.*;
import java.util.zip.*;
import java.net.*;
import java.io.*;
import java.text.*;
/**
 * <p>Title: RFSProcessor</p>
 *
 * <p>Description: This class is the client to send files to Remote File Service.</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author jerome
 * @version 1.0
 */
public class RFSProcessor
{
    Vector vc = null;     // Stores RFS records
    String filename = ""; // Base file name
    public static String FTP_SERVER="d2comm.paxar.com";
    public static String FTP_USER="rfs";
    public static String FTP_PASSWORD="rmtflsrie";

    public static String HTTP_URL="http://d2comm.paxar.com/rfs/RemoteFileServices?filename=";

    public static String SCRATCH="/tmp";
    public static String DIRECTOR_EXT=".ILA";
    public static String ZIP_EXT=".ZIP";

    /**
     *
     * @param prefix String Add a prefix to the ZIP file generated
     */
    public RFSProcessor(String prefix)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        vc = new Vector();
        this.filename=prefix + sdf.format(new Date());
    }

    /**
     * Add a new RFS to schedule
     * @param rfsf RFSFile
     */
    public void add(RFSFile rfsf)
    {
        vc.add(rfsf);
    }

    /**
     * Get RFS file
     * @param i int
     * @return RFSFile
     */
    public RFSFile get(int i)
    {
        return (RFSFile) vc.get(i);
    }

    /**
     * No of RFS files
     * @return int
     */
    public int size()
    {
        return vc.size();
    }

    /**
     * Schedule the RFS files
     * @return int error code if not zero
     */
    public void schedule() throws IOException
    {
        if (vc.size()==0)
            return;

        if (createDirector()==0)
            return;
        createZIP();
        uploadZIP();
        httpCall();
    }

    /**
     * Upload the ZIP package
     * @throws Exception
     */
    public void uploadZIP() throws IOException
    {
        File fptr = new File(SCRATCH + "/" + filename + ZIP_EXT);
        FileInputStream is = new FileInputStream(fptr);

        URL url = new URL("ftp://" + FTP_USER + ":" + FTP_PASSWORD + "@" + FTP_SERVER + "/" + filename + ZIP_EXT + ";type=i");
        URLConnection urlc = url.openConnection();
        OutputStream os = urlc.getOutputStream();

        byte[] buf= new byte[16384];
        int c;
        while(true) {
            c=is.read(buf);
            if (c<=0)
                break;
            os.write(buf,0,c);
        }
        os.close();
        is.close();

        // Remove ILA file
        fptr.delete();
    }

    /**
     * Invoke the RFS service
     * @throws IOException
     */
    public void httpCall() throws IOException
    {
        char[] buf = new char[16384];
        URL url = new URL(HTTP_URL + filename + ZIP_EXT);
        URLConnection urlc = url.openConnection();
        InputStreamReader is = new InputStreamReader(urlc.getInputStream());

        String returnPage = "";
        int c;
        while (true)
        {
            c=is.read(buf);
            if (c<=0)
                break;
            returnPage += String.valueOf( buf );
        }
        is.close();
        if (!returnPage.trim().startsWith(filename))
            throw new IOException("RFS Error: " + returnPage);
    }

    /**
     * Create zip file
     * @throws IOException
     */
    public void createZIP() throws IOException
    {
        Vector zipContent = new Vector();
        byte[] buf = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(SCRATCH + "/" + filename + ZIP_EXT));

        // Upload RFS files
        for (int i=0; i<size(); i++)
        {
            if (get(i).getBatchFile()==null)
                continue;
            if (!get(i).getBatchFile().exists())
                continue;
            if (zipContent.contains(get(i).getBatchFile().getName()))
                continue;
            FileInputStream in = new FileInputStream(get(i).getBatchFile());
            out.putNextEntry(new ZipEntry(get(i).getBatchFile().getName()));
            int len=0;
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);
            out.closeEntry();
            in.close();
            zipContent.add(get(i).getBatchFile().getName());
        }

        // Upload ILA file
        File fptr = new File(SCRATCH + "/" + filename + DIRECTOR_EXT);
        FileInputStream in = new FileInputStream(fptr);
        out.putNextEntry(new ZipEntry(filename + DIRECTOR_EXT));
        int len=0;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
        in.close();
        fptr.delete();

        out.close();
    }

    /**
     * Create director file
     * @return String
     * @throws IOException
     */
    public int createDirector() throws IOException
    {
        String fileContent = "";
        int size=0;

        // Create file content
        for (int i=0; i< size(); i++)
        {
            if (get(i).getBatchFile()==null)
                continue;
            if (!get(i).getBatchFile().exists())
                continue;

            fileContent += get( i ).getBatchFile().getName() + "|" + get(i).getGroup() + "|" + get(i).getType() + "|";
            fileContent += get(i).getRetailer() + "|" + get( i ).getSite() + "|" + get( i ).getUser() + "|";
            fileContent += get(i).getComment() + "|0\r\n";
            size++;
        }

        if (size==0)
            return 0;

        // Write file
        File fptr = new File( SCRATCH + "/" + filename + DIRECTOR_EXT );
        PrintWriter writer = new PrintWriter( new OutputStreamWriter(
                         new FileOutputStream( fptr )));
        writer.print(fileContent);
        writer.close();
        return size;
    }
}
