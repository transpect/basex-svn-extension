package io.transpect.basex.extensions.subversion;

import java.io.File;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import org.basex.query.value.node.FNode;
import org.basex.query.value.map.XQMap;
import org.basex.query.QueryException;
/**
 * Performs svn lock and unlock as XML Calabash extension step for
 * XProc. The class connects to a Subversion repository and
 * provides the results as XML document.
 *
 * @see XSvnLock
 */

public class XSvnLock {
	
  /**
  * @deprecated  username/password login replaced with XQMap auth
  */
  public FNode XSvnLock ( String url, String username, String password, String paths, Boolean unlock, Boolean breakLock, String message ) {

    String[] pathsArr = paths.split(" ");
    try{
      XSvnConnect connection = new XSvnConnect(url, username, password);
      SVNClientManager clientmngr = connection.getClientManager();
      SVNWCClient client = clientmngr.getWCClient();
      for (int i = 0; i < pathsArr.length; i++){
        pathsArr[i] = url + "/" + pathsArr[i];
      }
      if(connection.isRemote()){
        SVNURL[] svnUrlArr = new SVNURL[pathsArr.length];
        for (int i = 0; i < pathsArr.length; i++){
          svnUrlArr[i] = SVNURL.parseURIEncoded(pathsArr[i]);
        }
        if(unlock){
          client.doUnlock(svnUrlArr, breakLock);
        } else {
          client.doLock(svnUrlArr, breakLock, message);
        }
      } else {
        File[] filesArr = null;
        for (int i = 0; i < pathsArr.length; i++){
          filesArr[i] = new File(pathsArr[i]);
        }
        if(unlock){
          client.doUnlock(filesArr, breakLock);
        } else {
          client.doLock(filesArr, breakLock, message);
        }
      }
      return XSvnXmlReport.createXmlResult(url, ( unlock ? "unlock" : "lock" ), pathsArr);
    } catch (SVNException svne){
      System.out.println(svne.getMessage());
      return XSvnXmlReport.createXmlError(svne.getMessage());
    }
  }
	public FNode XSvnLock ( String url, XQMap auth, String paths, Boolean unlock, Boolean breakLock, String message ) {

    String[] pathsArr = paths.split(" ");
    try{
      XSvnConnect connection = new XSvnConnect(url, auth);
      SVNClientManager clientmngr = connection.getClientManager();
      SVNWCClient client = clientmngr.getWCClient();
      for (int i = 0; i < pathsArr.length; i++){
        pathsArr[i] = url + "/" + pathsArr[i];
      }
      if(connection.isRemote()){
        SVNURL[] svnUrlArr = new SVNURL[pathsArr.length];
        for (int i = 0; i < pathsArr.length; i++){
          svnUrlArr[i] = SVNURL.parseURIEncoded(pathsArr[i]);
        }
        if(unlock){
          client.doUnlock(svnUrlArr, breakLock);
        } else {
          client.doLock(svnUrlArr, breakLock, message);
        }
      } else {
        File[] filesArr = null;
        for (int i = 0; i < pathsArr.length; i++){
          filesArr[i] = new File(pathsArr[i]);
        }
        if(unlock){
          client.doUnlock(filesArr, breakLock);
        } else {
          client.doLock(filesArr, breakLock, message);
        }
      }
      return XSvnXmlReport.createXmlResult(url, ( unlock ? "unlock" : "lock" ), pathsArr);
    } catch (QueryException | SVNException svne){
      System.out.println(svne.getMessage());
      return XSvnXmlReport.createXmlError(svne.getMessage());
    }
  }
}
