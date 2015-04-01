/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.paxar.utilities;
import javax.servlet.ServletOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @author DickLobacz
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ftp implements java.io.Serializable{

	/**
	 * Properties
	 */
    public boolean storeFile = false; 
	public boolean binaryTransfer = false;
	private boolean error = false;
    public String server;					// FTP host name.
    public String username;					// FTP user name.
    public String password;					// FTP passwd.
    public String remote;					// The remote file name.
    public String local;					// The local file path.
    public String contentType;				// For sending a file back through a browser (download).
    FTPClient ftp;
    
	public Ftp() {
		super();
        ftp = new FTPClient();
        this.contentType = "text/plain";
	}
	
	public boolean connect() throws IOException{
        try
        {
            int reply;
            ftp.connect(server);

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
            }
        }
        catch (IOException e)
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            e.printStackTrace();
            
            System.err.println("Could not connect to server " + this.server + " login=" + this.username + ".");
	    	throw new IOException("Ftp: Could not connect to server " + this.server + " login=" + this.username + ".  " + e.getMessage());

        }

		return true;
	}
	
	/**
	 * getFile will retrieve a file from an FTP server.
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean getFile() throws IOException{
		
        try
        {
            if (!ftp.login(username, password))
            {
                ftp.logout();
                error = true;
                return false;
            }

            // System.out.println("Remote system is " + ftp.getSystemName());

            if (binaryTransfer)
                ftp.setFileType(FTP.BINARY_FILE_TYPE);

	        // Use passive mode as default because most of us are
	        // behind firewalls these days.
	        ftp.enterLocalPassiveMode();

            if (storeFile)
            {
                InputStream input;

                input = new FileInputStream(local);
                ftp.storeFile(remote, input);
            }
            else
            {
                OutputStream output;

                output = new FileOutputStream(local);
                ftp.retrieveFile(remote, output);
            }

            ftp.logout();
        }
        catch (FTPConnectionClosedException e)
        {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            error = true;
            e.printStackTrace();
            System.err.println("Could not get file from server.");
	    	throw new IOException("Ftp: Could not get file from server.\n" + e.getMessage());
        }
        finally
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }
        return true;
		
	}
	
	/**
	 * getFile will stream an FTP'd file back to a servlet's 
	 * response object (the browser).
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean getFile(HttpServletResponse response) throws IOException{
		
		
        try
        {
            if (!ftp.login(username, password))
            {
                ftp.logout();
                error = true;
                System.out.println("Login to FTP server failed.");
                return false;
            }

            //System.out.println("Remote system is " + ftp.getSystemName());

            // Check if the file exists.
            FTPFile files[] = ftp.listFiles(remote);
            if (files == null || files.length == 0){
            	System.out.println("File " + remote + " does not exist");
            	return false;
            }
            
            if (binaryTransfer)
                ftp.setFileType(FTP.BINARY_FILE_TYPE);

            response.setContentType(this.contentType);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + this.local + "\"");
            
	        // Use passive mode as default because most of us are
	        // behind firewalls these days.
	        ftp.enterLocalPassiveMode();

			ServletOutputStream output = response.getOutputStream();
			
            ftp.retrieveFile(remote, output);

            ftp.logout();

        }
        catch (FTPConnectionClosedException e)
        {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            error = true;
            e.printStackTrace();
            System.err.println("Could not get file from server.");
	    	throw new IOException("Ftp: Could not get file from server.\n" + e.getMessage());
        }
        finally
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }
        return true;
		
	}
	/**
	 * putFile will send a file to an FTP server.
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean putFile() throws IOException{
		
        try
        {
            if (!ftp.login(username, password))
            {
                ftp.logout();
                error = true;
                return false;
            }

            // System.out.println("Remote system is " + ftp.getSystemName());

            if (binaryTransfer)
                ftp.setFileType(FTP.BINARY_FILE_TYPE);

	        // Use passive mode as default because most of us are
	        // behind firewalls these days.
	        ftp.enterLocalPassiveMode();

	        // Cd to the directory on the FTP server.
	  //      ftp.cwd(directory);
	        
	        // Put the file on the FTP server.
            InputStream input;

            input = new FileInputStream(local);
            ftp.storeFile(remote, input);

            ftp.logout();
            
        }
        catch (FTPConnectionClosedException e)
        {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            error = true;
            e.printStackTrace();
            System.err.println("Could not put the file on the server " +
            					this.server + ".");
	    	throw new IOException("Ftp: Could not put file on the server.\n" + e.getMessage());
        }
        finally
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }
        return true;
		
	}
}
