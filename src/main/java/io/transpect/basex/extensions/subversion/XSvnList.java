package io.transpect.basex.extensions.subversion;

import java.io.File;
import java.io.IOException;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.admin.SVNLookClient;
import org.tmatesoft.svn.core.wc.admin.SVNAdminPath;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;

import org.basex.query.value.node.FNode;
import org.basex.util.InputInfo;
import org.basex.query.value.map.XQMap;
import org.basex.query.QueryException;
import org.basex.query.value.item.*;
import org.basex.query.value.Value;

/**
 * Performs svn list. The class connects to a Subversion repository
 * and provides the results as XML directory tree.
 *
 * @see XSvnConnect
 */
public class XSvnList {
  
  /**
  * @deprecated  username/password login replaced with XQMap auth
  */
  public FNode XSvnList (String url, String username, String password, Boolean recursive) {
    FNode xmlResult;
    try{
      XSvnConnect connection = new XSvnConnect(url, username, password);
      if(connection.isRemote()){
        SVNRepository repository = connection.getRepository();
        xmlResult = XSvnXmlReport.createXmlDirTree(url, repository, recursive);
      } else {
        File path = new File(new File(url).getCanonicalPath());
        xmlResult = XSvnXmlReport.createXmlDirTree(path, recursive);
        return xmlResult; 
      }
      return xmlResult;
    } catch(SVNException | IOException e) {
      System.out.println(e.getMessage());
      FNode xmlError = XSvnXmlReport.createXmlError(e.getMessage());
      return xmlError;
    }
  }
  
  public FNode XSvnList (String url, XQMap auth, Boolean recursive) {
    FNode xmlResult;
    try{
      XSvnConnect connection = new XSvnConnect(url, auth);
      if(connection.isRemote()){
        SVNRepository repository = connection.getRepository();
        xmlResult = XSvnXmlReport.createXmlDirTree(url, repository, recursive);
      } else {
        File path = new File(new File(url).getCanonicalPath());
        xmlResult = XSvnXmlReport.createXmlDirTree(path, recursive);
        return xmlResult; 
      }
      return xmlResult;
    } catch(QueryException | SVNException | IOException e) {
      System.out.println(e.getMessage());
      FNode xmlError = XSvnXmlReport.createXmlError(e.getMessage());
      return xmlError;
    }
  }
  
  private String getStringFromMap(XQMap map, String key) throws QueryException{
      Str strKey = Str.get(key);
    if (map.contains(strKey,new InputInfo("text.txt", 1, 1))){
      Value val = map.get(strKey,new InputInfo("text.txt", 1, 1));
      String result = new String(val.toString());
      return result.replace("\"","");
    } else {
      return null;
    }
  }
  
  public FNode XSvnLook (String rootPath, String path, XQMap auth, Boolean recursive) {
    try{
      String password = getStringFromMap(auth,"password");
      String username = getStringFromMap(auth,"username");
      DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
      BasicAuthenticationManager authManager = new BasicAuthenticationManager(username, password);
      SVNLookClient looky = new SVNLookClient(authManager, options);
      File root = new File(new File(rootPath).getCanonicalPath());
      XSvnTreeHandler handler = new XSvnTreeHandler(path);
      SVNAdminPath admin = new SVNAdminPath(path,null,looky.doGetYoungestRevision(root));
      
      looky.doGetTree(root,admin.getPath(),SVNRevision.create(admin.getRevision()),true,true,handler);
      XSvnXmlReport.createXmlError(String.valueOf(handler.Result));
      return handler.getResult();
      
    } catch(QueryException | SVNException | IOException e) {
      System.out.println(e.getMessage());
      FNode xmlError = XSvnXmlReport.createXmlError(e.getMessage());
      return xmlError;
    }
  }
}
