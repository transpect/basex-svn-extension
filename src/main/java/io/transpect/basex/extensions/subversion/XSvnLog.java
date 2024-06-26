package io.transpect.basex.extensions.subversion;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.basex.query.value.node.FNode;
import org.basex.query.value.map.XQMap;
import org.basex.query.QueryException;

/**
 * This class provides the svn info command. The class 
 * connects to a Subversion repository and provides 
 * the results as XML document.
 *
 * @see XSvnConnect
 */
public class XSvnLog {
  
  /**
  * @deprecated  username/password login replaced with XQMap auth
  */
  public FNode XSvnLog (String url, String username, String password, int revisionStart, int revisionEnd, int limit) {
    try{
      XSvnConnect connection = new XSvnConnect(url, username, password);
      SVNLogClient client = connection.getClientManager().getLogClient();
      SVNRevision SVNRevisionStart = SVNRevision.create(revisionStart);
      SVNRevision SVNRevisionEnd = SVNRevision.create(revisionEnd);
      if (!SVNRevisionStart.isValid()) SVNRevisionStart = SVNRevision.create(0);
      if (!SVNRevisionEnd.isValid() || revisionEnd <= 0) SVNRevisionEnd = SVNRevision.HEAD;
      
      XSvnLogEntryHandler handler = new XSvnLogEntryHandler();
      client.doLog(connection.getSVNURL(), null, SVNRevision.HEAD, SVNRevisionStart, SVNRevisionEnd, false, true, true, limit, null, handler);
      
      FNode xmlResult = handler.getResult();
      return xmlResult;
    } catch(SVNException svne) {
      System.out.println(svne.getMessage());
      return XSvnXmlReport.createXmlError(svne.getMessage());
    }
  }
  
  public FNode XSvnLog (String url, XQMap auth, int revisionStart, int revisionEnd, int limit) {
    try{
      XSvnConnect connection = new XSvnConnect(url, auth);
      SVNLogClient client = connection.getClientManager().getLogClient();
      
      SVNRevision SVNRevisionStart = SVNRevision.create(revisionStart);
      SVNRevision SVNRevisionEnd = SVNRevision.create(revisionEnd);
      if (!SVNRevisionStart.isValid()) SVNRevisionStart = SVNRevision.create(0);
      if (!SVNRevisionEnd.isValid() || revisionEnd <= 0) SVNRevisionEnd = SVNRevision.HEAD;
      
      XSvnLogEntryHandler handler = new XSvnLogEntryHandler();
      client.doLog(connection.getSVNURL(), null, SVNRevision.HEAD, SVNRevisionStart, SVNRevisionEnd, false, true, true, limit, null, handler);
      
      FNode xmlResult = handler.getResult();
      return xmlResult;
    } catch(QueryException | SVNException svne) {
      System.out.println(svne.getMessage());
      return XSvnXmlReport.createXmlError(svne.getMessage());
    }
  }
}
