package io.transpect.basex.extensions.subversion;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.admin.SVNLookClient;
import org.tmatesoft.svn.core.wc.admin.SVNAdminPath;
import org.tmatesoft.svn.core.wc.admin.ISVNTreeHandler;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;

import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FNode;
import org.basex.query.value.node.FBuilder;
import org.basex.query.value.map.XQMap;
import org.basex.query.QueryException;
import org.basex.query.value.item.*;
import org.basex.query.value.Value;

import io.transpect.basex.extensions.subversion.XSvnConnect;
import io.transpect.basex.extensions.subversion.XSvnXmlReport;
import io.transpect.basex.extensions.subversion.XSvnHelper;
/**
 * Performs svn list. The class connects to a Subversion repository
 * and provides the results as XML directory tree.
 *
 * @see XSvnConnect
 */
public class XSvnTreeHandler implements ISVNTreeHandler {
  
  final static String nsprefix = "c";
  final static String nsuri = "http://www.w3.org/ns/xproc-step";
  
  public String Result;
  public FBuilder XmlResult;
  private String rootPath;
  
  public XSvnTreeHandler(String rootPath)
  {
    this.XmlResult = FElem.build(new QNm("root"));
    this.rootPath = rootPath;
  }

  public FNode getResult (){
    return this.XmlResult.finish();
  }
  
  public void handlePath (SVNAdminPath path){
    
    Result = Result + '\n' + path.getPath() + path.getTreeDepth();
    
    String elementName = "file";
    if (path.isDir()) elementName = "directory";
    FBuilder element = XSvnHelper.build(elementName,nsuri,nsprefix);
    
    Pattern p = Pattern.compile("([^/]*)$");
    Matcher m = p.matcher(path.getPath());
    if (m.find()){
      
      element.add(new QNm("name"), m.group(1).getBytes());
      element.add(new QNm("depth"), String.valueOf(path.getTreeDepth()).getBytes());
      
      if (path.getTreeDepth() == 1){
        this.XmlResult.add(element);
      }
    }
  }
}
