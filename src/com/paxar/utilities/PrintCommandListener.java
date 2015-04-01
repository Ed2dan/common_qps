/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.paxar.utilities;

  
import java.io.PrintWriter;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;

/****
* This is a support class for some of the example programs.  It is
* a sample implementation of the ProtocolCommandListener interface
* which just prints out to a specified stream all command/reply traffic.
* <p>
***/
public class PrintCommandListener implements ProtocolCommandListener
{
    private PrintWriter __writer;

    public PrintCommandListener(PrintWriter writer)
    {
        __writer = writer;
     }
 
    public void protocolCommandSent(ProtocolCommandEvent event)
    {
       __writer.print(event.getMessage());
       __writer.flush();
     }
 
    public void protocolReplyReceived(ProtocolCommandEvent event)
    {
        __writer.print(event.getMessage());
        __writer.flush();
    }
}
