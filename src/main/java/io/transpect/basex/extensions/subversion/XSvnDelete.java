package io.transpect.basex.extensions.subversion;

import java.io.File;
import java.io.IOException;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import org.basex.query.value.node.FNode;
import org.basex.query.value.map.XQMap;
import org.basex.query.QueryException;

/**
 * This class provides the svn delete command. The class 
 * connects to a Subversion remote repository or a
 * working copy and attempts to delete the selected paths.
 *
 * @see XSvnDelete
 */
public class XSvnDelete {
  /**
  * @deprecated  username/password login replaced with XQMap auth
  */
  public FNode XSvnDelete ( String url, String username, String password, String path, Boolean force, String commitMessage ) {
    Boolean dryRun = false;
    try{
      XSvnConnect connection = new XSvnConnect(url, username, password);
      SVNClientManager clientmngr = connection.getClientManager();
      String baseURI = connection.isRemote() ? url : connection.getPath();
      SVNCommitClient commitClient = clientmngr.getCommitClient();
      SVNWCClient client = clientmngr.getWCClient();
      String[] paths = path.split(" ");
      for(int i = 0; i < paths.length; i++) {
        String currentPath = paths[i];
        if( connection.isRemote() ){
          SVNURL[] svnurl = { SVNURL.parseURIEncoded( url + "/" + currentPath )};
          commitClient.doDelete(svnurl, commitMessage);
        } else {
          File fullPath = new File( url + "/" + currentPath );
          client.doDelete(fullPath, force, dryRun);
        }
      }
      return XSvnXmlReport.createXmlResult(baseURI, "delete", paths);
    } catch(SVNException|IOException svne) {
      System.out.println(svne.getMessage());
      return XSvnXmlReport.createXmlError(svne.getMessage());
    }
  }
  public FNode XSvnDelete ( String url, XQMap auth, String path, Boolean force, String commitMessage ) {
    Boolean dryRun = false;
    try{
      XSvnConnect connection = new XSvnConnect(url, auth);
      SVNClientManager clientmngr = connection.getClientManager();
      String baseURI = connection.isRemote() ? url : connection.getPath();
      SVNCommitClient commitClient = clientmngr.getCommitClient();
      SVNWCClient client = clientmngr.getWCClient();
      String[] paths = path.split(" ");
      for(int i = 0; i < paths.length; i++) {
        String currentPath = paths[i];
        if( connection.isRemote() ){
          SVNURL[] svnurl = { SVNURL.parseURIEncoded( url + "/" + currentPath )};
          commitClient.doDelete(svnurl, commitMessage);
        } else {
          File fullPath = new File( url + "/" + currentPath );
          client.doDelete(fullPath, force, dryRun);
        }
      }
      return XSvnXmlReport.createXmlResult(baseURI, "delete", paths);
    } catch(QueryException | SVNException|IOException svne) {
      System.out.println(svne.getMessage());
      return XSvnXmlReport.createXmlError(svne.getMessage());
    }
  }
}
