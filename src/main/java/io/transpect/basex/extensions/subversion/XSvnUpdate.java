package io.transpect.basex.extensions.subversion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.basex.query.value.node.FNode;
import org.basex.query.value.map.XQMap;
import org.basex.query.QueryException;
/**
 * Updates one or multiple paths and their children 
 * in a SVN working copy.
 *
 */
public class XSvnUpdate {
		
  /**
  * @deprecated  username/password login replaced with XQMap auth
  */
  public FNode XSvnUpdate(String username, String password, String path, String revision) {
    XSvnXmlReport report = new XSvnXmlReport();
    try{
      String[] paths = path.split(" ");
      File[] filePaths = new File[paths.length];
      XSvnConnect connection = new XSvnConnect(paths[0], username, password);
      SVNClientManager clientmngr = connection.getClientManager();
      SVNUpdateClient updateClient = clientmngr.getUpdateClient();
      String baseURI = connection.isRemote() ? paths[0] : connection.getPath();
      SVNRevision svnRevision;
      boolean allowUnversionedObstructions = true;
      boolean depthIsSticky = true;;
      if(revision.trim().isEmpty()){
        svnRevision = SVNRevision.HEAD;                
      } else {
        svnRevision = SVNRevision.parse(revision);
      }            
      for(int i = 0; i < paths.length; i++) {
        filePaths[i] = new File(paths[i]);
      }
      long[] updatedRevision = updateClient.doUpdate(filePaths, svnRevision, SVNDepth.INFINITY, allowUnversionedObstructions, depthIsSticky);
      HashMap<String, String> results = new HashMap<String, String>();
      for(int i = 0; i < updatedRevision.length; i++) {
        results.put(paths[i], String.valueOf(updatedRevision[i]));
      }
      return XSvnXmlReport.createXmlResult(results);
    } catch(SVNException|IOException svne) {
      System.out.println(svne.getMessage());
      return XSvnXmlReport.createXmlError(svne.getMessage());
    }
  }
	public FNode XSvnUpdate(XQMap auth, String path, String revision) {
    try{
      String[] paths = path.split(" ");
      File[] filePaths = new File[paths.length];
      XSvnConnect connection = new XSvnConnect(paths[0], auth);
      SVNClientManager clientmngr = connection.getClientManager();
      SVNUpdateClient updateClient = clientmngr.getUpdateClient();
      String baseURI = connection.isRemote() ? paths[0] : connection.getPath();
      SVNRevision svnRevision;
      boolean allowUnversionedObstructions = true;
      boolean depthIsSticky = true;;
      if(revision.trim().isEmpty()){
        svnRevision = SVNRevision.HEAD;                
      } else {
        svnRevision = SVNRevision.parse(revision);
      }            
      for(int i = 0; i < paths.length; i++) {
        filePaths[i] = new File(paths[i]);
      }
      long[] updatedRevision = updateClient.doUpdate(filePaths, svnRevision, SVNDepth.INFINITY, allowUnversionedObstructions, depthIsSticky);
      HashMap<String, String> results = new HashMap<String, String>();
      for(int i = 0; i < updatedRevision.length; i++) {
        results.put(paths[i], String.valueOf(updatedRevision[i]));
      }
      return XSvnXmlReport.createXmlResult(results);
    } catch(QueryException | SVNException|IOException svne) {
      System.out.println(svne.getMessage());
      return XSvnXmlReport.createXmlError(svne.getMessage());
    }
  }
}
